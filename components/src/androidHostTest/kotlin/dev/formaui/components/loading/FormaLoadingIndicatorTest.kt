/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.loading

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
 *
 * The indicator is stateless — `progress` is hoisted — so the PRD §5.3 "responds to a state change"
 * bar is met by driving `progress` from caller state and asserting the exposed
 * [ProgressBarRangeInfo] tracks it, including the `null` ⇄ value flip that switches the component
 * between its indeterminate and determinate forms.
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

    @Test
    fun determinate_progressSemanticsFollowStateChanges() {
        var progress by mutableStateOf(0.25f)
        composeRule.setContent {
            FormaTheme {
                FormaLoadingIndicator(
                    modifier = Modifier.testTag("progress"),
                    progress = progress,
                    contentDescription = "Uploading",
                )
            }
        }

        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.25f, range = 0f..1f))

        composeRule.runOnIdle { progress = 0.75f }

        // The advertised fraction tracks the hoisted state, not just the initial composition.
        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.75f, range = 0f..1f))
    }

    @Test
    fun indeterminateToDeterminate_switchesFormOnStateChange() {
        var progress by mutableStateOf<Float?>(null)
        composeRule.setContent {
            FormaTheme {
                FormaLoadingIndicator(
                    modifier = Modifier.testTag("progress"),
                    progress = progress,
                    contentDescription = "Loading",
                )
            }
        }

        // null ⇒ the indeterminate form, which advertises itself as such to accessibility.
        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo.Indeterminate)

        composeRule.runOnIdle { progress = 0.4f }

        // A non-null value swaps in the determinate form, carrying the fraction.
        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.4f, range = 0f..1f))
        // The contentDescription survives the swap.
        composeRule.onNodeWithContentDescription("Loading").assertExists()
    }

    @Test
    fun linearDeterminate_progressSemanticsFollowStateChanges() {
        var progress by mutableStateOf(0f)
        composeRule.setContent {
            FormaTheme {
                FormaLoadingIndicator(
                    modifier = Modifier.testTag("progress"),
                    variant = FormaLoadingIndicatorVariant.Linear,
                    progress = progress,
                    contentDescription = "Downloading",
                )
            }
        }

        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0f, range = 0f..1f))

        composeRule.runOnIdle { progress = 1f }

        // Same hoisting contract holds for the linear variant, through to completion.
        composeRule.onNodeWithTag("progress")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 1f, range = 0f..1f))
    }
}
