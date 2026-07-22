/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/** Provides the ambient [FormaSpacing]; read via [FormaTheme.spacing]. */
internal val LocalFormaSpacing = staticCompositionLocalOf { FormaSpacing() }

/** Provides the ambient [FormaShapes]; read via [FormaTheme.shapes]. */
internal val LocalFormaShapes = staticCompositionLocalOf { FormaShapes() }

/** Provides the ambient [FormaTypography]; read via [FormaTheme.typography]. */
internal val LocalFormaTypography = staticCompositionLocalOf { FormaTypography() }

/**
 * FormaUI's theme wrapper — the FormaUI analogue of
 * [MaterialTheme][androidx.compose.material3.MaterialTheme].
 *
 * `FormaTheme` layers FormaUI's design tokens (color, typography, shapes, spacing) on top of
 * Material 3. Wrap your app (or a subtree) in it, then read tokens through the [FormaTheme]
 * object (e.g. [FormaTheme.spacing], [FormaTheme.typography]`.numeric`) or through the standard
 * `MaterialTheme.*` accessors, which resolve to FormaUI's values.
 *
 * Color resolution, in priority order:
 * 1. If [dynamicColor] is `true` **and** Material You dynamic color is available (Android 12+),
 *    a wallpaper-derived scheme is used — in its light or dark form per [darkTheme].
 * 2. Otherwise the light or dark member of [colorScheme] is chosen per [darkTheme].
 *
 * [darkTheme] drives **both** paths, so an in-app light/dark toggle works even with dynamic color
 * enabled: pass your own boolean (e.g. from a settings switch) to override the system default.
 *
 * On the `wasmJs` target dynamic color is never available, so the static [colorScheme] is always
 * used — the previews on the docs site render FormaUI's warm-editorial brand palette (cream
 * canvas, coral primary, Public Sans with an editorial display scale).
 *
 * Spacing is not a parameter: FormaUI's [FormaSpacing] scale is a fixed 4dp-grid contract shared
 * by all components and is always provided. Retune it, if ever needed, by providing
 * [LocalFormaSpacing] yourself around `FormaTheme`'s content.
 *
 * @param colorScheme the light/dark brand color pair (defaults to [FormaTheme.defaultColorScheme]).
 * @param typography the type scale, including the tabular [numeric][FormaTypography.numeric] style
 *   (defaults to [FormaTheme.defaultTypography]).
 * @param shapes the corner-radius scale (defaults to [FormaTheme.defaultShapes]).
 * @param dynamicColor whether to use Material You dynamic color when available (Android 12+).
 *   Defaults to `false` so FormaUI's brand palette shows out of the box; set `true` to opt into
 *   Material You. Ignored where dynamic color is unsupported.
 * @param darkTheme whether to use the dark palette. Defaults to [isSystemInDarkTheme]; pass an
 *   explicit value to drive an in-app theme switch. Applies to both the static and dynamic paths.
 * @param content the themed UI.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTheme(
    colorScheme: FormaColorScheme = FormaTheme.defaultColorScheme(),
    typography: FormaTypography = FormaTheme.defaultTypography(),
    shapes: FormaShapes = FormaTheme.defaultShapes(),
    dynamicColor: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val dynamicScheme = if (dynamicColor) dynamicColorSchemeOrNull(darkTheme) else null
    val resolvedColorScheme: ColorScheme =
        dynamicScheme ?: if (darkTheme) colorScheme.dark else colorScheme.light

    CompositionLocalProvider(
        LocalFormaSpacing provides FormaSpacing(),
        LocalFormaShapes provides shapes,
        LocalFormaTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = resolvedColorScheme,
            shapes = shapes.material,
            typography = typography.material,
            content = content,
        )
    }
}

/**
 * Accessors for the current FormaUI theme values and the FormaUI default tokens.
 *
 * Mirrors the `MaterialTheme` object convention: read the active tokens inside a [FormaTheme]
 * with [colorScheme], [typography], [shapes], and [spacing]; obtain the FormaUI defaults with
 * [defaultColorScheme], [defaultTypography], and [defaultShapes].
 */
@ExperimentalFormaUiApi
object FormaTheme {
    /**
     * The active Material 3 [ColorScheme] — FormaUI's brand palette, or the Material You dynamic
     * scheme when in effect. Identical to
     * [MaterialTheme.colorScheme][androidx.compose.material3.MaterialTheme.colorScheme].
     */
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /** The active [FormaTypography], including its tabular [numeric][FormaTypography.numeric] style. */
    val typography: FormaTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFormaTypography.current

    /** The active [FormaShapes]. */
    val shapes: FormaShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalFormaShapes.current

    /** The active [FormaSpacing] (4dp-grid scale). */
    val spacing: FormaSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalFormaSpacing.current

    /** FormaUI's default light/dark brand [FormaColorScheme]. */
    fun defaultColorScheme(): FormaColorScheme = FormaColorScheme(
        light = FormaLightColorScheme,
        dark = FormaDarkColorScheme,
    )

    /**
     * FormaUI's default [FormaTypography] — Public Sans with an editorial display scale, plus
     * the tabular [numeric][FormaTypography.numeric] style.
     */
    @Composable
    fun defaultTypography(): FormaTypography = rememberBrandTypography()

    /** FormaUI's default [FormaShapes] corner scale. */
    fun defaultShapes(): FormaShapes = FormaShapes()
}
