/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.datepicker.FormaDatePickerSheet
import dev.formaui.components.datepicker.FormaDateRangePickerSheet
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/** Live preview for `date-picker-sheet`: a button opens the sheet; confirming echoes the date. */
@Composable
internal fun ColumnScope.DatePickerSheetPreview() {
    var open by remember { mutableStateOf(false) }
    var picked by remember { mutableStateOf<Long?>(null) }
    val state = rememberDatePickerState()

    FormaButton(onClick = { open = true }) { Text("Pick a date") }
    Text(
        text = "Selected: ${picked?.let(::formatUtcDate) ?: "none yet"}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (open) {
        FormaDatePickerSheet(
            onDismissRequest = { open = false },
            state = state,
            confirmButton = {
                FormaButton(
                    onClick = {
                        picked = state.selectedDateMillis
                        open = false
                    },
                    enabled = state.selectedDateMillis != null,
                ) { Text("OK") }
            },
            dismissButton = {
                FormaButton(onClick = { open = false }, variant = FormaButtonVariant.Text) {
                    Text("Cancel")
                }
            },
        )
    }
}

/** Live preview for `date-range-picker-sheet`: a button opens the sheet; confirming echoes the range. */
@Composable
internal fun ColumnScope.DateRangePickerSheetPreview() {
    var open by remember { mutableStateOf(false) }
    var pickedStart by remember { mutableStateOf<Long?>(null) }
    var pickedEnd by remember { mutableStateOf<Long?>(null) }
    val state = rememberDateRangePickerState()

    FormaButton(onClick = { open = true }) { Text("Pick a date range") }
    Text(
        text = "Selected: " +
            if (pickedStart != null && pickedEnd != null) {
                "${formatUtcDate(pickedStart!!)} – ${formatUtcDate(pickedEnd!!)}"
            } else {
                "none yet"
            },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (open) {
        FormaDateRangePickerSheet(
            onDismissRequest = { open = false },
            state = state,
            confirmButton = {
                FormaButton(
                    onClick = {
                        pickedStart = state.selectedStartDateMillis
                        pickedEnd = state.selectedEndDateMillis
                        open = false
                    },
                    enabled = state.selectedEndDateMillis != null,
                ) { Text("OK") }
            },
            dismissButton = {
                FormaButton(onClick = { open = false }, variant = FormaButtonVariant.Text) {
                    Text("Cancel")
                }
            },
        )
    }
}

/**
 * Formats UTC epoch millis (as produced by the Material 3 picker states) as an ISO-8601 date,
 * e.g. `2026-07-23`. Uses the civil-from-days algorithm directly so the harness needs no
 * date-time dependency.
 */
private fun formatUtcDate(epochMillis: Long): String {
    val z = epochMillis.floorDiv(86_400_000L) + 719_468L
    val era = z.floorDiv(146_097L)
    val dayOfEra = z - era * 146_097L
    val yearOfEra = (dayOfEra - dayOfEra / 1_460L + dayOfEra / 36_524L - dayOfEra / 146_096L) / 365L
    val dayOfYear = dayOfEra - (365L * yearOfEra + yearOfEra / 4L - yearOfEra / 100L)
    val mp = (5L * dayOfYear + 2L) / 153L
    val day = dayOfYear - (153L * mp + 2L) / 5L + 1L
    val month = if (mp < 10L) mp + 3L else mp - 9L
    val year = yearOfEra + era * 400L + (if (month <= 2L) 1L else 0L)
    return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
}
