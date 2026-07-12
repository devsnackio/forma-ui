/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaSearchBar], hosted on the JVM via Robolectric. Covers the PRD §5.3
 * bar (renders + responds to a state change) for both [FormaSearchBarVariant]s, plus the
 * expanded/results state and the disabled input field.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaSearchBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun docked_collapsed_showsPlaceholder() {
        composeRule.setContent {
            FormaTheme {
                FormaSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("Search contacts") },
                )
            }
        }

        composeRule.onNodeWithText("Search contacts").assertIsDisplayed()
    }

    @Test
    fun typing_firesOnQueryChange_andUpdatesHoistedQuery() {
        var lastChange: String? = null
        composeRule.setContent {
            FormaTheme {
                // Hoisted state owned by the caller, per Compose convention.
                var query by remember { mutableStateOf("") }
                FormaSearchBar(
                    query = query,
                    onQueryChange = { query = it; lastChange = it },
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("Search contacts") },
                )
            }
        }

        composeRule.onNodeWithText("Search contacts").performTextInput("Ada")

        // onQueryChange fired with the typed text...
        composeRule.runOnIdle { assertEquals("Ada", lastChange) }
        // ...and the hoisted value flowed back into the field's displayed content.
        composeRule.onNodeWithText("Ada").assertIsDisplayed()
    }

    @Test
    fun expanded_showsResultsContent() {
        composeRule.setContent {
            FormaTheme {
                FormaSearchBar(
                    query = "Ada",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = true,
                    onExpandedChange = {},
                    placeholder = { Text("Search contacts") },
                ) {
                    Text("Ada Lovelace")
                    Text("Ada Byron")
                }
            }
        }

        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()
        composeRule.onNodeWithText("Ada Byron").assertIsDisplayed()
    }

    @Test
    fun fullScreenVariant_collapsed_showsPlaceholder() {
        composeRule.setContent {
            FormaTheme {
                FormaSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    variant = FormaSearchBarVariant.FullScreen,
                    placeholder = { Text("Search") },
                )
            }
        }

        composeRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun disabled_isNotEnabled() {
        composeRule.setContent {
            FormaTheme {
                FormaSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    enabled = false,
                    placeholder = { Text("Search contacts") },
                )
            }
        }

        composeRule.onNodeWithText("Search contacts").assertIsNotEnabled()
    }
}
