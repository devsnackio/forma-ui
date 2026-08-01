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

group = "dev.formaui"
version = "0.2.0"

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
            // The project's home page — the docs site. `scm` below stays on GitHub, which is
            // where the source actually lives; the two are deliberately different.
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

/**
 * The `wasmJs` publication is deliberately NOT published.
 *
 * `wasmJs` exists as a compile target because `:preview-wasm` links against it to build the docs
 * site's live previews (and `compileKotlinWasmJs` is part of the QA gate) — but nothing consumes
 * `*-wasm-js` from a Maven repository. The docs site gets its bundle from the `previews-<version>`
 * GitHub release asset (`.github/workflows/previews.yml`), never from Maven. Android is the
 * shipped, published artifact.
 *
 * Why bother: the wasm publications are 130 of the 370 files in a release, and Maven Central's
 * free tier is limited on monthly FILE COUNT (~1,167) long before size (~78 MB) — this project's
 * payload is only ~3 MB, so file count is the binding constraint. Dropping them takes a release
 * from 370 to 240 files.
 *
 * Trade-off, verified by inspecting the published Gradle Module Metadata: the root
 * `kotlinMultiplatform` module still advertises a wasmJs variant with an `available-at` pointer to
 * the absent `*-wasm-js` module, and Gradle offers no supported way to edit GMM. A consumer that
 * asks for wasmJs therefore gets a 404 on that pointer rather than a clean "no matching variant"
 * error. That is acceptable here precisely because wasmJs is not a supported consumer target — but
 * it is the reason to re-enable this the moment we DO want to ship wasm to consumers.
 */
tasks.withType<AbstractPublishToMaven>().configureEach {
    onlyIf("wasmJs is a build-only target — see the note above") { task ->
        (task as AbstractPublishToMaven).publication?.name != "wasmJs"
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

