/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.bottomappbar

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
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
 * Compose UI tests for [FormaBottomAppBar], hosted on the JVM via Robolectric. Covers the actions
 * slot rendering and firing its click callback, and the optional floating action button slot
 * rendering and firing its click callback.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaBottomAppBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun actions_render() {
        composeRule.setContent {
            FormaTheme {
                FormaBottomAppBar(
                    actions = {
                        Text("Search")
                        Text("More")
                    },
                )
            }
        }

        composeRule.onNodeWithText("Search").assertIsDisplayed()
        composeRule.onNodeWithText("More").assertIsDisplayed()
    }

    @Test
    fun actionClick_firesCallback() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaBottomAppBar(
                    actions = {
                        Text(
                            "Search",
                            modifier = Modifier
                                .testTag("action-search")
                                .clickable { clicks++ },
                        )
                    },
                )
            }
        }

        composeRule.onNodeWithTag("action-search").performClick()
        composeRule.runOnIdle { assertEquals("action click should fire once", 1, clicks) }
    }

    @Test
    fun floatingActionButton_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaBottomAppBar(
                    actions = { Text("Search") },
                    floatingActionButton = { Text("Add") },
                )
            }
        }

        composeRule.onNodeWithText("Add").assertIsDisplayed()
    }

    @Test
    fun floatingActionButtonClick_firesCallback() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaBottomAppBar(
                    actions = { Text("Search") },
                    floatingActionButton = {
                        Text(
                            "Add",
                            modifier = Modifier
                                .testTag("fab")
                                .clickable { clicks++ },
                        )
                    },
                )
            }
        }

        composeRule.onNodeWithTag("fab").performClick()
        composeRule.runOnIdle { assertEquals("FAB click should fire once", 1, clicks) }
    }
}
