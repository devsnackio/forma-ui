/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of the content a [FormaTimePickerSheet] hosts, in clock-dial mode.
 *
 * The sheet itself renders inside its own window (`ModalBottomSheet`) and does not display in a
 * static `@Preview`, so — per the `FormaDatePickerSheetPreviews` pattern — this previews the sheet's
 * content: the [TimePicker] (with the default transparent-container colors) above the end-aligned
 * button row. The time is fixed (09:30) so the preview is deterministic.
 */
@Preview
@Composable
private fun FormaTimePickerSheetContentPreview() {
    FormaTheme {
        Surface {
            Column(modifier = Modifier.fillMaxWidth()) {
                TimePicker(
                    state = rememberTimePickerState(initialHour = 9, initialMinute = 30),
                    colors = formaTimePickerSheetColors(),
                    modifier = Modifier
                        .padding(FormaTheme.spacing.md)
                        .align(Alignment.CenterHorizontally),
                )
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
        }
    }
}
