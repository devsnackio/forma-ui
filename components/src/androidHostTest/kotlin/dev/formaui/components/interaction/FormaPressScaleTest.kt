/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.interaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [Modifier.formaPressScale], hosted on the JVM via Robolectric (no device
 * needed). Covers the reusable, non-button story: the observe-only contract, the
 * `animationSpec = null` disable path, the `require` on [FormaPressScaleDefaults.PressedScale]
 * overrides, and the sequential-queue regression class (a quick tap must play the full dip and
 * leave the element settled and interactive — the graphicsLayer scale value itself is not
 * readable via semantics, so settledness + interactivity is the asserted contract).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaPressScaleTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun nullSpec_returnsReceiverUnchanged() {
        var result: Modifier? = null
        composeRule.setContent {
            FormaTheme {
                val source = remember { MutableInteractionSource() }
                result = Modifier.formaPressScale(source, animationSpec = null)
            }
        }

        // The receiver is the Modifier companion, so identity proves the disable path adds no
        // modifier nodes at all (the chart components' animationSpec = null convention).
        composeRule.runOnIdle { assertSame(Modifier, result) }
    }

    @Test
    fun standalone_pressAndRelease_composesAndStaysDisplayed() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                val source = remember { MutableInteractionSource() }
                Box(
                    Modifier
                        .size(64.dp)
                        .testTag("pressable")
                        .clickable(interactionSource = source, indication = null) { clicks++ }
                        .formaPressScale(source),
                )
            }
        }

        val pressable = composeRule.onNodeWithTag("pressable")

        // Hold the press: the finite spring settles at pressedScale and the element stays
        // displayed (the scale is purely visual).
        pressable.performTouchInput { down(center) }
        composeRule.waitForIdle()
        pressable.assertIsDisplayed()
        pressable.performTouchInput { up() }
        composeRule.waitForIdle()

        // The modifier is observe-only, so the clickable it is layered on keeps working.
        var clicksAfterRelease = 0
        composeRule.runOnIdle { clicksAfterRelease = clicks }
        pressable.performClick()
        composeRule.runOnIdle {
            assertEquals(clicksAfterRelease + 1, clicks)
        }
    }

    @Test
    fun quickTap_playsFullDipAndSettles() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                val source = remember { MutableInteractionSource() }
                Box(
                    Modifier
                        .size(64.dp)
                        .testTag("pressable")
                        .clickable(interactionSource = source, indication = null) { clicks++ }
                        .formaPressScale(source),
                )
            }
        }

        val pressable = composeRule.onNodeWithTag("pressable")

        // Pause the clock so the Release is guaranteed to arrive before the dip has played a
        // single frame — the exact interleaving where the pre-Animatable implementation
        // retargeted mid-dip and skipped the animation. The sequential collect must instead
        // queue the Release, play the full dip, then spring back and settle.
        composeRule.mainClock.autoAdvance = false
        pressable.performTouchInput {
            down(center)
            up()
        }
        // Advance well past dip (100ms tween) + spring-back settle time, then settle for real.
        composeRule.mainClock.advanceTimeBy(2_000)
        composeRule.mainClock.autoAdvance = true
        composeRule.waitForIdle()

        pressable.assertIsDisplayed()
        composeRule.runOnIdle { assertEquals(1, clicks) }

        // The animation queue must be fully drained and interactive again: a follow-up click
        // still fires (this is what would wedge if sequential collection misbehaved).
        pressable.performClick()
        composeRule.runOnIdle { assertEquals(2, clicks) }
    }

    @Test
    fun rapidDoubleTap_bothClicksRegister_andSettles() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                val source = remember { MutableInteractionSource() }
                Box(
                    Modifier
                        .size(64.dp)
                        .testTag("pressable")
                        .clickable(interactionSource = source, indication = null) { clicks++ }
                        .formaPressScale(source),
                )
            }
        }

        val pressable = composeRule.onNodeWithTag("pressable")

        // Two back-to-back taps pile four press interactions into the sequential queue
        // (Press, Release, Press, Release). Both specs are finite, so the queue must drain and
        // both clicks must land.
        pressable.performTouchInput {
            down(center)
            up()
        }
        pressable.performTouchInput {
            down(center)
            up()
        }
        composeRule.waitForIdle()

        pressable.assertIsDisplayed()
        composeRule.runOnIdle { assertEquals(2, clicks) }

        // Still interactive after the burst — the queue did not wedge.
        pressable.performClick()
        composeRule.runOnIdle { assertEquals(3, clicks) }
    }

    @Test
    fun invalidPressedScale_throws() {
        var thrown: Throwable? = null
        try {
            composeRule.setContent {
                FormaTheme {
                    val source = remember { MutableInteractionSource() }
                    Box(Modifier.formaPressScale(source, pressedScale = 0f))
                }
            }
        } catch (t: Throwable) {
            thrown = t
        }

        // The require() may be wrapped by the compose runtime — walk the cause chain.
        var cause = thrown
        while (cause != null && cause !is IllegalArgumentException) {
            cause = cause.cause
        }
        assertTrue(
            "Expected IllegalArgumentException from require(), but got: $thrown",
            cause is IllegalArgumentException,
        )
    }
}
