/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.divider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaDivider]: horizontal rule between stacked text, and a vertical rule between
 * side-by-side text.
 */
@Preview
@Composable
private fun FormaDividerPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                Text("Above")
                FormaDivider()
                Text("Below")

                Row(
                    modifier = Modifier.height(FormaTheme.spacing.xl),
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                ) {
                    Text("Left")
                    FormaDivider(orientation = FormaDividerOrientation.Vertical)
                    Text("Right")
                }
            }
        }
    }
}
