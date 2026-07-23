/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chart

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaChartLegend], hosted on the JVM via Robolectric. Legend labels
 * are real `Text` nodes, so every entry is asserted directly via `onNodeWithText` — with and
 * without a `valueFormatter` — plus the empty-entries and custom-palette variants. Assertions
 * are semantics-only, never pixel geometry.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaChartLegendTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultEntries = listOf(
        FormaChartEntry("Rent", 850f),
        FormaChartEntry("Groceries", 420f),
        FormaChartEntry("Transport", 210f),
    )

    @Test
    fun rendersALabelPerEntry() {
        composeRule.setContent {
            FormaTheme {
                FormaChartLegend(
                    entries = defaultEntries,
                    modifier = Modifier.testTag("legend"),
                )
            }
        }

        composeRule.onNodeWithTag("legend").assertExists()
        composeRule.onNodeWithText("Rent").assertExists()
        composeRule.onNodeWithText("Groceries").assertExists()
        composeRule.onNodeWithText("Transport").assertExists()
    }

    @Test
    fun valueFormatter_appendsFormattedValuesToLabels() {
        composeRule.setContent {
            FormaTheme {
                FormaChartLegend(
                    entries = defaultEntries,
                    valueFormatter = FormaChartDefaults.ValueFormatter,
                )
            }
        }

        composeRule.onNodeWithText("Rent: 850").assertExists()
        composeRule.onNodeWithText("Groceries: 420").assertExists()
        composeRule.onNodeWithText("Transport: 210").assertExists()
        // Plain labels are replaced by the formatted ones, not shown alongside them.
        composeRule.onNodeWithText("Rent").assertDoesNotExist()
    }

    @Test
    fun customValueFormatter_isApplied() {
        composeRule.setContent {
            FormaTheme {
                FormaChartLegend(
                    entries = listOf(FormaChartEntry("Rent", 850f)),
                    valueFormatter = { value -> "$${value.toInt()}" },
                )
            }
        }

        composeRule.onNodeWithText("Rent: $850").assertExists()
    }

    @Test
    fun customPaletteAndEntryColors_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                FormaChartLegend(
                    entries = listOf(
                        FormaChartEntry("A", 1f, color = Color.Magenta),
                        FormaChartEntry("B", 2f),
                    ),
                    modifier = Modifier.testTag("palette-legend"),
                    colors = listOf(Color.Red, Color.Green),
                )
            }
        }

        composeRule.onNodeWithTag("palette-legend").assertExists()
        composeRule.onNodeWithText("A").assertExists()
        composeRule.onNodeWithText("B").assertExists()
    }

    @Test
    fun emptyEntries_renderNoItems() {
        composeRule.setContent {
            FormaTheme {
                FormaChartLegend(
                    entries = emptyList(),
                    modifier = Modifier.testTag("empty-legend"),
                )
            }
        }

        composeRule.onNodeWithTag("empty-legend").assertExists()
        composeRule.onNodeWithText("Rent").assertDoesNotExist()
    }
}
