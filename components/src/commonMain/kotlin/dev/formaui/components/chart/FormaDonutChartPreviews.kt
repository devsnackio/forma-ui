/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/** Shared sample data for the donut chart previews. */
private val PreviewEntries = listOf(
    FormaChartEntry("Rent", 850f),
    FormaChartEntry("Groceries", 420f),
    FormaChartEntry("Transport", 210f),
    FormaChartEntry("Savings", 120f),
)

/** The demo diameter used across the previews. */
private val PreviewDonutSize = 200.dp

/** Preview of [FormaDonutChart] with all defaults: cycled series colors, rounded caps, 2° gaps. */
@Preview
@Composable
private fun FormaDonutChartPreview() {
    FormaTheme {
        Surface {
            FormaDonutChart(
                entries = PreviewEntries,
                modifier = Modifier.padding(FormaTheme.spacing.md).size(PreviewDonutSize),
            )
        }
    }
}

/** Preview of [FormaDonutChart] with `strokeCap = StrokeCap.Butt`: flat, squared segment ends. */
@Preview
@Composable
private fun FormaDonutChartButtCapPreview() {
    FormaTheme {
        Surface {
            FormaDonutChart(
                entries = PreviewEntries,
                modifier = Modifier.padding(FormaTheme.spacing.md).size(PreviewDonutSize),
                strokeCap = StrokeCap.Butt,
            )
        }
    }
}

/** Preview of [FormaDonutChart] with a custom `segmentColors` palette. */
@Preview
@Composable
private fun FormaDonutChartCustomColorsPreview() {
    FormaTheme {
        Surface {
            FormaDonutChart(
                entries = PreviewEntries,
                modifier = Modifier.padding(FormaTheme.spacing.md).size(PreviewDonutSize),
                segmentColors = listOf(
                    FormaTheme.colorScheme.tertiary,
                    FormaTheme.colorScheme.primary,
                    FormaTheme.colorScheme.secondary,
                    FormaTheme.colorScheme.error,
                ),
            )
        }
    }
}

/** Preview of [FormaDonutChart] with a total composed in the center hole. */
@Preview
@Composable
private fun FormaDonutChartCenterContentPreview() {
    FormaTheme {
        Surface {
            FormaDonutChart(
                entries = PreviewEntries,
                modifier = Modifier.padding(FormaTheme.spacing.md).size(PreviewDonutSize),
                centerContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total",
                            style = FormaTheme.typography.material.labelSmall,
                        )
                        Text(
                            text = "1600",
                            style = FormaTheme.typography.material.titleMedium,
                        )
                    }
                },
            )
        }
    }
}

/** Preview of [FormaDonutChart] paired with a [FormaChartLegend] showing formatted values. */
@Preview
@Composable
private fun FormaDonutChartWithLegendPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FormaDonutChart(
                    entries = PreviewEntries,
                    modifier = Modifier.size(PreviewDonutSize),
                )
                FormaChartLegend(
                    entries = PreviewEntries,
                    valueFormatter = FormaChartDefaults.ValueFormatter,
                )
            }
        }
    }
}

/** Preview of [FormaChartLegend] with all defaults: labels only, cycled series colors. */
@Preview
@Composable
private fun FormaChartLegendPreview() {
    FormaTheme {
        Surface {
            FormaChartLegend(
                entries = PreviewEntries,
                modifier = Modifier.padding(FormaTheme.spacing.md),
            )
        }
    }
}

/** Preview of [FormaDonutChart] with an all-zero dataset: a neutral track ring, no crash. */
@Preview
@Composable
private fun FormaDonutChartZeroTotalPreview() {
    FormaTheme {
        Surface {
            FormaDonutChart(
                entries = listOf(
                    FormaChartEntry("Rent", 0f),
                    FormaChartEntry("Groceries", 0f),
                ),
                modifier = Modifier.padding(FormaTheme.spacing.md).size(PreviewDonutSize),
            )
        }
    }
}

/** Preview of [FormaDonutChart] with `animationSpec = null`: a static final frame. */
@Preview
@Composable
private fun FormaDonutChartStaticPreview() {
    FormaTheme {
        Surface {
            FormaDonutChart(
                entries = PreviewEntries,
                modifier = Modifier.padding(FormaTheme.spacing.md).size(PreviewDonutSize),
                animationSpec = null,
            )
        }
    }
}
