/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.interaction

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * FormaUI's press-scale micro-interaction: scales the element down to [pressedScale] while it is
 * pressed, and springs back to full size on release — layered on top of (not replacing) the
 * component's ripple indication.
 *
 * The dip **always fully plays**, even for the quickest tap: press interactions are processed
 * sequentially, so a release that arrives mid-dip waits for the dip to complete before the
 * spring-back starts. The motion is split across two specs — [downAnimationSpec] drives the
 * press dip (fast and deterministic by default) and [animationSpec] drives the release
 * spring-back (slightly bouncy by default).
 *
 * This modifier is **observe-only**: it never detects input itself, it just watches the press
 * interactions of [interactionSource]. Pass the *same* source that the element's `clickable` (or
 * the underlying Material 3 component) emits into, mirroring the `Modifier.indication` pattern:
 *
 * ```
 * val interactionSource = remember { MutableInteractionSource() }
 * Box(
 *     Modifier
 *         .formaPressScale(interactionSource)
 *         .clickable(interactionSource = interactionSource, indication = ripple()) { /* … */ },
 * )
 * ```
 *
 * The scale is applied via [graphicsLayer], so it is purely visual: the element's measured size —
 * and therefore its 48dp minimum touch target — is unaffected, and the animated value is read
 * only inside the layer block (a draw-time read, no recomposition per frame).
 *
 * @param interactionSource the [InteractionSource] whose press interactions drive the scale. This
 *   modifier only observes it; the caller (or the wrapped component) is responsible for emitting
 *   interactions into it.
 * @param pressedScale the scale factor applied while pressed (defaults to
 *   [FormaPressScaleDefaults.PressedScale], a subtle 0.97). Must be greater than 0.
 * @param downAnimationSpec the animation used for the press dip down to [pressedScale] (defaults
 *   to [FormaPressScaleDefaults.DownAnimationSpec], a fast 100ms tween so rapid taps settle
 *   quickly instead of queueing up).
 * @param animationSpec the animation used to spring back to full size on release or cancel
 *   (defaults to [FormaPressScaleDefaults.AnimationSpec], a responsive spring with a slight
 *   bounce). Pass `null` to disable the press-scale entirely — the receiver is returned
 *   unchanged, matching the chart components' `animationSpec = null` convention.
 */
@ExperimentalFormaUiApi
@Composable
fun Modifier.formaPressScale(
    interactionSource: InteractionSource,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    downAnimationSpec: FiniteAnimationSpec<Float> = FormaPressScaleDefaults.DownAnimationSpec,
    animationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
): Modifier {
    require(pressedScale > 0f) { "pressedScale must be > 0, was $pressedScale" }
    if (animationSpec == null) return this
    val scale = remember { Animatable(1f) }
    LaunchedEffect(interactionSource, pressedScale, downAnimationSpec, animationSpec) {
        // Sequential collection is the guarantee that the dip always completes: a Release that
        // arrives while animateTo(pressedScale) is still running queues here until the dip
        // finishes, then the spring-back plays.
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press ->
                    scale.animateTo(pressedScale, downAnimationSpec)

                is PressInteraction.Release, is PressInteraction.Cancel ->
                    scale.animateTo(1f, animationSpec)
            }
        }
    }
    return graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}

/**
 * Default values used by [Modifier.formaPressScale]. Override any of these per call site, or read
 * them to build a customised press interaction that still inherits FormaUI's defaults.
 */
@ExperimentalFormaUiApi
object FormaPressScaleDefaults {
    /**
     * The default pressed scale factor (0.97) — a 3% dip, subtle enough to read as tactile
     * feedback without drawing attention to itself.
     */
    const val PressedScale: Float = 0.97f

    /**
     * The default press-dip animation: a fast 100ms [tween] with [FastOutSlowInEasing]. Kept
     * short and deterministic so the dip completes near-instantly under the finger and rapid
     * taps don't queue up sluggishly.
     */
    val DownAnimationSpec: FiniteAnimationSpec<Float> =
        tween(durationMillis = 100, easing = FastOutSlowInEasing)

    /**
     * The default release animation, driving the spring back to full size after the dip: a
     * spring with [Spring.StiffnessMedium] and [Spring.DampingRatioLowBouncy] (a slight
     * overshoot on release). Pass `animationSpec = null` to disable the press-scale instead.
     */
    val AnimationSpec: FiniteAnimationSpec<Float> =
        spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium)
}
