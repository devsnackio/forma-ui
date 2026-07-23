/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A legend for FormaUI charts — a wrapping row of color swatches and labels, one item per
 * [FormaChartEntry].
 *
 * FormaUI charts never embed their own legend (callers own composition); place one next to the
 * chart it describes and pass the same entries — and, for a donut, the same palette — so the
 * swatches resolve to exactly the chart's segment colors:
 *
 * ```
 * Column {
 *     FormaDonutChart(entries = budget)
 *     FormaChartLegend(entries = budget)
 * }
 * ```
 *
 * Items wrap onto new lines as needed. Labels are real [Text], so the legend is natively
 * accessible: screen readers announce each entry, complementing the chart's summary content
 * description. An empty [entries] list renders nothing.
 *
 * @param entries the entries to describe, in the same order they appear in the chart.
 * @param modifier the [Modifier] applied to the legend.
 * @param colors the palette cycled for swatches whose entry has no explicit
 *   [FormaChartEntry.color]; `null` (the default) uses [FormaChartDefaults.seriesColors] —
 *   the same resolution a donut chart applies to its segments.
 * @param valueFormatter when non-null, each entry's formatted value is appended to its label
 *   (e.g. "Rent: 850"); when `null` (the default) only labels are shown.
 */
@ExperimentalFormaUiApi
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormaChartLegend(
    entries: List<FormaChartEntry>,
    modifier: Modifier = Modifier,
    colors: List<Color>? = null,
    valueFormatter: ((Float) -> String)? = null,
) {
    val palette = colors ?: FormaChartDefaults.seriesColors
    val fallbackColor = FormaTheme.colorScheme.primary
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
    ) {
        entries.forEachIndexed { index, entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            ) {
                Box(
                    modifier = Modifier
                        .size(FormaChartDefaults.LegendSwatchSize)
                        .background(
                            color = resolveSeriesColor(entry.color, index, palette)
                                .takeOrElse { fallbackColor },
                            shape = RoundedCornerShape(FormaChartDefaults.BarCornerRadius),
                        ),
                )
                Text(
                    text = if (valueFormatter != null) {
                        "${entry.label}: ${valueFormatter(entry.value)}"
                    } else {
                        entry.label
                    },
                    style = FormaTheme.typography.material.labelMedium,
                )
            }
        }
    }
}
