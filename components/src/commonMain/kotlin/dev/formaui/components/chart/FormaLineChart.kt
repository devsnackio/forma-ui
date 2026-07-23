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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
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
 * The alpha of the area fill's gradient where it meets the line; the fill fades from this
 * down to fully transparent at the plot floor.
 */
private const val FillStartAlpha = 0.25f

/**
 * A FormaUI line chart — a single series plotted as a trend line, drawn with pure Compose
 * `Canvas` (no chart-library dependency).
 *
 * ```
 * FormaLineChart(
 *     values = listOf(4f, 9f, 6f, 14f, 11f),
 *     xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri"),
 * )
 * ```
 *
 * Opinionated defaults, all overridable:
 * - **Sizing**: fills the available width at a minimum height of
 *   [FormaChartDefaults.MinChartHeight]; size it explicitly via [modifier].
 * - **Color**: the line uses the theme's `primary` color; override with [lineColor].
 * - **Shape**: a [smooth] cubic curve with an area [fillArea] gradient beneath it. Smoothing
 *   clamps its control points horizontally, so the curve never overshoots a value vertically.
 * - **Scale**: the y-axis spans the data's own min–max range, or the explicit
 *   [minValue]/[maxValue]; values outside an explicit range are clipped to the plot's edge.
 *   Negative values are welcome — the line simply spans the full range.
 * - **Motion**: the line sweeps in from the leading edge once on entry
 *   ([FormaChartDefaults.EntryAnimationSpec]), replaying only when [values] structurally
 *   change — never for an equal-but-new list. Pass `animationSpec = null` for a static final
 *   frame (deterministic in tests and screenshots).
 *
 * **Accessibility**: the chart exposes a text summary of its data (e.g. "Line chart with 5
 * points. Range 4 to 14. Latest value 11.") as its semantics content description,
 * auto-generated unless [contentDescription] is supplied. Empty [values] render nothing and
 * describe themselves as "Line chart with no data".
 *
 * Degenerate data never crashes: an empty list renders an empty (but described) chart area, a
 * single value renders as a lone point marker (there is no segment to draw), and a flat series
 * (min == max) draws a centered horizontal line.
 *
 * The chart is stateless: it draws exactly the [values] it is given, and callers own the data.
 *
 * @param values the series to plot, in order, spread evenly across the width. Values must be
 *   finite (negative values are allowed). An empty list renders an empty (but described)
 *   chart area.
 * @param modifier the [Modifier] applied to the chart.
 * @param lineColor the color of the line, its points, and the area fill (defaults to the
 *   theme's `primary`).
 * @param strokeWidth the thickness of the line (defaults to
 *   [FormaChartDefaults.LineStrokeWidth]).
 * @param minValue the value the y-axis bottoms out at. When `null`, the data's own minimum is
 *   used. Must be finite when supplied; values below it are clipped to the plot's bottom edge.
 * @param maxValue the value the y-axis tops out at. When `null`, the data's own maximum is
 *   used. Must be finite (and greater than [minValue] when both are supplied); values above it
 *   are clipped to the plot's top edge.
 * @param smooth whether segments are smoothed cubic curves (the default) or straight polyline
 *   segments. Smoothing clamps control points horizontally to the midpoint between neighboring
 *   x positions, so the curve never overshoots a value vertically.
 * @param fillArea whether to fill the area under the line with a vertical gradient of
 *   [lineColor] fading to transparent at the plot floor (defaults to `true`).
 * @param showPoints whether to draw a [FormaChartDefaults.LinePointRadius] circle marker at
 *   each data point (defaults to `false`). A single-value dataset always draws its point.
 * @param showGridLines whether to draw horizontal gridlines dividing the plot into
 *   [FormaChartDefaults.GridLineCount] bands (defaults to `true`), in
 *   [FormaChartDefaults.gridLineColor].
 * @param xLabels optional x-axis labels drawn under the plot in
 *   [FormaChartDefaults.axisLabelStyle], paired index-by-index with [values] and centered under
 *   their points; labels beyond the point count are ignored. Empty (the default) reserves no
 *   label band.
 * @param valueFormatter formats a value for the auto-generated accessibility summary (defaults
 *   to [FormaChartDefaults.ValueFormatter]).
 * @param animationSpec the entry animation for the leading-edge sweep (defaults to
 *   [FormaChartDefaults.EntryAnimationSpec]), or `null` to render statically.
 * @param contentDescription an explicit accessibility description; when `null` a summary of
 *   the data is generated automatically.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Unspecified,
    strokeWidth: Dp = FormaChartDefaults.LineStrokeWidth,
    minValue: Float? = null,
    maxValue: Float? = null,
    smooth: Boolean = true,
    fillArea: Boolean = true,
    showPoints: Boolean = false,
    showGridLines: Boolean = true,
    xLabels: List<String> = emptyList(),
    valueFormatter: (Float) -> String = FormaChartDefaults.ValueFormatter,
    animationSpec: FiniteAnimationSpec<Float>? = FormaChartDefaults.EntryAnimationSpec,
    contentDescription: String? = null,
) {
    values.forEachIndexed { index, value ->
        require(value.isFinite()) {
            "FormaLineChart values must be finite, but values[$index] is $value."
        }
    }
    if (minValue != null) {
        require(minValue.isFinite()) {
            "FormaLineChart minValue must be finite, but was $minValue."
        }
    }
    if (maxValue != null) {
        require(maxValue.isFinite()) {
            "FormaLineChart maxValue must be finite, but was $maxValue."
        }
    }
    if (minValue != null && maxValue != null) {
        require(maxValue > minValue) {
            "FormaLineChart maxValue must be greater than minValue, " +
                "but maxValue $maxValue <= minValue $minValue."
        }
    }

    val autoDescription = remember(values, valueFormatter) {
        lineChartContentDescription(values, valueFormatter)
    }
    val resolvedDescription = contentDescription ?: autoDescription
    val progress = rememberChartRevealProgress(dataKey = values, animationSpec = animationSpec)

    // Resolve theme-dependent values in composable scope: canvas-drawn text and shapes do not
    // read LocalContentColor or theme locals inside the draw lambda.
    val resolvedLineColor = lineColor.takeOrElse { FormaTheme.colorScheme.primary }
    val gridColor = FormaChartDefaults.gridLineColor
    val axisStyle = FormaChartDefaults.axisLabelStyle
    val labelGap = FormaTheme.spacing.xxs
    val pointRadius = FormaChartDefaults.LinePointRadius
    val textMeasurer = rememberTextMeasurer()
    val resolvedMin = minValue ?: (values.minOrNull() ?: 0f)
    val resolvedMax = maxValue ?: (values.maxOrNull() ?: 1f)

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
            if (values.isEmpty() || size.width <= 0f || size.height <= 0f) return@Canvas

            val labelGapPx = labelGap.toPx()
            val strokePx = strokeWidth.toPx()
            val pointRadiusPx = pointRadius.toPx()
            val drawsPoints = showPoints || values.size == 1

            // Reserve a horizontal band for x labels. All labels are single-line in one style, so
            // one measurement yields the band's height.
            val axisLabelHeight = if (xLabels.isNotEmpty()) {
                textMeasurer.measure(
                    text = AnnotatedString(xLabels.first()),
                    style = axisStyle,
                    maxLines = 1,
                ).size.height.toFloat()
            } else {
                0f
            }

            // Inset the plot vertically so a stroke (or point marker) sitting exactly on the
            // range's edge isn't clipped by the canvas bounds.
            val edgeInset = maxOf(strokePx / 2f, if (drawsPoints) pointRadiusPx else 0f)
            val plotTop = edgeInset
            val plotBottom = size.height - edgeInset -
                if (xLabels.isNotEmpty()) axisLabelHeight + labelGapPx else 0f
            val plotHeight = (plotBottom - plotTop).coerceAtLeast(0f)
            if (plotHeight <= 0f) return@Canvas

            val range = resolvedMax - resolvedMin
            // A flat series (or a degenerate range) centers the line instead of dividing by zero.
            fun yFor(value: Float): Float = if (range > 0f) {
                plotBottom - plotHeight * ((value - resolvedMin) / range).coerceIn(0f, 1f)
            } else {
                plotTop + plotHeight / 2f
            }

            // The same inset applies horizontally, so end caps and edge markers aren't clipped.
            val plotLeft = edgeInset
            val plotWidth = (size.width - 2f * edgeInset).coerceAtLeast(0f)
            val stepX = if (values.size > 1) plotWidth / (values.size - 1) else 0f
            fun xFor(index: Int): Float =
                if (values.size > 1) plotLeft + index * stepX else size.width / 2f

            if (showGridLines) {
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

            // The reveal sweeps the line in from the leading edge: everything data-driven draws
            // inside a clip that widens with progress. Gridlines and labels stay static.
            clipRect(right = size.width * progress) {
                if (values.size > 1) {
                    val linePath = Path().apply {
                        moveTo(xFor(0), yFor(values[0]))
                        for (index in 1 until values.size) {
                            val endX = xFor(index)
                            val endY = yFor(values[index])
                            if (smooth) {
                                // Horizontally-clamped cubic: both control points sit at the
                                // midpoint x and keep their endpoint's y — no vertical overshoot.
                                val midX = (xFor(index - 1) + endX) / 2f
                                cubicTo(midX, yFor(values[index - 1]), midX, endY, endX, endY)
                            } else {
                                lineTo(endX, endY)
                            }
                        }
                    }

                    if (fillArea) {
                        val fillPath = Path().apply {
                            addPath(linePath)
                            lineTo(xFor(values.size - 1), plotBottom)
                            lineTo(xFor(0), plotBottom)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    resolvedLineColor.copy(alpha = FillStartAlpha),
                                    Color.Transparent,
                                ),
                                startY = plotTop,
                                endY = plotBottom,
                            ),
                        )
                    }

                    drawPath(
                        path = linePath,
                        color = resolvedLineColor,
                        style = Stroke(
                            width = strokePx,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        ),
                    )
                }

                if (drawsPoints) {
                    values.forEachIndexed { index, value ->
                        drawCircle(
                            color = resolvedLineColor,
                            radius = pointRadiusPx,
                            center = Offset(xFor(index), yFor(value)),
                        )
                    }
                }
            }

            if (xLabels.isNotEmpty()) {
                val slotWidth = if (values.size > 1) stepX else size.width
                val labelCount = minOf(xLabels.size, values.size)
                for (index in 0 until labelCount) {
                    val labelLayout = textMeasurer.measure(
                        text = AnnotatedString(xLabels[index]),
                        style = axisStyle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        constraints = Constraints(maxWidth = slotWidth.toInt().coerceAtLeast(0)),
                    )
                    val x = (xFor(index) - labelLayout.size.width / 2f)
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
