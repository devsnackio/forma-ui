/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Pixel-level render regression tests for the chart components — these verify that a chart
 * actually PAINTS something, not merely that its semantics node exists with plausible bounds.
 *
 * Motivation (user-reported bug): [FormaBarChart] and [FormaLineChart] rendered a
 * correctly-sized but completely empty band in the sample app, while [FormaDonutChart] rendered
 * fine. Semantics-only assertions (`assertExists`) cannot catch this — the layout slot is
 * coerced to [FormaChartDefaults.MinChartHeight] so bounds look right even when the canvas
 * draws at zero height and the `size.height <= 0f` guard bails out.
 *
 * Each test composes one chart with `animationSpec = null` (deterministic final frame), only
 * `Modifier.fillMaxWidth()` from the caller (the unfixed-height default-sizing path IS the
 * path under test — no explicit height), and all non-data decoration (gridlines, labels)
 * disabled, so the only pixels a chart can draw are its data. Pixels are captured via
 * [captureNodeToImage] — a software `View.draw` capture, because `captureToImage()` cannot
 * work under Robolectric (see that helper's KDoc). The capture's top-left corner pixel is
 * taken as the background reference (data never reaches that corner with these datasets), and
 * the test asserts that at least one pixel differs from it.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
// Required for real rasterization — see the KDoc on [captureNodeToImage].
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ChartPixelRenderTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val barEntries = listOf(
        FormaChartEntry("Jan", 12f),
        FormaChartEntry("Feb", 32f),
        FormaChartEntry("Mar", 21f),
    )

    private val lineValues = listOf(4f, 9f, 6f, 14f, 11f)

    @Test
    fun barChart_defaultHeight_drawsPixels() {
        assertChartDrawsPixels(tag = "bar-pixel-chart") {
            FormaBarChart(
                entries = barEntries,
                modifier = Modifier
                    .testTag("bar-pixel-chart")
                    .fillMaxWidth(),
                showCategoryLabels = false,
                showGridLines = false,
                animationSpec = null,
            )
        }
    }

    @Test
    fun lineChart_defaultHeight_drawsPixels() {
        assertChartDrawsPixels(tag = "line-pixel-chart") {
            FormaLineChart(
                values = lineValues,
                modifier = Modifier
                    .testTag("line-pixel-chart")
                    .fillMaxWidth(),
                showGridLines = false,
                animationSpec = null,
            )
        }
    }

    @Test
    fun donutChart_defaultHeight_drawsPixels() {
        assertChartDrawsPixels(tag = "donut-pixel-chart") {
            FormaDonutChart(
                entries = barEntries,
                modifier = Modifier
                    .testTag("donut-pixel-chart")
                    .fillMaxWidth(),
                animationSpec = null,
            )
        }
    }

    // ---- Donut strokeCap coverage (pixel-level — the cap inset is pure canvas math) ----

    @Test
    fun donutChart_explicitRoundCap_drawsPixels() {
        assertChartDrawsPixels(tag = "donut-round-cap") {
            FormaDonutChart(
                entries = barEntries,
                modifier = Modifier
                    .testTag("donut-round-cap")
                    .fillMaxWidth(),
                strokeCap = StrokeCap.Round,
                animationSpec = null,
            )
        }
    }

    @Test
    fun donutChart_buttCap_drawsPixels() {
        assertChartDrawsPixels(tag = "donut-butt-cap") {
            FormaDonutChart(
                entries = barEntries,
                modifier = Modifier
                    .testTag("donut-butt-cap")
                    .fillMaxWidth(),
                strokeCap = StrokeCap.Butt,
                animationSpec = null,
            )
        }
    }

    @Test
    fun donutChart_tinySegmentWithRoundCaps_drawsPixels() {
        // A 0.5% share cannot fit its two round-cap insets: the epsilon-sweep "dot" clamp
        // path must render (not crash, not vanish the whole chart).
        assertChartDrawsPixels(tag = "donut-tiny-segment") {
            FormaDonutChart(
                entries = listOf(
                    FormaChartEntry("Big", 995f),
                    FormaChartEntry("Tiny", 5f),
                ),
                modifier = Modifier
                    .testTag("donut-tiny-segment")
                    .fillMaxWidth(),
                animationSpec = null,
            )
        }
    }

    @Test
    fun donutChart_singleVisibleSegment_fullRing_drawsPixels() {
        // A lone visible segment is a closed 360-degree ring: the Round default must apply
        // NO cap inset here and still paint the full ring.
        assertChartDrawsPixels(tag = "donut-lone-segment") {
            FormaDonutChart(
                entries = listOf(
                    FormaChartEntry("A", 10f),
                    FormaChartEntry("B", 0f),
                ),
                modifier = Modifier
                    .testTag("donut-lone-segment")
                    .fillMaxWidth(),
                animationSpec = null,
            )
        }
    }

    @Test
    fun donutChart_zeroTotal_trackRing_drawsPixels() {
        // The zero-total track ring is also a closed ring: uninset under the Round default,
        // and it must actually paint (surfaceVariant differs from the background).
        assertChartDrawsPixels(tag = "donut-zero-total") {
            FormaDonutChart(
                entries = listOf(
                    FormaChartEntry("Rent", 0f),
                    FormaChartEntry("Food", 0f),
                ),
                modifier = Modifier
                    .testTag("donut-zero-total")
                    .fillMaxWidth(),
                animationSpec = null,
            )
        }
    }

    /**
     * Composes [content] inside [FormaTheme], captures the node tagged [tag] to an image, and
     * asserts that at least one pixel differs from the background (sampled at the capture's
     * top-left corner — a location no chart data reaches with the datasets above). Comparing
     * against the captured corner rather than a hardcoded ARGB keeps the assertion robust to
     * theme/surface colors.
     */
    private fun assertChartDrawsPixels(tag: String, content: @Composable () -> Unit) {
        composeRule.setContent {
            FormaTheme {
                content()
            }
        }

        val image = composeRule.onNodeWithTag(tag).captureNodeToImage()
        assertTrue(
            "Captured image for \"$tag\" has degenerate size ${image.width}x${image.height}.",
            image.width > 0 && image.height > 0,
        )

        val pixels = image.toPixelMap()
        val background = pixels[0, 0]
        var drawnPixelCount = 0
        for (y in 0 until pixels.height) {
            for (x in 0 until pixels.width) {
                if (pixels[x, y] != background) drawnPixelCount++
            }
        }

        assertTrue(
            "Expected \"$tag\" to draw at least one pixel differing from the background " +
                "($background) in its ${pixels.width}x${pixels.height} capture, but all " +
                "${pixels.width * pixels.height} pixels matched the background — " +
                "nothing was drawn.",
            drawnPixelCount > 0,
        )
    }
}
