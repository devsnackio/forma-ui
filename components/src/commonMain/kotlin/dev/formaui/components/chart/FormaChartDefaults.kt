/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Default values shared by FormaUI's chart components ([FormaBarChart] and friends). Override
 * any of these per call site, or read them to build a customised chart that still inherits
 * FormaUI's defaults.
 */
@ExperimentalFormaUiApi
object FormaChartDefaults {
    /** The minimum height a chart occupies when the caller doesn't size it (180dp). */
    val MinChartHeight: Dp = 180.dp

    /** The minimum diameter of a donut chart when the caller doesn't size it (160dp). */
    val MinDonutSize: Dp = 160.dp

    /** The default stroke width of a donut chart's ring (24dp). */
    val DonutStrokeWidth: Dp = 24.dp

    /** The default stroke width of a line chart's data line (2dp). */
    val LineStrokeWidth: Dp = 2.dp

    /**
     * The radius of a line chart's point markers (3dp) — drawn at each data point when
     * `showPoints` is enabled, and for a single-value dataset (which has no segment to draw).
     */
    val LinePointRadius: Dp = 3.dp

    /**
     * The default radius of a bar's top corners (4dp) — mirrors FormaUI's `xs` shape tier as a
     * plain [Dp], since shape tokens are corner-based shapes rather than radii.
     */
    val BarCornerRadius: Dp = 4.dp

    /**
     * The size of a [FormaChartLegend] color swatch: a 12dp square, drawn with
     * [BarCornerRadius] corners so swatches match the charts' corner treatment.
     */
    val LegendSwatchSize: Dp = 12.dp

    /**
     * How many horizontal divisions the gridlines split the plot area into. With the default of
     * 4, lines are drawn at 0%, 25%, 50%, 75%, and 100% of the value range.
     */
    const val GridLineCount: Int = 4

    /**
     * The default palette for multi-color series (e.g. donut segments), taken from the current
     * Material color scheme: `primary`, `secondary`, `tertiary`, `onPrimaryContainer`,
     * `onSecondaryContainer`, and `error` — six distinguishable colors with `error` last, so
     * small datasets never look like errors. Charts cycle through the list when a series has
     * more entries than colors.
     */
    val seriesColors: List<Color>
        @Composable
        @ReadOnlyComposable
        get() = with(FormaTheme.colorScheme) {
            listOf(primary, secondary, tertiary, onPrimaryContainer, onSecondaryContainer, error)
        }

    /** The default gridline color: the color scheme's subtle `outlineVariant`. */
    val gridLineColor: Color
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.colorScheme.outlineVariant

    /**
     * The style for numeric value labels: [FormaTheme.typography]`.numeric` (tabular figures,
     * so digits stay column-aligned) at `labelSmall` size and weight, colored `onSurface`.
     * The color is baked into the style because canvas-drawn text does not inherit
     * `LocalContentColor`.
     */
    val valueLabelStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() {
            val labelSmall = FormaTheme.typography.material.labelSmall
            return FormaTheme.typography.numeric.copy(
                color = FormaTheme.colorScheme.onSurface,
                fontSize = labelSmall.fontSize,
                lineHeight = labelSmall.lineHeight,
                fontWeight = labelSmall.fontWeight,
            )
        }

    /**
     * The style for axis/category labels: the type scale's `labelSmall`, colored
     * `onSurfaceVariant` (de-emphasised relative to the data). The color is baked into the
     * style because canvas-drawn text does not inherit `LocalContentColor`.
     */
    val axisLabelStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.typography.material.labelSmall.copy(
            color = FormaTheme.colorScheme.onSurfaceVariant,
        )

    /**
     * The default value-to-text formatter: rounds to at most one decimal place and drops a
     * trailing `.0` (`12f` → `"12"`, `12.34f` → `"12.3"`). Deterministic across platforms —
     * no locale-dependent formatting.
     */
    val ValueFormatter: (Float) -> String = { value ->
        val scaled = (value * 10f).roundToInt()
        val sign = if (scaled < 0) "-" else ""
        val magnitude = abs(scaled)
        if (magnitude % 10 == 0) {
            "$sign${magnitude / 10}"
        } else {
            "$sign${magnitude / 10}.${magnitude % 10}"
        }
    }

    /**
     * The default entry animation: an 800ms [tween] with [FastOutSlowInEasing]. Charts play it
     * once when their data first appears (and again when the data structurally changes). Pass
     * `animationSpec = null` to a chart to skip the animation and render the final frame
     * immediately.
     */
    val EntryAnimationSpec: FiniteAnimationSpec<Float> =
        tween(durationMillis = 800, easing = FastOutSlowInEasing)
}
