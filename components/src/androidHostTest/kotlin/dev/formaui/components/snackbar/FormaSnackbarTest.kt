/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
 * Compose UI tests for [FormaSnackbar], hosted on the JVM via Robolectric. Covers the standard
 * (message-only) variant, the action variant, and the action click callback.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaSnackbarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun standard_showsMessage() {
        composeRule.setContent {
            FormaTheme {
                FormaSnackbar(message = "Changes saved.")
            }
        }

        composeRule.onNodeWithText("Changes saved.").assertIsDisplayed()
        // No action label supplied → the action button is absent.
        composeRule.onNodeWithText("Undo").assertDoesNotExist()
    }

    @Test
    fun actionVariant_showsActionAndFires() {
        var acted = false
        composeRule.setContent {
            FormaTheme {
                FormaSnackbar(
                    message = "Message deleted.",
                    actionLabel = "Undo",
                    onAction = { acted = true },
                )
            }
        }

        composeRule.onNodeWithText("Message deleted.").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").performClick()
        composeRule.runOnIdle { assertTrue("snackbar action should fire onAction", acted) }
    }

    @Test
    fun host_showsQueuedMessageAndAction() {
        composeRule.setContent {
            FormaTheme {
                val hostState = remember { SnackbarHostState() }
                Scaffold(snackbarHost = { FormaSnackbarHost(hostState) }) { innerPadding ->
                    Box(Modifier.padding(innerPadding))
                }
                LaunchedEffect(Unit) {
                    // With an actionLabel, the duration defaults to Indefinite, so the snackbar
                    // stays shown (no auto-dismiss race with the test clock).
                    hostState.showSnackbar(message = "Changes saved.", actionLabel = "Undo")
                }
            }
        }

        composeRule.onNodeWithText("Changes saved.").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").assertIsDisplayed()
    }

    // --- customization params (colors + text styles) ---

    /**
     * Reads the fully-resolved [TextStyle] a text node was actually laid out with, via the
     * `GetTextLayoutResult` semantics action — the style FormaUI produced with
     * `LocalTextStyle.current.merge(override)`.
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
    fun colorParams_areAcceptedAndRender() {
        // Container/content/action content colors aren't reliably pixel-assertable in Robolectric;
        // assert the new params are accepted and the message + action still render.
        composeRule.setContent {
            FormaTheme {
                FormaSnackbar(
                    message = "Message deleted.",
                    actionLabel = "Undo",
                    onAction = {},
                    containerColor = Color(0xFF102027),
                    contentColor = Color(0xFFECEFF1),
                    actionContentColor = Color(0xFF00E5FF),
                )
            }
        }

        composeRule.onNodeWithText("Message deleted.").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").assertIsDisplayed()
    }

    @Test
    fun messageTextStyle_overrideReachesRenderedMessage() {
        // Black (900) is not the M3 snackbar body weight, so a match proves the merge reached the
        // laid-out glyphs, not merely that the param was accepted.
        composeRule.setContent {
            FormaTheme {
                FormaSnackbar(
                    message = "Changes saved.",
                    messageTextStyle = TextStyle(fontWeight = FontWeight.Black),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("Changes saved.", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "messageTextStyle override should reach the rendered snackbar message",
            FontWeight.Black,
            style.fontWeight,
        )
    }

    @Test
    fun actionLabelTextStyle_overrideReachesRenderedActionLabel() {
        composeRule.setContent {
            FormaTheme {
                FormaSnackbar(
                    message = "Message deleted.",
                    actionLabel = "Undo",
                    onAction = {},
                    actionLabelTextStyle = TextStyle(fontWeight = FontWeight.Black),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("Undo", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "actionLabelTextStyle override should reach the rendered snackbar action label",
            FontWeight.Black,
            style.fontWeight,
        )
    }
}
