/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.swipedismiss

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaSwipeToDismiss], hosted on the JVM via Robolectric (no device needed).
 * Covers the PRD §5.3 bar: both slots (content + backgroundContent) render, and a state change is
 * exercised two ways — programmatically driving the hoisted [SwipeToDismissBoxState] to a dismissed
 * value, and toggling which row is shown (the "remove after dismiss" pattern). The swipe gesture is
 * not simulated (non-deterministic under the test clock); the state is driven directly.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaSwipeToDismissTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun content_and_background_render() {
        composeRule.setContent {
            FormaTheme {
                val state = rememberSwipeToDismissBoxState()
                FormaSwipeToDismiss(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundContent = { Text("Delete", modifier = Modifier.testTag("bg")) },
                ) {
                    Text("Row item", modifier = Modifier.fillMaxWidth())
                }
            }
        }

        composeRule.onNodeWithText("Row item").assertIsDisplayed()
        // The background slot is stacked behind the content at rest; assert it composed (exists).
        composeRule.onNodeWithTag("bg").assertExists()
        composeRule.onNodeWithText("Delete").assertExists()
    }

    @Test
    fun programmaticDismiss_updatesState() {
        lateinit var state: SwipeToDismissBoxState
        composeRule.setContent {
            FormaTheme {
                state = rememberSwipeToDismissBoxState()
                val scope = rememberCoroutineScope()
                Column {
                    Text(
                        "Dismiss",
                        modifier = Modifier
                            .testTag("dismiss")
                            .clickable {
                                scope.launch { state.dismiss(SwipeToDismissBoxValue.EndToStart) }
                            },
                    )
                    FormaSwipeToDismiss(
                        state = state,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundContent = { Text("Delete") },
                    ) {
                        Text("Row item", modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }

        composeRule.runOnIdle {
            assertEquals(
                "swipe state should start Settled",
                SwipeToDismissBoxValue.Settled,
                state.currentValue,
            )
        }

        composeRule.onNodeWithTag("dismiss").performClick()
        composeRule.waitForIdle()

        composeRule.runOnIdle {
            assertEquals(
                "dismiss(EndToStart) should settle the hoisted state to EndToStart",
                SwipeToDismissBoxValue.EndToStart,
                state.currentValue,
            )
        }
    }

    @Test
    fun togglingShownItem_updatesVisibleContent() {
        // Fully deterministic state change: a hoisted flag removes the dismissable row, mirroring
        // how a caller drops an item from a list after a completed dismiss.
        composeRule.setContent {
            FormaTheme {
                var removed by remember { mutableStateOf(false) }
                val state = rememberSwipeToDismissBoxState()
                Column {
                    Text(
                        "Remove",
                        modifier = Modifier
                            .testTag("remove")
                            .clickable { removed = true },
                    )
                    if (!removed) {
                        FormaSwipeToDismiss(
                            state = state,
                            modifier = Modifier.fillMaxWidth(),
                            backgroundContent = { Text("Delete") },
                        ) {
                            Text("Removable row", modifier = Modifier.fillMaxWidth())
                        }
                    } else {
                        Text("Row removed")
                    }
                }
            }
        }

        composeRule.onNodeWithText("Removable row").assertIsDisplayed()
        composeRule.onNodeWithText("Row removed").assertDoesNotExist()

        composeRule.onNodeWithTag("remove").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Removable row").assertDoesNotExist()
        composeRule.onNodeWithText("Row removed").assertIsDisplayed()
    }
}
