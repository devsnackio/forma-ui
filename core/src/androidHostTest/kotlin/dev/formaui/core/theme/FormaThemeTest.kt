/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.core.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * M1 QA gate: [FormaTheme] renders its content without crashing and every token accessor
 * ([FormaTheme.spacing], [FormaTheme.colorScheme], [FormaTheme.typography], [FormaTheme.shapes])
 * resolves inside the theme. Hosted on the JVM via Robolectric — same path as the component tests.
 *
 * `dynamicColor = false` keeps color resolution deterministic (static brand palette, no Material
 * You wallpaper scheme) so the color assertion holds regardless of the emulated SDK.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaThemeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun formaTheme_rendersContent_andResolvesTokens() {
        // Expected static-palette primary (light mode, dynamic color off) computed from the
        // public default scheme so the assertion is not tied to a hardcoded hex literal.
        val expectedPrimary: Color = FormaTheme.defaultColorScheme().light.primary

        // Tokens captured out of composition so we can assert their resolved values.
        var resolvedSpacingMd: Dp? = null
        var resolvedPrimary: Color? = null
        var resolvedNumeric: TextStyle? = null
        var resolvedMediumShape: Shape? = null

        composeRule.setContent {
            FormaTheme(dynamicColor = false) {
                resolvedSpacingMd = FormaTheme.spacing.md
                resolvedPrimary = FormaTheme.colorScheme.primary
                resolvedNumeric = FormaTheme.typography.numeric
                resolvedMediumShape = FormaTheme.shapes.medium

                Column {
                    Text("themed", modifier = Modifier.testTag("themed-content"))
                }
            }
        }

        // Renders without crashing: the themed content is present and displayed.
        composeRule.onNodeWithTag("themed-content").assertIsDisplayed()

        // Token accessors resolve to the expected FormaUI defaults inside the theme.
        composeRule.runOnIdle {
            assertEquals(
                "FormaTheme.spacing.md should be the 4dp-grid 16dp token",
                16.dp,
                resolvedSpacingMd,
            )
            assertNotNull("FormaTheme.colorScheme must resolve inside FormaTheme", resolvedPrimary)
            assertEquals(
                "static light primary should be the brand default",
                expectedPrimary,
                resolvedPrimary,
            )
            assertNotNull(
                "FormaTheme.typography.numeric must resolve inside FormaTheme",
                resolvedNumeric,
            )
            assertNotNull(
                "FormaTheme.shapes.medium must resolve inside FormaTheme",
                resolvedMediumShape,
            )
        }
    }

    @Test
    fun darkThemeTrue_forcesDarkPalette() {
        val expectedDarkPrimary: Color = FormaTheme.defaultColorScheme().dark.primary
        var resolvedPrimary: Color? = null

        composeRule.setContent {
            FormaTheme(dynamicColor = false, darkTheme = true) {
                resolvedPrimary = FormaTheme.colorScheme.primary
            }
        }

        composeRule.runOnIdle {
            assertEquals(
                "darkTheme = true must resolve the dark palette",
                expectedDarkPrimary,
                resolvedPrimary,
            )
        }
    }

    @Test
    fun darkThemeFalse_forcesLightPalette() {
        val expectedLightPrimary: Color = FormaTheme.defaultColorScheme().light.primary
        var resolvedPrimary: Color? = null

        composeRule.setContent {
            FormaTheme(dynamicColor = false, darkTheme = false) {
                resolvedPrimary = FormaTheme.colorScheme.primary
            }
        }

        composeRule.runOnIdle {
            assertEquals(
                "darkTheme = false must resolve the light palette",
                expectedLightPrimary,
                resolvedPrimary,
            )
        }
    }

    @Test
    fun defaultTypography_usesPublicSansAndSignatureLabelWeight() {
        // defaultTypography() is @Composable now (Public Sans is a bundled resource font), so
        // capture it inside composition.
        var bodyLargeFamily: FontFamily? = null
        var labelLargeWeight: FontWeight? = null

        composeRule.setContent {
            val typography = FormaTheme.defaultTypography()
            bodyLargeFamily = typography.material.bodyLarge.fontFamily
            labelLargeWeight = typography.material.labelLarge.fontWeight
        }

        composeRule.runOnIdle {
            // M3's default bodyLarge.fontFamily is null; FormaUI sets Public Sans across the scale.
            assertNotNull(
                "default typography should set a brand font family (Public Sans)",
                bodyLargeFamily,
            )
            assertEquals(
                "labelLarge (buttons) should be the signature SemiBold weight",
                FontWeight.SemiBold,
                labelLargeWeight,
            )
        }
    }

    @Test
    fun defaultArgs_resolveBrandLightPalette() {
        // Proves the flipped `dynamicColor` default (true -> false): with no dynamicColor argument,
        // light mode must resolve FormaUI's brand palette, not a Material You dynamic scheme.
        val expectedLightPrimary: Color = FormaTheme.defaultColorScheme().light.primary
        var resolvedPrimary: Color? = null

        composeRule.setContent {
            FormaTheme(darkTheme = false) {
                resolvedPrimary = FormaTheme.colorScheme.primary
            }
        }

        composeRule.runOnIdle {
            assertEquals(
                "with default args, light mode must resolve the brand palette (dynamicColor now defaults false)",
                expectedLightPrimary,
                resolvedPrimary,
            )
        }
    }
}
