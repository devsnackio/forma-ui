/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.emptystate

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.components.button.FormaButton
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaEmptyState]: illustration slot + title + description + an action button.
 */
@Preview
@Composable
private fun FormaEmptyStatePreview() {
    FormaTheme {
        Surface {
            FormaEmptyState(
                title = "No transactions yet",
                description = "Your transactions will appear here once you make your first payment.",
                modifier = Modifier.fillMaxWidth(),
                icon = { Text("🧾") },
                action = {
                    FormaButton(onClick = {}) { Text("Add payment") }
                },
            )
        }
    }
}
