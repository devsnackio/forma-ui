/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Returns a Material You **dynamic** [ColorScheme] derived from the OS wallpaper, or `null` when
 * dynamic color is unavailable.
 *
 * Dynamic color is an Android-only OS feature (Android 12 / API 31+). This `expect` declaration
 * lets `commonMain` request it without depending on Android APIs:
 * - **Android 12+** (`androidMain`): returns the wallpaper-derived light or dark scheme.
 * - **Older Android**: returns `null`.
 * - **`wasmJs` / any non-Android target**: always returns `null`.
 *
 * [FormaTheme] uses `null` as the signal to fall back to its static brand palette.
 *
 * @param darkTheme whether to request the dark variant of the dynamic scheme.
 */
@Composable
internal expect fun dynamicColorSchemeOrNull(darkTheme: Boolean): ColorScheme?
