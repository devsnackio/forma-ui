/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Previews of the content a [FormaDatePickerSheet] / [FormaDateRangePickerSheet] hosts.
 *
 * The sheet itself renders inside its own window (`ModalBottomSheet`) and does not display in a
 * static `@Preview`, so — per the `FormaBottomSheetPreviews` pattern — these preview the sheet's
 * content: the picker (with the default transparent-container colors) above the end-aligned
 * button row. Dates are fixed (January 2024, UTC epoch millis) so the previews are deterministic.
 */

/** 2024-01-01T00:00:00Z — the fixed month/date all previews display. */
private const val PreviewDateMillis = 1704067200000L

/** 2024-01-10T00:00:00Z — the range preview's start date. */
private const val PreviewRangeStartMillis = 1704844800000L

/** 2024-01-20T00:00:00Z — the range preview's end date. */
private const val PreviewRangeEndMillis = 1705708800000L

/** The bounded height the range preview stands in for the sheet's height constraint with. */
private val PreviewRangePickerHeight = 480.dp

@Composable
private fun PreviewButtonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = FormaTheme.spacing.lg,
                top = FormaTheme.spacing.xs,
                end = FormaTheme.spacing.lg,
                bottom = FormaTheme.spacing.lg,
            ),
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs, Alignment.End),
    ) {
        FormaButton(onClick = {}, variant = FormaButtonVariant.Text) { Text("Cancel") }
        FormaButton(onClick = {}) { Text("OK") }
    }
}

@Preview
@Composable
private fun FormaDatePickerSheetContentPreview() {
    FormaTheme {
        Surface {
            Column(modifier = Modifier.fillMaxWidth()) {
                DatePicker(
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = PreviewDateMillis,
                        initialDisplayedMonthMillis = PreviewDateMillis,
                    ),
                    colors = formaDatePickerSheetColors(),
                )
                PreviewButtonRow()
            }
        }
    }
}

@Preview
@Composable
private fun FormaDatePickerSheetInputContentPreview() {
    FormaTheme {
        Surface {
            Column(modifier = Modifier.fillMaxWidth()) {
                DatePicker(
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = PreviewDateMillis,
                        initialDisplayedMonthMillis = PreviewDateMillis,
                        initialDisplayMode = DisplayMode.Input,
                    ),
                    colors = formaDatePickerSheetColors(),
                )
                PreviewButtonRow()
            }
        }
    }
}

@Preview
@Composable
private fun FormaDateRangePickerSheetContentPreview() {
    FormaTheme {
        Surface {
            Column(modifier = Modifier.fillMaxWidth()) {
                DateRangePicker(
                    state = rememberDateRangePickerState(
                        initialSelectedStartDateMillis = PreviewRangeStartMillis,
                        initialSelectedEndDateMillis = PreviewRangeEndMillis,
                        initialDisplayedMonthMillis = PreviewDateMillis,
                    ),
                    modifier = Modifier.height(PreviewRangePickerHeight),
                    colors = formaDatePickerSheetColors(),
                )
                PreviewButtonRow()
            }
        }
    }
}
