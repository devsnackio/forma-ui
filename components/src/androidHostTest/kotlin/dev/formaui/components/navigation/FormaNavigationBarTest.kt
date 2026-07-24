/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaNavigationBar] / [FormaNavigationBarItem], hosted on the JVM via
 * Robolectric. Covers item/label rendering, the selected semantics, the click callback, and the
 * per-item numeric badge.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaNavigationBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun items_renderWithLabels() {
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar {
                    FormaNavigationBarItem(selected = true, onClick = {}, icon = { Text("H") }, label = "Home")
                    FormaNavigationBarItem(selected = false, onClick = {}, icon = { Text("A") }, label = "Alerts", badgeCount = 3)
                    FormaNavigationBarItem(selected = false, onClick = {}, icon = { Text("P") }, label = "Profile", showBadgeDot = true)
                }
            }
        }

        composeRule.onNodeWithText("Home").assertIsDisplayed()
        composeRule.onNodeWithText("Alerts").assertIsDisplayed()
        composeRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun selectedItem_reflectsSelectedState() {
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar {
                    FormaNavigationBarItem(
                        selected = true, onClick = {}, icon = { Text("H") }, label = "Home",
                        modifier = Modifier.testTag("home"),
                    )
                    FormaNavigationBarItem(
                        selected = false, onClick = {}, icon = { Text("A") }, label = "Alerts",
                        modifier = Modifier.testTag("alerts"),
                    )
                }
            }
        }

        composeRule.onNodeWithTag("home").assertIsSelected()
        composeRule.onNodeWithTag("alerts").assertIsNotSelected()
    }

    @Test
    fun itemClick_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar {
                    FormaNavigationBarItem(
                        selected = false, onClick = { clicks++ }, icon = { Text("H") }, label = "Home",
                        modifier = Modifier.testTag("home"),
                    )
                }
            }
        }

        composeRule.onNodeWithTag("home").performClick()
        composeRule.runOnIdle { assertEquals("nav item click should fire onClick once", 1, clicks) }
    }

    @Test
    fun badgeCount_showsNumberOnItem() {
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar {
                    FormaNavigationBarItem(
                        selected = false, onClick = {}, icon = { Text("A") }, label = "Alerts",
                        badgeCount = 7,
                    )
                }
            }
        }

        // The badge's number sits inside the item's merged semantics node, so query the
        // unmerged tree to find the badge Text node directly.
        composeRule.onNodeWithText("7", useUnmergedTree = true).assertExists()
    }

    // --- item color params (colors = NavigationBarItemDefaults.colors(...)) ---

    /**
     * Reads the fully-resolved [TextStyle] a label node was actually laid out with, via the
     * `GetTextLayoutResult` semantics action. M3's `NavigationBarItem` drives the label color by
     * merging the selection-resolved content color into `LocalTextStyle`, so the resolved
     * `layoutInput.style.color` reflects the item `colors`' selected/unselected text color.
     */
    private fun SemanticsNodeInteraction.resolvedTextStyle(): TextStyle {
        val node = fetchSemanticsNode()
        val action = node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action
        assertNotNull("label node should expose the GetTextLayoutResult semantics action", action)
        val results = mutableListOf<TextLayoutResult>()
        action!!.invoke(results)
        assertTrue("GetTextLayoutResult must yield a layout", results.isNotEmpty())
        return results.first().layoutInput.style
    }

    @Test
    fun selectedTextColor_reachesSelectedItemLabel() {
        val selectedColor = Color(0xFF00E5FF) // distinctive cyan — unlikely to match any M3 default
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar {
                    FormaNavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Text("H") },
                        label = "Home",
                        colors = NavigationBarItemDefaults.colors(selectedTextColor = selectedColor),
                    )
                }
            }
        }

        val style =
            composeRule.onNodeWithText("Home", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "custom selectedTextColor should reach the selected item's rendered label",
            selectedColor,
            style.color,
        )
    }

    @Test
    fun selectedAndUnselectedTextColors_applyPerSelectionState() {
        val selectedColor = Color(0xFF00E5FF)   // cyan
        val unselectedColor = Color(0xFFFFAB00) // amber
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar {
                    FormaNavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Text("H") },
                        label = "Home",
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = selectedColor,
                            unselectedTextColor = unselectedColor,
                        ),
                    )
                    FormaNavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Text("A") },
                        label = "Alerts",
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = selectedColor,
                            unselectedTextColor = unselectedColor,
                        ),
                    )
                }
            }
        }

        val selectedStyle =
            composeRule.onNodeWithText("Home", useUnmergedTree = true).resolvedTextStyle()
        val unselectedStyle =
            composeRule.onNodeWithText("Alerts", useUnmergedTree = true).resolvedTextStyle()

        assertEquals(
            "selected item label should use selectedTextColor",
            selectedColor,
            selectedStyle.color,
        )
        assertEquals(
            "unselected item label should use unselectedTextColor",
            unselectedColor,
            unselectedStyle.color,
        )
        assertNotEquals(
            "selected vs unselected colors must differ, else the assertion could pass vacuously",
            selectedStyle.color,
            unselectedStyle.color,
        )
    }

    @Test
    fun customContainerAndContentColor_areAcceptedAndRender() {
        // The container background is drawn behind M3's tonal-elevation overlay, so a captured-pixel
        // assertion in Robolectric would be flaky. We instead assert the new containerColor /
        // contentColor params are accepted and forwarded without breaking item layout/rendering.
        composeRule.setContent {
            FormaTheme {
                FormaNavigationBar(
                    containerColor = Color(0xFF102027),
                    contentColor = Color(0xFFECEFF1),
                ) {
                    FormaNavigationBarItem(
                        selected = true, onClick = {}, icon = { Text("H") }, label = "Home",
                    )
                    FormaNavigationBarItem(
                        selected = false, onClick = {}, icon = { Text("A") }, label = "Alerts",
                    )
                }
            }
        }

        composeRule.onNodeWithText("Home").assertIsDisplayed()
        composeRule.onNodeWithText("Alerts").assertIsDisplayed()
    }
}
