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
 * Compose UI tests for [FormaBarChart], hosted on the JVM via Robolectric. Covers the PRD §5.3
 * bar (renders + responds to a state change) plus the chart-specific accessibility contract:
 * auto-generated vs explicit content descriptions, empty/all-zero data, static rendering with
 * `animationSpec = null`, and `require`-based input validation. Assertions are semantics-only —
 * never pixel geometry (Robolectric density differs from devices).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaBarChartTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultEntries = listOf(
        FormaChartEntry("Jan", 12f),
        FormaChartEntry("Feb", 32f),
        FormaChartEntry("Mar", 21f),
    )

    @Test
    fun rendersWithTestTag() {
        composeRule.setContent {
            FormaTheme {
                FormaBarChart(
                    entries = defaultEntries,
                    modifier = Modifier.testTag("bar-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("bar-chart").assertExists()
    }

    @Test
    fun allVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaBarChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("defaults"),
                    )
                    FormaBarChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("value-labels"),
                        showValueLabels = true,
                    )
                    FormaBarChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("no-category-labels"),
                        showCategoryLabels = false,
                    )
                    FormaBarChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("no-gridlines"),
                        showGridLines = false,
                    )
                    FormaBarChart(
                        entries = listOf(
                            FormaChartEntry("Q1", 18f),
                            FormaChartEntry("Q2", 30f, color = Color.Red),
                        ),
                        modifier = Modifier.testTag("custom-colors"),
                        barColor = Color.Blue,
                    )
                    FormaBarChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("explicit-max"),
                        maxValue = 100f,
                    )
                }
            }
        }

        listOf(
            "defaults", "value-labels", "no-category-labels",
            "no-gridlines", "custom-colors", "explicit-max",
        ).forEach { tag ->
            composeRule.onNodeWithTag(tag).assertExists()
        }
    }

    @Test
    fun autoContentDescription_summarizesData() {
        composeRule.setContent {
            FormaTheme {
                FormaBarChart(entries = defaultEntries)
            }
        }

        composeRule
            .onNodeWithContentDescription("Bar chart with 3 categories. Jan: 12. Feb: 32. Mar: 21.")
            .assertExists()
    }

    @Test
    fun explicitContentDescription_overridesAutoSummary() {
        composeRule.setContent {
            FormaTheme {
                FormaBarChart(
                    entries = defaultEntries,
                    contentDescription = "Monthly revenue chart",
                )
            }
        }

        composeRule.onNodeWithContentDescription("Monthly revenue chart").assertExists()
        composeRule
            .onNodeWithContentDescription("Bar chart with 3 categories. Jan: 12. Feb: 32. Mar: 21.")
            .assertDoesNotExist()
    }

    @Test
    fun nullAnimationSpec_rendersStaticFinalFrame() {
        composeRule.setContent {
            FormaTheme {
                FormaBarChart(
                    entries = defaultEntries,
                    modifier = Modifier.testTag("static-chart"),
                    showValueLabels = true,
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("static-chart").assertExists()
        composeRule
            .onNodeWithContentDescription("Bar chart with 3 categories. Jan: 12. Feb: 32. Mar: 21.")
            .assertExists()
    }

    @Test
    fun emptyEntries_renderWithNoDataDescription() {
        composeRule.setContent {
            FormaTheme {
                FormaBarChart(
                    entries = emptyList(),
                    modifier = Modifier.testTag("empty-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("empty-chart").assertExists()
        composeRule.onNodeWithContentDescription("Bar chart with no data").assertExists()
    }

    @Test
    fun allZeroValues_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                FormaBarChart(
                    entries = listOf(
                        FormaChartEntry("Jan", 0f),
                        FormaChartEntry("Feb", 0f),
                    ),
                    modifier = Modifier.testTag("zero-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("zero-chart").assertExists()
        composeRule
            .onNodeWithContentDescription("Bar chart with 2 categories. Jan: 0. Feb: 0.")
            .assertExists()
    }

    @Test
    fun dataSwap_updatesAutoContentDescription() {
        val first = listOf(FormaChartEntry("Jan", 12f), FormaChartEntry("Feb", 32f))
        val second = listOf(FormaChartEntry("Mar", 5f))
        val data = mutableStateOf(first)

        composeRule.setContent {
            FormaTheme {
                FormaBarChart(
                    entries = data.value,
                    modifier = Modifier.testTag("stateful-chart"),
                    animationSpec = null,
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Bar chart with 2 categories. Jan: 12. Feb: 32.")
            .assertExists()

        composeRule.runOnIdle { data.value = second }

        composeRule
            .onNodeWithContentDescription("Bar chart with 1 category. Mar: 5.")
            .assertExists()
        composeRule
            .onNodeWithContentDescription("Bar chart with 2 categories. Jan: 12. Feb: 32.")
            .assertDoesNotExist()
    }

    @Test
    fun negativeValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaBarChart(entries = listOf(FormaChartEntry("Bad", -1f)))
        }
    }

    @Test
    fun nonFiniteValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaBarChart(entries = listOf(FormaChartEntry("Bad", Float.NaN)))
        }
    }

    @Test
    fun nonPositiveMaxValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaBarChart(entries = defaultEntries, maxValue = 0f)
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
