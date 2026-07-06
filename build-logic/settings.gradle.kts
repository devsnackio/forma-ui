@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        // Share the single source-of-truth version catalog with the main build.
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
