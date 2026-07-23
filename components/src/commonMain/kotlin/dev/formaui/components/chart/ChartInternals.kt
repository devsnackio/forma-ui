/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Remembers the chart's entry-reveal progress in `0f..1f`.
 *
 * On first composition (and whenever [dataKey] becomes structurally different) an
 * [Animatable] restarts at `0f` and animates to `1f` with [animationSpec]. Because both
 * [remember] and [LaunchedEffect] compare keys with `equals`, passing an equal-but-new list
 * as [dataKey] does **not** replay the animation.
 *
 * A `null` [animationSpec] disables the animation entirely: the function returns a constant
 * `1f`, so the chart renders its final frame immediately (deterministic for tests and
 * previews).
 */
@Composable
internal fun rememberChartRevealProgress(
    dataKey: Any?,
    animationSpec: FiniteAnimationSpec<Float>?,
): Float {
    if (animationSpec == null) return 1f
    val progress = remember(dataKey) { Animatable(0f) }
    LaunchedEffect(dataKey, animationSpec) {
        progress.animateTo(targetValue = 1f, animationSpec = animationSpec)
    }
    return progress.value
}

/**
 * Rounds [rawMax] up to a "nice" axis maximum (1, 2, 2.5, or 5 times a power of ten), so the
 * top gridline lands on a friendly value. Non-positive or non-finite input falls back to `1f`,
 * which keeps all-zero datasets drawable without a divide-by-zero.
 */
internal fun computeNiceMax(rawMax: Float): Float {
    if (!rawMax.isFinite() || rawMax <= 0f) return 1f
    val exponent = floor(log10(rawMax.toDouble()))
    val magnitude = 10.0.pow(exponent)
    val fraction = rawMax / magnitude
    val niceFraction = when {
        fraction <= 1.0 -> 1.0
        fraction <= 2.0 -> 2.0
        fraction <= 2.5 -> 2.5
        fraction <= 5.0 -> 5.0
        else -> 10.0
    }
    return (niceFraction * magnitude).toFloat()
}

/**
 * Builds the auto-generated accessibility summary for a bar chart, e.g.
 * `"Bar chart with 4 categories. Jan: 12. Feb: 32. Mar: 21. Apr: 45."`.
 * Empty [entries] produce `"Bar chart with no data"`.
 *
 * Pure and non-composable so it is unit-testable without a compose host.
 */
internal fun barChartContentDescription(
    entries: List<FormaChartEntry>,
    valueFormatter: (Float) -> String,
): String {
    if (entries.isEmpty()) return "Bar chart with no data"
    val categories = if (entries.size == 1) "1 category" else "${entries.size} categories"
    val data = entries.joinToString(separator = ". ", postfix = ".") { entry ->
        "${entry.label}: ${valueFormatter(entry.value)}"
    }
    return "Bar chart with $categories. $data"
}

/**
 * Builds the auto-generated accessibility summary for a donut chart, e.g.
 * `"Donut chart with 3 segments. Rent: 50 percent. Food: 30 percent. Fun: 20 percent."`.
 * Percentages are integers (independently rounded, so they may not sum to exactly 100).
 * Empty [entries] produce `"Donut chart with no data"`; a non-empty, all-zero dataset reports
 * its zero total instead of percentages.
 *
 * Pure and non-composable so it is unit-testable without a compose host.
 */
internal fun donutChartContentDescription(entries: List<FormaChartEntry>): String {
    if (entries.isEmpty()) return "Donut chart with no data"
    val segments = if (entries.size == 1) "1 segment" else "${entries.size} segments"
    val total = entries.fold(0f) { acc, entry -> acc + entry.value }
    if (total <= 0f) return "Donut chart with $segments. Total is zero."
    val data = entries.joinToString(separator = ". ", postfix = ".") { entry ->
        val percent = (entry.value / total * 100f).roundToInt()
        "${entry.label}: $percent percent"
    }
    return "Donut chart with $segments. $data"
}

/**
 * Builds the auto-generated accessibility summary for a line chart, e.g.
 * `"Line chart with 5 points. Range 3 to 45. Latest value 27."`.
 * Empty [values] produce `"Line chart with no data"`.
 *
 * Pure and non-composable so it is unit-testable without a compose host.
 */
internal fun lineChartContentDescription(
    values: List<Float>,
    valueFormatter: (Float) -> String,
): String {
    if (values.isEmpty()) return "Line chart with no data"
    val points = if (values.size == 1) "1 point" else "${values.size} points"
    val min = valueFormatter(values.min())
    val max = valueFormatter(values.max())
    val latest = valueFormatter(values.last())
    return "Line chart with $points. Range $min to $max. Latest value $latest."
}

/**
 * Resolves the color for the series entry at [index]: an explicitly [entryColor] wins;
 * otherwise [seriesColors] is cycled ([index] modulo size). Returns [Color.Unspecified] only
 * when no explicit color is given and [seriesColors] is empty.
 */
internal fun resolveSeriesColor(
    entryColor: Color,
    index: Int,
    seriesColors: List<Color>,
): Color = when {
    entryColor.isSpecified -> entryColor
    seriesColors.isEmpty() -> Color.Unspecified
    else -> seriesColors[index % seriesColors.size]
}
