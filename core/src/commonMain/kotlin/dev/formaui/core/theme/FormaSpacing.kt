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
 * | Token      | Value | Typical use                                   |
 * |------------|-------|-----------------------------------------------|
 * | [xxs]      | 4dp   | icon-to-label gaps                            |
 * | [xs]       | 8dp   | compact padding                               |
 * | [sm]       | 12dp  | intermediate padding                          |
 * | [md]       | 16dp  | default component padding                    |
 * | [lg]       | 24dp  | section spacing, dialog padding               |
 * | [xl]       | 32dp  | screen margins                                |
 * | [xxl]      | 48dp  | callout/CTA padding                           |
 * | [section]  | 96dp  | section-band rhythm                           |
 *
 * Every value is overridable; construct a custom [FormaSpacing] to retune the rhythm.
 *
 * @property xxs extra-extra-small spacing (default 4dp) — icon-to-label gaps.
 * @property xs extra-small spacing (default 8dp) — compact padding.
 * @property sm small spacing (default 12dp) — intermediate padding.
 * @property md medium spacing (default 16dp) — the most common component padding.
 * @property lg large spacing (default 24dp) — section spacing, dialog padding.
 * @property xl extra-large spacing (default 32dp) — screen margins.
 * @property xxl extra-extra-large spacing (default 48dp) — callout/CTA padding.
 * @property section section-band spacing (default 96dp) — section-band rhythm.
 */
@ExperimentalFormaUiApi
@Immutable
class FormaSpacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val sm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
    val section: Dp = 96.dp,
)
