/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chip

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
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaChip]: all four variants, including live selection for the selectable ones.
 */
@Preview
@Composable
private fun FormaChipVariantsPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
            ) {
                var filterSelected by remember { mutableStateOf(true) }
                var inputSelected by remember { mutableStateOf(false) }

                Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
                    FormaChip(label = "Assist", onClick = {}, variant = FormaChipVariant.Assist)
                    FormaChip(label = "Suggestion", onClick = {}, variant = FormaChipVariant.Suggestion)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
                    FormaChip(
                        label = "Filter",
                        onClick = { filterSelected = !filterSelected },
                        variant = FormaChipVariant.Filter,
                        selected = filterSelected,
                        leadingIcon = if (filterSelected) {
                            { Text("✓") }
                        } else {
                            null
                        },
                    )
                    FormaChip(
                        label = "Input",
                        onClick = { inputSelected = !inputSelected },
                        variant = FormaChipVariant.Input,
                        selected = inputSelected,
                    )
                    FormaChip(label = "Disabled", onClick = {}, variant = FormaChipVariant.Assist, enabled = false)
                }
            }
        }
    }
}
