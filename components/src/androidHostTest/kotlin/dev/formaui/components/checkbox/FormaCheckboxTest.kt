/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.checkbox

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
 * Compose UI tests for [FormaCheckbox], hosted on the JVM via Robolectric. Covers the toggle
 * state-change (callback fires + checked state flips) and the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaCheckboxTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun toggle_firesCallback_andFlipsCheckedState() {
        var toggledTo: Boolean? = null
        composeRule.setContent {
            FormaTheme {
                // Hoisted state owned by the caller.
                var checked by remember { mutableStateOf(false) }
                FormaCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it; toggledTo = it },
                    modifier = Modifier.testTag("checkbox"),
                )
            }
        }

        composeRule.onNodeWithTag("checkbox").assertIsOff()
        composeRule.onNodeWithTag("checkbox").performClick()

        composeRule.runOnIdle { assertEquals(true, toggledTo) }
        composeRule.onNodeWithTag("checkbox").assertIsOn()
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotToggle() {
        var toggledTo: Boolean? = null
        composeRule.setContent {
            FormaTheme {
                FormaCheckbox(
                    checked = false,
                    onCheckedChange = { toggledTo = it },
                    enabled = false,
                    modifier = Modifier.testTag("checkbox"),
                )
            }
        }

        composeRule.onNodeWithTag("checkbox").assertIsNotEnabled()
        composeRule.onNodeWithTag("checkbox").performClick()
        composeRule.runOnIdle { assertNull("disabled checkbox must not fire onCheckedChange", toggledTo) }
    }
}
