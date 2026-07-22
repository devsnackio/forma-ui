/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
 * Preview of every [FormaButtonVariant] in both enabled and disabled states, inside a
 * [FormaTheme]. Multiplatform `@Preview` — renders in the IDE and compiles for the `wasmJs`
 * docs previews.
 */
@Preview
@Composable
private fun FormaButtonVariantsPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            ) {
                FormaButtonVariant.entries.forEach { variant ->
                    Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs)) {
                        FormaButton(onClick = {}, variant = variant) {
                            Text(variant.name)
                        }
                        FormaButton(onClick = {}, variant = variant, enabled = false) {
                            Text("Disabled")
                        }
                    }
                }
            }
        }
    }
}
