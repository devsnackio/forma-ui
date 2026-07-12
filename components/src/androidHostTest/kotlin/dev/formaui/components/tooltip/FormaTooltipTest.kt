/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.tooltip

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaTooltip], hosted on the JVM via Robolectric. Tooltips render into
 * their own popup, but the test framework merges that popup's semantics into the root tree, so
 * content is assertable via [onNodeWithText]. Visibility is driven through the hoisted
 * [TooltipState] rather than real long-press/hover gestures, per the proven Compose testing idiom.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaTooltipTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun anchorContent_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaTooltip(text = "Search") {
                    Text("Anchor")
                }
            }
        }

        composeRule.onNodeWithText("Anchor").assertIsDisplayed()
    }

    @Test
    fun plainTooltip_show_showsText() {
        lateinit var tooltipState: TooltipState
        composeRule.setContent {
            FormaTheme {
                val state = rememberTooltipState()
                tooltipState = state
                // Trigger show() reactively once composed, so the anchor is already laid out
                // (and its bounds known to the tooltip's position provider) by the time the
                // tooltip itself composes — avoids a same-frame race with initialIsVisible=true.
                LaunchedEffect(Unit) { state.show() }
                FormaTooltip(text = "Adds the item to your cart", state = state) {
                    Text("Anchor")
                }
            }
        }

        composeRule.waitForIdle()
        composeRule.runOnIdle { assertTrue(tooltipState.isVisible) }
        composeRule.onNodeWithText("Adds the item to your cart").assertIsDisplayed()
    }

    @Test
    fun richTooltip_show_showsTitleTextAndAction() {
        lateinit var tooltipState: TooltipState
        composeRule.setContent {
            FormaTheme {
                val state = rememberTooltipState(isPersistent = true)
                tooltipState = state
                LaunchedEffect(Unit) { state.show() }
                FormaTooltip(
                    text = "You can undo this for the next 10 seconds.",
                    variant = FormaTooltipVariant.Rich,
                    title = { Text("Item archived") },
                    action = { Text("Undo") },
                    state = state,
                ) {
                    Text("Anchor")
                }
            }
        }

        composeRule.waitForIdle()
        composeRule.runOnIdle { assertTrue(tooltipState.isVisible) }
        composeRule.onNodeWithText("Item archived").assertIsDisplayed()
        composeRule.onNodeWithText("You can undo this for the next 10 seconds.").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").assertIsDisplayed()
    }

    @Test
    fun state_reflectsShowAndDismiss() {
        lateinit var tooltipState: TooltipState
        composeRule.setContent {
            FormaTheme {
                var shouldShow by remember { mutableStateOf(false) }
                val state = rememberTooltipState()
                tooltipState = state

                LaunchedEffect(shouldShow) {
                    if (shouldShow) state.show()
                }

                FormaTooltip(text = "Tap target", state = state) {
                    Text(
                        "Anchor",
                        modifier = Modifier
                            .testTag("anchor")
                            .clickable { shouldShow = true },
                    )
                }
            }
        }

        composeRule.runOnIdle { assertFalse(tooltipState.isVisible) }

        composeRule.onNodeWithTag("anchor").performClick()
        composeRule.waitForIdle()

        composeRule.runOnIdle { assertTrue(tooltipState.isVisible) }
        composeRule.onNodeWithText("Tap target").assertIsDisplayed()

        composeRule.runOnIdle { tooltipState.dismiss() }
        composeRule.waitForIdle()

        composeRule.runOnIdle { assertFalse(tooltipState.isVisible) }
    }
}
