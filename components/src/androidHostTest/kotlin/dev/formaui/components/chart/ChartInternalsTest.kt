/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.ui.graphics.Color
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Plain-JUnit unit tests for the pure, non-composable chart internals ([computeNiceMax],
 * [barChartContentDescription], [donutChartContentDescription], [lineChartContentDescription],
 * [resolveSeriesColor]) plus the public [FormaChartDefaults.ValueFormatter]. No compose host or
 * Robolectric needed.
 */
class ChartInternalsTest {

    // -- computeNiceMax ---------------------------------------------------------------------

    @Test
    fun computeNiceMax_roundsUpToNiceFractions() {
        assertEquals(20f, computeNiceMax(12f), 1e-4f) // fraction 1.2 -> 2
        assertEquals(2.5f, computeNiceMax(2.3f), 1e-4f) // fraction 2.3 -> 2.5
        assertEquals(50f, computeNiceMax(45f), 1e-4f) // fraction 4.5 -> 5
        assertEquals(100f, computeNiceMax(70f), 1e-4f) // fraction 7 -> 10
        assertEquals(1f, computeNiceMax(0.7f), 1e-4f) // sub-1 magnitudes work too
    }

    @Test
    fun computeNiceMax_exactNiceValues_areKept() {
        assertEquals(1f, computeNiceMax(1f), 1e-4f)
        assertEquals(2f, computeNiceMax(2f), 1e-4f)
        assertEquals(2.5f, computeNiceMax(2.5f), 1e-4f)
        assertEquals(5f, computeNiceMax(5f), 1e-4f)
        assertEquals(100f, computeNiceMax(100f), 1e-4f)
    }

    @Test
    fun computeNiceMax_nonPositiveOrNonFinite_fallsBackToOne() {
        assertEquals(1f, computeNiceMax(0f), 0f)
        assertEquals(1f, computeNiceMax(-3f), 0f)
        assertEquals(1f, computeNiceMax(Float.NaN), 0f)
        assertEquals(1f, computeNiceMax(Float.POSITIVE_INFINITY), 0f)
        assertEquals(1f, computeNiceMax(Float.NEGATIVE_INFINITY), 0f)
    }

    // -- barChartContentDescription ---------------------------------------------------------

    @Test
    fun barChartContentDescription_summarizesPopulatedData() {
        val entries = listOf(
            FormaChartEntry("Jan", 12f),
            FormaChartEntry("Feb", 32.5f),
        )
        assertEquals(
            "Bar chart with 2 categories. Jan: 12. Feb: 32.5.",
            barChartContentDescription(entries, FormaChartDefaults.ValueFormatter),
        )
    }

    @Test
    fun barChartContentDescription_singleEntry_usesSingularCategory() {
        val entries = listOf(FormaChartEntry("Only", 7f))
        assertEquals(
            "Bar chart with 1 category. Only: 7.",
            barChartContentDescription(entries, FormaChartDefaults.ValueFormatter),
        )
    }

    @Test
    fun barChartContentDescription_emptyData_saysNoData() {
        assertEquals(
            "Bar chart with no data",
            barChartContentDescription(emptyList(), FormaChartDefaults.ValueFormatter),
        )
    }

    @Test
    fun barChartContentDescription_usesTheGivenFormatter() {
        val entries = listOf(FormaChartEntry("A", 3f))
        assertEquals(
            "Bar chart with 1 category. A: $3.",
            barChartContentDescription(entries) { value -> "$${value.toInt()}" },
        )
    }

    // -- donutChartContentDescription -------------------------------------------------------

    @Test
    fun donutChartContentDescription_summarizesWithIntegerPercentages() {
        // 2/3 and 1/3 shares round to whole percents (67 / 33) — never fractional output.
        val entries = listOf(
            FormaChartEntry("A", 2f),
            FormaChartEntry("B", 1f),
        )
        assertEquals(
            "Donut chart with 2 segments. A: 67 percent. B: 33 percent.",
            donutChartContentDescription(entries),
        )
    }

    @Test
    fun donutChartContentDescription_exactShares_readAsExactPercentages() {
        val entries = listOf(
            FormaChartEntry("Rent", 50f),
            FormaChartEntry("Food", 30f),
            FormaChartEntry("Fun", 20f),
        )
        assertEquals(
            "Donut chart with 3 segments. Rent: 50 percent. Food: 30 percent. Fun: 20 percent.",
            donutChartContentDescription(entries),
        )
    }

    @Test
    fun donutChartContentDescription_singleEntry_usesSingularSegment() {
        assertEquals(
            "Donut chart with 1 segment. Only: 100 percent.",
            donutChartContentDescription(listOf(FormaChartEntry("Only", 7f))),
        )
    }

    @Test
    fun donutChartContentDescription_emptyData_saysNoData() {
        assertEquals(
            "Donut chart with no data",
            donutChartContentDescription(emptyList()),
        )
    }

    @Test
    fun donutChartContentDescription_zeroTotal_reportsZeroTotal() {
        val entries = listOf(
            FormaChartEntry("A", 0f),
            FormaChartEntry("B", 0f),
        )
        assertEquals(
            "Donut chart with 2 segments. Total is zero.",
            donutChartContentDescription(entries),
        )
    }

    @Test
    fun donutChartContentDescription_zeroValueEntry_isIncludedAtZeroPercent() {
        val entries = listOf(
            FormaChartEntry("A", 10f),
            FormaChartEntry("B", 0f),
        )
        assertEquals(
            "Donut chart with 2 segments. A: 100 percent. B: 0 percent.",
            donutChartContentDescription(entries),
        )
    }

    // -- lineChartContentDescription --------------------------------------------------------

    @Test
    fun lineChartContentDescription_summarizesPointsRangeAndLatest() {
        assertEquals(
            "Line chart with 5 points. Range 4 to 14. Latest value 11.",
            lineChartContentDescription(
                listOf(4f, 9f, 6f, 14f, 11f),
                FormaChartDefaults.ValueFormatter,
            ),
        )
    }

    @Test
    fun lineChartContentDescription_singlePoint_usesSingularPoint() {
        assertEquals(
            "Line chart with 1 point. Range 7 to 7. Latest value 7.",
            lineChartContentDescription(listOf(7f), FormaChartDefaults.ValueFormatter),
        )
    }

    @Test
    fun lineChartContentDescription_negativeValues_appearInRange() {
        assertEquals(
            "Line chart with 4 points. Range -8 to 12. Latest value -2.",
            lineChartContentDescription(
                listOf(-8f, 12f, 3f, -2f),
                FormaChartDefaults.ValueFormatter,
            ),
        )
    }

    @Test
    fun lineChartContentDescription_emptyData_saysNoData() {
        assertEquals(
            "Line chart with no data",
            lineChartContentDescription(emptyList(), FormaChartDefaults.ValueFormatter),
        )
    }

    @Test
    fun lineChartContentDescription_usesTheGivenFormatter() {
        assertEquals(
            "Line chart with 2 points. Range $1 to $3. Latest value $3.",
            lineChartContentDescription(listOf(1f, 3f)) { value -> "$${value.toInt()}" },
        )
    }

    // -- resolveSeriesColor -----------------------------------------------------------------

    @Test
    fun resolveSeriesColor_explicitEntryColorWins() {
        assertEquals(
            Color.Red,
            resolveSeriesColor(entryColor = Color.Red, index = 0, seriesColors = listOf(Color.Blue)),
        )
    }

    @Test
    fun resolveSeriesColor_unspecifiedEntry_takesPaletteColorByIndex() {
        val palette = listOf(Color.Red, Color.Green, Color.Blue)
        assertEquals(
            Color.Green,
            resolveSeriesColor(entryColor = Color.Unspecified, index = 1, seriesColors = palette),
        )
    }

    @Test
    fun resolveSeriesColor_cyclesBeyondPaletteSize() {
        val palette = listOf(Color.Red, Color.Green, Color.Blue)
        assertEquals(
            Color.Green, // 7 % 3 == 1
            resolveSeriesColor(entryColor = Color.Unspecified, index = 7, seriesColors = palette),
        )
        assertEquals(
            Color.Red, // 3 % 3 == 0
            resolveSeriesColor(entryColor = Color.Unspecified, index = 3, seriesColors = palette),
        )
    }

    @Test
    fun resolveSeriesColor_emptyPalette_returnsUnspecified() {
        assertEquals(
            Color.Unspecified,
            resolveSeriesColor(entryColor = Color.Unspecified, index = 2, seriesColors = emptyList()),
        )
    }

    // -- FormaChartDefaults.ValueFormatter --------------------------------------------------

    @Test
    fun valueFormatter_dropsTrailingZeroAndKeepsOneDecimal() {
        assertEquals("12", FormaChartDefaults.ValueFormatter(12f))
        assertEquals("12.3", FormaChartDefaults.ValueFormatter(12.34f))
        assertEquals("0", FormaChartDefaults.ValueFormatter(0f))
        assertEquals("-4.2", FormaChartDefaults.ValueFormatter(-4.2f))
    }
}
