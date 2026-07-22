/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
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
 * Preview of [FormaCheckbox]: a row-toggleable checkbox (whole row is one a11y target) plus a
 * disabled state.
 */
@Preview
@Composable
private fun FormaCheckboxStatesPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            ) {
                var checked by remember { mutableStateOf(true) }
                Row(
                    modifier = Modifier.toggleable(
                        value = checked,
                        role = Role.Checkbox,
                        onValueChange = { checked = it },
                    ),
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Row owns the toggle → the control itself is display-only.
                    FormaCheckbox(checked = checked, onCheckedChange = null)
                    Text("Enable notifications")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FormaCheckbox(checked = false, onCheckedChange = {}, enabled = false)
                    Text("Unavailable option")
                }
            }
        }
    }
}
