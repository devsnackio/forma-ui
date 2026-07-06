/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.iconbutton

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The visual emphasis of a [FormaIconButton], mirroring Material 3's four icon button variants.
 */
@ExperimentalFormaUiApi
enum class FormaIconButtonVariant {
    /** Lowest emphasis: no container, icon only. The most common icon button. */
    Standard,

    /** Highest emphasis: a solid `primary` fill. Use for a prominent supplementary action. */
    Filled,

    /** Medium-high emphasis: a tonal `secondaryContainer` fill. Softer than [Filled]. */
    Tonal,

    /** Medium-low emphasis: outlined with no fill. */
    Outlined,
}

/**
 * A FormaUI icon button — "Material 3 with better defaults".
 *
 * One entry point covers all four Material 3 icon button variants via [variant]. Supply the
 * [content] slot yourself (typically an `Icon` with a `contentDescription`, since the button
 * carries no text label of its own):
 *
 * ```
 * FormaIconButton(onClick = ::openMenu, variant = FormaIconButtonVariant.Filled) {
 *     Icon(Icons.Default.Menu, contentDescription = "Open menu")
 * }
 * ```
 *
 * The 48dp minimum touch target and `Role.Button` semantics are inherited from the underlying
 * Material 3 icon button.
 *
 * @param onClick called when the button is clicked. Not invoked while [enabled] is `false`.
 * @param modifier the [Modifier] applied to the button.
 * @param variant the visual emphasis (defaults to [FormaIconButtonVariant.Standard]).
 * @param enabled whether the button is interactive.
 * @param colors the container/content colors. When `null`, the Material 3 default colors for the
 *   chosen [variant] are used.
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 * @param content the button's content — typically a single `Icon` with a `contentDescription`.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaIconButtonVariant = FormaIconButtonVariant.Standard,
    enabled: Boolean = true,
    colors: IconButtonColors? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    when (variant) {
        FormaIconButtonVariant.Standard -> IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.iconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )

        FormaIconButtonVariant.Filled -> FilledIconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.filledIconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )

        FormaIconButtonVariant.Tonal -> FilledTonalIconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.filledTonalIconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )

        FormaIconButtonVariant.Outlined -> OutlinedIconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.outlinedIconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )
    }
}
