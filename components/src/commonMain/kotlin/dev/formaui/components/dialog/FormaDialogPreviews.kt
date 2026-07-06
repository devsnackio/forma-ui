/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaAlertDialog] with a title, body, confirm, and dismiss actions.
 */
@Preview
@Composable
private fun FormaAlertDialogPreview() {
    FormaTheme {
        FormaAlertDialog(
            onDismissRequest = {},
            title = "Delete payment?",
            text = "This action can't be undone.",
            confirmButton = {
                FormaButton(onClick = {}) { Text("Delete") }
            },
            dismissButton = {
                FormaButton(onClick = {}, variant = FormaButtonVariant.Text) { Text("Cancel") }
            },
        )
    }
}

/**
 * Preview of [FormaFullScreenDialog] with a Close action, a title, a Save action, and body content.
 */
@Preview
@Composable
private fun FormaFullScreenDialogPreview() {
    FormaTheme {
        FormaFullScreenDialog(
            onDismissRequest = {},
            title = "New payment",
            confirmAction = {
                FormaButton(onClick = {}, variant = FormaButtonVariant.Text) { Text("Save") }
            },
        ) {
            Text("Full-screen dialog body content goes here.")
        }
    }
}
