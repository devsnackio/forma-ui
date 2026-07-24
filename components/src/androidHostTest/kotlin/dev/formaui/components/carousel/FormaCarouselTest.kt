/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.carousel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.rememberCarouselState
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
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaCarousel], hosted on the JVM via Robolectric.
 *
 * Covers the item slot rendering for a `rememberCarouselState(itemCount = { N })`, both layout
 * variants ([FormaCarouselVariant.MultiBrowse] + [FormaCarouselVariant.Uncontained]) composing, and
 * a hoisted-state change: flipping the label the first item shows recomposes the item. The carousel
 * is given an explicit width (`fillMaxWidth` on a fixed screen) and height so its items are measured
 * deterministically; the value change is a hoisted flip, not a brittle fling/scroll gesture.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class FormaCarouselTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun multiBrowseVariant_rendersItems() {
        composeRule.setContent {
            FormaTheme {
                val state = rememberCarouselState(itemCount = { 5 })
                FormaCarousel(
                    state = state,
                    itemWidth = 100.dp,
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    variant = FormaCarouselVariant.MultiBrowse,
                ) { index ->
                    Text("MB-$index", modifier = Modifier.testTag("mb-$index"))
                }
            }
        }

        composeRule.onNodeWithText("MB-0").assertExists()
    }

    @Test
    fun uncontainedVariant_rendersItems() {
        composeRule.setContent {
            FormaTheme {
                val state = rememberCarouselState(itemCount = { 5 })
                FormaCarousel(
                    state = state,
                    itemWidth = 100.dp,
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    variant = FormaCarouselVariant.Uncontained,
                ) { index ->
                    Text("UC-$index", modifier = Modifier.testTag("uc-$index"))
                }
            }
        }

        composeRule.onNodeWithText("UC-0").assertExists()
    }

    @Test
    fun hoistedLabelChange_updatesItem() {
        composeRule.setContent {
            FormaTheme {
                var label by remember { mutableStateOf("first") }
                val state = rememberCarouselState(itemCount = { 3 })
                Column {
                    Text(
                        "flip",
                        modifier = Modifier
                            .testTag("flip")
                            .clickable { label = "second" },
                    )
                    FormaCarousel(
                        state = state,
                        itemWidth = 100.dp,
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                    ) { index ->
                        Text(if (index == 0) label else "item-$index")
                    }
                }
            }
        }

        composeRule.onNodeWithText("first").assertExists()
        composeRule.onNodeWithTag("flip").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("second").assertExists()
        composeRule.onNodeWithText("first").assertDoesNotExist()
    }
}
