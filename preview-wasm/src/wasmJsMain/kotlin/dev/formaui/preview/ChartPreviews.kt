/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.chart.FormaBarChart
import dev.formaui.components.chart.FormaChartEntry
import dev.formaui.components.chart.FormaChartLegend
import dev.formaui.components.chart.FormaDonutChart
import dev.formaui.components.chart.FormaLineChart
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlin.random.Random

private val barMonths = listOf("Jan", "Feb", "Mar", "Apr")

private fun randomBarEntries(): List<FormaChartEntry> =
    barMonths.map { month -> FormaChartEntry(month, Random.nextInt(from = 6, until = 40).toFloat()) }

/** Live preview for `bar-chart`: sample monthly data; randomizing replays the entry animation. */
@Composable
internal fun ColumnScope.BarChartPreview() {
    var entries by remember {
        mutableStateOf(
            listOf(
                FormaChartEntry("Jan", 12f),
                FormaChartEntry("Feb", 32f),
                FormaChartEntry("Mar", 21f),
                FormaChartEntry("Apr", 27f),
            ),
        )
    }

    FormaBarChart(entries = entries, showValueLabels = true)

    FormaButton(onClick = { entries = randomBarEntries() }, variant = FormaButtonVariant.Tonal) {
        Text("Randomize data")
    }
}

/** Live preview for `donut-chart`: proportional segments, center content, and the shared legend. */
@Composable
internal fun ColumnScope.DonutChartPreview() {
    val entries = remember {
        listOf(
            FormaChartEntry("Rent", 850f),
            FormaChartEntry("Groceries", 420f),
            FormaChartEntry("Transport", 210f),
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormaDonutChart(
            entries = entries,
            centerContent = { Text("1,480", style = MaterialTheme.typography.titleMedium) },
        )
        FormaChartLegend(entries = entries)
    }
}

/** Live preview for `line-chart`: a week of sample data with live smooth/points toggles. */
@Composable
internal fun ColumnScope.LineChartPreview() {
    var smooth by remember { mutableStateOf(true) }
    var showPoints by remember { mutableStateOf(true) }

    FormaLineChart(
        values = listOf(4f, 9f, 6f, 14f, 11f, 18f, 13f),
        smooth = smooth,
        showPoints = showPoints,
        xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaSwitch(checked = smooth, onCheckedChange = { smooth = it })
            Text("Smooth", style = MaterialTheme.typography.bodyMedium)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaSwitch(checked = showPoints, onCheckedChange = { showPoints = it })
            Text("Points", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
