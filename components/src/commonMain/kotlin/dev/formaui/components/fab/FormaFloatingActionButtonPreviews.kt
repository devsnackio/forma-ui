/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.fab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaFloatingActionButton]: all three sizes, plus a live expand/collapse toggle for
 * [FormaExtendedFloatingActionButton].
 */
@Preview
@Composable
private fun FormaFloatingActionButtonVariantsPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Small) {
                        Text("+")
                    }
                    FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Regular) {
                        Text("+")
                    }
                    FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Large) {
                        Text("+")
                    }
                }

                var expanded by remember { mutableStateOf(true) }
                FormaExtendedFloatingActionButton(
                    text = "Compose",
                    icon = { Text("+") },
                    onClick = { expanded = !expanded },
                    expanded = expanded,
                )
            }
        }
    }
}
