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
        // WORKAROUND (SSL-inspecting proxy blocks fresh Gradle downloads on this machine):
        // serves ONLY the curl-fetched org.jetbrains.compose.ui:ui-tooling-preview artifacts from
        // ~/.m2. Remove once the proxy CA is trusted by the JBR truststore and these resolve from
        // Maven Central directly.
        mavenLocal {
            content {
                includeModule("org.jetbrains.compose.ui", "ui-tooling-preview")
                includeModule("org.jetbrains.compose.ui", "ui-tooling-preview-wasm-js")
            }
        }
    }
}

include(":core")
include(":components")
include(":sample")
