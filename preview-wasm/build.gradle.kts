import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

/*
 * :preview-wasm — a wasm-only harness that renders REAL FormaUI components in the browser.
 *
 * This is a spike to de-risk the docs-site live-preview pipeline (PRD §7.3/§10): compile a
 * component to a wasmJs browser bundle and confirm it runs interactively in a page. It is NOT a
 * library module — it applies no publishing plugin and ships no artifact. It deliberately does NOT
 * use the `formaui.kmp.library` convention plugin (which adds the Android target + Robolectric host
 * tests we don't want here); it declares only the wasmJs target directly.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinComposeCompiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        // Produce an executable (has a main()) so it can be bundled/served, not just a klib.
        binaries.executable()
    }

    sourceSets {
        val wasmJsMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":components"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
            }
        }
    }
}
