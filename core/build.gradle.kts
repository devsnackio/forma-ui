plugins {
    id("formaui.kmp.library")
    id("formaui.publishing")
}

// Bundle the Public Sans typeface (in commonMain/composeResources/font) into the published
// artifact and generate a `Res` accessor for it. Font files are loaded by FormaTypography.
compose.resources {
    packageOfResClass = "dev.formaui.core.generated.resources"
    publicResClass = false
}

kotlin {
    android {
        namespace = "dev.formaui.core"
    }

    sourceSets {
        commonMain.dependencies {
            // Exposed through FormaUI's public token types (ColorScheme, Typography, Shapes,
            // TextStyle), so these are `api` — consumers get the M3 types transitively.
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.ui)
            // Compose Resources runtime — loads the bundled Public Sans font from commonMain.
            implementation(libs.compose.components.resources)
        }

        // AGP 9 KMP: host (JVM/Robolectric) unit tests live in `androidHostTest` (was androidUnitTest).
        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
            implementation(libs.robolectric)
            implementation(libs.androidx.compose.ui.test.junit4)
            implementation(libs.androidx.compose.ui.test.manifest)
        }
    }
}
