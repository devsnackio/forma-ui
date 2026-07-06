/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.slider

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI slider — lets the user select a value from a continuous or stepped range, delegating
 * to Material 3's `Slider`.
 *
 * Stateless with hoisted state: the caller owns [value] and updates it in [onValueChange]. Use
 * [onValueChangeFinished] to react once the user stops dragging (e.g. to commit the value). Set
 * [steps] to make the slider snap to discrete stops within [valueRange]. The control exposes a
 * 48dp touch target via Material 3's minimum interactive component size.
 *
 * @param value the current value; should sit within [valueRange].
 * @param onValueChange called continuously with the new value as the user drags.
 * @param modifier the [Modifier] applied to the slider.
 * @param enabled whether the slider is interactive.
 * @param valueRange the inclusive range of selectable values (defaults to `0f..1f`).
 * @param steps the number of discrete intermediate stops (0 = continuous).
 * @param onValueChangeFinished called once when the user finishes changing the value.
 * @param colors the slider colors (defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors? = null,
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors ?: SliderDefaults.colors(),
    )
}
