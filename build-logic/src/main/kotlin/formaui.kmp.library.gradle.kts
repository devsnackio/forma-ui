import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Convention plugin: shared Kotlin Multiplatform + Android + Compose Multiplatform setup
 * for every FormaUI library module (`:core`, `:components`, and future modules).
 *
 * A module applies this via `plugins { id("formaui.kmp.library") }`, then only needs to
 * declare its own `kotlin.android.namespace` and dependencies — targets, SDK levels, the JVM
 * toolchain, host (Robolectric) unit tests, and the Compose compiler are configured here once.
 *
 * Uses the AGP 9 KMP Android library plugin (`com.android.kotlin.multiplatform.library`) with the
 * `kotlin { android { } }` DSL; the legacy `com.android.library` plugin is incompatible with
 * `org.jetbrains.kotlin.multiplatform` since AGP 9.0.
 */

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

private val versionCatalog =
    extensions.getByType<VersionCatalogsExtension>().named("libs")

private fun catalogVersion(name: String): String =
    versionCatalog.findVersion(name).get().requiredVersion

kotlin {
    android {
        compileSdk = catalogVersion("android-compileSdk").toInt()
        minSdk = catalogVersion("android-minSdk").toInt()

        // Host-side (JVM) unit tests, Robolectric-hosted — they need real Android resources.
        withHostTest {
            isIncludeAndroidResources = true
        }
        // Merge Android resources (needed for Robolectric + bundled Compose resources on Android).
        androidResources {
            enable = true
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        // Browser target: this is what the docs site compiles component previews against.
        browser()
    }
}
