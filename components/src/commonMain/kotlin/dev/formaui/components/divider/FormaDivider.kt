/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.divider

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The direction a [FormaDivider] runs.
 */
@ExperimentalFormaUiApi
enum class FormaDividerOrientation {
    /** A full-width horizontal rule separating stacked content. */
    Horizontal,

    /** A full-height vertical rule separating side-by-side content. */
    Vertical,
}

/**
 * A FormaUI divider — a thin rule for separating content, delegating to Material 3's
 * `HorizontalDivider` / `VerticalDivider`.
 *
 * Purely decorative: a divider carries no semantics and does not need a content description.
 *
 * @param modifier the [Modifier] applied to the divider.
 * @param orientation whether the rule runs horizontally or vertically (defaults to
 *   [FormaDividerOrientation.Horizontal]).
 * @param thickness the stroke thickness (defaults to Material 3's hairline `DividerDefaults.Thickness`).
 * @param color the stroke color (defaults to Material 3's `DividerDefaults.color`).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaDivider(
    modifier: Modifier = Modifier,
    orientation: FormaDividerOrientation = FormaDividerOrientation.Horizontal,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) {
    when (orientation) {
        FormaDividerOrientation.Horizontal -> HorizontalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color,
        )

        FormaDividerOrientation.Vertical -> VerticalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color,
        )
    }
}
