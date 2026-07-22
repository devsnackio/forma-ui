/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.generated.resources.Res
import dev.formaui.core.generated.resources.public_sans_bold
import dev.formaui.core.generated.resources.public_sans_medium
import dev.formaui.core.generated.resources.public_sans_regular
import dev.formaui.core.generated.resources.public_sans_semibold
import org.jetbrains.compose.resources.Font

/**
 * OpenType feature settings that enable **tabular** (monospaced) and **lining** figures.
 *
 * `tnum` makes every digit occupy the same advance width so numbers align in columns and don't
 * shift as their value changes — essential for balances, prices, and data tables. `lnum` selects
 * lining (uniform-height) figures. See the OpenType feature registry for `tnum`/`lnum`.
 */
private const val TabularFigureFeatures = "tnum, lnum"

/** Base Material 3 type scale FormaUI extends. */
private val BaseMaterialTypography = Typography()

/**
 * FormaUI type scale — wraps Material 3's [Typography] and adds FormaUI-specific styles.
 *
 * The full M3 scale (`displayLarge` … `labelSmall`) is available through [material], which
 * [FormaTheme] hands to [MaterialTheme][androidx.compose.material3.MaterialTheme]. FormaUI's
 * default ([FormaTheme.defaultTypography]) sets **Public Sans** across an editorial display
 * scale — display/headline styles at weight 400 with negative tracking (see
 * [rememberBrandTypography]); on top of that it adds:
 *
 * - [numeric] — a body-sized style with **tabular figures** (`tnum`) so digits stay column-aligned.
 *   This is the style to use for balances, prices, quantities, and any data/numeric display.
 *   Read it via [FormaTheme.typography]`.numeric`.
 *
 * To customise, pass your own [material] scale and/or [numeric] style. Deriving `numeric` from a
 * custom base is a one-liner: `myBody.copy(fontFeatureSettings = "tnum, lnum")`.
 *
 * @property material the underlying Material 3 [Typography] scale.
 * @property numeric tabular-figure style for numeric/financial display.
 */
@ExperimentalFormaUiApi
@Immutable
class FormaTypography(
    val material: Typography = BaseMaterialTypography,
    val numeric: TextStyle =
        BaseMaterialTypography.bodyLarge.copy(fontFeatureSettings = TabularFigureFeatures),
)

/**
 * The **Public Sans** font family (regular / medium / semibold / bold), loaded from the fonts
 * bundled in `:core`. Public Sans is an open, OFL-licensed typeface; the license ships alongside
 * the font files. Remembered so the same [FontFamily] instance is reused across recompositions.
 */
@Composable
internal fun rememberPublicSansFamily(): FontFamily {
    val regular = Font(Res.font.public_sans_regular, weight = FontWeight.Normal)
    val medium = Font(Res.font.public_sans_medium, weight = FontWeight.Medium)
    val semiBold = Font(Res.font.public_sans_semibold, weight = FontWeight.SemiBold)
    val bold = Font(Res.font.public_sans_bold, weight = FontWeight.Bold)
    return remember(regular, medium, semiBold, bold) {
        FontFamily(regular, medium, semiBold, bold)
    }
}

/**
 * FormaUI's default typography: an editorial type scale in **Public Sans**, plus the tabular
 * [numeric][FormaTypography.numeric] style. Display and headline styles stay at weight 400 (never
 * bold) with negative letter-spacing — the scale's defining move — while titles and labels step
 * up to Medium (500) for a firmer, more confident hierarchy. `labelLarge` (buttons) is Medium, not
 * M3's usual SemiBold signature, matching the spec's "labels weight 500."
 */
@Composable
internal fun rememberBrandTypography(): FormaTypography {
    val family = rememberPublicSansFamily()
    return remember(family) {
        val material = brandTypography(family)
        FormaTypography(
            material = material,
            numeric = material.bodyLarge.copy(fontFeatureSettings = TabularFigureFeatures),
        )
    }
}

/** Builds FormaUI's editorial [Typography] scale in [family]. */
private fun brandTypography(family: FontFamily): Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp,
        lineHeight = 67.sp,
        letterSpacing = (-1.5).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 53.sp,
        letterSpacing = (-1.0).sp,
    ),
    displaySmall = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 41.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 29.sp,
        letterSpacing = (-0.25).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = (-0.2).sp,
    ),
    titleLarge = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 29.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp,
    ),
    // Medium (500), not M3's usual SemiBold — the spec's labels are Medium weight.
    labelLarge = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        letterSpacing = 1.5.sp,
    ),
)
