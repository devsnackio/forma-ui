/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.emptystate

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
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
 * Compose UI tests for [FormaEmptyState], hosted on the JVM via Robolectric. Covers the required
 * title, the optional description, the icon slot, and the action slot including its click callback.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaEmptyStateTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun title_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaEmptyState(title = "No transactions yet")
            }
        }

        composeRule.onNodeWithText("No transactions yet").assertIsDisplayed()
    }

    @Test
    fun description_rendersWhenProvided() {
        composeRule.setContent {
            FormaTheme {
                FormaEmptyState(
                    title = "No transactions yet",
                    description = "They will appear here once you make a payment.",
                )
            }
        }

        composeRule.onNodeWithText("They will appear here once you make a payment.").assertIsDisplayed()
    }

    @Test
    fun iconSlot_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaEmptyState(
                    title = "No transactions yet",
                    icon = { Text("ICON") },
                )
            }
        }

        composeRule.onNodeWithText("ICON").assertIsDisplayed()
    }

    @Test
    fun actionSlot_rendersAndFiresClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaEmptyState(
                    title = "No transactions yet",
                    action = {
                        Button(onClick = { clicks++ }) { Text("Add payment") }
                    },
                )
            }
        }

        composeRule.onNodeWithText("Add payment").assertIsDisplayed()
        composeRule.onNodeWithText("Add payment").performClick()
        composeRule.runOnIdle { assertEquals("empty-state action should fire its onClick", 1, clicks) }
    }
}
