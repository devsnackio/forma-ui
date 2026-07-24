/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.autocomplete

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaExposedDropdownMenu], hosted on the JVM via Robolectric.
 *
 * The menu rows live in a `Popup`, whose content merges into the test root tree, so they are
 * assertable/clickable via `onNodeWithText` (same idiom as `FormaDropdownMenuTest`). Everything is
 * hoisted — the tests own `expanded`, the query `value`, and the (already-filtered) `options` — so
 * the generic option path is exercised with a plain `List<String>` and `optionLabel = { it }`. The
 * taller screen qualifier gives the anchor a stable width for popup positioning.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class FormaExposedDropdownMenuTest {

    private val fruits = listOf("Apple", "Banana", "Cherry")

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun anchor_rendersValueAndLabel_collapsedHidesRows() {
        composeRule.setContent {
            FormaTheme {
                FormaExposedDropdownMenu(
                    expanded = false,
                    onExpandedChange = {},
                    value = "Cherry",
                    onValueChange = {},
                    options = fruits,
                    onOptionSelected = {},
                    optionLabel = { it },
                    label = "Fruit",
                )
            }
        }

        composeRule.onNodeWithText("Cherry").assertIsDisplayed() // the anchor field's current value
        composeRule.onNodeWithText("Fruit").assertIsDisplayed() // the floating label
        // Collapsed: option rows are not composed (only the field value "Cherry" exists).
        composeRule.onNodeWithText("Apple").assertDoesNotExist()
        composeRule.onNodeWithText("Banana").assertDoesNotExist()
    }

    @Test
    fun expandedToggle_showsOptionRow() {
        composeRule.setContent {
            FormaTheme {
                var expanded by remember { mutableStateOf(false) }
                Column {
                    Text(
                        "open",
                        modifier = Modifier
                            .testTag("open")
                            .clickable { expanded = true },
                    )
                    FormaExposedDropdownMenu(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        value = "",
                        onValueChange = {},
                        options = fruits,
                        onOptionSelected = {},
                        optionLabel = { it },
                        label = "Fruit",
                    )
                }
            }
        }

        composeRule.onNodeWithText("Banana").assertDoesNotExist()
        composeRule.onNodeWithTag("open").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Banana").assertExists()
    }

    @Test
    fun selectingOption_firesCallback_andUpdatesValue() {
        var selected: String? = null
        composeRule.setContent {
            FormaTheme {
                var expanded by remember { mutableStateOf(true) }
                var value by remember { mutableStateOf("") }
                FormaExposedDropdownMenu(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    value = value,
                    onValueChange = { value = it },
                    options = fruits,
                    onOptionSelected = { selected = it; value = it },
                    optionLabel = { it },
                    label = "Fruit",
                )
            }
        }

        composeRule.onNodeWithText("Banana").performClick() // a menu row
        composeRule.waitForIdle()

        // onOptionSelected fired with the tapped option; the harness pushed it into value, so the
        // now-collapsed anchor field displays it.
        composeRule.runOnIdle { assertEquals("Banana", selected) }
        composeRule.onNodeWithText("Banana").assertIsDisplayed()
    }

    @Test
    fun emptyOptions_showsDisabledNoMatchesRow() {
        composeRule.setContent {
            FormaTheme {
                FormaExposedDropdownMenu(
                    expanded = true,
                    onExpandedChange = {},
                    value = "zzz",
                    onValueChange = {},
                    options = emptyList<String>(),
                    onOptionSelected = {},
                    optionLabel = { it },
                    label = "Fruit",
                )
            }
        }

        composeRule.onNodeWithText(FormaExposedDropdownMenuDefaults.NoMatchesText)
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }
}
