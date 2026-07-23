/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.datepicker

import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** 2024-01-01T00:00:00Z — every test pins the displayed month here (never wall clock). */
private const val JAN_1_2024_UTC = 1704067200000L

/** 2024-01-10T00:00:00Z — the millis a click on day "10" of January 2024 must produce. */
private const val JAN_10_2024_UTC = 1704844800000L

/** 2024-01-20T00:00:00Z — the millis a click on day "20" of January 2024 must produce. */
private const val JAN_20_2024_UTC = 1705708800000L

/**
 * Compose UI tests for [FormaDateRangePickerSheet], hosted on the JVM via Robolectric.
 *
 * `ModalBottomSheet` renders inside its own window; the test framework merges that window's
 * semantics into the root tree, so the hosted range picker and button slots are assertable (same
 * idiom as `FormaBottomSheetTest`). Every test injects `initialDisplayedMonthMillis` (January
 * 2024, UTC); Robolectric's default en-US locale pins Material 3's strings ("Select dates",
 * "Switch to text input mode"). Day cells expose the full formatted date as their semantics text
 * (the bare day number is cleared via `clearAndSetSemantics` inside the cell), so days are
 * located by full date — e.g. "Wednesday, January 10, 2024" — which is also unambiguous across
 * the month list's multiple composed months; the millis assertions double-check the produced
 * values. The taller-than-default screen qualifier keeps the first month's grid inside the
 * sheet's visible bounds.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class FormaDateRangePickerSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun dateRangePickerSheet_rendersPickerContentInSheet() {
        composeRule.setContent {
            FormaTheme {
                FormaDateRangePickerSheet(
                    onDismissRequest = {},
                    state = rememberDateRangePickerState(
                        initialDisplayedMonthMillis = JAN_1_2024_UTC,
                    ),
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                    dismissButton = { Button(onClick = {}) { Text("Cancel") } },
                )
            }
        }

        composeRule.onNodeWithText("Select dates").assertExists()
        composeRule.onNodeWithText("Wednesday, January 10, 2024").assertExists()
        composeRule.onNodeWithContentDescription("Switch to text input mode").assertExists()
        composeRule.onNodeWithText("OK").assertExists()
        composeRule.onNodeWithText("Cancel").assertExists()
    }

    @Test
    fun dateRangePickerSheet_dayClicks_updateStartAndEndReadByConfirmSlot() {
        var confirmedStart: Long? = null
        var confirmedEnd: Long? = null
        lateinit var state: DateRangePickerState
        composeRule.setContent {
            FormaTheme {
                state = rememberDateRangePickerState(
                    initialDisplayedMonthMillis = JAN_1_2024_UTC,
                )
                FormaDateRangePickerSheet(
                    onDismissRequest = {},
                    state = state,
                    confirmButton = {
                        Button(
                            onClick = {
                                confirmedStart = state.selectedStartDateMillis
                                confirmedEnd = state.selectedEndDateMillis
                            },
                        ) {
                            Text("OK")
                        }
                    },
                )
            }
        }

        composeRule.onNodeWithText("Wednesday, January 10, 2024").performClick()
        composeRule.onNodeWithText("Saturday, January 20, 2024").performClick()
        composeRule.runOnIdle {
            assertEquals(JAN_10_2024_UTC, state.selectedStartDateMillis)
            assertEquals(JAN_20_2024_UTC, state.selectedEndDateMillis)
        }

        composeRule.onNodeWithText("OK").performClick()
        composeRule.runOnIdle {
            assertEquals(JAN_10_2024_UTC, confirmedStart)
            assertEquals(JAN_20_2024_UTC, confirmedEnd)
        }
    }

    @Test
    fun dateRangePickerSheet_showModeToggleFalse_hidesToggle() {
        composeRule.setContent {
            FormaTheme {
                FormaDateRangePickerSheet(
                    onDismissRequest = {},
                    state = rememberDateRangePickerState(
                        initialDisplayedMonthMillis = JAN_1_2024_UTC,
                    ),
                    confirmButton = { Button(onClick = {}) { Text("OK") } },
                    showModeToggle = false,
                )
            }
        }

        composeRule.onNodeWithContentDescription("Switch to text input mode").assertDoesNotExist()
    }

    /**
     * [FormaDateRangePickerSheet] is shown while composed — callers gate it behind a hoisted flag
     * (the documented `if (open)` pattern); `onDismissRequest` and the dismiss slot both route to
     * the same "close" state.
     */
    @Test
    fun dateRangePickerSheet_dismiss_hidesSheet() {
        composeRule.setContent {
            FormaTheme {
                var show by remember { mutableStateOf(true) }
                if (show) {
                    FormaDateRangePickerSheet(
                        onDismissRequest = { show = false },
                        state = rememberDateRangePickerState(
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

        composeRule.onNodeWithText("Select dates").assertExists()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.onNodeWithText("Select dates").assertDoesNotExist()
    }
}
