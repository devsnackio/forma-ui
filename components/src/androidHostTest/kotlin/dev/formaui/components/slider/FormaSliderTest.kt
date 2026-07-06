/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.slider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performSemanticsAction
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaSlider], hosted on the JVM via Robolectric. Covers rendering, the
 * value-change state-change (driven via the SetProgress semantics action), and the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaSliderTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun slider_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaSlider(value = 0.5f, onValueChange = {}, modifier = Modifier.testTag("slider"))
            }
        }

        composeRule.onNodeWithTag("slider").assertExists()
    }

    @Test
    fun setProgress_firesOnValueChange() {
        var lastValue = -1f
        composeRule.setContent {
            FormaTheme {
                // Hoisted state owned by the caller.
                var value by remember { mutableFloatStateOf(0.5f) }
                FormaSlider(
                    value = value,
                    onValueChange = { value = it; lastValue = it },
                    modifier = Modifier.testTag("slider"),
                )
            }
        }

        composeRule.onNodeWithTag("slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { setProgress -> setProgress(0.7f) }

        composeRule.runOnIdle { assertEquals(0.7f, lastValue, 0.01f) }
    }

    @Test
    fun disabled_isNotEnabled() {
        composeRule.setContent {
            FormaTheme {
                FormaSlider(
                    value = 0.5f,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("slider"),
                )
            }
        }

        composeRule.onNodeWithTag("slider").assertIsNotEnabled()
    }
}
