/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A single data point in a FormaUI chart — one bar in a [FormaBarChart] or one segment in a
 * donut chart.
 *
 * Entries are plain, immutable data: charts are stateless and redraw whenever the caller passes
 * a new (structurally different) list. Two lists with equal entries are treated as the same data,
 * so recreating an equal list does **not** replay the chart's entry animation.
 *
 * ```
 * FormaBarChart(
 *     entries = listOf(
 *         FormaChartEntry("Jan", 12f),
 *         FormaChartEntry("Feb", 32f),
 *         FormaChartEntry("Mar", 21f),
 *     ),
 * )
 * ```
 *
 * @property label the category name for this entry (e.g. a month, a product). Used for axis
 *   labels and the auto-generated accessibility summary.
 * @property value the entry's numeric value. Bar and donut charts require finite, non-negative
 *   values.
 * @property color an optional explicit color for this entry. When [Color.Unspecified] (the
 *   default), the chart resolves a color itself — a bar chart uses its single bar color, a donut
 *   chart cycles [FormaChartDefaults.seriesColors].
 */
@ExperimentalFormaUiApi
@Immutable
data class FormaChartEntry(
    val label: String,
    val value: Float,
    val color: Color = Color.Unspecified,
)
