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
 * default ([FormaTheme.defaultTypography]) sets **Public Sans** as the family across the whole
 * scale (a distinctive, brand-defining typeface — see [rememberBrandTypography]); on top of that
 * it adds:
 *
 * - [numeric] — a body-sized style with **tabular figures** (`tnum`) so digits stay column-aligned.
 *   This is the style to use for balances, prices, quantities, and any financial/data display,
 *   reflecting FormaUI's fintech lineage. Read it via [FormaTheme.typography]`.numeric`.
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
 * FormaUI's default typography: the Material 3 scale re-set in **Public Sans**, plus the tabular
 * [numeric][FormaTypography.numeric] style. Buttons/other `labelLarge` text is bumped to
 * **SemiBold** — FormaUI's signature, more confident label weight (M3 defaults to Medium).
 */
@Composable
internal fun rememberBrandTypography(): FormaTypography {
    val family = rememberPublicSansFamily()
    return remember(family) {
        FormaTypography(
            material = BaseMaterialTypography.withBrandFont(family),
            numeric = BaseMaterialTypography.bodyLarge.copy(
                fontFamily = family,
                fontFeatureSettings = TabularFigureFeatures,
            ),
        )
    }
}

/** Re-sets every style in this [Typography] to [family]; `labelLarge` also gains SemiBold weight. */
private fun Typography.withBrandFont(family: FontFamily): Typography = copy(
    displayLarge = displayLarge.copy(fontFamily = family),
    displayMedium = displayMedium.copy(fontFamily = family),
    displaySmall = displaySmall.copy(fontFamily = family),
    headlineLarge = headlineLarge.copy(fontFamily = family),
    headlineMedium = headlineMedium.copy(fontFamily = family),
    headlineSmall = headlineSmall.copy(fontFamily = family),
    titleLarge = titleLarge.copy(fontFamily = family),
    titleMedium = titleMedium.copy(fontFamily = family),
    titleSmall = titleSmall.copy(fontFamily = family),
    bodyLarge = bodyLarge.copy(fontFamily = family),
    bodyMedium = bodyMedium.copy(fontFamily = family),
    bodySmall = bodySmall.copy(fontFamily = family),
    // Signature: buttons (labelLarge) read in a more confident SemiBold rather than M3's Medium.
    labelLarge = labelLarge.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
    labelMedium = labelMedium.copy(fontFamily = family),
    labelSmall = labelSmall.copy(fontFamily = family),
)
