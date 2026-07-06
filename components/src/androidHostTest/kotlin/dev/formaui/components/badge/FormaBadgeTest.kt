/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.badge

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaBadge], hosted on the JVM via Robolectric. Covers the dot form, the
 * numeric form, and the `maxCount` overflow ("99+"), each anchored with [FormaBadgedBox] as in
 * real usage.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaBadgeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun numericBadge_showsCount() {
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(badge = { FormaBadge(count = 5) }) {
                    Text("Inbox")
                }
            }
        }

        composeRule.onNodeWithText("Inbox").assertIsDisplayed()
        composeRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun overflowingCount_showsMaxCountPlus() {
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(badge = { FormaBadge(count = 150, maxCount = 99) }) {
                    Text("Alerts")
                }
            }
        }

        composeRule.onNodeWithText("99+").assertIsDisplayed()
        // The raw value is never shown once it overflows the cap.
        composeRule.onNodeWithText("150").assertDoesNotExist()
    }

    @Test
    fun badgedBox_anchorsBadgeOverContent() {
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(badge = { FormaBadge(count = 3) }) {
                    Text("Cart")
                }
            }
        }

        // The wrapper renders both the anchored content and the badge over it.
        composeRule.onNodeWithText("Cart").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()
    }

    @Test
    fun dotBadge_rendersWithoutAnyNumber() {
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(badge = { FormaBadge(modifier = Modifier.testTag("dot")) }) {
                    Text("Updates")
                }
            }
        }

        // The dot badge composes (renders without crashing) and carries no numeric text.
        composeRule.onNodeWithText("Updates").assertIsDisplayed()
        composeRule.onNodeWithTag("dot").assertExists()
        composeRule.onNodeWithText("0").assertDoesNotExist()
    }
}
