/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaModalNavigationDrawer] / [FormaNavigationDrawerItem], hosted on the
 * JVM via Robolectric. Covers the screen [content] behind the drawer, opening the drawer via the
 * hoisted [androidx.compose.material3.DrawerState], the item click callback, and the trailing
 * badge text.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaNavigationDrawerTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun content_rendersBehindDrawer() {
        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FormaNavigationDrawerItem(label = "Inbox", selected = true, onClick = {})
                    },
                ) {
                    Text("Mail content", modifier = Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onNodeWithText("Mail content").assertIsDisplayed()
    }

    @Test
    fun openingDrawer_showsItem_andItemClick_firesOnClick() {
        var clicks = 0
        var isOpenAfterOpen = false

        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FormaNavigationDrawerItem(
                            label = "Inbox",
                            selected = false,
                            onClick = { clicks++ },
                            modifier = Modifier.testTag("inbox-item"),
                        )
                    },
                ) {
                    Text(
                        "Open drawer",
                        modifier = Modifier
                            .testTag("open-drawer")
                            .clickable { scope.launch { drawerState.open() } },
                    )
                    if (drawerState.isOpen) {
                        isOpenAfterOpen = true
                    }
                }
            }
        }

        composeRule.onNodeWithTag("open-drawer").performClick()
        composeRule.waitForIdle()

        composeRule.runOnIdle { assertTrue("drawer should report isOpen after open()", isOpenAfterOpen) }

        composeRule.onNodeWithText("Inbox").assertIsDisplayed()
        composeRule.onNodeWithTag("inbox-item").performClick()
        composeRule.runOnIdle { assertEquals("drawer item click should fire onClick once", 1, clicks) }
    }

    @Test
    fun badge_rendersTrailingText() {
        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Open)
                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FormaNavigationDrawerItem(
                            label = "Inbox",
                            selected = false,
                            onClick = {},
                            badge = "24",
                        )
                    },
                ) {
                    Text("Mail content", modifier = Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onNodeWithText("Inbox").assertIsDisplayed()
        composeRule.onNodeWithText("24", useUnmergedTree = true).assertExists()
    }

    // --- drawer + item customization params (scrim/container/content colors, colors, text styles) ---

    /**
     * Reads the fully-resolved [TextStyle] a text node was actually laid out with, via the
     * `GetTextLayoutResult` semantics action — the style FormaUI produced with
     * `LocalTextStyle.current.merge(override)`.
     */
    private fun SemanticsNodeInteraction.resolvedTextStyle(): TextStyle {
        val node = fetchSemanticsNode()
        val action = node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action
        assertNotNull("text node should expose the GetTextLayoutResult semantics action", action)
        val results = mutableListOf<TextLayoutResult>()
        action!!.invoke(results)
        assertTrue("GetTextLayoutResult must yield a layout", results.isNotEmpty())
        return results.first().layoutInput.style
    }

    @Test
    fun drawerColorParams_areAcceptedAndRender() {
        // Scrim/container/content colors aren't reliably pixel-assertable under Robolectric; assert
        // the new params are accepted and the open drawer sheet + its item still render.
        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Open)
                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    scrimColor = Color(0x88FF0000),
                    drawerContainerColor = Color(0xFF102027),
                    drawerContentColor = Color(0xFFECEFF1),
                    drawerContent = {
                        FormaNavigationDrawerItem(label = "Inbox", selected = true, onClick = {})
                    },
                ) {
                    Text("Mail content", modifier = Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onNodeWithText("Inbox").assertIsDisplayed()
    }

    @Test
    fun itemColors_areAcceptedAndRender() {
        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Open)
                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FormaNavigationDrawerItem(
                            label = "Inbox",
                            selected = true,
                            onClick = {},
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedTextColor = Color(0xFF00E5FF),
                            ),
                        )
                    },
                ) {
                    Text("Mail content", modifier = Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onNodeWithText("Inbox").assertIsDisplayed()
    }

    @Test
    fun labelTextStyle_overrideReachesRenderedLabel() {
        // Black (900) is not an M3 default drawer-label weight, so a match proves the merge landed.
        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Open)
                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FormaNavigationDrawerItem(
                            label = "Inbox",
                            selected = true,
                            onClick = {},
                            labelTextStyle = TextStyle(fontWeight = FontWeight.Black),
                        )
                    },
                ) {
                    Text("Mail content", modifier = Modifier.fillMaxSize())
                }
            }
        }

        val style =
            composeRule.onNodeWithText("Inbox", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "labelTextStyle override should reach the rendered drawer item label",
            FontWeight.Black,
            style.fontWeight,
        )
    }

    @Test
    fun badgeTextStyle_overrideReachesRenderedBadge() {
        composeRule.setContent {
            FormaTheme {
                val drawerState = rememberDrawerState(DrawerValue.Open)
                FormaModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FormaNavigationDrawerItem(
                            label = "Inbox",
                            selected = true,
                            onClick = {},
                            badge = "24",
                            badgeTextStyle = TextStyle(fontWeight = FontWeight.Black),
                        )
                    },
                ) {
                    Text("Mail content", modifier = Modifier.fillMaxSize())
                }
            }
        }

        val style =
            composeRule.onNodeWithText("24", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "badgeTextStyle override should reach the rendered drawer item badge",
            FontWeight.Black,
            style.fontWeight,
        )
    }
}
