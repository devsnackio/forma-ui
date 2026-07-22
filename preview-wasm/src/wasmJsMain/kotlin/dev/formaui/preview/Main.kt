/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.browser.document

/**
 * Browser entry point for the Wasm preview harness. Renders the real [FormaButton] (every variant)
 * inside [FormaTheme], with a light/dark toggle — the smallest end-to-end proof that a live,
 * interactive FormaUI component can run in the browser via the `wasmJs` target.
 */
fun main() {
    ComposeViewport(document.body!!) {
        ButtonPreview()
    }
}

@Composable
private fun ButtonPreview() {
    var dark by remember { mutableStateOf(false) }
    var clicks by remember { mutableIntStateOf(0) }

    FormaTheme(darkTheme = dark) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
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

                Text("FormaButton", style = MaterialTheme.typography.headlineSmall)

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                ) {
                    FormaButtonVariant.entries.forEach { variant ->
                        FormaButton(onClick = { clicks++ }, variant = variant) {
                            Text(variant.name)
                        }
                    }
                }

                Text(
                    text = "Clicked $clicks times — this is the real component running in your browser.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
