plugins {
    `kotlin-dsl`
}

// The convention plugins here apply the KMP, Android, and Compose Gradle plugins,
// so those plugin artifacts must be on this project's compile classpath.
dependencies {
    implementation(libs.build.kotlin.gradlePlugin)
    implementation(libs.build.kotlin.composeCompilerGradlePlugin)
    implementation(libs.build.android.gradlePlugin)
    implementation(libs.build.compose.gradlePlugin)
}
