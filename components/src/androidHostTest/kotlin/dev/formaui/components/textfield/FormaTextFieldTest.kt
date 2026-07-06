/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.textfield

import androidx.compose.foundation.layout.Column
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
 * Compose UI tests for [FormaTextField], hosted on the JVM via Robolectric (no device needed).
 * Covers the PRD §5.3 bar (renders + responds to a state change) plus the field's error and
 * disabled states and both variants.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaTextFieldTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersLabelAndValue() {
        composeRule.setContent {
            FormaTheme {
                FormaTextField(
                    value = "Ada Lovelace",
                    onValueChange = {},
                    label = "Name",
                    singleLine = true,
                )
            }
        }

        composeRule.onNodeWithText("Name").assertIsDisplayed()
        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()
    }

    @Test
    fun typing_firesOnValueChange_andUpdatesHoistedValue() {
        var lastChange: String? = null
        composeRule.setContent {
            FormaTheme {
                // Hoisted state owned by the caller, per Compose convention.
                var text by remember { mutableStateOf("") }
                FormaTextField(
                    value = text,
                    onValueChange = { text = it; lastChange = it },
                    modifier = Modifier.testTag("field"),
                    label = "Name",
                    singleLine = true,
                )
            }
        }

        composeRule.onNodeWithTag("field").performTextInput("Grace")

        // onValueChange fired with the typed text...
        composeRule.runOnIdle { assertEquals("Grace", lastChange) }
        // ...and the hoisted value flowed back into the field's displayed content.
        composeRule.onNodeWithText("Grace").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorText_andHidesHelperText() {
        composeRule.setContent {
            FormaTheme {
                FormaTextField(
                    value = "abc",
                    onValueChange = {},
                    label = "Account number",
                    isError = true,
                    helperText = "Digits only",
                    errorText = "Enter a valid account number",
                    singleLine = true,
                )
            }
        }

        composeRule.onNodeWithText("Enter a valid account number").assertIsDisplayed()
        composeRule.onNodeWithText("Digits only").assertDoesNotExist()
    }

    @Test
    fun disabled_isNotEnabled() {
        composeRule.setContent {
            FormaTheme {
                FormaTextField(
                    value = "Locked field",
                    onValueChange = {},
                    modifier = Modifier.testTag("field"),
                    label = "Reference",
                    enabled = false,
                    singleLine = true,
                )
            }
        }

        composeRule.onNodeWithTag("field").assertIsNotEnabled()
    }

    @Test
    fun bothVariants_renderWithoutCrashing() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    FormaTextFieldVariant.entries.forEach { variant ->
                        FormaTextField(
                            value = "",
                            onValueChange = {},
                            variant = variant,
                            label = variant.name,
                            singleLine = true,
                        )
                    }
                }
            }
        }

        // Each variant's floating label is distinct (the enum constant name).
        FormaTextFieldVariant.entries.forEach { variant ->
            composeRule.onNodeWithText(variant.name).assertIsDisplayed()
        }
    }
}
