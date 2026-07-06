/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.checkbox

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI checkbox — a themed binary selection control.
 *
 * Stateless with hoisted state: the caller owns [checked] and updates it in [onCheckedChange].
 * Pass `null` for [onCheckedChange] to render a display-only checkbox whose toggling is handled
 * elsewhere (e.g. by a click on an enclosing row). The control exposes a 48dp touch target via
 * Material 3's minimum interactive component size.
 *
 * When placing a checkbox next to a label, prefer toggling from the row (via
 * `Modifier.toggleable`) with `onCheckedChange = null` here, so the whole row is one accessible
 * target.
 *
 * @param checked whether the box is checked.
 * @param onCheckedChange called with the new value when toggled, or `null` to make the checkbox
 *   non-interactive.
 * @param modifier the [Modifier] applied to the checkbox.
 * @param enabled whether the checkbox is interactive.
 * @param colors the checkbox colors (defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors ?: CheckboxDefaults.colors(),
        interactionSource = interactionSource,
    )
}
