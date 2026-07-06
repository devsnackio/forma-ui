// Root build script. Real configuration lives in the build-logic convention plugins
// (formaui.kmp.library) applied by each module. Plugins are declared here with
// `apply false` only so their versions resolve consistently across the build.
plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinComposeCompiler) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.composeMultiplatform) apply false
}

group = "dev.formaui"
version = "0.1.0"
