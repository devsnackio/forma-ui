/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A FormaUI bar chart — vertical bars with rounded top corners, drawn with pure Compose
 * `Canvas` (no chart-library dependency).
 *
 * ```
 * FormaBarChart(
 *     entries = listOf(
 *         FormaChartEntry("Jan", 12f),
 *         FormaChartEntry("Feb", 32f),
 *         FormaChartEntry("Mar", 21f),
 *     ),
 *     showValueLabels = true,
 * )
 * ```
 *
 * Opinionated defaults, all overridable:
 * - **Sizing**: fills the available width at a minimum height of
 *   [FormaChartDefaults.MinChartHeight]; size it explicitly via [modifier].
 * - **Color**: bars use the theme's `primary` color; override globally with [barColor] or
 *   per entry with [FormaChartEntry.color].
 * - **Scale**: the y-axis runs from zero to a "nice" rounded maximum derived from the data,
 *   or to an explicit [maxValue].
 * - **Motion**: bars grow from the baseline once on entry
 *   ([FormaChartDefaults.EntryAnimationSpec]), replaying only when [entries] structurally
 *   change — never for an equal-but-new list. Pass `animationSpec = null` for a static
 *   final frame (deterministic in tests and screenshots).
 *
 * **Accessibility**: the chart exposes a text summary of its data (e.g. "Bar chart with 3
 * categories. Jan: 12. …") as its semantics content description, auto-generated unless
 * [contentDescription] is supplied. Empty [entries] render nothing and describe themselves as
 * "Bar chart with no data".
 *
 * The chart is stateless: it draws exactly the [entries] it is given, and callers own the data.
 *
 * @param entries the data to plot, one bar per entry, in order. Values must be finite and
 *   non-negative. An empty list renders an empty (but described) chart area.
 * @param modifier the [Modifier] applied to the chart.
 * @param barColor the fill color for all bars (defaults to the theme's `primary`). A
 *   [FormaChartEntry.color] that is specified overrides this for that bar.
 * @param maxValue the value the y-axis tops out at. When `null`, a "nice" rounded maximum is
 *   computed from the data. Must be finite and positive when supplied; values above it are
 *   clipped to the top of the plot.
 * @param showValueLabels whether to draw each bar's formatted value above it (defaults to
 *   `false`). Labels use [FormaChartDefaults.valueLabelStyle] (tabular figures).
 * @param showCategoryLabels whether to draw each entry's [FormaChartEntry.label] below its bar
 *   (defaults to `true`). Labels use [FormaChartDefaults.axisLabelStyle], centered per bar and
 *   ellipsized to the bar's slot width.
 * @param showGridLines whether to draw horizontal gridlines dividing the plot into
 *   [FormaChartDefaults.GridLineCount] bands (defaults to `true`), in
 *   [FormaChartDefaults.gridLineColor].
 * @param barCornerRadius the radius of each bar's top corners (defaults to
 *   [FormaChartDefaults.BarCornerRadius]).
 * @param valueFormatter formats a value for value labels and the auto-generated accessibility
 *   summary (defaults to [FormaChartDefaults.ValueFormatter]).
 * @param animationSpec the entry animation for bar growth (defaults to
 *   [FormaChartDefaults.EntryAnimationSpec]), or `null` to render statically.
 * @param contentDescription an explicit accessibility description; when `null` a summary of
 *   the data is generated automatically.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaBarChart(
    entries: List<FormaChartEntry>,
    modifier: Modifier = Modifier,
    barColor: Color = Color.Unspecified,
    maxValue: Float? = null,
    showValueLabels: Boolean = false,
    showCategoryLabels: Boolean = true,
    showGridLines: Boolean = true,
    barCornerRadius: Dp = FormaChartDefaults.BarCornerRadius,
    valueFormatter: (Float) -> String = FormaChartDefaults.ValueFormatter,
    animationSpec: FiniteAnimationSpec<Float>? = FormaChartDefaults.EntryAnimationSpec,
    contentDescription: String? = null,
) {
    entries.forEach { entry ->
        require(entry.value.isFinite() && entry.value >= 0f) {
            "FormaBarChart values must be finite and non-negative, " +
                "but \"${entry.label}\" is ${entry.value}."
        }
    }
    if (maxValue != null) {
        require(maxValue.isFinite() && maxValue > 0f) {
            "FormaBarChart maxValue must be finite and positive, but was $maxValue."
        }
    }

    val autoDescription = remember(entries, valueFormatter) {
        barChartContentDescription(entries, valueFormatter)
    }
    val resolvedDescription = contentDescription ?: autoDescription
    val progress = rememberChartRevealProgress(dataKey = entries, animationSpec = animationSpec)

    // Resolve theme-dependent values in composable scope: canvas-drawn text and shapes do not
    // read LocalContentColor or theme locals inside the draw lambda.
    val resolvedBarColor = barColor.takeOrElse { FormaTheme.colorScheme.primary }
    val gridColor = FormaChartDefaults.gridLineColor
    val valueStyle = FormaChartDefaults.valueLabelStyle
    val axisStyle = FormaChartDefaults.axisLabelStyle
    val barGap = FormaTheme.spacing.xs
    val labelGap = FormaTheme.spacing.xxs
    val textMeasurer = rememberTextMeasurer()
    val resolvedMax = maxValue ?: computeNiceMax(entries.maxOfOrNull { it.value } ?: 0f)

    // The Box carries the sizing and semantics while the Canvas fills it via matchParentSize:
    // Canvas alone is a Spacer whose measure policy reports 0 for any non-fixed dimension, so
    // DrawScope.size would be 0 even though the layout slot looks right. matchParentSize hands
    // the canvas FIXED constraints from the Box, making DrawScope.size the real slot size.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = FormaChartDefaults.MinChartHeight)
            .semantics { this.contentDescription = resolvedDescription },
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            if (entries.isEmpty() || size.width <= 0f || size.height <= 0f) return@Canvas

            val barGapPx = barGap.toPx()
            val labelGapPx = labelGap.toPx()
            val cornerPx = barCornerRadius.toPx()

            // Reserve horizontal bands for labels. All labels are single-line in a single style,
            // so one measurement per band yields its height.
            val valueLabelHeight = if (showValueLabels) {
                textMeasurer.measure(
                    text = AnnotatedString(valueFormatter(entries.first().value)),
                    style = valueStyle,
                    maxLines = 1,
                ).size.height.toFloat()
            } else {
                0f
            }
            val axisLabelHeight = if (showCategoryLabels) {
                textMeasurer.measure(
                    text = AnnotatedString(entries.first().label),
                    style = axisStyle,
                    maxLines = 1,
                ).size.height.toFloat()
            } else {
                0f
            }

            val plotTop = if (showValueLabels) valueLabelHeight + labelGapPx else 0f
            val plotBottom =
                size.height - if (showCategoryLabels) axisLabelHeight + labelGapPx else 0f
            val plotHeight = (plotBottom - plotTop).coerceAtLeast(0f)

            val barCount = entries.size
            val barWidth = ((size.width - barGapPx * (barCount - 1)) / barCount).coerceAtLeast(0f)
            val slotWidth = barWidth + barGapPx

            if (showGridLines && plotHeight > 0f) {
                // Baseline plus GridLineCount evenly spaced lines up to the plot top (hairline).
                for (line in 0..FormaChartDefaults.GridLineCount) {
                    val y = plotBottom - plotHeight * line / FormaChartDefaults.GridLineCount
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                    )
                }
            }

            entries.forEachIndexed { index, entry ->
                val left = index * slotWidth
                val centerX = left + barWidth / 2f
                val fraction =
                    if (resolvedMax > 0f) (entry.value / resolvedMax).coerceIn(0f, 1f) else 0f
                val barHeight = plotHeight * fraction * progress

                if (barHeight > 0f && barWidth > 0f) {
                    val radius = minOf(cornerPx, barWidth / 2f, barHeight)
                    val barPath = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    left = left,
                                    top = plotBottom - barHeight,
                                    right = left + barWidth,
                                    bottom = plotBottom,
                                ),
                                topLeft = CornerRadius(radius),
                                topRight = CornerRadius(radius),
                            ),
                        )
                    }
                    drawPath(path = barPath, color = entry.color.takeOrElse { resolvedBarColor })
                }

                if (showValueLabels) {
                    val valueLayout = textMeasurer.measure(
                        text = AnnotatedString(valueFormatter(entry.value)),
                        style = valueStyle,
                        maxLines = 1,
                    )
                    val x = (centerX - valueLayout.size.width / 2f)
                        .coerceIn(0f, (size.width - valueLayout.size.width).coerceAtLeast(0f))
                    val y = (plotBottom - barHeight - labelGapPx - valueLayout.size.height)
                        .coerceAtLeast(0f)
                    drawText(textLayoutResult = valueLayout, topLeft = Offset(x, y))
                }

                if (showCategoryLabels) {
                    val labelLayout = textMeasurer.measure(
                        text = AnnotatedString(entry.label),
                        style = axisStyle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        constraints = Constraints(maxWidth = slotWidth.toInt().coerceAtLeast(0)),
                    )
                    val x = (centerX - labelLayout.size.width / 2f)
                        .coerceIn(0f, (size.width - labelLayout.size.width).coerceAtLeast(0f))
                    drawText(
                        textLayoutResult = labelLayout,
                        topLeft = Offset(x, size.height - labelLayout.size.height),
                    )
                }
            }
        }
    }
}
