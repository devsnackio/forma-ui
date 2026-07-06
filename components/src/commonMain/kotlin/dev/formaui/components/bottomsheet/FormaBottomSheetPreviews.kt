/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.bottomsheet

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
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of the content a [FormaBottomSheet] hosts.
 *
 * `ModalBottomSheet` renders inside its own window and does not display in a static `@Preview`, so
 * this shows the sheet's typical content laid out with FormaUI spacing. See [FormaBottomSheet]'s
 * KDoc for live usage.
 */
@Preview
@Composable
private fun FormaBottomSheetContentPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                Text("Payment options", style = MaterialTheme.typography.titleMedium)
                Text("Choose how you'd like to pay.")
                FormaButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Continue") }
            }
        }
    }
}
