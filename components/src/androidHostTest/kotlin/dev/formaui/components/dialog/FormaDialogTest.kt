/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.dialog

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for the FormaUI dialogs, hosted on the JVM via Robolectric. Compose dialogs
 * render into their own window, but the test framework merges that window's semantics into the
 * root tree, so content and actions are assertable via [onNodeWithText].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    // --- FormaAlertDialog -------------------------------------------------------------------

    @Test
    fun alertDialog_showsTitleTextAndButtons() {
        composeRule.setContent {
            FormaTheme {
                FormaAlertDialog(
                    onDismissRequest = {},
                    title = "Delete payment?",
                    text = "This action can't be undone.",
                    confirmButton = { Button(onClick = {}) { Text("Delete") } },
                    dismissButton = { Button(onClick = {}) { Text("Cancel") } },
                )
            }
        }

        composeRule.onNodeWithText("Delete payment?").assertIsDisplayed()
        composeRule.onNodeWithText("This action can't be undone.").assertIsDisplayed()
        composeRule.onNodeWithText("Delete").assertIsDisplayed()
        composeRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun alertDialog_confirmButton_firesOnClick() {
        var confirmed = false
        composeRule.setContent {
            FormaTheme {
                FormaAlertDialog(
                    onDismissRequest = {},
                    title = "Delete payment?",
                    confirmButton = { Button(onClick = { confirmed = true }) { Text("Delete") } },
                )
            }
        }

        composeRule.onNodeWithText("Delete").performClick()
        composeRule.runOnIdle { assertTrue("confirm button should fire its onClick", confirmed) }
    }

    @Test
    fun alertDialog_dismissButton_firesDismiss() {
        var dismissed = false
        composeRule.setContent {
            FormaTheme {
                FormaAlertDialog(
                    onDismissRequest = { dismissed = true },
                    title = "Delete payment?",
                    confirmButton = { Button(onClick = {}) { Text("Delete") } },
                    // Typical wiring: the dismiss button routes to onDismissRequest.
                    dismissButton = { Button(onClick = { dismissed = true }) { Text("Cancel") } },
                )
            }
        }

        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.runOnIdle { assertTrue("dismiss button should trigger dismissal", dismissed) }
    }

    // --- FormaFullScreenDialog --------------------------------------------------------------

    @Test
    fun fullScreenDialog_showsTitleContentAndClose() {
        composeRule.setContent {
            FormaTheme {
                FormaFullScreenDialog(
                    onDismissRequest = {},
                    title = "New payment",
                    confirmAction = { Button(onClick = {}) { Text("Save") } },
                ) {
                    Text("Body content goes here")
                }
            }
        }

        composeRule.onNodeWithText("New payment").assertIsDisplayed()
        composeRule.onNodeWithText("Body content goes here").assertIsDisplayed()
        composeRule.onNodeWithText("Close").assertIsDisplayed()
        composeRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun fullScreenDialog_closeAndConfirmActions_fire() {
        var closed = false
        var saved = false
        composeRule.setContent {
            FormaTheme {
                FormaFullScreenDialog(
                    onDismissRequest = { closed = true },
                    title = "New payment",
                    confirmAction = { Button(onClick = { saved = true }) { Text("Save") } },
                ) {
                    Text("Body content goes here")
                }
            }
        }

        composeRule.onNodeWithText("Save").performClick()
        composeRule.runOnIdle { assertTrue("confirm action should fire", saved) }

        composeRule.onNodeWithText("Close").performClick()
        composeRule.runOnIdle { assertTrue("Close should fire onDismissRequest", closed) }
    }
}
