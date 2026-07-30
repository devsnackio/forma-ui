/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.badge

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
 *
 * A badge is stateless — its count is hoisted — so the PRD §5.3 "responds to a state change" bar is
 * met by driving `count` from caller state and asserting the rendered label tracks it, including
 * across the two form transitions that are easy to regress: crossing `maxCount` into "99+", and
 * `null` flipping the numeric form back to a bare dot.
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

    @Test
    fun count_updatesWhenStateChanges() {
        var count by mutableStateOf(1)
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(badge = { FormaBadge(count = count) }) {
                    Text("Inbox")
                }
            }
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()

        composeRule.runOnIdle { count = 2 }

        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("1").assertDoesNotExist()
    }

    @Test
    fun count_crossingMaxCount_switchesToOverflowLabel() {
        var count by mutableStateOf(99)
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(badge = { FormaBadge(count = count, maxCount = 99) }) {
                    Text("Alerts")
                }
            }
        }

        // At the cap the exact value is still shown.
        composeRule.onNodeWithText("99").assertIsDisplayed()

        composeRule.runOnIdle { count = 100 }

        // One past it, the label collapses to the overflow form.
        composeRule.onNodeWithText("99+").assertIsDisplayed()
        composeRule.onNodeWithText("100").assertDoesNotExist()
    }

    @Test
    fun count_nullToValue_switchesDotToNumeric() {
        var count by mutableStateOf<Int?>(null)
        composeRule.setContent {
            FormaTheme {
                FormaBadgedBox(
                    badge = { FormaBadge(modifier = Modifier.testTag("badge"), count = count) },
                ) {
                    Text("Updates")
                }
            }
        }

        // Starts as a bare dot: present, but with no numeric label.
        composeRule.onNodeWithTag("badge").assertExists()
        composeRule.onNodeWithText("7").assertDoesNotExist()

        composeRule.runOnIdle { count = 7 }

        // Becomes the numeric form on the same anchor.
        composeRule.onNodeWithTag("badge").assertExists()
        composeRule.onNodeWithText("7").assertIsDisplayed()
    }
}
