/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A FormaUI date picker presented in a modal bottom sheet — Material 3's inline `DatePicker`
 * hosted in a [FormaBottomSheet] instead of the M3 `DatePickerDialog`, for flows where a
 * bottom sheet fits the surrounding navigation better than a centered dialog.
 *
 * State is hoisted: pass a [DatePickerState] created with Material 3's `rememberDatePickerState`
 * (which also gives you `selectableDates`, `yearRange`, `initialDisplayMode`, and saveability for
 * free). The component wires nothing to the button slots — your [confirmButton] reads
 * `state.selectedDateMillis` (a UTC-epoch-millis `Long?`) itself, so it can also disable itself
 * until a date is picked:
 *
 * ```
 * val state = rememberDatePickerState()
 * if (open) {
 *     FormaDatePickerSheet(
 *         onDismissRequest = { open = false },
 *         state = state,
 *         confirmButton = {
 *             FormaButton(
 *                 onClick = { onPicked(state.selectedDateMillis); open = false },
 *                 enabled = state.selectedDateMillis != null,
 *             ) { Text("OK") }
 *         },
 *         dismissButton = {
 *             FormaButton(onClick = { open = false }, variant = FormaButtonVariant.Text) { Text("Cancel") }
 *         },
 *     )
 * }
 * ```
 *
 * The sheet is shown while composed; [onDismissRequest] fires for scrim tap, back press, and
 * swipe-down alike — there is no separate cancel callback, so treat it as "closed without
 * confirming". The picker's locale is captured when the state is created (Material 3 behavior);
 * recreate the state to react to a locale change.
 *
 * @param onDismissRequest called when the sheet is dismissed (scrim tap, back press, or swipe
 *   down) — hide the sheet by no longer composing it.
 * @param state the hoisted Material 3 [DatePickerState]; create it with `rememberDatePickerState`.
 *   Read `state.selectedDateMillis` (UTC epoch millis) in your [confirmButton]'s click handler.
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
fun FormaDatePickerSheet(
    onDismissRequest: () -> Unit,
    state: DatePickerState,
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
        DatePicker(
            state = state,
            colors = colors ?: formaDatePickerSheetColors(),
            showModeToggle = showModeToggle,
        )
    }
}

/**
 * The [DatePickerColors] used when a picker sheet's `colors` is `null`: Material 3's defaults with
 * a transparent picker container, so the picker sits directly on the sheet surface instead of
 * drawing its own `surfaceContainerHigh` panel on the sheet's `surfaceContainerLow` (a visible
 * seam).
 */
@Composable
internal fun formaDatePickerSheetColors(): DatePickerColors =
    DatePickerDefaults.colors().copy(containerColor = Color.Transparent)

/**
 * The shared sheet scaffold for the date picker sheets: a [FormaBottomSheet] hosting the [picker]
 * (centered, and bounded with `weight(1f, fill = false)` so the buttons stay on-screen on short
 * screens and the range picker's internal list scrolls internally) above an end-aligned button row.
 */
@Composable
internal fun FormaDatePickerSheetScaffold(
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    skipPartiallyExpanded: Boolean,
    shape: Shape?,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)?,
    picker: @Composable () -> Unit,
) {
    FormaBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        skipPartiallyExpanded = skipPartiallyExpanded,
        shape = shape,
    ) {
        Box(
            modifier = Modifier
                .weight(1f, fill = false)
                .align(Alignment.CenterHorizontally),
        ) {
            picker()
        }
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
            dismissButton?.invoke()
            confirmButton()
        }
    }
}
