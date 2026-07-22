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
 * FormaUI ships a **warm editorial** default (tinted cream canvas, coral primary, warm ink text,
 * dark-navy dark scheme) via [FormaTheme.defaultColorScheme]. Override by constructing your own
 * pair — the simplest path is `lightColorScheme(...)` / `darkColorScheme(...)` with your brand
 * roles.
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
// Warm editorial: a tinted cream canvas, a warm coral primary, a teal secondary, and an amber
// tertiary accent, paired with warm-ink (not pure black) text. Shared across both schemes so the
// dark scheme reads as "the same brand at night," not a desaturated inversion.
//
// Deliberate deviations from stock Material 3 neutrals — every surface/outline/inverse role below
// is spec'd explicitly (docs/DESIGN.md) so no default M3 gray leaks in:
//
// 1. White-on-coral (`onPrimary` #FFFFFF on `primary` #CC785C) is ~3.3:1 contrast — below AA for
//    small text. This is brand-mandated (docs/DESIGN.md `button-primary`); mitigated by using it
//    only at 14sp/500 label weight, never for long-form body text.
// 2. `onSecondary`/`onTertiary` are warm ink (#141413), not white — white fails contrast on the
//    teal secondary and amber tertiary containers.
// 3. The spec's hairline (#E6DFD8) maps to [outlineVariant] (dividers, hairline card borders)
//    while the spec's muted (#6C6A64) takes [outline] (control borders, input outlines) — the
//    hairline tone is too close to the surface fills to read as a border on its own.
// 4. Spec tokens NOT mapped to a role here: `body`/`body-strong` text tiers (no M3 slot — use
//    `onSurface` at typography weight instead), `hairline-soft`, `primary-disabled` (M3 derives
//    disabled state from `onSurface` alpha, not a fixed color), `primary-active` (press state is
//    M3's ripple/state-layer, not a static color — its hex `#A9583E` is reused below as the dark
//    scheme's `inversePrimary`), and `success`/`warning` (no M3 color role exists for these;
//    FormaUI deliberately ships no extended-color API in this pass).

private val Coral = Color(0xFFCC785C)
private val Teal = Color(0xFF5DB8A6)
private val Amber = Color(0xFFE8A55A)
private val Ink = Color(0xFF141413)
private val Canvas = Color(0xFFFAF9F5)
private val SurfaceDark = Color(0xFF181715)
private val HairlineTone = Color(0xFFE6DFD8)
private val MutedTone = Color(0xFF6C6A64)

/** FormaUI's default **light** brand [ColorScheme] — cream canvas, coral primary. */
internal val FormaLightColorScheme: ColorScheme = lightColorScheme(
    primary = Coral,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF3DDD1),
    onPrimaryContainer = Color(0xFF5C2E1C),
    inversePrimary = Color(0xFFE5A184),
    secondary = Teal,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFD9ECE6),
    onSecondaryContainer = Color(0xFF1F4D42),
    tertiary = Amber,
    onTertiary = Ink,
    tertiaryContainer = Color(0xFFF7E5CD),
    onTertiaryContainer = Color(0xFF5C3D12),
    error = Color(0xFFC64545),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF7D9D5),
    onErrorContainer = Color(0xFF6E1F1F),
    background = Canvas,
    onBackground = Ink,
    surface = Canvas,
    onSurface = Ink,
    surfaceVariant = Color(0xFFEFE9DE),
    onSurfaceVariant = MutedTone,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F0E8),
    surfaceContainer = Color(0xFFEFE9DE),
    surfaceContainerHigh = Color(0xFFE8E0D2),
    surfaceContainerHighest = Color(0xFFE0D7C6),
    surfaceBright = Canvas,
    surfaceDim = Color(0xFFDAD5C9),
    surfaceTint = Coral,
    outline = MutedTone,
    outlineVariant = HairlineTone,
    inverseSurface = Color(0xFF252320),
    inverseOnSurface = Canvas,
    scrim = Ink,
)

/** FormaUI's default **dark** brand [ColorScheme] — dark-navy canvas, coral primary. */
internal val FormaDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Coral,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF6E3A26),
    onPrimaryContainer = Color(0xFFF6DDD2),
    inversePrimary = Color(0xFFA9583E),
    secondary = Teal,
    onSecondary = Ink,
    secondaryContainer = Color(0xFF234F45),
    onSecondaryContainer = Color(0xFFCFE8E0),
    tertiary = Amber,
    onTertiary = Ink,
    tertiaryContainer = Color(0xFF6A4B21),
    onTertiaryContainer = Color(0xFFF7E5CD),
    error = Color(0xFFE0837A),
    onError = Color(0xFF4A1512),
    errorContainer = Color(0xFF5C221E),
    onErrorContainer = Color(0xFFF7D9D5),
    background = SurfaceDark,
    onBackground = Canvas,
    surface = SurfaceDark,
    onSurface = Canvas,
    surfaceVariant = Color(0xFF2C2925),
    onSurfaceVariant = Color(0xFFA09D96),
    surfaceContainerLowest = Color(0xFF100F0E),
    surfaceContainerLow = Color(0xFF1F1E1B),
    surfaceContainer = Color(0xFF252320),
    surfaceContainerHigh = Color(0xFF2C2925),
    surfaceContainerHighest = Color(0xFF363330),
    surfaceBright = Color(0xFF393632),
    surfaceDim = Color(0xFF121110),
    surfaceTint = Coral,
    outline = Color(0xFF8E8B82),
    outlineVariant = Color(0xFF393632),
    inverseSurface = Canvas,
    inverseOnSurface = Ink,
    scrim = Color(0xFF000000),
)
