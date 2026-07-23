/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlin.math.PI
import kotlin.math.min

/**
 * The minimal sweep, in degrees, drawn for a segment whose rounded-cap inset would otherwise
 * reduce it to nothing (a tiny segment, or one early in the reveal animation): it renders as a
 * dot rather than vanishing.
 */
private const val MinInsetSweepDegrees = 0.1f

/**
 * A FormaUI donut chart — proportional stroked arc segments around an open center, drawn with
 * pure Compose `Canvas` (no chart-library dependency).
 *
 * ```
 * FormaDonutChart(
 *     entries = listOf(
 *         FormaChartEntry("Rent", 850f),
 *         FormaChartEntry("Groceries", 420f),
 *         FormaChartEntry("Transport", 210f),
 *     ),
 *     centerContent = { Text("1480") },
 * )
 * ```
 *
 * Opinionated defaults, all overridable:
 * - **Sizing**: keeps a 1:1 aspect ratio, filling the available width at a minimum size of
 *   [FormaChartDefaults.MinDonutSize]; size it explicitly via [modifier] (e.g.
 *   `Modifier.size(200.dp)`).
 * - **Color**: segments cycle [FormaChartDefaults.seriesColors]; override the palette with
 *   [segmentColors] or a single segment with [FormaChartEntry.color].
 * - **Shape**: segments are stroked arcs ([strokeWidth] thick) with rounded ends
 *   ([strokeCap], [StrokeCap.Round] by default), separated by [segmentGapDegrees]-degree gaps,
 *   starting at [startAngle] (12 o'clock by default) and proceeding clockwise.
 * - **Motion**: the ring sweeps in clockwise from [startAngle] once on entry
 *   ([FormaChartDefaults.EntryAnimationSpec]), replaying only when [entries] structurally
 *   change — never for an equal-but-new list. Pass `animationSpec = null` for a static final
 *   frame (deterministic in tests and screenshots).
 *
 * **Accessibility**: the chart exposes a text summary of its data with integer percentages
 * (e.g. "Donut chart with 3 segments. Rent: 57 percent. …") as its semantics content
 * description, auto-generated unless [contentDescription] is supplied. Empty [entries]
 * describe themselves as "Donut chart with no data"; a non-empty all-zero dataset reports its
 * zero total. In both cases a neutral `surfaceVariant` track ring is drawn instead of segments.
 *
 * The chart is stateless: it draws exactly the [entries] it is given, and callers own the data.
 * It never embeds a legend — compose a [FormaChartLegend] next to it with the same entries.
 *
 * @param entries the data to plot, one segment per entry, clockwise in order. Values must be
 *   finite and non-negative; each segment's sweep is proportional to its share of the total.
 * @param modifier the [Modifier] applied to the chart.
 * @param segmentColors the palette cycled for segments whose entry has no explicit
 *   [FormaChartEntry.color]; `null` (the default) uses [FormaChartDefaults.seriesColors].
 * @param strokeWidth the thickness of the donut's ring (defaults to
 *   [FormaChartDefaults.DonutStrokeWidth]).
 * @param strokeCap the cap style for each segment's ends (defaults to [StrokeCap.Round],
 *   giving segments softly rounded ends; pass [StrokeCap.Butt] for flat, squared ends). With
 *   rounded caps each drawn arc is inset by the caps' angular overhang at both ends, so the
 *   rounded ends land exactly on the segment's true angular bounds and the
 *   [segmentGapDegrees] gaps stay visible; a segment too small to fit its two caps renders as
 *   a dot. Irrelevant on a closed ring (a lone visible segment, or the zero-total track ring),
 *   which always draws the full 360 degrees.
 * @param startAngle the angle the first segment starts at, in degrees clockwise from
 *   3 o'clock; the default `-90f` starts at 12 o'clock.
 * @param segmentGapDegrees the angular gap between adjacent segments, in degrees (defaults to
 *   `2f`). Ignored when only one segment is visible. Must be finite and non-negative.
 * @param animationSpec the entry animation for the clockwise reveal (defaults to
 *   [FormaChartDefaults.EntryAnimationSpec]), or `null` to render statically.
 * @param contentDescription an explicit accessibility description; when `null` a percentage
 *   summary of the data is generated automatically.
 * @param centerContent a slot composed in the donut's center hole (e.g. a total or a caption).
 *   It is centered but not clipped, so keep it smaller than the hole — the chart's size minus
 *   twice [strokeWidth]. Its semantics stay independently accessible next to the chart summary.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaDonutChart(
    entries: List<FormaChartEntry>,
    modifier: Modifier = Modifier,
    segmentColors: List<Color>? = null,
    strokeWidth: Dp = FormaChartDefaults.DonutStrokeWidth,
    strokeCap: StrokeCap = StrokeCap.Round,
    startAngle: Float = -90f,
    segmentGapDegrees: Float = 2f,
    animationSpec: FiniteAnimationSpec<Float>? = FormaChartDefaults.EntryAnimationSpec,
    contentDescription: String? = null,
    centerContent: @Composable () -> Unit = {},
) {
    entries.forEach { entry ->
        require(entry.value.isFinite() && entry.value >= 0f) {
            "FormaDonutChart values must be finite and non-negative, " +
                "but \"${entry.label}\" is ${entry.value}."
        }
    }
    require(segmentGapDegrees.isFinite() && segmentGapDegrees >= 0f) {
        "FormaDonutChart segmentGapDegrees must be finite and non-negative, " +
            "but was $segmentGapDegrees."
    }

    val autoDescription = remember(entries) { donutChartContentDescription(entries) }
    val resolvedDescription = contentDescription ?: autoDescription
    val progress = rememberChartRevealProgress(dataKey = entries, animationSpec = animationSpec)

    // Resolve theme-dependent values in composable scope: the draw lambda cannot read
    // composition locals.
    val palette = segmentColors ?: FormaChartDefaults.seriesColors
    val fallbackColor = FormaTheme.colorScheme.primary
    val resolvedColors = remember(entries, palette, fallbackColor) {
        entries.mapIndexed { index, entry ->
            resolveSeriesColor(entry.color, index, palette).takeOrElse { fallbackColor }
        }
    }
    val trackColor = FormaTheme.colorScheme.surfaceVariant
    val total = remember(entries) { entries.fold(0f) { acc, entry -> acc + entry.value } }

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = FormaChartDefaults.MinDonutSize,
                minHeight = FormaChartDefaults.MinDonutSize,
            )
            .aspectRatio(1f)
            .semantics { this.contentDescription = resolvedDescription },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            if (size.width <= 0f || size.height <= 0f) return@Canvas
            val strokePx = strokeWidth.toPx()
            // The arc is stroked centered on its path, so inset the ring by half the stroke to
            // keep the outer edge inside the canvas.
            val ringDiameter = (min(size.width, size.height) - strokePx).coerceAtLeast(0f)
            if (strokePx <= 0f || ringDiameter <= 0f) return@Canvas
            val topLeft = Offset(
                x = (size.width - ringDiameter) / 2f,
                y = (size.height - ringDiameter) / 2f,
            )
            val arcSize = Size(ringDiameter, ringDiameter)
            val stroke = Stroke(width = strokePx, cap = strokeCap)

            if (total <= 0f) {
                // No data (or an all-zero dataset): draw a neutral track ring, never crash.
                drawArc(
                    color = trackColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = stroke,
                )
                return@Canvas
            }

            // Zero-value entries draw nothing and consume no gap; a lone visible segment gets
            // a full, gapless ring.
            val visibleCount = entries.count { it.value > 0f }
            val gapDegrees = if (visibleCount > 1) segmentGapDegrees else 0f
            val sweepAvailable = (360f - gapDegrees * visibleCount).coerceAtLeast(0f)
            // A round cap bulges past the arc's angular bounds by half the stroke width;
            // converted to degrees at the ring's radius, that overhang is the inset applied to
            // each end of a drawn arc so the rounded ends land on the segment's true bounds
            // and the gaps stay visible. A lone visible segment is a closed 360-degree ring —
            // caps never show, so it draws uninset (as does the zero-total track above).
            val capDegrees = if (strokeCap == StrokeCap.Round && visibleCount > 1) {
                ((strokePx / 2f) / (ringDiameter / 2f)) * (180f / PI.toFloat())
            } else {
                0f
            }
            var angle = startAngle
            entries.forEachIndexed { index, entry ->
                if (entry.value <= 0f) return@forEachIndexed
                val sweep = entry.value / total * sweepAvailable * progress
                if (sweep > 0f) {
                    // Inset after progress scaling, so the reveal never yields a negative
                    // sweep. When the inset would consume the whole segment (a tiny share, or
                    // early in the reveal), draw a minimal sweep centered in the segment's
                    // span instead — with round caps it renders as a dot.
                    val insetSweep = sweep - 2f * capDegrees
                    if (insetSweep > 0f) {
                        drawArc(
                            color = resolvedColors[index],
                            startAngle = angle + capDegrees,
                            sweepAngle = insetSweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = stroke,
                        )
                    } else {
                        drawArc(
                            color = resolvedColors[index],
                            startAngle = angle + (sweep - MinInsetSweepDegrees) / 2f,
                            sweepAngle = MinInsetSweepDegrees,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = stroke,
                        )
                    }
                }
                // Scale the gap by the reveal progress too, so the whole ring sweeps in
                // clockwise from startAngle and segments land exactly on their final angles.
                angle += sweep + gapDegrees * progress
            }
        }
        centerContent()
    }
}
