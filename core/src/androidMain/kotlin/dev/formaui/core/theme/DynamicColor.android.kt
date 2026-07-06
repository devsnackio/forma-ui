/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Android actual: returns a wallpaper-derived dynamic [ColorScheme] on Android 12+ (API 31+),
 * or `null` on older devices so [FormaTheme] falls back to the static brand palette.
 */
@Composable
internal actual fun dynamicColorSchemeOrNull(darkTheme: Boolean): ColorScheme? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return null
    val context = LocalContext.current
    return if (darkTheme) {
        dynamicDarkColorScheme(context)
    } else {
        dynamicLightColorScheme(context)
    }
}
