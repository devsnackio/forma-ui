/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaTabRow]: both fixed [FormaTabRowVariant]s with text-only [FormaTab]s.
 */
@Preview
@Composable
private fun FormaTabRowVariantsPreview() {
    FormaTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
                var primarySelected by remember { mutableStateOf(0) }
                FormaTabRow(selectedTabIndex = primarySelected, variant = FormaTabRowVariant.Primary) {
                    listOf("Chat", "Calls", "Contacts").forEachIndexed { index, label ->
                        FormaTab(
                            selected = primarySelected == index,
                            onClick = { primarySelected = index },
                            text = { Text(label) },
                        )
                    }
                }

                var secondarySelected by remember { mutableStateOf(1) }
                FormaTabRow(selectedTabIndex = secondarySelected, variant = FormaTabRowVariant.Secondary) {
                    listOf("Overview", "Details", "Reviews").forEachIndexed { index, label ->
                        FormaTab(
                            selected = secondarySelected == index,
                            onClick = { secondarySelected = index },
                            text = { Text(label) },
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview of [FormaTabRow] with `scrollable = true` (icon+text tabs, dispatching to
 * `LeadingIconTab`), an icon-only tab row, and a disabled tab.
 */
@Preview
@Composable
private fun FormaTabRowScrollableAndStatesPreview() {
    FormaTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
                var scrollableSelected by remember { mutableStateOf(0) }
                val destinations = listOf("Home", "Search", "Favorites", "Updates", "Profile", "Settings")
                FormaTabRow(
                    selectedTabIndex = scrollableSelected,
                    variant = FormaTabRowVariant.Primary,
                    scrollable = true,
                ) {
                    destinations.forEachIndexed { index, label ->
                        FormaTab(
                            selected = scrollableSelected == index,
                            onClick = { scrollableSelected = index },
                            text = { Text(label) },
                            icon = { Text("●") },
                        )
                    }
                }

                var iconOnlySelected by remember { mutableStateOf(0) }
                FormaTabRow(selectedTabIndex = iconOnlySelected, variant = FormaTabRowVariant.Secondary) {
                    FormaTab(
                        selected = iconOnlySelected == 0,
                        onClick = { iconOnlySelected = 0 },
                        icon = { Text("🏠") },
                    )
                    FormaTab(
                        selected = iconOnlySelected == 1,
                        onClick = { iconOnlySelected = 1 },
                        icon = { Text("🔍") },
                    )
                    FormaTab(
                        selected = false,
                        onClick = {},
                        text = { Text("Disabled") },
                        enabled = false,
                    )
                }
            }
        }
    }
}
