/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Shared chrome around every component preview: an in-canvas light/dark toggle (a real
 * [FormaSwitch] driving `FormaTheme(darkTheme = …)`, so both themes are demonstrated inside the
 * preview itself — PRD §8.4), the component name heading, then the interactive preview [content].
 */
@Composable
internal fun PreviewScaffold(title: String, content: @Composable ColumnScope.() -> Unit) {
    var dark by remember { mutableStateOf(false) }

    FormaTheme(darkTheme = dark) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(FormaTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FormaSwitch(checked = dark, onCheckedChange = { dark = it })
                    Text(
                        text = if (dark) "Dark theme" else "Light theme",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                Text(title, style = MaterialTheme.typography.headlineSmall)

                content()
            }
        }
    }
}

/**
 * Full-canvas message rendered when the `?component=<id>` query parameter names no known preview.
 */
@Composable
internal fun UnknownComponentMessage(id: String) {
    FormaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            ) {
                Text("Unknown component: $id", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Pass ?component=<id> using a kebab-case id from docs/component-inventory.json (e.g. button, icon-button, bar-chart).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
