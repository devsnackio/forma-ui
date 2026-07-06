/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaLoadingIndicator]: circular and linear, both indeterminate and determinate.
 */
@Preview
@Composable
private fun FormaLoadingIndicatorPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FormaLoadingIndicator(contentDescription = "Loading")
                    FormaLoadingIndicator(progress = 0.6f, contentDescription = "60 percent loaded")
                    Text("Circular")
                }
                FormaLoadingIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaLoadingIndicatorVariant.Linear,
                    contentDescription = "Loading",
                )
                FormaLoadingIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaLoadingIndicatorVariant.Linear,
                    progress = 0.6f,
                    contentDescription = "60 percent loaded",
                )
            }
        }
    }
}
