/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaNavigationBar] with three destinations, including a numeric and a dot badge.
 */
@Preview
@Composable
private fun FormaNavigationBarPreview() {
    FormaTheme {
        Surface {
            var selected by remember { mutableIntStateOf(0) }
            FormaNavigationBar {
                FormaNavigationBarItem(
                    selected = selected == 0,
                    onClick = { selected = 0 },
                    icon = { Text("🏠") },
                    label = "Home",
                )
                FormaNavigationBarItem(
                    selected = selected == 1,
                    onClick = { selected = 1 },
                    icon = { Text("🔔") },
                    label = "Alerts",
                    badgeCount = 3,
                )
                FormaNavigationBarItem(
                    selected = selected == 2,
                    onClick = { selected = 2 },
                    icon = { Text("👤") },
                    label = "Profile",
                    showBadgeDot = true,
                )
            }
        }
    }
}

/**
 * Preview of [FormaNavigationBar] with custom colors: a tinted [FormaNavigationBar.containerColor]
 * and a custom `selectedTextColor` / `selectedIconColor` / `indicatorColor` on each item.
 */
@Preview
@Composable
private fun FormaNavigationBarCustomColorsPreview() {
    FormaTheme {
        Surface {
            var selected by remember { mutableIntStateOf(0) }
            val itemColors = NavigationBarItemDefaults.colors(
                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                selectedIconColor = MaterialTheme.colorScheme.tertiary,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
            )
            FormaNavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                FormaNavigationBarItem(
                    selected = selected == 0,
                    onClick = { selected = 0 },
                    icon = { Text("🏠") },
                    label = "Home",
                    colors = itemColors,
                )
                FormaNavigationBarItem(
                    selected = selected == 1,
                    onClick = { selected = 1 },
                    icon = { Text("🔔") },
                    label = "Alerts",
                    badgeCount = 3,
                    colors = itemColors,
                )
                FormaNavigationBarItem(
                    selected = selected == 2,
                    onClick = { selected = 2 },
                    icon = { Text("👤") },
                    label = "Profile",
                    colors = itemColors,
                )
            }
        }
    }
}
