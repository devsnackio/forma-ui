/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.dialog

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    // --- customization params (colors + text styles) ---------------------------------------

    /**
     * Reads the fully-resolved [TextStyle] a text node was actually laid out with, via the
     * `GetTextLayoutResult` semantics action — the style FormaUI produced by merging any override on
     * top of the M3 slot style.
     */
    private fun SemanticsNodeInteraction.resolvedTextStyle(): TextStyle {
        val node = fetchSemanticsNode()
        val action = node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action
        assertNotNull("text node should expose the GetTextLayoutResult semantics action", action)
        val results = mutableListOf<TextLayoutResult>()
        action!!.invoke(results)
        assertTrue("GetTextLayoutResult must yield a layout", results.isNotEmpty())
        return results.first().layoutInput.style
    }

    @Test
    fun alertDialog_colorParams_areAcceptedAndRender() {
        // Container/icon/title/text content colors aren't reliably pixel-assertable in Robolectric;
        // assert the new params are accepted and the dialog's slots still render.
        composeRule.setContent {
            FormaTheme {
                FormaAlertDialog(
                    onDismissRequest = {},
                    icon = { Text("i") },
                    title = "Delete payment?",
                    text = "This action can't be undone.",
                    confirmButton = { Button(onClick = {}) { Text("Delete") } },
                    containerColor = Color(0xFF102027),
                    iconContentColor = Color(0xFF00E5FF),
                    titleContentColor = Color(0xFFFFAB00),
                    textContentColor = Color(0xFFB2FF59),
                )
            }
        }

        composeRule.onNodeWithText("i").assertIsDisplayed()
        composeRule.onNodeWithText("Delete payment?").assertIsDisplayed()
        composeRule.onNodeWithText("This action can't be undone.").assertIsDisplayed()
        composeRule.onNodeWithText("Delete").assertIsDisplayed()
    }

    @Test
    fun alertDialog_titleTextStyle_overrideReachesRenderedTitle() {
        composeRule.setContent {
            FormaTheme {
                FormaAlertDialog(
                    onDismissRequest = {},
                    title = "Delete payment?",
                    confirmButton = { Button(onClick = {}) { Text("Delete") } },
                    titleTextStyle = TextStyle(fontWeight = FontWeight.Black),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("Delete payment?", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "titleTextStyle override should reach the rendered alert dialog title",
            FontWeight.Black,
            style.fontWeight,
        )
    }

    @Test
    fun alertDialog_textTextStyle_overrideReachesRenderedText() {
        composeRule.setContent {
            FormaTheme {
                FormaAlertDialog(
                    onDismissRequest = {},
                    title = "Delete payment?",
                    text = "This action can't be undone.",
                    confirmButton = { Button(onClick = {}) { Text("Delete") } },
                    textTextStyle = TextStyle(fontWeight = FontWeight.Black),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("This action can't be undone.", useUnmergedTree = true)
                .resolvedTextStyle()
        assertEquals(
            "textTextStyle override should reach the rendered alert dialog body text",
            FontWeight.Black,
            style.fontWeight,
        )
    }

    @Test
    fun fullScreenDialog_colorParams_areAcceptedAndRender() {
        composeRule.setContent {
            FormaTheme {
                FormaFullScreenDialog(
                    onDismissRequest = {},
                    title = "New payment",
                    containerColor = Color(0xFF102027),
                    contentColor = Color(0xFFECEFF1),
                ) {
                    Text("Body content goes here")
                }
            }
        }

        composeRule.onNodeWithText("New payment").assertIsDisplayed()
        composeRule.onNodeWithText("Body content goes here").assertIsDisplayed()
        composeRule.onNodeWithText("Close").assertIsDisplayed()
    }

    @Test
    fun fullScreenDialog_titleTextStyle_overrideReachesRenderedTitle() {
        composeRule.setContent {
            FormaTheme {
                FormaFullScreenDialog(
                    onDismissRequest = {},
                    title = "New payment",
                    titleTextStyle = TextStyle(fontWeight = FontWeight.Black),
                ) {
                    Text("Body content goes here")
                }
            }
        }

        val style =
            composeRule.onNodeWithText("New payment", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "titleTextStyle override should reach the rendered full-screen dialog title",
            FontWeight.Black,
            style.fontWeight,
        )
    }
}
