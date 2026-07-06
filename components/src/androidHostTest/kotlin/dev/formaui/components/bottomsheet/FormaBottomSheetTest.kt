/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.bottomsheet

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI test for [FormaBottomSheet], hosted on the JVM via Robolectric.
 *
 * `ModalBottomSheet` renders inside its own window; the test framework merges that window's
 * semantics into the root tree, so the sheet's content is assertable. This verifies the sheet
 * composes and surfaces its content when shown.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun bottomSheet_showsContentWhenShown() {
        composeRule.setContent {
            FormaTheme {
                FormaBottomSheet(onDismissRequest = {}) {
                    Text("Sheet content")
                }
            }
        }

        composeRule.onNodeWithText("Sheet content").assertExists()
    }
}
