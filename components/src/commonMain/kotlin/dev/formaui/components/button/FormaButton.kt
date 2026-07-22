/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The visual emphasis of a [FormaButton], mirroring Material 3's five button variants.
 *
 * Listed from highest to lowest emphasis. Pick the variant by the action's importance on the
 * screen: exactly one high-emphasis ([Filled]) button per view for the primary action, lower
 * emphasis for secondary actions.
 */
@ExperimentalFormaUiApi
enum class FormaButtonVariant {
    /** Highest emphasis: solid `primary` fill. Use for the single primary action on a screen. */
    Filled,

    /** Medium-high emphasis: a tonal `secondaryContainer` fill. Softer than [Filled]. */
    Tonal,

    /** Medium emphasis: a subtly elevated surface. Reads above the background without a fill. */
    Elevated,

    /** Medium-low emphasis: outlined with no fill. Use for important but non-primary actions. */
    Outlined,

    /** Lowest emphasis: text only, no container. Use for the least prominent actions. */
    Text,
}

/**
 * A FormaUI button — "Material 3 with better defaults".
 *
 * One entry point covers all five Material 3 button variants via [variant], so callers pick
 * emphasis with a single, discoverable parameter instead of choosing between five separate
 * functions. Content is a trailing [RowScope] slot, exactly like `androidx.compose.material3.Button`,
 * so icon + label layouts compose naturally:
 *
 * ```
 * FormaButton(onClick = ::submit) {
 *     Icon(Icons.Default.Check, contentDescription = null)
 *     Spacer(Modifier.width(FormaTheme.spacing.xxs))
 *     Text("Confirm")
 * }
 * ```
 *
 * Opinionated defaults that differ from bare Material 3:
 * - **Corners** default to [FormaShapes.md][dev.formaui.core.theme.FormaShapes.md] (a
 *   modern 8dp) rather than M3's fully-rounded pill.
 * - **Padding** is derived from [FormaSpacing][dev.formaui.core.theme.FormaSpacing] tokens
 *   (`lg` horizontal, `xs` vertical) — no hardcoded values.
 * - A **minimum 48dp touch target** ([FormaButtonDefaults.MinTouchTargetSize]) is always
 *   enforced for accessibility.
 *
 * Everything is overridable: pass [shape], [colors], [contentPadding], or extend [modifier].
 * The button is stateless — click handling ([onClick]) and [enabled] state are hoisted to the
 * caller per standard Compose convention. The `Role.Button` semantics and disabled state are
 * inherited from the underlying Material 3 button.
 *
 * @param onClick called when the button is clicked. Not invoked while [enabled] is `false`.
 * @param modifier the [Modifier] applied to the button. A minimum height of
 *   [FormaButtonDefaults.MinTouchTargetSize] is always merged in.
 * @param variant the visual emphasis (defaults to [FormaButtonVariant.Filled]).
 * @param enabled whether the button is interactive. When `false` it is visually de-emphasised
 *   and does not respond to input.
 * @param shape the button's container shape (defaults to [FormaButtonDefaults.shape]).
 * @param colors the container/content colors. When `null`, the Material 3 default colors for the
 *   chosen [variant] are used.
 * @param contentPadding padding between the container edges and the [content] (defaults to
 *   [FormaButtonDefaults.contentPadding]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 * @param content the button's content, laid out in a [RowScope] (typically a `Text`, optionally
 *   with a leading/trailing `Icon`).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaButtonVariant = FormaButtonVariant.Filled,
    enabled: Boolean = true,
    shape: Shape = FormaButtonDefaults.shape,
    colors: ButtonColors? = null,
    contentPadding: PaddingValues = FormaButtonDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonModifier = modifier.heightIn(min = FormaButtonDefaults.MinTouchTargetSize)
    when (variant) {
        FormaButtonVariant.Filled -> Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = colors ?: ButtonDefaults.buttonColors(),
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content,
        )

        FormaButtonVariant.Tonal -> FilledTonalButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = colors ?: ButtonDefaults.filledTonalButtonColors(),
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content,
        )

        FormaButtonVariant.Elevated -> ElevatedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = colors ?: ButtonDefaults.elevatedButtonColors(),
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content,
        )

        FormaButtonVariant.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = colors ?: ButtonDefaults.outlinedButtonColors(),
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content,
        )

        FormaButtonVariant.Text -> TextButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = colors ?: ButtonDefaults.textButtonColors(),
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content,
        )
    }
}

/**
 * Default values used by [FormaButton]. Override any of these per call site, or read them to
 * build a customised button that still inherits FormaUI's defaults.
 */
@ExperimentalFormaUiApi
object FormaButtonDefaults {
    /**
     * The minimum touch-target size FormaUI enforces on every button (48dp), meeting the
     * Material accessibility guideline for interactive targets.
     */
    val MinTouchTargetSize: Dp = 48.dp

    /** The default button [Shape]: FormaUI's md corner tier. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.md

    /** The default content padding: `lg` horizontal, `xs` vertical, from [FormaSpacing][dev.formaui.core.theme.FormaSpacing]. */
    val contentPadding: PaddingValues
        @Composable
        @ReadOnlyComposable
        get() = PaddingValues(
            horizontal = FormaTheme.spacing.lg,
            vertical = FormaTheme.spacing.xs,
        )
}
