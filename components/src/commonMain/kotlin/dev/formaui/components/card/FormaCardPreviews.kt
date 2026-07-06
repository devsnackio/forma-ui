/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaCard]: all three variants using the header / content / footer slot API.
 */
@Preview
@Composable
private fun FormaCardVariantsPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                FormaCardVariant.entries.forEach { variant ->
                    FormaCard(
                        modifier = Modifier.fillMaxWidth(),
                        variant = variant,
                        header = {
                            Text(
                                text = "${variant.name} card",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        footer = {
                            FormaButton(
                                onClick = {},
                                variant = FormaButtonVariant.Text,
                            ) {
                                Text("View details")
                            }
                        },
                    ) {
                        Text(
                            text = "Balance",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "$1,240.00",
                            style = FormaTheme.typography.numeric,
                        )
                    }
                }
            }
        }
    }
}
