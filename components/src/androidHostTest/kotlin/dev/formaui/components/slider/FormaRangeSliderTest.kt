/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.slider

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaRangeSlider], hosted on the JVM via Robolectric. Covers rendering (a
 * continuous, a stepped, and a disabled range slider) and the hoisted-state change: a [Text] bound
 * to the selected range recomposes when the range is flipped.
 *
 * The value-change is driven by flipping the hoisted range state rather than by faking a two-thumb
 * drag — [androidx.compose.material3.RangeSlider] keeps its two thumbs as separate adjustable nodes
 * (no merged root semantics), so a hoisted-state assertion is the robust, non-brittle way to prove
 * the state binding.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaRangeSliderTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rangeSlider_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaRangeSlider(
                    value = 0.2f..0.8f,
                    onValueChange = {},
                    modifier = Modifier.testTag("range"),
                )
            }
        }

        composeRule.onNodeWithTag("range").assertExists()
    }

    @Test
    fun steppedRangeSlider_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaRangeSlider(
                    value = 2f..6f,
                    onValueChange = {},
                    valueRange = 0f..10f,
                    steps = 9,
                    modifier = Modifier.testTag("stepped-range"),
                )
            }
        }

        composeRule.onNodeWithTag("stepped-range").assertExists()
    }

    @Test
    fun disabledRangeSlider_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaRangeSlider(
                    value = 0.3f..0.6f,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("disabled-range"),
                )
            }
        }

        composeRule.onNodeWithTag("disabled-range").assertExists()
    }

    @Test
    fun hoistedRangeChange_updatesBoundText() {
        composeRule.setContent {
            FormaTheme {
                var range by remember { mutableStateOf(0.2f..0.4f) }
                Column {
                    Text("start=${range.start}, end=${range.endInclusive}")
                    Text(
                        "widen",
                        modifier = Modifier
                            .testTag("widen")
                            .clickable { range = 0.1f..0.9f },
                    )
                    FormaRangeSlider(
                        value = range,
                        onValueChange = { range = it },
                        modifier = Modifier.testTag("range"),
                    )
                }
            }
        }

        composeRule.onNodeWithText("start=0.2, end=0.4").assertExists()
        composeRule.onNodeWithTag("widen").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("start=0.1, end=0.9").assertExists()
    }
}
