/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
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
}
