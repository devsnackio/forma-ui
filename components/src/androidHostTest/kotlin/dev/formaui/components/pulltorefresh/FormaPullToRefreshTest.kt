/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.pulltorefresh

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaPullToRefresh], hosted on the JVM via Robolectric (no device needed).
 * Covers the PRD §5.3 bar: the content slot renders + the hoisted [isRefreshing] state change
 * recomposes the indicator. The refresh gesture itself is not driven (non-deterministic under the
 * test clock); [isRefreshing] is flipped through hoisted state, as documented for the component.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaPullToRefreshTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun content_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaPullToRefresh(isRefreshing = false, onRefresh = {}) {
                    Text("Feed body", modifier = Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onNodeWithText("Feed body").assertIsDisplayed()
    }

    @Test
    fun rendersWhileRefreshing_withDefaultIndicator() {
        // isRefreshing = true drives the default Material 3 indicator into its spinning state;
        // proves the wrapper composes the DEFAULT indicator slot (its typical usage) without crashing.
        composeRule.setContent {
            FormaTheme {
                FormaPullToRefresh(isRefreshing = true, onRefresh = {}) {
                    Text("Loading feed", modifier = Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onNodeWithText("Loading feed").assertIsDisplayed()
    }

    @Test
    fun togglingIsRefreshing_recomposesIndicator() {
        // A custom indicator slot that is only composed while refreshing lets us assert the
        // hoisted-state change deterministically (false -> true) without a gesture.
        composeRule.setContent {
            FormaTheme {
                var refreshing by remember { mutableStateOf(false) }
                FormaPullToRefresh(
                    isRefreshing = refreshing,
                    onRefresh = { refreshing = true },
                    indicator = {
                        if (refreshing) {
                            Text(
                                "Refreshing",
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .testTag("indicator"),
                            )
                        }
                    },
                ) {
                    Text(
                        "Toggle",
                        modifier = Modifier.clickable { refreshing = !refreshing },
                    )
                }
            }
        }

        composeRule.onNodeWithTag("indicator").assertDoesNotExist()
        composeRule.onNodeWithText("Toggle").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("indicator").assertIsDisplayed()
        composeRule.onNodeWithText("Toggle").assertIsDisplayed()
    }
}
