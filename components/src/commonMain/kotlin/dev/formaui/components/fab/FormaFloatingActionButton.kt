/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.fab

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.formaui.components.interaction.FormaPressScaleDefaults
import dev.formaui.components.interaction.formaPressScale
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The size of a [FormaFloatingActionButton], mirroring Material 3's three FAB sizes.
 */
@ExperimentalFormaUiApi
enum class FormaFabSize {
    /** A compact FAB for secondary screens or when screen space is limited. */
    Small,

    /** The standard FAB size — the default choice for a screen's single primary action. */
    Regular,

    /** A prominent FAB for emphasizing the primary action on large-format screens. */
    Large,
}

/**
 * A FormaUI floating action button — "Material 3 with better defaults".
 *
 * One entry point covers all three Material 3 FAB sizes via [size]. The FAB represents the most
 * important action on a screen and should typically host a single [content] `Icon`. For a FAB with
 * a text label, use [FormaExtendedFloatingActionButton] instead.
 *
 * Corners default to FormaUI's [xl][dev.formaui.core.theme.FormaShapes.xl] tier
 * ([FormaFabDefaults.shape]) rather than Material 3's per-size squircle, for a consistent
 * silhouette across sizes.
 *
 * A **press-scale micro-interaction** ([Modifier.formaPressScale][dev.formaui.components.interaction.formaPressScale])
 * is on by default, layered on top of the Material ripple and the FAB's own elevation behavior:
 * the FAB dips to [pressedScale] while held and springs back on release. The dip always
 * completes, even on the quickest tap. Pass `pressAnimationSpec = null` to disable it.
 *
 * @param onClick called when the FAB is clicked.
 * @param modifier the [Modifier] applied to the FAB.
 * @param size the FAB's size (defaults to [FormaFabSize.Regular]).
 * @param shape the FAB's container shape (defaults to [FormaFabDefaults.shape]).
 * @param containerColor the background color of the FAB.
 * @param contentColor the preferred content color, derived from [containerColor] by default.
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 * @param pressedScale the scale factor applied while the FAB is pressed (defaults to
 *   [FormaPressScaleDefaults.PressedScale], a subtle 0.97). Must be greater than 0.
 * @param pressAnimationSpec the animation used for the press-scale's release spring-back
 *   (defaults to [FormaPressScaleDefaults.AnimationSpec], a responsive spring; the press dip
 *   uses [FormaPressScaleDefaults.DownAnimationSpec]). Pass `null` to disable the press-scale
 *   entirely.
 * @param content the FAB's content, typically a single `Icon`.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: FormaFabSize = FormaFabSize.Regular,
    shape: Shape? = null,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    content: @Composable () -> Unit,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val fabModifier = modifier.formaPressScale(
        interactionSource = interactionSource,
        pressedScale = pressedScale,
        animationSpec = pressAnimationSpec,
    )
    val fabShape = shape ?: FormaFabDefaults.shape
    when (size) {
        FormaFabSize.Small -> SmallFloatingActionButton(
            onClick = onClick,
            modifier = fabModifier,
            shape = fabShape,
            containerColor = containerColor,
            contentColor = contentColor,
            interactionSource = interactionSource,
            content = content,
        )

        FormaFabSize.Regular -> FloatingActionButton(
            onClick = onClick,
            modifier = fabModifier,
            shape = fabShape,
            containerColor = containerColor,
            contentColor = contentColor,
            interactionSource = interactionSource,
            content = content,
        )

        FormaFabSize.Large -> LargeFloatingActionButton(
            onClick = onClick,
            modifier = fabModifier,
            shape = fabShape,
            containerColor = containerColor,
            contentColor = contentColor,
            interactionSource = interactionSource,
            content = content,
        )
    }
}

/**
 * A FormaUI extended floating action button — a wider FAB that pairs an icon with a text label,
 * delegating to Material 3's `ExtendedFloatingActionButton`.
 *
 * Set [expanded] to `false` to collapse the button down to just its [icon] (e.g. while the user
 * scrolls a list) — the width animates between states automatically.
 *
 * A **press-scale micro-interaction** ([Modifier.formaPressScale][dev.formaui.components.interaction.formaPressScale])
 * is on by default, layered on top of the Material ripple: the FAB dips to [pressedScale] while
 * held and springs back on release. The dip always completes, even on the quickest tap. Pass
 * `pressAnimationSpec = null` to disable it.
 *
 * @param text the FAB's text label.
 * @param icon the FAB's leading icon.
 * @param onClick called when the FAB is clicked.
 * @param modifier the [Modifier] applied to the FAB.
 * @param expanded whether the FAB shows both [icon] and [text] (`true`) or collapses to just
 *   [icon] (`false`).
 * @param shape the FAB's container shape (defaults to [FormaFabDefaults.shape]).
 * @param containerColor the background color of the FAB.
 * @param contentColor the preferred content color, derived from [containerColor] by default.
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 * @param pressedScale the scale factor applied while the FAB is pressed (defaults to
 *   [FormaPressScaleDefaults.PressedScale], a subtle 0.97). Must be greater than 0.
 * @param pressAnimationSpec the animation used for the press-scale's release spring-back
 *   (defaults to [FormaPressScaleDefaults.AnimationSpec], a responsive spring; the press dip
 *   uses [FormaPressScaleDefaults.DownAnimationSpec]). Pass `null` to disable the press-scale
 *   entirely.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaExtendedFloatingActionButton(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    shape: Shape? = null,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    ExtendedFloatingActionButton(
        text = { Text(text) },
        icon = icon,
        onClick = onClick,
        modifier = modifier.formaPressScale(
            interactionSource = interactionSource,
            pressedScale = pressedScale,
            animationSpec = pressAnimationSpec,
        ),
        expanded = expanded,
        shape = shape ?: FormaFabDefaults.shape,
        containerColor = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
    )
}

/**
 * Default values used by [FormaFloatingActionButton] and [FormaExtendedFloatingActionButton].
 */
@ExperimentalFormaUiApi
object FormaFabDefaults {
    /** The default FAB [Shape]: FormaUI's xl corner tier, used across all sizes. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.xl
}
