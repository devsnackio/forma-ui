/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.datepicker

import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** 2024-01-01T00:00:00Z — every test pins the displayed month/date here (never wall clock). */
private const val JAN_1_2024_UTC = 1704067200000L

/** 2024-01-15T00:00:00Z — the millis a click on day "15" of January 2024 must produce. */
private const val JAN_15_2024_UTC = 1705276800000L

/**
 * Compose UI tests for [FormaDatePickerSheet], hosted on the JVM via Robolectric.
 *
 * `ModalBottomSheet` renders inside its own window; the test framework merges that window's
 * semantics into the root tree, so the hosted picker and button slots are assertable (same idiom
 * as `FormaBottomSheetTest`). Every test injects `initialDisplayedMonthMillis` (January 2024,
 * UTC) so day-cell positions and produced millis are deterministic; Robolectric's default en-US
 * locale pins Material 3's strings ("Select date", "Switch to text input mode"). Day cells expose
 * the full formatted date as their semantics text (the bare day number is cleared via
 * `clearAndSetSemantics` inside the cell), so days are located by full date — e.g.
 * "Monday, January 15, 2024" — which is also unambiguous across composed month pages; the millis
 * assertion double-checks the produced value. The taller-than-default screen qualifier keeps the
 * calendar grid inside the sheet's visible bounds.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class FormaDatePickerSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun datePickerSheet_rendersPickerContentInSheet() {
        composeRule.setContent {
            FormaTheme {
                FormaDatePickerSheet(
                    onDismissRequest = {},
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = JAN_1_2024_UTC,
                        initialDisplayedMonthMillis = JAN_1_2024_UTC,
                    ),
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                    dismissButton = { Button(onClick = {}) { Text("Cancel") } },
                )
            }
        }

        composeRule.onNodeWithText("Select date").assertExists()
        composeRule.onNodeWithText("Monday, January 15, 2024").assertExists()
        composeRule.onNodeWithText("OK").assertExists()
        composeRule.onNodeWithText("Cancel").assertExists()
    }

    @Test
    fun datePickerSheet_dayClick_updatesHoistedStateReadByConfirmSlot() {
        var confirmedMillis: Long? = null
        lateinit var state: DatePickerState
        composeRule.setContent {
            FormaTheme {
                state = rememberDatePickerState(initialDisplayedMonthMillis = JAN_1_2024_UTC)
                FormaDatePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = {
                        Button(onClick = { confirmedMillis = state.selectedDateMillis }) {
                            Text("OK")
                        }
                    },
                )
            }
        }

        composeRule.onNodeWithText("Monday, January 15, 2024").performClick()
        composeRule.runOnIdle {
            assertEquals(JAN_15_2024_UTC, state.selectedDateMillis)
        }

        composeRule.onNodeWithText("OK").performClick()
        composeRule.runOnIdle {
            assertEquals(JAN_15_2024_UTC, confirmedMillis)
        }
    }

    @Test
    fun datePickerSheet_nothingSelected_confirmSlotReadsNull() {
        // Sentinel proves the slot's onClick actually ran and read null (not "never ran").
        var confirmedMillis: Long? = -1L
        lateinit var state: DatePickerState
        composeRule.setContent {
            FormaTheme {
                state = rememberDatePickerState(initialDisplayedMonthMillis = JAN_1_2024_UTC)
                FormaDatePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = {
                        Button(onClick = { confirmedMillis = state.selectedDateMillis }) {
                            Text("OK")
                        }
                    },
                )
            }
        }

        composeRule.onNodeWithText("OK").performClick()
        composeRule.runOnIdle {
            assertNull(state.selectedDateMillis)
            assertNull(confirmedMillis)
        }
    }

    /**
     * [FormaDatePickerSheet] is shown while composed — callers gate it behind a hoisted flag
     * (the documented `if (open)` pattern) and the dismiss slot routes to the same "close" state.
     */
    @Test
    fun datePickerSheet_dismissSlotClick_hidesSheet() {
        composeRule.setContent {
            FormaTheme {
                var show by remember { mutableStateOf(true) }
                if (show) {
                    FormaDatePickerSheet(
                        onDismissRequest = { show = false },
                        state = rememberDatePickerState(
                            initialDisplayedMonthMillis = JAN_1_2024_UTC,
                        ),
                        confirmButton = { Button(onClick = {}) { Text("OK") } },
                        dismissButton = {
                            Button(onClick = { show = false }) { Text("Cancel") }
                        },
                    )
                }
            }
        }

        composeRule.onNodeWithText("Select date").assertExists()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.onNodeWithText("Select date").assertDoesNotExist()
    }

    @Test
    fun datePickerSheet_showModeToggleDefault_displaysToggle() {
        composeRule.setContent {
            FormaTheme {
                FormaDatePickerSheet(
                    onDismissRequest = {},
                    state = rememberDatePickerState(initialDisplayedMonthMillis = JAN_1_2024_UTC),
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Switch to text input mode").assertExists()
    }

    @Test
    fun datePickerSheet_showModeToggleFalse_hidesToggle() {
        composeRule.setContent {
            FormaTheme {
                FormaDatePickerSheet(
                    onDismissRequest = {},
                    state = rememberDatePickerState(initialDisplayedMonthMillis = JAN_1_2024_UTC),
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                    showModeToggle = false,
                )
            }
        }

        composeRule.onNodeWithContentDescription("Switch to text input mode").assertDoesNotExist()
    }

    @Test
    fun datePickerSheet_modeToggleClick_switchesDisplayModeToInput() {
        lateinit var state: DatePickerState
        composeRule.setContent {
            FormaTheme {
                state = rememberDatePickerState(initialDisplayedMonthMillis = JAN_1_2024_UTC)
                FormaDatePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Switch to text input mode").performClick()
        composeRule.runOnIdle {
            assertEquals(DisplayMode.Input, state.displayMode)
        }
    }
}
