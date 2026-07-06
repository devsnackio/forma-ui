/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.radiobutton

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI radio button — a themed control for choosing one option from a set.
 *
 * Stateless with hoisted state: the caller owns [selected] and reacts to [onClick]. Pass `null`
 * for [onClick] to render a display-only radio button whose selection is handled elsewhere (e.g.
 * by a click on an enclosing row). The control exposes a 48dp touch target via Material 3's
 * minimum interactive component size.
 *
 * Radio buttons are meaningless alone — group several, each with the same shared state, and
 * apply `Modifier.selectableGroup()` to the container so screen readers announce the set and its
 * position (e.g. "2 of 4"). Prefer toggling from each row (`Modifier.selectable`) with
 * `onClick = null` here so the whole row is one accessible target.
 *
 * @param selected whether this option is the selected one in its group.
 * @param onClick called when this option is clicked, or `null` to make the radio button
 *   non-interactive.
 * @param modifier the [Modifier] applied to the radio button.
 * @param enabled whether the radio button is interactive.
 * @param colors the radio button colors (defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors ?: RadioButtonDefaults.colors(),
        interactionSource = interactionSource,
    )
}
