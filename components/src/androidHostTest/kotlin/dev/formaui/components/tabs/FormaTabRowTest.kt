/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
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
 * Compose UI tests for [FormaTabRow] and [FormaTab], hosted on the JVM via Robolectric. Covers
 * both [FormaTabRowVariant]s rendering their tab labels, a `scrollable = true` row, an icon+text
 * tab (dispatching to `LeadingIconTab`), and clicking an unselected tab firing its callback.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaTabRowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun bothVariants_renderTabLabels() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaTabRowVariant.entries.forEach { variant ->
                        var selected by remember { mutableStateOf(0) }
                        FormaTabRow(selectedTabIndex = selected, variant = variant) {
                            listOf("Chat", "Calls", "Contacts").forEachIndexed { index, label ->
                                FormaTab(
                                    selected = selected == index,
                                    onClick = { selected = index },
                                    text = { Text("${variant.name}-$label") },
                                )
                            }
                        }
                    }
                }
            }
        }

        FormaTabRowVariant.entries.forEach { variant ->
            composeRule.onNodeWithText("${variant.name}-Chat").assertExists()
            composeRule.onNodeWithText("${variant.name}-Calls").assertExists()
            composeRule.onNodeWithText("${variant.name}-Contacts").assertExists()
        }
    }

    @Test
    fun unselectedTab_click_firesCallbackAndUpdatesSelection() {
        var selected by mutableStateOf(0)
        composeRule.setContent {
            FormaTheme {
                FormaTabRow(selectedTabIndex = selected) {
                    listOf("Chat", "Calls", "Contacts").forEachIndexed { index, label ->
                        FormaTab(
                            selected = selected == index,
                            onClick = { selected = index },
                            text = { Text(label) },
                        )
                    }
                }
            }
        }

        composeRule.onNodeWithText("Calls").performClick()
        composeRule.runOnIdle { assertEquals(1, selected) }
    }

    @Test
    fun scrollableRow_renders() {
        composeRule.setContent {
            FormaTheme {
                var selected by remember { mutableStateOf(0) }
                val destinations = listOf("Home", "Search", "Favorites", "Updates", "Profile", "Settings")
                FormaTabRow(selectedTabIndex = selected, scrollable = true) {
                    destinations.forEachIndexed { index, label ->
                        FormaTab(
                            selected = selected == index,
                            onClick = { selected = index },
                            text = { Text(label) },
                        )
                    }
                }
            }
        }

        composeRule.onNodeWithText("Home").assertExists()
        composeRule.onNodeWithText("Settings").assertExists()
    }

    @Test
    fun tabWithTextAndIcon_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaTab(
                    selected = true,
                    onClick = {},
                    text = { Text("Home") },
                    icon = { Text("*") },
                )
            }
        }

        composeRule.onNodeWithText("Home").assertExists()
    }
}
