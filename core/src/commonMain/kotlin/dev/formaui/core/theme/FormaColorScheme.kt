/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI color scheme: a paired light and dark [ColorScheme].
 *
 * [FormaTheme] selects between [light] and [dark] using the system dark-theme setting
 * ([isSystemInDarkTheme][androidx.compose.foundation.isSystemInDarkTheme]). When Material You
 * dynamic color is enabled and available (Android 12+), a wallpaper-derived scheme takes
 * precedence over this pair; on older Android and on non-Android targets (`wasmJs`) this pair is
 * always the source of color.
 *
 * FormaUI ships a professional, fintech-adjacent default (deep blue primary, teal secondary,
 * violet accent) via [FormaTheme.defaultColorScheme]. Override by constructing your own pair —
 * the simplest path is `lightColorScheme(...)` / `darkColorScheme(...)` with your brand roles.
 *
 * @property light the [ColorScheme] used in light mode.
 * @property dark the [ColorScheme] used in dark mode.
 */
@ExperimentalFormaUiApi
@Immutable
class FormaColorScheme(
    val light: ColorScheme,
    val dark: ColorScheme,
)

// --- FormaUI default brand palette ---------------------------------------------------------
// Fintech-adjacent: a confident blue primary, a teal secondary that reads as "trust/positive",
// and a violet tertiary accent. Neutral surfaces are left to Material 3's sensible defaults.

private val BrandBlue = Color(0xFF1256D8)
private val BrandTeal = Color(0xFF0F8A82)
private val BrandViolet = Color(0xFF6D46C7)

/** FormaUI's default **light** brand [ColorScheme]. */
internal val FormaLightColorScheme: ColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD9E2FF),
    onPrimaryContainer = Color(0xFF001849),
    secondary = BrandTeal,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFA7F2EA),
    onSecondaryContainer = Color(0xFF00201E),
    tertiary = BrandViolet,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEADDFF),
    onTertiaryContainer = Color(0xFF23005C),
)

/** FormaUI's default **dark** brand [ColorScheme]. */
internal val FormaDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFAFC6FF),
    onPrimary = Color(0xFF002A78),
    primaryContainer = Color(0xFF003DA8),
    onPrimaryContainer = Color(0xFFD9E2FF),
    secondary = Color(0xFF88D6CE),
    onSecondary = Color(0xFF003733),
    secondaryContainer = Color(0xFF00504A),
    onSecondaryContainer = Color(0xFFA7F2EA),
    tertiary = Color(0xFFD3BBFF),
    onTertiary = Color(0xFF3B0F8F),
    tertiaryContainer = Color(0xFF542CAE),
    onTertiaryContainer = Color(0xFFEADDFF),
)
