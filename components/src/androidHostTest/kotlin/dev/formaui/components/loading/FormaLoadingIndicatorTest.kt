/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.loading

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaLoadingIndicator], hosted on the JVM via Robolectric. Covers circular
 * and linear rendering, the determinate progress semantics, and the accessibility
 * `contentDescription`.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaLoadingIndicatorTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun circularIndeterminate_rendersAndSurfacesContentDescription() {
        composeRule.setContent {
            FormaTheme {
                FormaLoadingIndicator(
                    modifier = Modifier.testTag("progress"),
                    contentDescription = "Loading",
                )
            }
        }

        composeRule.onNodeWithTag("progress").assertExists()
        // The contentDescription surfaces to accessibility.
        composeRule.onNodeWithContentDescription("Loading").assertExists()
    }

    @Test
    fun linear_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaLoadingIndicator(
                    modifier = Modifier.testTag("progress"),
                    variant = FormaLoadingIndicatorVariant.Linear,
                    contentDescription = "Loading linear",
                )
            }
        }

        composeRule.onNodeWithTag("progress").assertExists()
    }

    @Test
    fun determinate_reflectsProgressSemantics() {
        composeRule.setContent {
            FormaTheme {
                FormaLoadingIndicator(
                    modifier = Modifier.testTag("progress"),
                    variant = FormaLoadingIndicatorVariant.Circular,
                    progress = 0.5f,
                    contentDescription = "Half loaded",
                )
            }
        }

        // A determinate indicator exposes ProgressBarRangeInfo with the current fraction.
        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.5f, range = 0f..1f))
    }
}
