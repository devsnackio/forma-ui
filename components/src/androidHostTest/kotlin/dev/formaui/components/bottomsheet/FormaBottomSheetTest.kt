/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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

    /**
     * [FormaBottomSheet] is stateless — per its KDoc, callers drive visibility by conditionally
     * composing it. This mirrors that documented usage: a hoisted `visible` flag gates the call,
     * and flipping it removes the sheet (and its content) from composition entirely.
     */
    @Test
    fun bottomSheet_visibleToggle_hidesSheetContent() {
        composeRule.setContent {
            FormaTheme {
                var visible by remember { mutableStateOf(true) }
                Column {
                    Text(
                        "Hide",
                        modifier = Modifier
                            .testTag("hide")
                            .clickable { visible = false },
                    )
                    if (visible) {
                        FormaBottomSheet(onDismissRequest = { visible = false }) {
                            Text("Sheet content")
                        }
                    }
                }
            }
        }

        composeRule.onNodeWithText("Sheet content").assertExists()
        composeRule.onNodeWithTag("hide").performClick()
        composeRule.onNodeWithText("Sheet content").assertDoesNotExist()
    }
}
