/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chip

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
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
 * Compose UI tests for [FormaChip], hosted on the JVM via Robolectric. Covers all four variants
 * rendering, the click callback, the selectable-variant selected semantics, and the disabled
 * state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaChipTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun allVariants_renderWithLabel() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaChipVariant.entries.forEach { variant ->
                        FormaChip(label = variant.name, onClick = {}, variant = variant)
                    }
                }
            }
        }

        FormaChipVariant.entries.forEach { variant ->
            composeRule.onNodeWithText(variant.name).assertIsDisplayed()
        }
    }

    @Test
    fun click_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaChip(label = "Tap", onClick = { clicks++ }, modifier = Modifier.testTag("chip"))
            }
        }

        composeRule.onNodeWithTag("chip").performClick()
        composeRule.runOnIdle { assertEquals("chip click should fire onClick once", 1, clicks) }
    }

    @Test
    fun selectableVariants_reflectSelectedState() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaChip(
                        label = "F-on", onClick = {}, variant = FormaChipVariant.Filter,
                        selected = true, modifier = Modifier.testTag("filter-on"),
                    )
                    FormaChip(
                        label = "F-off", onClick = {}, variant = FormaChipVariant.Filter,
                        selected = false, modifier = Modifier.testTag("filter-off"),
                    )
                    FormaChip(
                        label = "I-on", onClick = {}, variant = FormaChipVariant.Input,
                        selected = true, modifier = Modifier.testTag("input-on"),
                    )
                }
            }
        }

        composeRule.onNodeWithTag("filter-on").assertIsSelected()
        composeRule.onNodeWithTag("filter-off").assertIsNotSelected()
        composeRule.onNodeWithTag("input-on").assertIsSelected()
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaChip(
                    label = "Nope", onClick = { clicks++ }, enabled = false,
                    modifier = Modifier.testTag("chip"),
                )
            }
        }

        composeRule.onNodeWithTag("chip").assertIsNotEnabled()
        composeRule.onNodeWithTag("chip").performClick()
        composeRule.runOnIdle { assertEquals("disabled chip must not fire onClick", 0, clicks) }
    }
}
