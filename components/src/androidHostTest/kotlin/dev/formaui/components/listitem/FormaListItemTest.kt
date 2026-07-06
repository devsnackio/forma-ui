/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.listitem

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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
 * Compose UI tests for [FormaListItem], hosted on the JVM via Robolectric. Covers the headline,
 * the three-line layout (overline + supporting), leading/trailing slots, the click callback, and
 * the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaListItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun headline_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(headline = "Single line")
            }
        }

        composeRule.onNodeWithText("Single line").assertIsDisplayed()
    }

    @Test
    fun threeLine_showsOverlineAndSupporting() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Payment received",
                    overline = "Today, 09:41",
                    supporting = "$1,240.00 from Grace Hopper",
                )
            }
        }

        composeRule.onNodeWithText("Today, 09:41").assertIsDisplayed()
        composeRule.onNodeWithText("Payment received").assertIsDisplayed()
        composeRule.onNodeWithText("$1,240.00 from Grace Hopper").assertIsDisplayed()
    }

    @Test
    fun leadingAndTrailingSlots_render() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "With slots",
                    leading = { Text("LEAD") },
                    trailing = { Text("TRAIL") },
                )
            }
        }

        composeRule.onNodeWithText("LEAD").assertIsDisplayed()
        composeRule.onNodeWithText("TRAIL").assertIsDisplayed()
    }

    @Test
    fun clickable_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Tap row",
                    onClick = { clicks++ },
                    modifier = Modifier.testTag("row"),
                )
            }
        }

        composeRule.onNodeWithTag("row").performClick()
        composeRule.runOnIdle { assertEquals("clickable row should fire onClick once", 1, clicks) }
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Disabled row",
                    onClick = { clicks++ },
                    enabled = false,
                    modifier = Modifier.testTag("row"),
                )
            }
        }

        composeRule.onNodeWithTag("row").assertIsNotEnabled()
        composeRule.onNodeWithTag("row").performClick()
        composeRule.runOnIdle { assertEquals("disabled row must not fire onClick", 0, clicks) }
    }
}
