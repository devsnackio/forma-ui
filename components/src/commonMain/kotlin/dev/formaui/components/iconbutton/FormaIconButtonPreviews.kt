/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.iconbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaIconButton]: all four variants, plus a disabled state.
 */
@Preview
@Composable
private fun FormaIconButtonVariantsPreview() {
    FormaTheme {
        Surface {
            Row(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            ) {
                FormaIconButton(onClick = {}, variant = FormaIconButtonVariant.Standard) {
                    Text("★")
                }
                FormaIconButton(onClick = {}, variant = FormaIconButtonVariant.Filled) {
                    Text("★")
                }
                FormaIconButton(onClick = {}, variant = FormaIconButtonVariant.Tonal) {
                    Text("★")
                }
                FormaIconButton(onClick = {}, variant = FormaIconButtonVariant.Outlined) {
                    Text("★")
                }
                FormaIconButton(onClick = {}, enabled = false) {
                    Text("★")
                }
            }
        }
    }
}
