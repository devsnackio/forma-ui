/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.datepicker

import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI date-range picker presented in a modal bottom sheet — Material 3's inline
 * `DateRangePicker` hosted in a [FormaBottomSheet] instead of the M3 `DatePickerDialog`, for
 * flows where a bottom sheet fits the surrounding navigation better than a centered dialog.
 *
 * State is hoisted: pass a [DateRangePickerState] created with Material 3's
 * `rememberDateRangePickerState` (which also gives you `selectableDates`, `yearRange`,
 * `initialDisplayMode`, and saveability for free). The component wires nothing to the button
 * slots — your [confirmButton] reads `state.selectedStartDateMillis` and
 * `state.selectedEndDateMillis` (UTC-epoch-millis `Long?`s) itself, so it can also disable
 * itself until a complete range is picked:
 *
 * ```
 * val state = rememberDateRangePickerState()
 * if (open) {
 *     FormaDateRangePickerSheet(
 *         onDismissRequest = { open = false },
 *         state = state,
 *         confirmButton = {
 *             FormaButton(
 *                 onClick = {
 *                     onPicked(state.selectedStartDateMillis, state.selectedEndDateMillis)
 *                     open = false
 *                 },
 *                 enabled = state.selectedEndDateMillis != null,
 *             ) { Text("OK") }
 *         },
 *         dismissButton = {
 *             FormaButton(onClick = { open = false }, variant = FormaButtonVariant.Text) { Text("Cancel") }
 *         },
 *     )
 * }
 * ```
 *
 * The range picker's scrollable month list is bounded by the sheet, so months scroll internally
 * while the buttons stay on-screen. The sheet is shown while composed; [onDismissRequest] fires
 * for scrim tap, back press, and swipe-down alike — there is no separate cancel callback, so
 * treat it as "closed without confirming". The picker's locale is captured when the state is
 * created (Material 3 behavior); recreate the state to react to a locale change.
 *
 * @param onDismissRequest called when the sheet is dismissed (scrim tap, back press, or swipe
 *   down) — hide the sheet by no longer composing it.
 * @param state the hoisted Material 3 [DateRangePickerState]; create it with
 *   `rememberDateRangePickerState`. Read `state.selectedStartDateMillis` and
 *   `state.selectedEndDateMillis` (UTC epoch millis) in your [confirmButton]'s click handler.
 * @param confirmButton the primary action slot (required), typically a FormaUI button. The
 *   component attaches no click handling or label — the slot owns both.
 * @param modifier the [Modifier] applied to the sheet.
 * @param dismissButton the optional secondary/cancel action slot, shown before [confirmButton].
 * @param showModeToggle whether the picker shows Material 3's calendar-to-text-input mode toggle.
 * @param skipPartiallyExpanded whether the sheet skips the half-expanded state and opens fully.
 *   Defaults to `true` — deliberately unlike [FormaBottomSheet] — because the half-expanded state
 *   clips the calendar grid.
 * @param shape the sheet container shape. When `null`, Material 3's top-rounded default is used.
 * @param colors the [DatePickerColors] for the picker. When `null`, Material 3's defaults are used
 *   with a transparent picker container so the picker sits directly on the sheet surface (avoiding
 *   a visible container-on-container seam). A supplied value is passed through untouched.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaDateRangePickerSheet(
    onDismissRequest: () -> Unit,
    state: DateRangePickerState,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = true,
    skipPartiallyExpanded: Boolean = true,
    shape: Shape? = null,
    colors: DatePickerColors? = null,
) {
    FormaDatePickerSheetScaffold(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        skipPartiallyExpanded = skipPartiallyExpanded,
        shape = shape,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    ) {
        DateRangePicker(
            state = state,
            colors = colors ?: formaDatePickerSheetColors(),
            showModeToggle = showModeToggle,
        )
    }
}
