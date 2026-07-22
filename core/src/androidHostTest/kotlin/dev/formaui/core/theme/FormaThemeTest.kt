/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.core.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        var resolvedMdShape: Shape? = null

        composeRule.setContent {
            FormaTheme(dynamicColor = false) {
                resolvedSpacingMd = FormaTheme.spacing.md
                resolvedPrimary = FormaTheme.colorScheme.primary
                resolvedNumeric = FormaTheme.typography.numeric
                resolvedMdShape = FormaTheme.shapes.md

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
                "FormaTheme.shapes.md must resolve inside FormaTheme",
                resolvedMdShape,
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
    fun defaultTypography_usesPublicSansAndMediumLabelWeight() {
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
                "labelLarge (buttons) should be Medium (500), not M3's usual SemiBold signature",
                FontWeight.Medium,
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

    @Test
    fun spacing_resolvesFullFourDpGridScale() {
        // The warm-editorial rebrand renamed/extended the scale (old xs/sm -> xxs/xs, plus a new
        // `section` tier); pin every tier, not just `md`, so a regression on any one is caught.
        var spacing: FormaSpacing? = null

        composeRule.setContent {
            FormaTheme(dynamicColor = false) {
                spacing = FormaTheme.spacing
            }
        }

        composeRule.runOnIdle {
            val resolved = spacing!!
            assertEquals("spacing.xxs should be 4dp", 4.dp, resolved.xxs)
            assertEquals("spacing.xs should be 8dp", 8.dp, resolved.xs)
            assertEquals("spacing.sm should be 12dp", 12.dp, resolved.sm)
            assertEquals("spacing.md should be the 4dp-grid 16dp token", 16.dp, resolved.md)
            assertEquals("spacing.lg should be 24dp", 24.dp, resolved.lg)
            assertEquals("spacing.xl should be 32dp", 32.dp, resolved.xl)
            assertEquals("spacing.xxl should be 48dp", 48.dp, resolved.xxl)
            assertEquals(
                "spacing.section should be the new 96dp section-band token",
                96.dp,
                resolved.section,
            )
        }
    }

    @Test
    fun shapes_resolveExpectedCornerRadii_andPillFullShareCircleShape() {
        // md's corner radius is measured via createOutline (rather than trusting the class's
        // default-arg literal) so a future default-arg change would actually fail this test.
        var mdCornerRadiusDp: Dp? = null
        var pillShape: Shape? = null
        var fullShape: Shape? = null

        composeRule.setContent {
            FormaTheme(dynamicColor = false) {
                val density = LocalDensity.current
                val mdOutline = FormaTheme.shapes.md.createOutline(
                    size = Size(100f, 100f),
                    layoutDirection = LayoutDirection.Ltr,
                    density = density,
                )
                mdCornerRadiusDp = with(density) {
                    (mdOutline as Outline.Rounded).roundRect.topLeftCornerRadius.x.toDp()
                }
                pillShape = FormaTheme.shapes.pill
                fullShape = FormaTheme.shapes.full
            }
        }

        composeRule.runOnIdle {
            assertEquals(
                "shapes.md should resolve to the spec's 8dp standard-control radius",
                8.dp,
                mdCornerRadiusDp,
            )
            assertEquals(
                "pill and full are both fully round and share the same CircleShape token",
                pillShape,
                fullShape,
            )
        }
    }

    @Test
    fun defaultColorScheme_resolvesWarmEditorialBrandHexes() {
        // Direct hex checks (not just self-referential comparisons against defaultColorScheme())
        // for the rebrand's defining tokens, per docs/DESIGN.md: cream canvas, coral primary
        // shared by both schemes, the dark-navy dark surface, and the hairline outline-variant.
        val scheme = FormaTheme.defaultColorScheme()

        assertEquals(
            "light background should be the cream canvas",
            Color(0xFFFAF9F5),
            scheme.light.background,
        )
        assertEquals(
            "light surface should match the cream canvas",
            Color(0xFFFAF9F5),
            scheme.light.surface,
        )
        assertEquals(
            "light primary should be the brand coral",
            Color(0xFFCC785C),
            scheme.light.primary,
        )
        assertEquals(
            "dark primary should be the same brand coral (shared across schemes)",
            Color(0xFFCC785C),
            scheme.dark.primary,
        )
        assertEquals(
            "light outlineVariant should be the hairline tone",
            Color(0xFFE6DFD8),
            scheme.light.outlineVariant,
        )
        assertEquals(
            "dark background should be the dark-navy surface",
            Color(0xFF181715),
            scheme.dark.background,
        )
        assertEquals(
            "dark surface should match the dark-navy surface",
            Color(0xFF181715),
            scheme.dark.surface,
        )
    }

    @Test
    fun defaultTypography_matchesEditorialDisplayAndLabelSmallSpec() {
        // Spot-checks two more docs/DESIGN.md anchors beyond labelLarge's weight: the 64sp
        // editorial display size and labelSmall's 1.5sp uppercase-caption tracking.
        var displayLargeFontSize: TextUnit? = null
        var labelSmallLetterSpacing: TextUnit? = null

        composeRule.setContent {
            val typography = FormaTheme.defaultTypography()
            displayLargeFontSize = typography.material.displayLarge.fontSize
            labelSmallLetterSpacing = typography.material.labelSmall.letterSpacing
        }

        composeRule.runOnIdle {
            assertEquals(
                "displayLarge should be the 64sp editorial display size",
                64.sp,
                displayLargeFontSize,
            )
            assertEquals(
                "labelSmall should carry 1.5sp tracking",
                1.5.sp,
                labelSmallLetterSpacing,
            )
        }
    }
}
