/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.iconbutton

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.height
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaIconButton], hosted on the JVM via Robolectric. Covers all four
 * variants rendering, the click callback firing, and the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaIconButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun allVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaIconButtonVariant.entries.forEach { variant ->
                        FormaIconButton(
                            onClick = {},
                            variant = variant,
                            modifier = Modifier.testTag("icon-button-${variant.name}"),
                        ) {
                            Text("X")
                        }
                    }
                }
            }
        }

        FormaIconButtonVariant.entries.forEach { variant ->
            composeRule.onNodeWithTag("icon-button-${variant.name}").assertExists()
        }
    }

    @Test
    fun click_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaIconButton(
                    onClick = { clicks++ },
                    variant = FormaIconButtonVariant.Filled,
                    modifier = Modifier.testTag("icon-button"),
                ) {
                    Text("X")
                }
            }
        }

        composeRule.onNodeWithTag("icon-button").assertExists()
        composeRule.onNodeWithTag("icon-button").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun click_updatesHoistedState() {
        composeRule.setContent {
            FormaTheme {
                var count by remember { mutableStateOf(0) }
                FormaIconButton(
                    onClick = { count++ },
                    modifier = Modifier.testTag("icon-button"),
                ) {
                    Text("Count: $count")
                }
            }
        }

        composeRule.onNodeWithText("Count: 0").assertExists()
        composeRule.onNodeWithTag("icon-button").performClick()
        composeRule.onNodeWithText("Count: 1").assertExists()
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotInvokeOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaIconButton(
                    onClick = { clicks++ },
                    enabled = false,
                    modifier = Modifier.testTag("icon-button"),
                ) {
                    Text("X")
                }
            }
        }

        composeRule.onNodeWithTag("icon-button").assertIsNotEnabled()
        composeRule.onNodeWithTag("icon-button").performClick()
        composeRule.runOnIdle { assertEquals(0, clicks) }
    }

    @Test
    fun pressScale_disabledViaNullSpec_stillClicks() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaIconButton(
                    onClick = { clicks++ },
                    pressAnimationSpec = null,
                    modifier = Modifier.testTag("icon-button"),
                ) {
                    Text("X")
                }
            }
        }

        composeRule.onNodeWithTag("icon-button").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun pressScale_whileHeld_keepsMinTouchTarget() {
        composeRule.setContent {
            FormaTheme {
                FormaIconButton(
                    onClick = {},
                    modifier = Modifier.testTag("icon-button"),
                ) {
                    Text("X")
                }
            }
        }

        val button = composeRule.onNodeWithTag("icon-button")
        // The M3 icon button measures 40dp — its 48dp accessible touch target comes from M3's
        // minimumInteractiveComponentSize touch-bounds EXPANSION, which is not part of measured
        // size — so the invariant here is "held size == rest size", not an absolute 48dp.
        val restHeight = button.getUnclippedBoundsInRoot().height

        button.performTouchInput { down(center) }
        composeRule.waitForIdle()
        // The icon button uses the deepest dip in the library (FormaIconButtonDefaults
        // PressedScale 0.92), so this is the strongest check that the press-scale is a pure
        // graphicsLayer effect: the measured layout must not shrink while held.
        button.assertHeightIsEqualTo(restHeight)
        button.performTouchInput { up() }
        composeRule.waitForIdle()
    }
}
