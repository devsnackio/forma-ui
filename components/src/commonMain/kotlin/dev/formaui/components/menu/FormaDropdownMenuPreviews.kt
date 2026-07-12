/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaDropdownMenu] expanded, with a leading-icon item, a trailing-shortcut item, and
 * a disabled item.
 */
@Preview
@Composable
private fun FormaDropdownMenuPreview() {
    FormaTheme {
        Surface {
            Box(modifier = Modifier.padding(FormaTheme.spacing.xl)) {
                Text("⋮")
                FormaDropdownMenu(expanded = true, onDismissRequest = {}) {
                    FormaDropdownMenuItem(
                        text = "Share",
                        onClick = {},
                        leadingIcon = { Text("↗") },
                    )
                    FormaDropdownMenuItem(
                        text = "Rename",
                        onClick = {},
                        leadingIcon = { Text("✎") },
                    )
                    FormaDropdownMenuItem(
                        text = "Delete",
                        onClick = {},
                        leadingIcon = { Text("🗑") },
                        trailingIcon = { Text("⌘⌫") },
                    )
                    FormaDropdownMenuItem(
                        text = "Archive",
                        onClick = {},
                        enabled = false,
                    )
                }
            }
        }
    }
}
