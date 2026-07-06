/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.radiobutton

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaRadioButton], hosted on the JVM via Robolectric. Covers the click
 * callback (state-change), the selected semantics, and the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaRadioButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun click_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaRadioButton(
                    selected = false,
                    onClick = { clicks++ },
                    modifier = Modifier.testTag("radio"),
                )
            }
        }

        composeRule.onNodeWithTag("radio").performClick()
        composeRule.runOnIdle { assertEquals("radio click should fire onClick once", 1, clicks) }
    }

    @Test
    fun reflectsSelectedState() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaRadioButton(selected = true, onClick = {}, modifier = Modifier.testTag("on"))
                    FormaRadioButton(selected = false, onClick = {}, modifier = Modifier.testTag("off"))
                }
            }
        }

        composeRule.onNodeWithTag("on").assertIsSelected()
        composeRule.onNodeWithTag("off").assertIsNotSelected()
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaRadioButton(
                    selected = false,
                    onClick = { clicks++ },
                    enabled = false,
                    modifier = Modifier.testTag("radio"),
                )
            }
        }

        composeRule.onNodeWithTag("radio").assertIsNotEnabled()
        composeRule.onNodeWithTag("radio").performClick()
        composeRule.runOnIdle { assertEquals("disabled radio must not fire onClick", 0, clicks) }
    }
}
