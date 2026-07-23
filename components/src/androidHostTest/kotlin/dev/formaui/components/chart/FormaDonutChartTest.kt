/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaDonutChart], hosted on the JVM via Robolectric. Covers the PRD
 * §5.3 bar (renders + responds to a state change) plus the chart-specific accessibility
 * contract: auto-generated integer-percentage vs explicit content descriptions, the
 * [FormaDonutChart] `centerContent` slot, empty/all-zero data (track ring, no crash), static
 * rendering with `animationSpec = null`, and `require`-based input validation. Assertions are
 * semantics-only — never pixel geometry (Robolectric density differs from devices).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaDonutChartTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultEntries = listOf(
        FormaChartEntry("Rent", 50f),
        FormaChartEntry("Food", 30f),
        FormaChartEntry("Fun", 20f),
    )

    private val defaultDescription =
        "Donut chart with 3 segments. Rent: 50 percent. Food: 30 percent. Fun: 20 percent."

    @Test
    fun rendersWithTestTag() {
        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = defaultEntries,
                    modifier = Modifier.testTag("donut-chart"),
                )
            }
        }

        composeRule.onNodeWithTag("donut-chart").assertExists()
    }

    @Test
    fun allVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("defaults"),
                    )
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("custom-palette"),
                        segmentColors = listOf(Color.Red, Color.Green, Color.Blue),
                    )
                    FormaDonutChart(
                        entries = listOf(
                            FormaChartEntry("A", 10f, color = Color.Magenta),
                            FormaChartEntry("B", 20f),
                        ),
                        modifier = Modifier.testTag("entry-color"),
                    )
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("custom-stroke"),
                        strokeWidth = 8.dp,
                    )
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("custom-angles"),
                        startAngle = 0f,
                        segmentGapDegrees = 6f,
                    )
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("static"),
                        animationSpec = null,
                    )
                }
            }
        }

        listOf(
            "defaults", "custom-palette", "entry-color",
            "custom-stroke", "custom-angles", "static",
        ).forEach { tag ->
            composeRule.onNodeWithTag(tag).assertExists()
        }
    }

    @Test
    fun strokeCapVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    // The Round default, stated explicitly.
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("round-cap"),
                        strokeCap = StrokeCap.Round,
                        animationSpec = null,
                    )
                    // Flat, squared ends — the pre-existing look, now opt-in.
                    FormaDonutChart(
                        entries = defaultEntries,
                        modifier = Modifier.testTag("butt-cap"),
                        strokeCap = StrokeCap.Butt,
                        animationSpec = null,
                    )
                    // A 0.5% segment under the Round default: the epsilon-dot clamp path.
                    FormaDonutChart(
                        entries = listOf(
                            FormaChartEntry("Big", 995f),
                            FormaChartEntry("Tiny", 5f),
                        ),
                        modifier = Modifier.testTag("tiny-round-segment"),
                        animationSpec = null,
                    )
                }
            }
        }

        listOf("round-cap", "butt-cap", "tiny-round-segment").forEach { tag ->
            composeRule.onNodeWithTag(tag).assertExists()
        }
    }

    @Test
    fun autoContentDescription_usesIntegerPercentages() {
        composeRule.setContent {
            FormaTheme {
                // 2/3 and 1/3 shares: the summary must round to whole percents (67 / 33).
                FormaDonutChart(
                    entries = listOf(
                        FormaChartEntry("A", 2f),
                        FormaChartEntry("B", 1f),
                    ),
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Donut chart with 2 segments. A: 67 percent. B: 33 percent.")
            .assertExists()
    }

    @Test
    fun explicitContentDescription_overridesAutoSummary() {
        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = defaultEntries,
                    contentDescription = "Monthly budget breakdown",
                )
            }
        }

        composeRule.onNodeWithContentDescription("Monthly budget breakdown").assertExists()
        composeRule.onNodeWithContentDescription(defaultDescription).assertDoesNotExist()
    }

    @Test
    fun centerContent_isComposedInTheCenterSlot() {
        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = defaultEntries,
                    modifier = Modifier.testTag("donut-with-center"),
                    animationSpec = null,
                    centerContent = { Text("1480") },
                )
            }
        }

        composeRule.onNodeWithTag("donut-with-center").assertExists()
        composeRule.onNodeWithText("1480").assertExists()
    }

    @Test
    fun zeroTotalData_rendersTrackRingWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = listOf(
                        FormaChartEntry("Rent", 0f),
                        FormaChartEntry("Food", 0f),
                    ),
                    modifier = Modifier.testTag("zero-donut"),
                )
            }
        }

        composeRule.onNodeWithTag("zero-donut").assertExists()
        composeRule
            .onNodeWithContentDescription("Donut chart with 2 segments. Total is zero.")
            .assertExists()
    }

    @Test
    fun emptyEntries_renderWithNoDataDescription() {
        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = emptyList(),
                    modifier = Modifier.testTag("empty-donut"),
                )
            }
        }

        composeRule.onNodeWithTag("empty-donut").assertExists()
        composeRule.onNodeWithContentDescription("Donut chart with no data").assertExists()
    }

    @Test
    fun singleVisibleSegmentAmongZeroes_rendersWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                // One non-zero entry next to a zero entry: a gapless full ring, no crash.
                FormaDonutChart(
                    entries = listOf(
                        FormaChartEntry("A", 10f),
                        FormaChartEntry("B", 0f),
                    ),
                    modifier = Modifier.testTag("lone-segment"),
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("lone-segment").assertExists()
        composeRule
            .onNodeWithContentDescription("Donut chart with 2 segments. A: 100 percent. B: 0 percent.")
            .assertExists()
    }

    @Test
    fun nullAnimationSpec_rendersStaticFinalFrame() {
        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = defaultEntries,
                    modifier = Modifier.testTag("static-donut"),
                    animationSpec = null,
                )
            }
        }

        composeRule.onNodeWithTag("static-donut").assertExists()
        composeRule.onNodeWithContentDescription(defaultDescription).assertExists()
    }

    @Test
    fun dataSwap_updatesAutoContentDescription() {
        val first = listOf(FormaChartEntry("Rent", 50f), FormaChartEntry("Food", 50f))
        val second = listOf(FormaChartEntry("Solo", 10f))
        val data = mutableStateOf(first)

        composeRule.setContent {
            FormaTheme {
                FormaDonutChart(
                    entries = data.value,
                    modifier = Modifier.testTag("stateful-donut"),
                    animationSpec = null,
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(
                "Donut chart with 2 segments. Rent: 50 percent. Food: 50 percent.",
            )
            .assertExists()

        composeRule.runOnIdle { data.value = second }

        composeRule
            .onNodeWithContentDescription("Donut chart with 1 segment. Solo: 100 percent.")
            .assertExists()
        composeRule
            .onNodeWithContentDescription(
                "Donut chart with 2 segments. Rent: 50 percent. Food: 50 percent.",
            )
            .assertDoesNotExist()
    }

    @Test
    fun negativeValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaDonutChart(entries = listOf(FormaChartEntry("Bad", -1f)))
        }
    }

    @Test
    fun nonFiniteValue_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaDonutChart(entries = listOf(FormaChartEntry("Bad", Float.NaN)))
        }
    }

    @Test
    fun negativeSegmentGap_throwsIllegalArgumentException() {
        assertSetContentThrowsIllegalArgument {
            FormaDonutChart(entries = defaultEntries, segmentGapDegrees = -1f)
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
