/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/** Shared sample data for the bar chart previews. */
private val PreviewEntries = listOf(
    FormaChartEntry("Jan", 12f),
    FormaChartEntry("Feb", 32f),
    FormaChartEntry("Mar", 21f),
    FormaChartEntry("Apr", 45f),
    FormaChartEntry("May", 27f),
)

/** Preview of [FormaBarChart] with all defaults: primary bars, gridlines, category labels. */
@Preview
@Composable
private fun FormaBarChartPreview() {
    FormaTheme {
        Surface {
            FormaBarChart(
                entries = PreviewEntries,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
            )
        }
    }
}

/** Preview of [FormaBarChart] with value labels drawn above each bar. */
@Preview
@Composable
private fun FormaBarChartValueLabelsPreview() {
    FormaTheme {
        Surface {
            FormaBarChart(
                entries = PreviewEntries,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                showValueLabels = true,
            )
        }
    }
}

/** Preview of [FormaBarChart] with a custom bar color plus one per-entry color override. */
@Preview
@Composable
private fun FormaBarChartCustomColorPreview() {
    FormaTheme {
        Surface {
            FormaBarChart(
                entries = listOf(
                    FormaChartEntry("Q1", 18f),
                    FormaChartEntry("Q2", 30f),
                    FormaChartEntry("Q3", 24f, color = FormaTheme.colorScheme.error),
                    FormaChartEntry("Q4", 38f),
                ),
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                barColor = FormaTheme.colorScheme.tertiary,
            )
        }
    }
}

/** Preview of [FormaBarChart] with gridlines disabled — bars and labels only. */
@Preview
@Composable
private fun FormaBarChartNoGridLinesPreview() {
    FormaTheme {
        Surface {
            FormaBarChart(
                entries = PreviewEntries,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                showGridLines = false,
            )
        }
    }
}

/** Preview of [FormaBarChart] with `animationSpec = null`: a static final frame. */
@Preview
@Composable
private fun FormaBarChartStaticPreview() {
    FormaTheme {
        Surface {
            FormaBarChart(
                entries = PreviewEntries,
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
                showValueLabels = true,
                animationSpec = null,
            )
        }
    }
}

/** Preview of [FormaBarChart] with no data: an empty, described chart area. */
@Preview
@Composable
private fun FormaBarChartEmptyPreview() {
    FormaTheme {
        Surface {
            FormaBarChart(
                entries = emptyList(),
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.md),
            )
        }
    }
}
