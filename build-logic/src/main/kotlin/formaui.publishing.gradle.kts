import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.tasks.bundling.Jar
import org.gradle.plugins.signing.Sign

/**
 * Convention plugin: Maven Central publishing for FormaUI library modules.
 *
 * Uses only Gradle's built-in `maven-publish` + `signing` — no external Gradle plugin — so it
 * works in restricted network environments where the plugin portal / Maven Central cannot be
 * reached for fresh plugin downloads (e.g. behind an SSL-inspecting proxy).
 *
 * Applied by `:core` and `:components` (after `formaui.kmp.library`). It configures the full POM,
 * a javadoc jar (Kotlin Multiplatform already publishes sources jars), PGP signing, and a local
 * **Central Portal bundle** repository.
 *
 * ### Publishing to Maven Central (Central Portal — central.sonatype.com)
 * Legacy OSSRH is decommissioned; the Central Portal does not accept a direct Gradle upload from
 * plain `maven-publish`. Instead this produces an upload-ready bundle you push manually:
 *
 * 1. Set the signing key (see `gradle.properties.template`) and run:
 *    `./gradlew publishAllPublicationsToCentralBundleRepository`
 *    → writes a signed Maven-repo tree to `build/central-bundle/` (shared across modules).
 * 2. Zip the CONTENTS of `build/central-bundle/` (so `dev/formaui/...` is at the zip root).
 * 3. Upload the zip at https://central.sonatype.com → "Publish Component" (namespace
 *    `dev.formaui` must be verified first).
 *
 * `./gradlew publishToMavenLocal` works with no signing key for local verification.
 *
 * Credentials/signing are read from Gradle properties or env vars and are never committed:
 * `signingInMemoryKey` (ASCII-armored) + `signingInMemoryKeyPassword`.
 */

plugins {
    id("maven-publish")
    id("signing")
}

group = "io.github.devsnackio"
version = "0.1.0-beta02"

val javadocJar = tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        artifact(javadocJar)
        pom {
            name.set("FormaUI :${project.name}")
            description.set(
                "FormaUI — opinionated, Material You-native Compose Multiplatform UI components, " +
                        "built as a themed layer on Material 3.",
            )
            url.set("https://github.com/devsnackio/forma-ui")
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
        }
    }

    repositories {
        // Every module writes into ONE shared tree so the whole bundle can be zipped and uploaded
        // to the Central Portal in a single deployment.
        maven {
            name = "centralBundle"
            url = rootProject.layout.buildDirectory.dir("central-bundle").get().asFile.toURI()
        }
    }
}

signing {
    val signingKey = providers.gradleProperty("signingInMemoryKey")
        .orElse(providers.environmentVariable("SIGNING_KEY"))
        .orNull
        // gradle.properties unescapes `\n` sequences to real newlines; env vars (CI secrets) do
        // not. Tolerate the single-line `\n`-escaped form either way — armored keys contain no
        // literal backslashes, so this is a no-op for a properly multiline key.
        ?.replace("\\n", "\n")
    val signingPassword = providers.gradleProperty("signingInMemoryKeyPassword")
        .orElse(providers.environmentVariable("SIGNING_PASSWORD"))
        .orNull

    if (!signingKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)

        // KMP creates publish/link tasks that consume signing output; make the dependency explicit
        // so signed publishing doesn't hit Gradle's implicit-dependency error.
        tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(tasks.withType<Sign>())
        }
    }
}

