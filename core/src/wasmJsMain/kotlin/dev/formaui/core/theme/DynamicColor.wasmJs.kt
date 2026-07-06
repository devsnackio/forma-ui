/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * `wasmJs` actual: Material You dynamic color is an Android-only OS feature, so the web target
 * always returns `null` and [FormaTheme] uses the static brand palette.
 */
@Composable
internal actual fun dynamicColorSchemeOrNull(darkTheme: Boolean): ColorScheme? = null
