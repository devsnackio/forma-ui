/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
 * Compose UI tests for [FormaDropdownMenu] and [FormaDropdownMenuItem], hosted on the JVM via
 * Robolectric. Covers items showing while expanded, clicking an item firing its callback, and
 * items not existing while collapsed. The menu is hosted inside a `Box` anchor, matching the
 * KDoc's usage example.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaDropdownMenuTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun expanded_showsItemText() {
        composeRule.setContent {
            FormaTheme {
                Box {
                    Text("anchor")
                    FormaDropdownMenu(expanded = true, onDismissRequest = {}) {
                        FormaDropdownMenuItem(text = "Share", onClick = {})
                        FormaDropdownMenuItem(text = "Delete", onClick = {})
                    }
                }
            }
        }

        composeRule.onNodeWithText("Share").assertExists()
        composeRule.onNodeWithText("Delete").assertExists()
    }

    @Test
    fun item_click_firesCallback() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                Box {
                    Text("anchor")
                    FormaDropdownMenu(expanded = true, onDismissRequest = {}) {
                        FormaDropdownMenuItem(text = "Share", onClick = { clicks++ })
                    }
                }
            }
        }

        composeRule.onNodeWithText("Share").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun collapsed_itemDoesNotExist() {
        composeRule.setContent {
            FormaTheme {
                Box {
                    Text("anchor")
                    FormaDropdownMenu(expanded = false, onDismissRequest = {}) {
                        FormaDropdownMenuItem(text = "Share", onClick = {})
                    }
                }
            }
        }

        composeRule.onNodeWithText("Share").assertDoesNotExist()
    }

    @Test
    fun expandedToggle_updatesVisibility() {
        composeRule.setContent {
            FormaTheme {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Text(
                        "anchor",
                        modifier = Modifier
                            .testTag("anchor")
                            .clickable { expanded = true },
                    )
                    FormaDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        FormaDropdownMenuItem(text = "Rename", onClick = {})
                    }
                }
            }
        }

        composeRule.onNodeWithText("Rename").assertDoesNotExist()
        composeRule.onNodeWithTag("anchor").performClick()
        composeRule.onNodeWithText("Rename").assertExists()
    }

    @Test
    fun disabledItem_click_doesNotFireCallback() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                Box {
                    Text("anchor")
                    FormaDropdownMenu(expanded = true, onDismissRequest = {}) {
                        FormaDropdownMenuItem(text = "Archive", onClick = { clicks++ }, enabled = false)
                    }
                }
            }
        }

        composeRule.onNodeWithText("Archive").performClick()
        composeRule.runOnIdle { assertEquals(0, clicks) }
    }
}
