/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.divider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
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
 * (no semantics of its own), so each orientation is verified to compose/render via a test tag.
 *
 * A divider holds no internal state, so the PRD §5.3 "responds to a state change" bar is met the
 * only way it meaningfully can: driving its parameters from hoisted state and asserting the
 * *measured layout* changes on recomposition.
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

    @Test
    fun thickness_remeasuresWhenStateChanges() {
        var thickness by mutableStateOf(1.dp)
        composeRule.setContent {
            FormaTheme {
                FormaDivider(
                    modifier = Modifier.testTag("divider"),
                    thickness = thickness,
                )
            }
        }

        // A horizontal divider measures exactly its thickness tall.
        composeRule.onNodeWithTag("divider").assertHeightIsEqualTo(1.dp)

        composeRule.runOnIdle { thickness = 8.dp }

        composeRule.onNodeWithTag("divider").assertHeightIsEqualTo(8.dp)
    }

    @Test
    fun orientation_swapsMeasuredAxesWhenStateChanges() {
        var orientation by mutableStateOf(FormaDividerOrientation.Horizontal)
        composeRule.setContent {
            FormaTheme {
                Box(modifier = Modifier.size(40.dp)) {
                    FormaDivider(
                        modifier = Modifier.testTag("divider"),
                        orientation = orientation,
                        thickness = 4.dp,
                    )
                }
            }
        }

        // Horizontal: spans the parent's width, measures `thickness` tall.
        composeRule.onNodeWithTag("divider")
            .assertWidthIsEqualTo(40.dp)
            .assertHeightIsEqualTo(4.dp)

        composeRule.runOnIdle { orientation = FormaDividerOrientation.Vertical }

        // Vertical: the axes swap — `thickness` wide, spanning the parent's height.
        composeRule.onNodeWithTag("divider")
            .assertWidthIsEqualTo(4.dp)
            .assertHeightIsEqualTo(40.dp)
    }
}
