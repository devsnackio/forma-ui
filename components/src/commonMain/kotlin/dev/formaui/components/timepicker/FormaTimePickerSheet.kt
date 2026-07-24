/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.timepicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.components.iconbutton.FormaIconButton
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A FormaUI time picker presented in a modal bottom sheet — Material 3's `TimePicker` (clock dial)
 * and `TimeInput` (text entry) hosted in a [FormaBottomSheet], for flows where a bottom sheet fits
 * the surrounding navigation better than a centered dialog.
 *
 * State is hoisted: pass a [TimePickerState] created with Material 3's `rememberTimePickerState`
 * (which also sets the initial time and the 12h/24h format). The component wires nothing to the
 * button slots — your [confirmButton] reads `state.hour` (0–23) and `state.minute` (0–59) itself, so
 * it can commit the time (and be always-enabled, since a time picker always has a valid value):
 *
 * ```
 * val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)
 * if (open) {
 *     FormaTimePickerSheet(
 *         onDismissRequest = { open = false },
 *         state = state,
 *         confirmButton = {
 *             FormaButton(onClick = { onPicked(state.hour, state.minute); open = false }) { Text("OK") }
 *         },
 *         dismissButton = {
 *             FormaButton(onClick = { open = false }, variant = FormaButtonVariant.Text) { Text("Cancel") }
 *         },
 *     )
 * }
 * ```
 *
 * Unlike Material 3's `DatePicker`, the `TimePicker` has no built-in dial-to-text-input toggle, so
 * FormaUI adds one: when [showModeToggle] is `true`, a [FormaIconButton] at the start of the button
 * row swaps the picker between the clock dial and the text-entry [TimeInput]. The chosen mode is
 * internal UI state — it starts on the dial and resets each time the sheet is opened.
 *
 * The sheet is shown while composed; [onDismissRequest] fires for scrim tap, back press, and
 * swipe-down alike — there is no separate cancel callback, so treat it as "closed without
 * confirming". The picker's 12h/24h format and locale are captured when the state is created
 * (Material 3 behavior); recreate the state to react to a change.
 *
 * @param onDismissRequest called when the sheet is dismissed (scrim tap, back press, or swipe
 *   down) — hide the sheet by no longer composing it.
 * @param state the hoisted Material 3 [TimePickerState]; create it with `rememberTimePickerState`.
 *   Read `state.hour` / `state.minute` in your [confirmButton]'s click handler.
 * @param confirmButton the primary action slot (required), typically a FormaUI button. The
 *   component attaches no click handling or label — the slot owns both.
 * @param modifier the [Modifier] applied to the sheet.
 * @param dismissButton the optional secondary/cancel action slot, shown before [confirmButton].
 * @param showModeToggle whether to show the FormaUI-provided toggle that swaps the picker between
 *   the clock dial and text input. When `false`, only the clock dial is shown.
 * @param skipPartiallyExpanded whether the sheet skips the half-expanded state and opens fully.
 *   Defaults to `true` — deliberately unlike [FormaBottomSheet] — because the half-expanded state
 *   clips the clock dial.
 * @param shape the sheet container shape. When `null`, Material 3's top-rounded default is used.
 * @param colors the [TimePickerColors] applied to both the dial and text-input modes. When `null`,
 *   Material 3's defaults are used with a transparent picker container so the picker sits directly
 *   on the sheet surface (avoiding a visible container-on-container seam). A supplied value is
 *   passed through untouched.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTimePickerSheet(
    onDismissRequest: () -> Unit,
    state: TimePickerState,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = true,
    skipPartiallyExpanded: Boolean = true,
    shape: Shape? = null,
    colors: TimePickerColors? = null,
) {
    var mode by remember { mutableStateOf(FormaTimePickerDisplayMode.Dial) }
    val resolvedColors = colors ?: formaTimePickerSheetColors()

    FormaTimePickerSheetScaffold(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        skipPartiallyExpanded = skipPartiallyExpanded,
        shape = shape,
        modeToggle = if (showModeToggle) {
            {
                val inputMode = mode == FormaTimePickerDisplayMode.Input
                FormaIconButton(
                    onClick = {
                        mode = if (inputMode) {
                            FormaTimePickerDisplayMode.Dial
                        } else {
                            FormaTimePickerDisplayMode.Input
                        }
                    },
                ) {
                    FormaTimePickerModeGlyph(
                        glyph = if (inputMode) FormaTimePickerGlyph.Clock else FormaTimePickerGlyph.Keyboard,
                        contentDescription = if (inputMode) "Switch to clock dial" else "Switch to text input",
                    )
                }
            }
        } else {
            null
        },
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    ) {
        when (mode) {
            FormaTimePickerDisplayMode.Dial -> TimePicker(state = state, colors = resolvedColors)
            FormaTimePickerDisplayMode.Input -> TimeInput(state = state, colors = resolvedColors)
        }
    }
}

/** The dial-vs-text-input display modes of a [FormaTimePickerSheet]. Internal UI state. */
private enum class FormaTimePickerDisplayMode { Dial, Input }

/** The two toggle glyphs, each pointing at the mode the toggle will switch *to*. */
private enum class FormaTimePickerGlyph { Keyboard, Clock }

/** The size of the mode-toggle glyph — the Material standard 24dp icon square. */
private val ToggleGlyphSize: Dp = 24.dp

/**
 * The [TimePickerColors] used when a picker sheet's `colors` is `null`: Material 3's defaults with a
 * transparent picker container, so the picker sits directly on the sheet surface instead of drawing
 * its own `surfaceContainerHigh` panel on the sheet's `surfaceContainerLow` (a visible seam).
 */
@Composable
internal fun formaTimePickerSheetColors(): TimePickerColors =
    TimePickerDefaults.colors().copy(containerColor = Color.Transparent)

/**
 * The sheet scaffold for the time picker: a [FormaBottomSheet] hosting the [picker] (centered, and
 * bounded with `weight(1f, fill = false)` so the buttons stay on-screen on short screens) above a
 * button row with the optional [modeToggle] at the start and the end-aligned dismiss/confirm slots.
 */
@Composable
private fun FormaTimePickerSheetScaffold(
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    skipPartiallyExpanded: Boolean,
    shape: Shape?,
    modeToggle: (@Composable () -> Unit)?,
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
            verticalAlignment = Alignment.CenterVertically,
        ) {
            modeToggle?.invoke()
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs)) {
                dismissButton?.invoke()
                confirmButton()
            }
        }
    }
}

/**
 * The mode-toggle glyph, drawn with [Canvas] rather than a Material Icons vector: the
 * `androidx.compose.material.icons` artifact is not available on the `wasmJs` target, so a
 * self-contained drawing keeps [FormaTimePickerSheet] compiling on every FormaUI target with no
 * extra dependency. Inherits the icon button's content color via [LocalContentColor].
 */
@Composable
private fun FormaTimePickerModeGlyph(
    glyph: FormaTimePickerGlyph,
    contentDescription: String,
) {
    val color = LocalContentColor.current
    Canvas(
        modifier = Modifier
            .size(ToggleGlyphSize)
            .semantics { this.contentDescription = contentDescription },
    ) {
        val w = size.width
        val h = size.height
        val strokeWidth = w * 0.08f
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (glyph) {
            FormaTimePickerGlyph.Keyboard -> {
                val left = w * 0.10f
                val top = h * 0.28f
                val bodyWidth = w * 0.80f
                val bodyHeight = h * 0.44f
                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = Size(bodyWidth, bodyHeight),
                    cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
                    style = stroke,
                )
                val keyRadius = w * 0.035f
                val keyRowY = top + bodyHeight * 0.34f
                listOf(0.30f, 0.50f, 0.70f).forEach { fraction ->
                    drawCircle(color = color, radius = keyRadius, center = Offset(w * fraction, keyRowY))
                }
                drawLine(
                    color = color,
                    start = Offset(w * 0.34f, top + bodyHeight * 0.70f),
                    end = Offset(w * 0.66f, top + bodyHeight * 0.70f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }

            FormaTimePickerGlyph.Clock -> {
                val center = Offset(w * 0.5f, h * 0.5f)
                val radius = w * 0.34f
                drawCircle(color = color, radius = radius, center = center, style = stroke)
                drawLine(
                    color = color,
                    start = center,
                    end = Offset(center.x, center.y - radius * 0.5f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = color,
                    start = center,
                    end = Offset(center.x + radius * 0.62f, center.y),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}
