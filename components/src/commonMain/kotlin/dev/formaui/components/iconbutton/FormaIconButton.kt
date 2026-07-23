/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.iconbutton

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.formaui.components.interaction.FormaPressScaleDefaults
import dev.formaui.components.interaction.formaPressScale
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
 * A **press-scale micro-interaction** ([Modifier.formaPressScale][dev.formaui.components.interaction.formaPressScale])
 * is on by default, layered on top of the Material ripple: the button dips to [pressedScale]
 * while held and springs back on release. The dip always completes, even on the quickest tap.
 * Pass `pressAnimationSpec = null` to disable it.
 *
 * @param onClick called when the button is clicked. Not invoked while [enabled] is `false`.
 * @param modifier the [Modifier] applied to the button.
 * @param variant the visual emphasis (defaults to [FormaIconButtonVariant.Standard]).
 * @param enabled whether the button is interactive.
 * @param colors the container/content colors. When `null`, the Material 3 default colors for the
 *   chosen [variant] are used.
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 * @param pressedScale the scale factor applied while the button is pressed (defaults to
 *   [FormaIconButtonDefaults.PressedScale], a deeper 0.92 — see that constant for why). Must be
 *   greater than 0.
 * @param pressAnimationSpec the animation used for the press-scale's release spring-back
 *   (defaults to [FormaPressScaleDefaults.AnimationSpec], a responsive spring; the press dip
 *   uses [FormaPressScaleDefaults.DownAnimationSpec]). Pass `null` to disable the press-scale
 *   entirely.
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
    pressedScale: Float = FormaIconButtonDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    content: @Composable () -> Unit,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val iconButtonModifier = modifier.formaPressScale(
        interactionSource = interactionSource,
        pressedScale = pressedScale,
        animationSpec = pressAnimationSpec,
    )
    when (variant) {
        FormaIconButtonVariant.Standard -> IconButton(
            onClick = onClick,
            modifier = iconButtonModifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.iconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )

        FormaIconButtonVariant.Filled -> FilledIconButton(
            onClick = onClick,
            modifier = iconButtonModifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.filledIconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )

        FormaIconButtonVariant.Tonal -> FilledTonalIconButton(
            onClick = onClick,
            modifier = iconButtonModifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.filledTonalIconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )

        FormaIconButtonVariant.Outlined -> OutlinedIconButton(
            onClick = onClick,
            modifier = iconButtonModifier,
            enabled = enabled,
            colors = colors ?: IconButtonDefaults.outlinedIconButtonColors(),
            interactionSource = interactionSource,
            content = content,
        )
    }
}

/**
 * Default values used by [FormaIconButton]. Override any of these per call site, or read them to
 * build a customised icon button that still inherits FormaUI's defaults.
 */
@ExperimentalFormaUiApi
object FormaIconButtonDefaults {
    /**
     * The default pressed scale factor (0.92) — deeper than the standard
     * [FormaPressScaleDefaults.PressedScale] (0.97) because an icon button's small footprint
     * needs a larger relative dip for the feedback to read at all.
     */
    const val PressedScale: Float = 0.92f
}
