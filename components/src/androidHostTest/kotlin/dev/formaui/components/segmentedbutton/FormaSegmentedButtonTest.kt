/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.segmentedbutton

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaSegmentedButtonRow] and its two [FormaSegmentedButton] overloads,
 * hosted on the JVM via Robolectric. Covers a single-select row rendering all labels and updating
 * selection on click, and a multi-select row toggling a segment via `onCheckedChange`.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaSegmentedButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun singleSelectRow_rendersAllSegmentLabels() {
        val periods = listOf("Day", "Week", "Month")
        composeRule.setContent {
            FormaTheme {
                var selected by remember { mutableStateOf(0) }
                FormaSegmentedButtonRow(multiSelect = false) {
                    periods.forEachIndexed { index, label ->
                        FormaSegmentedButton(
                            selected = selected == index,
                            onClick = { selected = index },
                            index = index,
                            count = periods.size,
                            label = { Text(label) },
                        )
                    }
                }
            }
        }

        periods.forEach { label ->
            composeRule.onNodeWithText(label).assertExists()
        }
    }

    @Test
    fun singleSelectRow_click_firesOnClickAndUpdatesSelection() {
        val periods = listOf("Day", "Week", "Month")
        var selected by mutableStateOf(0)
        composeRule.setContent {
            FormaTheme {
                FormaSegmentedButtonRow(multiSelect = false) {
                    periods.forEachIndexed { index, label ->
                        FormaSegmentedButton(
                            selected = selected == index,
                            onClick = { selected = index },
                            index = index,
                            count = periods.size,
                            label = { Text(label) },
                        )
                    }
                }
            }
        }

        composeRule.onNodeWithText("Week").performClick()
        composeRule.runOnIdle { assertEquals(1, selected) }
    }

    @Test
    fun multiSelectRow_toggle_firesOnCheckedChange() {
        val styles = listOf("Bold", "Italic", "Underline")
        val checkedStyles = mutableStateListOf(false, false, false)
        composeRule.setContent {
            FormaTheme {
                FormaSegmentedButtonRow(multiSelect = true) {
                    styles.forEachIndexed { index, label ->
                        FormaSegmentedButton(
                            checked = checkedStyles[index],
                            onCheckedChange = { checkedStyles[index] = it },
                            index = index,
                            count = styles.size,
                            label = { Text(label) },
                        )
                    }
                }
            }
        }

        composeRule.onNodeWithText("Bold").performClick()
        composeRule.runOnIdle { assertTrue(checkedStyles[0]) }
        assertEquals(false, checkedStyles[1])
        assertEquals(false, checkedStyles[2])
    }

    @Test
    fun disabledSegment_click_doesNotFireOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaSegmentedButtonRow(multiSelect = false) {
                    FormaSegmentedButton(
                        selected = false,
                        onClick = { clicks++ },
                        index = 0,
                        count = 2,
                        enabled = false,
                        label = { Text("Disabled") },
                    )
                    FormaSegmentedButton(
                        selected = true,
                        onClick = {},
                        index = 1,
                        count = 2,
                        label = { Text("Enabled") },
                    )
                }
            }
        }

        composeRule.onNodeWithText("Disabled").performClick()
        composeRule.runOnIdle { assertEquals(0, clicks) }
    }
}
