/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/** Shared sample data for the line chart previews. */
private val PreviewValues = listOf(4f, 9f, 6f, 14f, 11f, 18f, 15f)

/** Preview of [FormaLineChart] with all defaults: a smooth primary line with an area fill. */
@Preview
@Composable
private fun FormaLineChartPreview() {
    FormaTheme {
        Surface {
            FormaLineChart(
                values = PreviewValues,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
            )
        }
    }
}

/** Preview of [FormaLineChart] with `smooth = false`: straight polyline segments, no fill. */
@Preview
@Composable
private fun FormaLineChartStraightPreview() {
    FormaTheme {
        Surface {
            FormaLineChart(
                values = PreviewValues,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                smooth = false,
                fillArea = false,
            )
        }
    }
}

/** Preview of [FormaLineChart] with point markers drawn at each data point. */
@Preview
@Composable
private fun FormaLineChartPointsPreview() {
    FormaTheme {
        Surface {
            FormaLineChart(
                values = PreviewValues,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                showPoints = true,
            )
        }
    }
}

/** Preview of [FormaLineChart] with negative values: the y-range spans below zero. */
@Preview
@Composable
private fun FormaLineChartNegativeValuesPreview() {
    FormaTheme {
        Surface {
            FormaLineChart(
                values = listOf(-8f, -3f, 5f, -1f, 12f, 7f, -4f),
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                showPoints = true,
            )
        }
    }
}

/** Preview of [FormaLineChart] with x-axis labels drawn under the plot. */
@Preview
@Composable
private fun FormaLineChartXLabelsPreview() {
    FormaTheme {
        Surface {
            FormaLineChart(
                values = PreviewValues,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            )
        }
    }
}

/** Preview of [FormaLineChart] with degenerate data: a single point and a flat series. */
@Preview
@Composable
private fun FormaLineChartMinimalDataPreview() {
    FormaTheme {
        Surface {
            Column {
                FormaLineChart(
                    values = listOf(7f),
                    modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                )
                FormaLineChart(
                    values = listOf(5f, 5f, 5f, 5f),
                    modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                    showPoints = true,
                )
            }
        }
    }
}
