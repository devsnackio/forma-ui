/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.foundation.layout.Row
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
 * Preview of [FormaNavigationRail] with a header, three destinations, and both badge forms.
 */
@Preview
@Composable
private fun FormaNavigationRailPreview() {
    FormaTheme {
        Surface {
            var selected by remember { mutableIntStateOf(0) }
            Row {
                FormaNavigationRail(
                    header = { Text("+") },
                ) {
                    FormaNavigationRailItem(
                        selected = selected == 0,
                        onClick = { selected = 0 },
                        icon = { Text("🏠") },
                        label = "Home",
                    )
                    FormaNavigationRailItem(
                        selected = selected == 1,
                        onClick = { selected = 1 },
                        icon = { Text("🔔") },
                        label = "Alerts",
                        badgeCount = 12,
                    )
                    FormaNavigationRailItem(
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
}
