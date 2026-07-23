/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.fab

import androidx.compose.foundation.layout.Column
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
 * Compose UI tests for [FormaFloatingActionButton] and [FormaExtendedFloatingActionButton],
 * hosted on the JVM via Robolectric. Covers all three sizes rendering, the click callback firing,
 * and the extended FAB's text label.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaFloatingActionButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun allSizes_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaFabSize.entries.forEach { size ->
                        FormaFloatingActionButton(
                            onClick = {},
                            size = size,
                            modifier = Modifier.testTag("fab-${size.name}"),
                        ) {
                            Text("+")
                        }
                    }
                }
            }
        }

        FormaFabSize.entries.forEach { size ->
            composeRule.onNodeWithTag("fab-${size.name}").assertExists()
        }
    }

    @Test
    fun click_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaFloatingActionButton(
                    onClick = { clicks++ },
                    modifier = Modifier.testTag("fab"),
                ) {
                    Text("+")
                }
            }
        }

        composeRule.onNodeWithTag("fab").assertExists()
        composeRule.onNodeWithTag("fab").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun extendedFab_showsTextLabel_andFiresOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaExtendedFloatingActionButton(
                    text = "Compose",
                    icon = { Text("+") },
                    onClick = { clicks++ },
                    modifier = Modifier.testTag("extended-fab"),
                )
            }
        }

        // The M3 ExtendedFloatingActionButton wraps its text label in `clearAndSetSemantics {}`
        // (so the button's own merged semantics own the accessible label), so the text node is
        // only visible in the unmerged tree.
        composeRule.onNodeWithText("Compose", useUnmergedTree = true).assertExists()
        composeRule.onNodeWithTag("extended-fab").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun extendedFab_collapsed_stillRenders() {
        composeRule.setContent {
            FormaTheme {
                FormaExtendedFloatingActionButton(
                    text = "Compose",
                    icon = { Text("+") },
                    onClick = {},
                    expanded = false,
                    modifier = Modifier.testTag("extended-fab"),
                )
            }
        }

        composeRule.onNodeWithTag("extended-fab").assertExists()
    }

    @Test
    fun pressScale_disabledViaNullSpec_stillClicks() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaFloatingActionButton(
                    onClick = { clicks++ },
                    pressAnimationSpec = null,
                    modifier = Modifier.testTag("fab"),
                ) {
                    Text("+")
                }
            }
        }

        composeRule.onNodeWithTag("fab").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun extendedFab_pressScale_disabledViaNullSpec_stillClicks() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaExtendedFloatingActionButton(
                    text = "Compose",
                    icon = { Text("+") },
                    onClick = { clicks++ },
                    pressAnimationSpec = null,
                    modifier = Modifier.testTag("extended-fab"),
                )
            }
        }

        composeRule.onNodeWithTag("extended-fab").performClick()
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }
}
