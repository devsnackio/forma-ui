/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.timepicker

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaTimePickerSheet], hosted on the JVM via Robolectric.
 *
 * The sheet is a [dev.formaui.components.bottomsheet.FormaBottomSheet] (a `ModalBottomSheet`), whose
 * separate window merges into the test root tree — so the hosted picker, button slots, and the
 * FormaUI mode-toggle are assertable (same idiom as `FormaDatePickerSheetTest`). The taller screen
 * qualifier keeps the clock dial inside the sheet's visible bounds. The confirm slot reads
 * `state.hour`/`state.minute` itself; with `initialHour = 9, initialMinute = 30` the deterministic
 * en-US label is "Set 9:30". The dial-mode toggle reads "Switch to text input"; after a click it
 * flips to input mode and reads "Switch to clock dial".
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class FormaTimePickerSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun timePickerSheet_rendersPickerContentInSheet() {
        composeRule.setContent {
            FormaTheme {
                val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)
                FormaTimePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = {
                        Button(onClick = {}) { Text("Set ${state.hour}:${state.minute}") }
                    },
                    dismissButton = { Button(onClick = {}) { Text("Cancel") } },
                )
            }
        }

        // Confirm slot reads the hoisted state; dismiss slot + dial-mode toggle are present.
        composeRule.onNodeWithText("Set 9:30").assertExists()
        composeRule.onNodeWithText("Cancel").assertExists()
        composeRule.onNodeWithContentDescription("Switch to text input").assertExists()
    }

    @Test
    fun modeToggle_switchesDialToInput() {
        composeRule.setContent {
            FormaTheme {
                val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)
                FormaTimePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                )
            }
        }

        // Starts on the dial (offers "Switch to text input"); clicking the toggle flips to input
        // mode, whose toggle now offers "Switch to clock dial" — proving the toggle recomposes.
        composeRule.onNodeWithContentDescription("Switch to text input").assertExists()
        composeRule.onNodeWithContentDescription("Switch to clock dial").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Switch to text input").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithContentDescription("Switch to clock dial").assertExists()
        composeRule.onNodeWithContentDescription("Switch to text input").assertDoesNotExist()
    }

    @Test
    fun showModeToggleFalse_hidesToggle() {
        composeRule.setContent {
            FormaTheme {
                val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)
                FormaTimePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                    showModeToggle = false,
                )
            }
        }

        composeRule.onNodeWithContentDescription("Switch to text input").assertDoesNotExist()
        composeRule.onNodeWithText("OK").assertExists()
    }

    /**
     * [FormaTimePickerSheet] is shown while composed — callers gate it behind a hoisted flag (the
     * documented `if (open)` pattern) and the dismiss slot routes to the same "close" state.
     */
    @Test
    fun dismissButton_hidesSheet() {
        composeRule.setContent {
            FormaTheme {
                var show by remember { mutableStateOf(true) }
                if (show) {
                    val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)
                    FormaTimePickerSheet(
                        onDismissRequest = { show = false },
                        state = state,
                        confirmButton = {
                            Button(onClick = {}) { Text("Set ${state.hour}:${state.minute}") }
                        },
                        dismissButton = { Button(onClick = { show = false }) { Text("Cancel") } },
                    )
                }
            }
        }

        composeRule.onNodeWithText("Set 9:30").assertExists()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Set 9:30").assertDoesNotExist()
    }
}
