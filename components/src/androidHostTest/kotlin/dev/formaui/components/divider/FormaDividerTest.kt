/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.divider

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaDivider], hosted on the JVM via Robolectric. A divider is decorative
 * (no semantics of its own), so each orientation is verified to compose/render without crashing
 * via a test tag.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaDividerTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun horizontalDivider_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaDivider(modifier = Modifier.testTag("divider"))
            }
        }

        composeRule.onNodeWithTag("divider").assertExists()
    }

    @Test
    fun verticalDivider_renders() {
        composeRule.setContent {
            FormaTheme {
                Row(modifier = Modifier.height(40.dp)) {
                    FormaDivider(
                        orientation = FormaDividerOrientation.Vertical,
                        modifier = Modifier.testTag("divider"),
                    )
                }
            }
        }

        composeRule.onNodeWithTag("divider").assertExists()
    }
}
