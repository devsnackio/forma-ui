/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaLineChart], hosted on the JVM via Robolectric. Covers the PRD §5.3
 * bar (renders + responds to a state change) plus the chart-specific accessibility contract:
 * auto-generated vs explicit content descriptions, degenerate data (empty, single point, flat
 * series, negative values), static rendering with `animationSpec = null`, one-sided range
 * overrides that invert the range (must degrade to a flat line, not crash), surplus x labels,
 * and `require`-based input validation. Assertions are semantics-only — never pixel geometry
 * (Robolectric density differs from devices).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaLineChartTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultValues = listOf(4f, 9f, 6f, 14f, 11f)

    @Test
    fun rendersWithTestTag() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = defaultValues,
                    modifier = Modifier.testTag("line-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("line-chart").assertExists()
    }

    @Test
    fun allVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaLineChart(
                        values = defaultValues,
                        modifier = Modifier.testTag("defaults"),
                    )
                    FormaLineChart(
                        values = defaultValues,
                        modifier = Modifier.testTag("straight-no-fill"),
                        smooth = false,
                        fillArea = false,
                    )
                    FormaLineChart(
                        values = defaultValues,
                        modifier = Modifier.testTag("points"),
                        showPoints = true,
                    )
                    FormaLineChart(
                        values = defaultValues,
                        modifier = Modifier.testTag("no-gridlines"),
                        showGridLines = false,
                    )
                    FormaLineChart(
                        values = defaultValues,
                        modifier = Modifier.testTag("x-labels"),
                        xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri"),
                    )
                    FormaLineChart(
                        values = defaultValues,
                        modifier = Modifier.testTag("custom-color-range"),
                        lineColor = Color.Red,
                        minValue = 0f,
                        maxValue = 20f,
                    )
                }
            }
        }

        listOf(
            "defaults", "straight-no-fill", "points",
            "no-gridlines", "x-labels", "custom-color-range",
        ).forEach { tag ->
            composeRule.onNodeWithTag(tag).assertExists()
        }
    }

    @Test
    fun singlePoint_rendersWithCorrectDescription() {
        // A single value has no segment to draw; the point marker is always drawn for it
        // (even with showPoints = false) — documented behavior, so just assert the render
        // and the summary.
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = listOf(7f),
                    modifier = Modifier.testTag("single-point"),
                )
            }
        }

        composeRule.onNodeWithTag("single-point").assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 1 point. Range 7 to 7. Latest value 7.")
            .assertExists()
    }

    @Test
    fun negativeValues_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = listOf(-8f, -3f, 5f, -1f, 12f),
                    modifier = Modifier.testTag("negative-chart"),
                    showPoints = true,
                )
            }
        }

        composeRule.onNodeWithTag("negative-chart").assertExists()
        composeRule
            .onNodeWithContentDescription(
                "Line chart with 5 points. Range -8 to 12. Latest value 12.",
            )
            .assertExists()
    }

    @Test
    fun flatLine_rendersWithoutCrashing() {
        // All values equal: min == max, so the range is zero — the line centers instead of
        // dividing by zero.
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = listOf(5f, 5f, 5f, 5f),
                    modifier = Modifier.testTag("flat-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("flat-chart").assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 4 points. Range 5 to 5. Latest value 5.")
            .assertExists()
    }

    @Test
    fun emptyValues_renderWithNoDataDescription() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = emptyList(),
                    modifier = Modifier.testTag("empty-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("empty-chart").assertExists()
        composeRule.onNodeWithContentDescription("Line chart with no data").assertExists()
    }

    @Test
    fun nullAnimationSpec_rendersStaticFinalFrame() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = defaultValues,
                    modifier = Modifier.testTag("static-chart"),
                    showPoints = true,
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("static-chart").assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 5 points. Range 4 to 14. Latest value 11.")
            .assertExists()
    }

    @Test
    fun autoContentDescription_summarizesData() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(values = defaultValues)
            }
        }

        composeRule
            .onNodeWithContentDescription("Line chart with 5 points. Range 4 to 14. Latest value 11.")
            .assertExists()
    }

    @Test
    fun explicitContentDescription_overridesAutoSummary() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = defaultValues,
                    contentDescription = "Weekly sessions trend",
                )
            }
        }

        composeRule.onNodeWithContentDescription("Weekly sessions trend").assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 5 points. Range 4 to 14. Latest value 11.")
            .assertDoesNotExist()
    }

    @Test
    fun dataSwap_updatesAutoContentDescription() {
        val first = listOf(4f, 9f, 6f)
        val second = listOf(20f, 25f)
        val data = mutableStateOf(first)

        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = data.value,
                    modifier = Modifier.testTag("stateful-chart"),
                    animationSpec = null,
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Line chart with 3 points. Range 4 to 9. Latest value 6.")
            .assertExists()

        composeRule.runOnIdle { data.value = second }

        composeRule
            .onNodeWithContentDescription("Line chart with 2 points. Range 20 to 25. Latest value 25.")
            .assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 3 points. Range 4 to 9. Latest value 6.")
            .assertDoesNotExist()
    }

    @Test
    fun oneSidedMaxValueBelowDataMin_degradesToFlatLine_withoutThrowing() {
        // Only maxValue is supplied and it sits below the data's minimum, so the effective
        // range inverts. The require(maxValue > minValue) guard does NOT apply (it needs both
        // overrides); the chart must degrade to flat-line rendering instead of crashing.
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = listOf(5f, 10f, 15f),
                    modifier = Modifier.testTag("inverted-max"),
                    maxValue = 2f,
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("inverted-max").assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 3 points. Range 5 to 15. Latest value 15.")
            .assertExists()
    }

    @Test
    fun oneSidedMinValueAboveDataMax_degradesToFlatLine_withoutThrowing() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = listOf(5f, 10f, 15f),
                    modifier = Modifier.testTag("inverted-min"),
                    minValue = 20f,
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("inverted-min").assertExists()
        composeRule
            .onNodeWithContentDescription("Line chart with 3 points. Range 5 to 15. Latest value 15.")
            .assertExists()
    }

    @Test
    fun extraXLabels_beyondPointCount_areIgnoredWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                FormaLineChart(
                    values = listOf(4f, 9f, 6f),
                    modifier = Modifier.testTag("surplus-labels"),
                    xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("surplus-labels").assertExists()
    }

    @Test
    fun bothBoundsInverted_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaLineChart(values = defaultValues, minValue = 10f, maxValue = 5f)
        }
    }

    @Test
    fun equalBounds_throwIllegalArgumentException() {
        // maxValue must be strictly greater than minValue when both are supplied.
        assertSetContentThrowsIllegalArgument {
            FormaLineChart(values = defaultValues, minValue = 5f, maxValue = 5f)
        }
    }

    @Test
    fun nanValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaLineChart(values = listOf(1f, Float.NaN, 3f))
        }
    }

    @Test
    fun infiniteValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaLineChart(values = listOf(1f, Float.POSITIVE_INFINITY))
        }
    }

    @Test
    fun nonFiniteMinValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaLineChart(values = defaultValues, minValue = Float.NaN)
        }
    }

    @Test
    fun nonFiniteMaxValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaLineChart(values = defaultValues, maxValue = Float.POSITIVE_INFINITY)
        }
    }

    /**
     * Asserts that composing [content] inside `FormaTheme` fails with an
     * [IllegalArgumentException] (possibly wrapped by the compose runtime — the cause chain is
     * walked).
     */
    private fun assertSetContentThrowsIllegalArgument(
        content: @androidx.compose.runtime.Composable () -> Unit,
    ) {
        var thrown: Throwable? = null
        try {
            composeRule.setContent {
                FormaTheme {
                    content()
                }
            }
        } catch (t: Throwable) {
            thrown = t
        }

        var cause = thrown
        while (cause != null && cause !is IllegalArgumentException) {
            cause = cause.cause
        }
        assertTrue(
            "Expected IllegalArgumentException from require(), but got: $thrown",
            cause is IllegalArgumentException,
        )
    }
}
