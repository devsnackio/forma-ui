/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.formaui.components.chart.FormaBarChart
import dev.formaui.components.chart.FormaChartDefaults
import dev.formaui.components.chart.FormaChartEntry
import dev.formaui.components.chart.FormaChartLegend
import dev.formaui.components.chart.FormaDonutChart
import dev.formaui.components.chart.FormaLineChart
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/** The chart demos shown in the **Containment** category: BarChart, DonutChart, LineChart. */

@Composable
fun BarChartShowcase() {
    ComponentShowcase(
        name = "BarChart",
        description = "Vertical rounded bars with gridlines, category labels, and value labels.",
    ) {
        FormaBarChart(
            entries = listOf(
                FormaChartEntry("Jan", 12f),
                FormaChartEntry("Feb", 32f),
                FormaChartEntry("Mar", 21f),
                FormaChartEntry("Apr", 45f),
                FormaChartEntry("May", 27f),
            ),
            modifier = Modifier.fillMaxWidth(),
            showValueLabels = true,
        )
    }
}

@Composable
fun DonutChartShowcase() {
    ComponentShowcase(
        name = "DonutChart",
        description = "Proportional segments around an open center slot, with a legend below.",
    ) {
        val entries = listOf(
            FormaChartEntry("Rent", 850f),
            FormaChartEntry("Groceries", 420f),
            FormaChartEntry("Transport", 210f),
            FormaChartEntry("Savings", 120f),
        )
        FormaDonutChart(
            entries = entries,
            modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally),
            centerContent = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total", style = MaterialTheme.typography.labelSmall)
                    Text("$1,600", style = FormaTheme.typography.numeric)
                }
            },
        )
        FormaChartLegend(
            entries = entries,
            valueFormatter = FormaChartDefaults.ValueFormatter,
        )
    }
}

@Composable
fun LineChartShowcase() {
    ComponentShowcase(
        name = "LineChart",
        description = "A smooth trend line with an area fill, point markers, and x-axis labels.",
    ) {
        FormaLineChart(
            values = listOf(4f, 9f, 6f, 14f, 11f, 18f, 15f),
            modifier = Modifier.fillMaxWidth(),
            showPoints = true,
            xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        )
        FormaLineChart(
            values = listOf(22f, 17f, 19f, 12f, 15f, 9f, 11f),
            modifier = Modifier.fillMaxWidth(),
            smooth = false,
            fillArea = false,
        )
    }
}
