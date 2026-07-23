/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaButton], hosted on the JVM via Robolectric (no device needed).
 * Covers the PRD §5.3 bar: renders without crashing + responds correctly to a state change.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersLabel() {
        composeRule.setContent {
            FormaTheme {
                FormaButton(onClick = {}) { Text("Pay now") }
            }
        }

        composeRule.onNodeWithText("Pay now").assertIsDisplayed()
    }

    @Test
    fun click_updatesHoistedState() {
        composeRule.setContent {
            FormaTheme {
                var count by remember { mutableStateOf(0) }
                FormaButton(onClick = { count++ }) { Text("Count: $count") }
            }
        }

        composeRule.onNodeWithText("Count: 0").assertIsDisplayed()
        composeRule.onNodeWithText("Count: 0").performClick()
        composeRule.onNodeWithText("Count: 1").assertIsDisplayed()
    }

    @Test
    fun disabled_doesNotInvokeOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaButton(onClick = { clicks++ }, enabled = false) { Text("Submit") }
            }
        }

        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assert(clicks == 0) { "Disabled button must not invoke onClick" } }
    }

    @Test
    fun allVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaButtonVariant.entries.forEach { variant ->
                        FormaButton(onClick = {}, variant = variant) { Text(variant.name) }
                    }
                }
            }
        }

        // Each variant's label is distinct (the enum constant name), so proving every one is
        // displayed proves all five variants composed without crashing.
        FormaButtonVariant.entries.forEach { variant ->
            composeRule.onNodeWithText(variant.name).assertIsDisplayed()
        }
    }

    @Test
    fun enforces48dpTouchTarget() {
        composeRule.setContent {
            FormaTheme {
                FormaButton(onClick = {}) { Text("Tap") }
            }
        }

        composeRule.onNodeWithText("Tap")
            .assertIsEnabled()
            .assertHeightIsAtLeast(FormaButtonDefaults.MinTouchTargetSize)
    }

    @Test
    fun pressScale_whileHeld_keepsMinTouchTargetHeight() {
        composeRule.setContent {
            FormaTheme {
                FormaButton(onClick = {}) { Text("Hold") }
            }
        }

        val button = composeRule.onNodeWithText("Hold")
        button.performTouchInput { down(center) }
        composeRule.waitForIdle()
        // The press-scale is a pure graphicsLayer effect: while held at pressedScale, the
        // measured layout — and therefore the 48dp minimum touch target — must not shrink.
        button.assertHeightIsAtLeast(FormaButtonDefaults.MinTouchTargetSize)
        button.performTouchInput { up() }
        composeRule.waitForIdle()
    }

    @Test
    fun pressScale_disabledViaNullSpec_stillClicks() {
        var count = 0
        composeRule.setContent {
            FormaTheme {
                FormaButton(onClick = { count++ }, pressAnimationSpec = null) { Text("No scale") }
            }
        }

        composeRule.onNodeWithText("No scale").performClick()
        composeRule.runOnIdle { assertEquals(1, count) }
    }

    @Test
    fun callerInteractionSource_stillReceivesPresses() {
        val source = MutableInteractionSource()
        var pressed = false
        composeRule.setContent {
            FormaTheme {
                pressed = source.collectIsPressedAsState().value
                FormaButton(onClick = {}, interactionSource = source) { Text("Press me") }
            }
        }

        // Guards the interactionSource materialization refactor: a caller-supplied source must
        // still receive every press interaction from the underlying Material 3 button.
        val button = composeRule.onNodeWithText("Press me")
        button.performTouchInput { down(center) }
        composeRule.runOnIdle {
            assertTrue("Caller-supplied source must observe the press", pressed)
        }
        button.performTouchInput { up() }
        composeRule.runOnIdle {
            assertFalse("Caller-supplied source must observe the release", pressed)
        }
    }
}
