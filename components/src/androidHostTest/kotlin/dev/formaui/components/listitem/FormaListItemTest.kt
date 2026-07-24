/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
 * Compose UI tests for [FormaListItem], hosted on the JVM via Robolectric. Covers the headline,
 * the three-line layout (overline + supporting), leading/trailing slots, the click callback, and
 * the disabled state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaListItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun headline_renders() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(headline = "Single line")
            }
        }

        composeRule.onNodeWithText("Single line").assertIsDisplayed()
    }

    @Test
    fun threeLine_showsOverlineAndSupporting() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Payment received",
                    overline = "Today, 09:41",
                    supporting = "$1,240.00 from Grace Hopper",
                )
            }
        }

        composeRule.onNodeWithText("Today, 09:41").assertIsDisplayed()
        composeRule.onNodeWithText("Payment received").assertIsDisplayed()
        composeRule.onNodeWithText("$1,240.00 from Grace Hopper").assertIsDisplayed()
    }

    @Test
    fun leadingAndTrailingSlots_render() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "With slots",
                    leading = { Text("LEAD") },
                    trailing = { Text("TRAIL") },
                )
            }
        }

        composeRule.onNodeWithText("LEAD").assertIsDisplayed()
        composeRule.onNodeWithText("TRAIL").assertIsDisplayed()
    }

    @Test
    fun clickable_firesOnClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Tap row",
                    onClick = { clicks++ },
                    modifier = Modifier.testTag("row"),
                )
            }
        }

        composeRule.onNodeWithTag("row").performClick()
        composeRule.runOnIdle { assertEquals("clickable row should fire onClick once", 1, clicks) }
    }

    @Test
    fun disabled_isNotEnabled_andDoesNotClick() {
        var clicks = 0
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Disabled row",
                    onClick = { clicks++ },
                    enabled = false,
                    modifier = Modifier.testTag("row"),
                )
            }
        }

        composeRule.onNodeWithTag("row").assertIsNotEnabled()
        composeRule.onNodeWithTag("row").performClick()
        composeRule.runOnIdle { assertEquals("disabled row must not fire onClick", 0, clicks) }
    }

    // --- text-style override params (headlineTextStyle / overlineTextStyle / supportingTextStyle) ---

    /**
     * Reads the fully-resolved [TextStyle] a text node was actually laid out with, via the
     * `GetTextLayoutResult` semantics action. This is the style FormaUI produced with
     * `LocalTextStyle.current.merge(override)`, so it reflects both the M3 per-slot token style and
     * any override merged on top.
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
    fun headlineTextStyle_overrideReachesRenderedHeadline() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Bold headline",
                    headlineTextStyle = TextStyle(fontWeight = FontWeight.Bold),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("Bold headline", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "headlineTextStyle override should apply to the rendered headline",
            FontWeight.Bold,
            style.fontWeight,
        )
    }

    @Test
    fun overlineTextStyle_overrideReachesRenderedOverline() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Row",
                    overline = "Bold overline",
                    overlineTextStyle = TextStyle(fontWeight = FontWeight.Bold),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("Bold overline", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "overlineTextStyle override should apply to the rendered overline",
            FontWeight.Bold,
            style.fontWeight,
        )
    }

    @Test
    fun supportingTextStyle_overrideReachesRenderedSupporting() {
        composeRule.setContent {
            FormaTheme {
                FormaListItem(
                    headline = "Row",
                    supporting = "Bold supporting",
                    supportingTextStyle = TextStyle(fontWeight = FontWeight.Bold),
                )
            }
        }

        val style =
            composeRule.onNodeWithText("Bold supporting", useUnmergedTree = true).resolvedTextStyle()
        assertEquals(
            "supportingTextStyle override should apply to the rendered supporting text",
            FontWeight.Bold,
            style.fontWeight,
        )
    }

    @Test
    fun textStyleOverride_mergesWithM3Style_ratherThanReplacingIt() {
        composeRule.setContent {
            FormaTheme {
                Column {
                    // No override: the pure M3 headline (bodyLarge) token style.
                    FormaListItem(headline = "Plain headline")
                    // Partial override: only fontWeight is set — everything else must survive.
                    FormaListItem(
                        headline = "Merged headline",
                        headlineTextStyle = TextStyle(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }

        val plain =
            composeRule.onNodeWithText("Plain headline", useUnmergedTree = true).resolvedTextStyle()
        val merged =
            composeRule.onNodeWithText("Merged headline", useUnmergedTree = true).resolvedTextStyle()

        // The override took effect...
        assertEquals(
            "partial override must apply its fontWeight",
            FontWeight.Bold,
            merged.fontWeight,
        )
        assertNotEquals(
            "control headline must not already be bold (otherwise the test proves nothing)",
            FontWeight.Bold,
            plain.fontWeight,
        )
        // ...but it merged, it did not replace: properties the override left unspecified are still
        // inherited from the underlying M3 headline style.
        assertEquals(
            "merge must preserve the M3 headline fontSize",
            plain.fontSize,
            merged.fontSize,
        )
        assertEquals(
            "merge must preserve the M3 headline letterSpacing",
            plain.letterSpacing,
            merged.letterSpacing,
        )
        assertEquals(
            "merge must preserve the M3 headline fontFamily",
            plain.fontFamily,
            merged.fontFamily,
        )
    }
}
