/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.switch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaSwitch], hosted on the JVM via Robolectric. Covers the toggle
 * state-change (callback fires + on/off state flips) and the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaSwitchTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun toggle_firesCallback_andFlipsOnState() {
        var toggledTo: Boolean? = null
        composeRule.setContent {
            FormaTheme {
                // Hoisted state owned by the caller.
                var checked by remember { mutableStateOf(false) }
                FormaSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it; toggledTo = it },
                    modifier = Modifier.testTag("switch"),
                )
            }
        }

        composeRule.onNodeWithTag("switch").assertIsOff()
        composeRule.onNodeWithTag("switch").performClick()

        composeRule.runOnIdle { assertEquals(true, toggledTo) }
        composeRule.onNodeWithTag("switch").assertIsOn()
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotToggle() {
        var toggledTo: Boolean? = null
        composeRule.setContent {
            FormaTheme {
                FormaSwitch(
                    checked = false,
                    onCheckedChange = { toggledTo = it },
                    enabled = false,
                    modifier = Modifier.testTag("switch"),
                )
            }
        }

        composeRule.onNodeWithTag("switch").assertIsNotEnabled()
        composeRule.onNodeWithTag("switch").performClick()
        composeRule.runOnIdle { assertNull("disabled switch must not fire onCheckedChange", toggledTo) }
    }
}
