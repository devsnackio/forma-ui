/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.switch

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI switch — a themed on/off toggle for a single setting.
 *
 * Stateless with hoisted state: the caller owns [checked] and updates it in [onCheckedChange].
 * Pass `null` for [onCheckedChange] to render a display-only switch whose toggling is handled
 * elsewhere (e.g. by a click on an enclosing row). The interactive control already exposes a
 * 48dp touch target via Material 3's minimum interactive component size.
 *
 * @param checked whether the switch is on.
 * @param onCheckedChange called with the new value when the user toggles the switch, or `null`
 *   to make the switch non-interactive.
 * @param modifier the [Modifier] applied to the switch.
 * @param enabled whether the switch is interactive.
 * @param thumbContent optional slot drawn inside the thumb (e.g. a check icon when [checked]).
 * @param colors the switch colors (defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = null,
    colors: SwitchColors? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        thumbContent = thumbContent,
        colors = colors ?: SwitchDefaults.colors(),
        interactionSource = interactionSource,
    )
}
