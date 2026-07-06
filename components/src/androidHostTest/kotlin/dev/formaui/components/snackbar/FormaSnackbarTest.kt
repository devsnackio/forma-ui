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
}
