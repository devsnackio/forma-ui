/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaModalNavigationDrawer] opened, showing selected/unselected items and a badge.
 */
@Preview
@Composable
private fun FormaModalNavigationDrawerPreview() {
    FormaTheme {
        Surface {
            val drawerState = rememberDrawerState(DrawerValue.Open)
            var selected by remember { mutableIntStateOf(0) }

            FormaModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    FormaNavigationDrawerItem(
                        label = "Inbox",
                        selected = selected == 0,
                        onClick = { selected = 0 },
                        icon = { Text("📥") },
                        badge = "24",
                    )
                    FormaNavigationDrawerItem(
                        label = "Sent",
                        selected = selected == 1,
                        onClick = { selected = 1 },
                        icon = { Text("📤") },
                    )
                    FormaNavigationDrawerItem(
                        label = "Trash",
                        selected = selected == 2,
                        onClick = { selected = 2 },
                        icon = { Text("🗑") },
                    )
                },
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Text("Mail content behind the drawer")
                }
            }
        }
    }
}
