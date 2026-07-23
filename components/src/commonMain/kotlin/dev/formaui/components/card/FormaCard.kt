/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.card

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.formaui.components.interaction.FormaPressScaleDefaults
import dev.formaui.components.interaction.formaPressScale
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The surface style of a [FormaCard], mirroring Material 3's three card families.
 */
@ExperimentalFormaUiApi
enum class FormaCardVariant {
    /** A shadow-elevated surface. The default; reads as a distinct raised container. */
    Elevated,

    /** A flat surface bounded by an outline. Lowest visual weight. */
    Outlined,

    /** A flat, tonally-filled surface. Sits between [Outlined] and [Elevated] in emphasis. */
    Filled,
}

/**
 * A FormaUI card — a themed container with an opinionated **header / content / footer** slot API.
 *
 * One entry point covers all three Material 3 card families via [variant]. The three slots stack
 * vertically inside the card, separated by [FormaSpacing.md][dev.formaui.core.theme.FormaSpacing.md]
 * and inset by [contentPadding] — all spacing comes from FormaUI tokens, never hardcoded values.
 * Only [content] is required; [header] and [footer] are optional.
 *
 * ```
 * FormaCard(
 *     header = { Text("Account", style = MaterialTheme.typography.titleMedium) },
 *     footer = { FormaButton(onClick = ::open, variant = FormaButtonVariant.Text) { Text("Details") } },
 * ) {
 *     Text("Balance $1,240.00", style = FormaTheme.typography.numeric)
 * }
 * ```
 *
 * Pass [onClick] to make the whole card a single clickable/focusable target (the underlying M3
 * clickable card handles `Role.Button` semantics and the ripple); leave it `null` for a static
 * container. The card shape defaults to FormaUI's
 * [lg][dev.formaui.core.theme.FormaShapes.lg] corner tier; colors fall back to the M3
 * defaults for the chosen [variant].
 *
 * A clickable card also gets a **press-scale micro-interaction**
 * ([Modifier.formaPressScale][dev.formaui.components.interaction.formaPressScale]) by default,
 * layered on top of the Material ripple: the card dips to [pressedScale] while held and springs
 * back on release. The dip always completes, even on the quickest tap. Pass
 * `pressAnimationSpec = null` to disable it. When [onClick] is `null` the card is static and
 * [interactionSource], [pressedScale], and [pressAnimationSpec] are no-ops.
 *
 * @param modifier the [Modifier] applied to the card.
 * @param variant the surface style (defaults to [FormaCardVariant.Elevated]).
 * @param onClick optional click handler. When non-null the entire card becomes clickable and
 *   focusable; when `null` the card is a static container.
 * @param enabled whether the card responds to clicks. Only meaningful when [onClick] is non-null.
 * @param shape the card's container shape (defaults to [FormaCardDefaults.shape]).
 * @param colors the card colors. When `null`, the M3 default colors for the [variant] are used.
 * @param contentPadding padding between the card edges and its slots (defaults to
 *   [FormaCardDefaults.contentPadding]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally for the clickable card. Unused when [onClick] is
 *   `null`.
 * @param pressedScale the scale factor applied while a clickable card is pressed (defaults to
 *   [FormaCardDefaults.PressedScale], a subtler 0.98 — see that constant for why). Must be
 *   greater than 0. Unused when [onClick] is `null`.
 * @param pressAnimationSpec the animation used for the press-scale's release spring-back
 *   (defaults to [FormaPressScaleDefaults.AnimationSpec], a responsive spring; the press dip
 *   uses [FormaPressScaleDefaults.DownAnimationSpec]). Pass `null` to disable the press-scale
 *   entirely. Unused when [onClick] is `null`.
 * @param header optional slot rendered above [content] (e.g. a title row).
 * @param footer optional slot rendered below [content] (e.g. an action row).
 * @param content the card's main content, laid out in a [ColumnScope].
 */
@ExperimentalFormaUiApi
@Composable
fun FormaCard(
    modifier: Modifier = Modifier,
    variant: FormaCardVariant = FormaCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = FormaCardDefaults.shape,
    colors: CardColors? = null,
    contentPadding: PaddingValues = FormaCardDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaCardDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Materialize the interaction source and press-scale only for a clickable card — a static
    // card must not pay for the Animatable/LaunchedEffect behind the micro-interaction.
    val clickInteractionSource: MutableInteractionSource?
    val cardModifier: Modifier
    if (onClick != null) {
        clickInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
        cardModifier = modifier.formaPressScale(
            interactionSource = clickInteractionSource,
            pressedScale = pressedScale,
            animationSpec = pressAnimationSpec,
        )
    } else {
        clickInteractionSource = null
        cardModifier = modifier
    }

    val cardBody: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
        ) {
            header?.invoke()
            content()
            footer?.invoke()
        }
    }

    when (variant) {
        FormaCardVariant.Elevated -> {
            val cardColors = colors ?: CardDefaults.elevatedCardColors()
            if (onClick == null) {
                ElevatedCard(modifier = modifier, shape = shape, colors = cardColors, content = cardBody)
            } else {
                ElevatedCard(
                    onClick = onClick,
                    modifier = cardModifier,
                    enabled = enabled,
                    shape = shape,
                    colors = cardColors,
                    interactionSource = clickInteractionSource,
                    content = cardBody,
                )
            }
        }

        FormaCardVariant.Outlined -> {
            val cardColors = colors ?: CardDefaults.outlinedCardColors()
            if (onClick == null) {
                OutlinedCard(modifier = modifier, shape = shape, colors = cardColors, content = cardBody)
            } else {
                OutlinedCard(
                    onClick = onClick,
                    modifier = cardModifier,
                    enabled = enabled,
                    shape = shape,
                    colors = cardColors,
                    interactionSource = clickInteractionSource,
                    content = cardBody,
                )
            }
        }

        FormaCardVariant.Filled -> {
            val cardColors = colors ?: CardDefaults.cardColors()
            if (onClick == null) {
                Card(modifier = modifier, shape = shape, colors = cardColors, content = cardBody)
            } else {
                Card(
                    onClick = onClick,
                    modifier = cardModifier,
                    enabled = enabled,
                    shape = shape,
                    colors = cardColors,
                    interactionSource = clickInteractionSource,
                    content = cardBody,
                )
            }
        }
    }
}

/**
 * Default values used by [FormaCard].
 */
@ExperimentalFormaUiApi
object FormaCardDefaults {
    /**
     * The default pressed scale factor for a clickable card (0.98) — subtler than the standard
     * [FormaPressScaleDefaults.PressedScale] (0.97) because a card's large surface makes even a
     * small relative dip read clearly.
     */
    const val PressedScale: Float = 0.98f

    /** The default card [Shape]: FormaUI's lg corner tier. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.lg

    /** The default inset between the card edges and its slots: [FormaSpacing.md][dev.formaui.core.theme.FormaSpacing.md] on all sides. */
    val contentPadding: PaddingValues
        @Composable
        @ReadOnlyComposable
        get() = PaddingValues(FormaTheme.spacing.md)
}
