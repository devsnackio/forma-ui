/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of [FormaSearchBar] with [FormaSearchBarVariant.Docked]: collapsed, and expanded with a
 * few result rows.
 */
@Preview
@Composable
private fun FormaSearchBarDockedPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
            ) {
                Text("Collapsed", style = MaterialTheme.typography.labelMedium)
                FormaSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaSearchBarVariant.Docked,
                    placeholder = { Text("Search contacts") },
                    leadingIcon = { Text("🔍") },
                )

                Text("Expanded, with results", style = MaterialTheme.typography.labelMedium)
                FormaSearchBar(
                    query = "Ada",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = true,
                    onExpandedChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaSearchBarVariant.Docked,
                    placeholder = { Text("Search contacts") },
                    leadingIcon = { Text("🔍") },
                    trailingIcon = { Text("✕") },
                ) {
                    listOf("Ada Lovelace", "Ada Byron", "Adam Smith").forEach { name ->
                        Text(
                            name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = FormaTheme.spacing.md,
                                    vertical = FormaTheme.spacing.sm,
                                ),
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview of [FormaSearchBar] with [FormaSearchBarVariant.FullScreen], collapsed and expanded.
 * Each state gets its own preview surface since the expanded state grows to fill all available
 * space, per Material 3's full-screen search behavior.
 */
@Preview
@Composable
private fun FormaSearchBarFullScreenCollapsedPreview() {
    FormaTheme {
        Surface {
            Column(modifier = Modifier.padding(FormaTheme.spacing.md)) {
                FormaSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaSearchBarVariant.FullScreen,
                    placeholder = { Text("Search") },
                    leadingIcon = { Text("🔍") },
                )
            }
        }
    }
}

@Preview
@Composable
private fun FormaSearchBarFullScreenExpandedPreview() {
    FormaTheme {
        Surface {
            FormaSearchBar(
                query = "coffee",
                onQueryChange = {},
                onSearch = {},
                expanded = true,
                onExpandedChange = {},
                modifier = Modifier.fillMaxWidth(),
                variant = FormaSearchBarVariant.FullScreen,
                placeholder = { Text("Search") },
                leadingIcon = { Text("🔍") },
                trailingIcon = { Text("✕") },
            ) {
                listOf("Coffee shops nearby", "Coffee beans", "Coffee grinder").forEach { result ->
                    Text(
                        result,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = FormaTheme.spacing.md,
                                vertical = FormaTheme.spacing.sm,
                            ),
                    )
                }
            }
        }
    }
}
