/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.tooltip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of [FormaTooltip]: [FormaTooltipVariant.Plain] (with and without a caret) and
 * [FormaTooltipVariant.Rich] (with a title, and with a title + action), each shown already
 * visible (`initialIsVisible = true`) since tooltips are normally hidden until a long-press/hover.
 */
@Preview
@Composable
private fun FormaTooltipVariantsPreview() {
    FormaTheme {
        Surface {
            Row(
                modifier = Modifier.padding(FormaTheme.spacing.xl),
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
            ) {
                FormaTooltip(
                    text = "Search",
                    state = rememberTooltipState(initialIsVisible = true),
                ) {
                    Text("🔍")
                }

                FormaTooltip(
                    text = "Search",
                    showCaret = false,
                    state = rememberTooltipState(initialIsVisible = true),
                ) {
                    Text("🔍")
                }

                FormaTooltip(
                    text = "Search across all of your boards and cards.",
                    variant = FormaTooltipVariant.Rich,
                    title = { Text("Search") },
                    state = rememberTooltipState(initialIsVisible = true, isPersistent = true),
                ) {
                    Text("🔍")
                }

                FormaTooltip(
                    text = "This item was moved to Archive. It'll be permanently deleted in 30 days.",
                    variant = FormaTooltipVariant.Rich,
                    title = { Text("Archived") },
                    action = { Text("Undo") },
                    state = rememberTooltipState(initialIsVisible = true, isPersistent = true),
                ) {
                    Text("🗑")
                }
            }
        }
    }
}

/**
 * Preview of [FormaTooltip] ([FormaTooltipVariant.Plain]) with a custom container color, content
 * color, and body text style.
 */
@Preview
@Composable
private fun FormaTooltipCustomPreview() {
    FormaTheme {
        Surface {
            Row(
                modifier = Modifier.padding(FormaTheme.spacing.xl),
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
            ) {
                FormaTooltip(
                    text = "Search",
                    state = rememberTooltipState(initialIsVisible = true),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    textStyle = MaterialTheme.typography.titleSmall,
                ) {
                    Text("🔍")
                }
            }
        }
    }
}
