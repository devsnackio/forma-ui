@file:Suppress("UnstableApiUsage")

rootProject.name = "forma-ui"

pluginManagement {
    // Convention plugins live in the build-logic composite build.
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

include(":core")
include(":components")
include(":sample")
// Wasm live-preview harness (spike). Not published — renders real components in the browser
// to de-risk the docs-site live-preview pipeline. Depends on :core + :components.
include(":preview-wasm")
// Relocation POMs redirecting the retired `io.github.devsnackio` coordinates to `dev.formaui`.
// Publishes on its own task into its own bundle — never part of a normal release. See its KDoc.
include(":relocation")
