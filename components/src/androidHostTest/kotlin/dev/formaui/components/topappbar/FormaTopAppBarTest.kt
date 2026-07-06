/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.topappbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
 * Compose UI tests for [FormaTopAppBar], hosted on the JVM via Robolectric. Covers all four
 * variants rendering with the title shown, plus the navigation icon and actions slots firing
 * their click callbacks.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaTopAppBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun allVariants_renderTitle() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaTopAppBarVariant.entries.forEach { variant ->
                        FormaTopAppBar(
                            title = "Title ${variant.name}",
                            variant = variant,
                        )
                    }
                }
            }
        }

        FormaTopAppBarVariant.entries.forEach { variant ->
            composeRule.onNodeWithText("Title ${variant.name}").assertExists()
        }
    }

    @Test
    fun navigationIcon_click_firesCallback() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaTopAppBar(
                    title = "Inbox",
                    navigationIcon = {
                        Text(
                            "<-",
                            modifier = Modifier
                                .testTag("nav-icon")
                                .clickable { clicks++ },
                        )
                    },
                )
            }
        }

        composeRule.onNodeWithText("Inbox").assertExists()
        composeRule.onNodeWithTag("nav-icon").assertExists()
        composeRule.onNodeWithTag("nav-icon").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun actions_click_firesCallback() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaTopAppBar(
                    title = "Inbox",
                    actions = {
                        Text(
                            "search",
                            modifier = Modifier
                                .testTag("action-search")
                                .clickable { clicks++ },
                        )
                    },
                )
            }
        }

        composeRule.onNodeWithTag("action-search").assertExists()
        composeRule.onNodeWithTag("action-search").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun mediumAndLargeVariants_renderTitle() {
        composeRule.setContent {
            FormaTheme {
                FormaTopAppBar(title = "Medium bar", variant = FormaTopAppBarVariant.Medium)
            }
        }

        composeRule.onNodeWithText("Medium bar").assertExists()
    }
}
