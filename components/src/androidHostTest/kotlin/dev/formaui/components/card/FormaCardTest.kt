/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.card

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
 * Compose UI tests for [FormaCard], hosted on the JVM via Robolectric (no device needed).
 * Covers the PRD §5.3 bar (renders + responds to a state change: the clickable card) plus the
 * three variants and the header/content/footer slot API.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun allVariants_renderContent() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaCardVariant.entries.forEach { variant ->
                        FormaCard(variant = variant) {
                            Text("card-${variant.name}")
                        }
                    }
                }
            }
        }

        // Each variant's content label is distinct, so proving all are displayed proves every
        // variant composed without crashing.
        FormaCardVariant.entries.forEach { variant ->
            composeRule.onNodeWithText("card-${variant.name}").assertIsDisplayed()
        }
    }

    @Test
    fun headerAndFooterSlots_render() {
        composeRule.setContent {
            FormaTheme {
                FormaCard(
                    header = { Text("Header slot") },
                    footer = { Text("Footer slot") },
                ) {
                    Text("Content slot")
                }
            }
        }

        composeRule.onNodeWithText("Header slot").assertIsDisplayed()
        composeRule.onNodeWithText("Content slot").assertIsDisplayed()
        composeRule.onNodeWithText("Footer slot").assertIsDisplayed()
    }

    @Test
    fun clickableCard_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaCard(
                    modifier = Modifier.testTag("card"),
                    onClick = { clicks++ },
                ) {
                    Text("Tap me")
                }
            }
        }

        composeRule.onNodeWithTag("card").performClick()
        composeRule.runOnIdle { assertEquals("clickable card should fire onClick once", 1, clicks) }
    }

    @Test
    fun disabledClickableCard_doesNotFireOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaCard(
                    modifier = Modifier.testTag("card"),
                    onClick = { clicks++ },
                    enabled = false,
                ) {
                    Text("Disabled")
                }
            }
        }

        composeRule.onNodeWithTag("card").assertIsNotEnabled()
        composeRule.onNodeWithTag("card").performClick()
        composeRule.runOnIdle { assertEquals("disabled card must not fire onClick", 0, clicks) }
    }

    @Test
    fun clickableCard_callerInteractionSource_receivesPresses() {
        // NEW capability in the press-scale rollout: the card now accepts an interactionSource
        // and forwards it to the underlying M3 clickable card.
        val source = MutableInteractionSource()
        var pressed = false
        composeRule.setContent {
            FormaTheme {
                pressed = source.collectIsPressedAsState().value
                FormaCard(
                    modifier = Modifier.testTag("card"),
                    onClick = {},
                    interactionSource = source,
                ) {
                    Text("Press me")
                }
            }
        }

        val card = composeRule.onNodeWithTag("card")
        card.performTouchInput { down(center) }
        composeRule.runOnIdle {
            assertTrue("Caller-supplied source must observe the press", pressed)
        }
        card.performTouchInput { up() }
        composeRule.runOnIdle {
            assertFalse("Caller-supplied source must observe the release", pressed)
        }
    }

    @Test
    fun pressScale_disabledViaNullSpec_stillClicks() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaCard(
                    modifier = Modifier.testTag("card"),
                    onClick = { clicks++ },
                    pressAnimationSpec = null,
                ) {
                    Text("No scale")
                }
            }
        }

        composeRule.onNodeWithTag("card").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun togglingOnClick_betweenClickableAndStatic_isStable() {
        // The press-scale internals (remember { Animatable } + LaunchedEffect) exist only in the
        // onClick != null branch. Flipping onClick between non-null and null across
        // recompositions changes composition groups, discarding and recreating that state — this
        // must never crash or wedge, including when the flip happens right after a press.
        var clicks = 0
        var clickable by mutableStateOf(true)
        composeRule.setContent {
            FormaTheme {
                FormaCard(
                    modifier = Modifier.testTag("card"),
                    onClick = if (clickable) { { clicks++ } } else null,
                ) {
                    Text("Toggle me")
                }
            }
        }

        val card = composeRule.onNodeWithTag("card")

        // Press the clickable card, then flip to static immediately after the gesture.
        card.performTouchInput { down(center) }
        composeRule.waitForIdle()
        card.performTouchInput { up() }
        composeRule.runOnIdle { assertEquals(1, clicks) }
        composeRule.runOnIdle { clickable = false }
        composeRule.waitForIdle()
        card.assertIsDisplayed()

        // Flip back to clickable: a fresh press-scale group is created and the card works again.
        composeRule.runOnIdle { clickable = true }
        card.performClick()
        composeRule.runOnIdle { assertEquals(2, clicks) }
    }
}
