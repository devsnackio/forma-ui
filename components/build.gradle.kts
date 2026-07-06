plugins {
    id("formaui.kmp.library")
    id("formaui.publishing")
}

kotlin {
    android {
        namespace = "dev.formaui.components"
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core"))
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            // Multiplatform @Preview annotation (compiles on both android + wasmJs).
            implementation(libs.compose.ui.toolingPreview)
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
