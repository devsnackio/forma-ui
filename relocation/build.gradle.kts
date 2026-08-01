import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.plugins.signing.Sign

/**
 * One-shot **relocation POMs** for the old `io.github.devsnackio` coordinates.
 *
 * FormaUI shipped `io.github.devsnackio:{core,components}` up to and including stable `0.1.0`
 * before the `formaui.dev` domain was owned. Published coordinates are permanent, so moving to
 * `dev.formaui` mints a *new* artifact rather than renaming the old one — anyone still writing the
 * old coordinate would silently sit on `0.1.0` forever.
 *
 * This module publishes nothing but `.pom` files under the OLD group, each carrying a
 * `<distributionManagement><relocation>` block pointing at the new coordinates. Gradle and Maven
 * both understand it: the consumer's build resolves through to `dev.formaui:*` and prints a
 * relocation warning telling them to edit their dependency line.
 *
 * ### Why this is a separate module and a separate deployment
 * The relocation POM points at an artifact that must **already exist** on Central — publishing it
 * alongside `dev.formaui:0.2.0` in one bundle would briefly advertise a redirect to a 404. So it
 * writes to its own `build/relocation-bundle/` tree and ships as a second Central deployment,
 * *after* the real release resolves on `repo1.maven.org`.
 *
 * Its repository is deliberately named `relocationBundle`, which keeps its publish task
 * (`publishAllPublicationsToRelocationBundleRepository`) distinct from the release task
 * (`publishAllPublicationsToCentralBundleRepository`) that `:core`/`:components` respond to — so a
 * normal release run never picks this up.
 *
 * ### Usage
 * ```
 * ./gradlew :relocation:publishAllPublicationsToRelocationBundleRepository
 * cd build/relocation-bundle && jar -cMf ../formaui-0.2.0-relocation.zip io
 * ```
 * Then upload that zip at https://central.sonatype.com → "Publish Component".
 *
 * This module is expected to be short-lived: once consumers have migrated, it can be deleted. Do
 * NOT bump it every release — one relocation POM per old artifact is enough.
 */

plugins {
    id("maven-publish")
    id("signing")
}

/** The namespace the artifacts moved FROM. Verified via GitHub account ownership. */
val oldGroup = "io.github.devsnackio"

/** The namespace the artifacts moved TO. Verified via a DNS TXT record on `formaui.dev`. */
val newGroup = "dev.formaui"

/**
 * The version the relocation POMs are published AT, under the old group. Must be greater than the
 * last real release under that group (`0.1.0`) so dependency resolution reaches it, and must match
 * the version actually published under the new group.
 */
val relocationVersion = "0.2.0"

group = oldGroup
version = relocationVersion

/**
 * Every artifactId that ever shipped under the old group, minus the `-wasm-js` variants: those were
 * published by accident (the publishing convention plugin means to exclude them) and are not a
 * supported consumer target, so pointing them at new coordinates would advertise support we don't
 * offer.
 *
 * The `-android` entries matter because Gradle Module Metadata resolves the root module to a
 * platform variant — a consumer who somehow pins the platform coordinate directly still gets
 * redirected.
 */
val relocatedArtifacts = listOf("core", "core-android", "components", "components-android")

publishing {
    publications {
        relocatedArtifacts.forEach { artifact ->
            register<MavenPublication>(artifact.replace("-", "")) {
                groupId = oldGroup
                artifactId = artifact
                version = relocationVersion

                pom {
                    // No jar/sources/javadoc is attached, so this resolves as a pom-only module.
                    // Central only demands sources+javadoc jars for non-`pom` packaging.
                    packaging = "pom"
                    name.set("FormaUI :$artifact (relocated)")
                    description.set(
                        "FormaUI moved to the dev.formaui namespace. This artifact only redirects " +
                            "to $newGroup:$artifact — depend on that instead.",
                    )
                    url.set("https://formaui.dev")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("devsnack")
                            name.set("Chanbo (DevSnack)")
                            url.set("https://github.com/devsnackio")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/devsnackio/forma-ui.git")
                        developerConnection.set("scm:git:ssh://git@github.com:devsnackio/forma-ui.git")
                        url.set("https://github.com/devsnackio/forma-ui")
                    }
                    // Gradle's POM DSL has no typed `distributionManagement`, so the relocation
                    // block is appended to the generated XML directly.
                    withXml {
                        val relocation = asNode()
                            .appendNode("distributionManagement")
                            .appendNode("relocation")
                        relocation.appendNode("groupId", newGroup)
                        relocation.appendNode("artifactId", artifact)
                        relocation.appendNode("version", relocationVersion)
                        relocation.appendNode(
                            "message",
                            "FormaUI moved to the $newGroup namespace — update your dependency to " +
                                "$newGroup:$artifact:$relocationVersion.",
                        )
                    }
                }
            }
        }
    }

    repositories {
        // Deliberately NOT `centralBundle` — see the module KDoc. A different repository name keeps
        // this out of the release task that :core and :components respond to.
        maven {
            name = "relocationBundle"
            url = rootProject.layout.buildDirectory.dir("relocation-bundle").get().asFile.toURI()
        }
    }
}

// Same credential surface as formaui.publishing.gradle.kts — Central requires a .asc signature on
// every uploaded file, relocation POMs included.
signing {
    val signingKey = providers.gradleProperty("signingInMemoryKey")
        .orElse(providers.environmentVariable("SIGNING_KEY"))
        .orNull
        ?.replace("\\n", "\n")
    val signingPassword = providers.gradleProperty("signingInMemoryKeyPassword")
        .orElse(providers.environmentVariable("SIGNING_PASSWORD"))
        .orNull

    if (!signingKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
        tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(tasks.withType<Sign>())
        }
    }
}
