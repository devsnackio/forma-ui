/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * FormaUI spacing scale — the single source of layout rhythm for every component.
 *
 * All values sit on a **4dp grid**, which keeps padding, gaps, and insets visually
 * consistent across the library. Components in `:components` must space themselves using
 * these tokens (read via [FormaTheme.spacing]) rather than hardcoded `dp` values.
 *
 * Default scale:
 *
 * | Token | Value | Typical use                                   |
 * |-------|-------|-----------------------------------------------|
 * | [xs]  | 4dp   | icon-to-label gaps, tight inset               |
 * | [sm]  | 8dp   | compact padding, chip/badge internal spacing  |
 * | [md]  | 16dp  | default component padding, list item padding  |
 * | [lg]  | 24dp  | section spacing, dialog padding               |
 * | [xl]  | 32dp  | screen margins, large separations             |
 *
 * Every value is overridable; construct a custom [FormaSpacing] to retune the rhythm.
 *
 * @property xs extra-small spacing (default 4dp).
 * @property sm small spacing (default 8dp).
 * @property md medium spacing (default 16dp) — the most common component padding.
 * @property lg large spacing (default 24dp).
 * @property xl extra-large spacing (default 32dp).
 */
@ExperimentalFormaUiApi
@Immutable
class FormaSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)
