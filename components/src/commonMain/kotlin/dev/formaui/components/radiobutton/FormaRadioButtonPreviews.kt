/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.radiobutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaRadioButton]: a single-choice group with row-level selection semantics.
 */
@Preview
@Composable
private fun FormaRadioButtonGroupPreview() {
    val options = listOf("Standard", "Priority", "Overnight")
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md).selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            ) {
                var selected by remember { mutableStateOf(options.first()) }
                options.forEach { option ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = option == selected,
                            role = Role.RadioButton,
                            onClick = { selected = option },
                        ),
                        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Row owns the selection → the control itself is display-only.
                        FormaRadioButton(selected = option == selected, onClick = null)
                        Text(option)
                    }
                }
            }
        }
    }
}
