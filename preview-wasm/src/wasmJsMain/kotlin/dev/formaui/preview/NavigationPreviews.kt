/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.formaui.components.bottomappbar.FormaBottomAppBar
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.chip.FormaChip
import dev.formaui.components.chip.FormaChipVariant
import dev.formaui.components.fab.FormaFloatingActionButton
import dev.formaui.components.iconbutton.FormaIconButton
import dev.formaui.components.navigation.FormaModalNavigationDrawer
import dev.formaui.components.navigation.FormaNavigationBar
import dev.formaui.components.navigation.FormaNavigationBarItem
import dev.formaui.components.navigation.FormaNavigationDrawerItem
import dev.formaui.components.navigation.FormaNavigationRail
import dev.formaui.components.navigation.FormaNavigationRailItem
import dev.formaui.components.tabs.FormaTab
import dev.formaui.components.tabs.FormaTabRow
import dev.formaui.components.tabs.FormaTabRowVariant
import dev.formaui.components.topappbar.FormaTopAppBar
import dev.formaui.components.topappbar.FormaTopAppBarVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.launch

/** Live preview for `navigation-bar`: three selectable destinations with numeric and dot badges. */
@Composable
internal fun ColumnScope.NavigationBarPreview() {
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

    Text(
        text = "Destination ${selected + 1} of 3 selected.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `navigation-rail`: a rail with a header slot and badged, selectable items. */
@Composable
internal fun ColumnScope.NavigationRailPreview() {
    var selected by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
    ) {
        FormaNavigationRail(header = { Text("+") }) {
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
        Text(
            text = "Destination ${selected + 1} of 3 selected.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Live preview for `navigation-drawer`: a modal drawer opened by button or edge swipe. */
@Composable
internal fun ColumnScope.NavigationDrawerPreview() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val destinations = remember { listOf("Inbox", "Sent", "Archive") }
    var selected by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
        FormaModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                FormaNavigationDrawerItem(
                    label = "Inbox",
                    selected = selected == 0,
                    onClick = {
                        selected = 0
                        scope.launch { drawerState.close() }
                    },
                    icon = { Text("📥") },
                    badge = "24",
                )
                FormaNavigationDrawerItem(
                    label = "Sent",
                    selected = selected == 1,
                    onClick = {
                        selected = 1
                        scope.launch { drawerState.close() }
                    },
                    icon = { Text("📤") },
                )
                FormaNavigationDrawerItem(
                    label = "Archive",
                    selected = selected == 2,
                    onClick = {
                        selected = 2
                        scope.launch { drawerState.close() }
                    },
                    icon = { Text("🗂") },
                )
            },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs)) {
                FormaButton(
                    onClick = { scope.launch { drawerState.open() } },
                    variant = FormaButtonVariant.Tonal,
                ) { Text("Open drawer") }
                Text(
                    text = "Selected: ${destinations[selected]}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Live preview for `tab-row`: a fixed Primary row and a scrollable Secondary row. */
@Composable
internal fun ColumnScope.TabRowPreview() {
    val primaryTabs = remember { listOf("Chat", "Calls", "People") }
    var primaryTab by remember { mutableIntStateOf(0) }

    FormaTabRow(selectedTabIndex = primaryTab) {
        primaryTabs.forEachIndexed { index, label ->
            FormaTab(
                selected = primaryTab == index,
                onClick = { primaryTab = index },
                text = { Text(label) },
            )
        }
    }
    Text(
        text = "Primary · ${primaryTabs[primaryTab]}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    val secondaryTabs = remember { listOf("Overview", "Spending", "Budgets", "Goals", "Cards", "Insights") }
    var secondaryTab by remember { mutableIntStateOf(0) }

    FormaTabRow(
        selectedTabIndex = secondaryTab,
        variant = FormaTabRowVariant.Secondary,
        scrollable = true,
    ) {
        secondaryTabs.forEachIndexed { index, label ->
            FormaTab(
                selected = secondaryTab == index,
                onClick = { secondaryTab = index },
                text = { Text(label) },
            )
        }
    }
    Text(
        text = "Secondary (scrollable) · ${secondaryTabs[secondaryTab]}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `top-app-bar`: chips switch between all four [FormaTopAppBarVariant]s. */
@Composable
internal fun ColumnScope.TopAppBarPreview() {
    var variant by remember { mutableStateOf(FormaTopAppBarVariant.Small) }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
    ) {
        FormaTopAppBarVariant.entries.forEach { candidate ->
            FormaChip(
                label = candidate.name,
                onClick = { variant = candidate },
                variant = FormaChipVariant.Filter,
                selected = variant == candidate,
            )
        }
    }

    FormaTopAppBar(
        title = "Inbox",
        variant = variant,
        navigationIcon = {
            FormaIconButton(onClick = {}) { Text("☰") }
        },
        actions = {
            FormaIconButton(onClick = {}) { Text("🔍") }
            FormaIconButton(onClick = {}) { Text("⋮") }
        },
    )
}

/** Live preview for `bottom-app-bar`: icon actions plus an embedded FAB, reporting the last action. */
@Composable
internal fun ColumnScope.BottomAppBarPreview() {
    var lastAction by remember { mutableStateOf("none yet") }

    FormaBottomAppBar(
        actions = {
            FormaIconButton(onClick = { lastAction = "Search" }) { Text("🔍") }
            FormaIconButton(onClick = { lastAction = "Favorite" }) { Text("❤") }
            FormaIconButton(onClick = { lastAction = "More" }) { Text("⋮") }
        },
        floatingActionButton = {
            FormaFloatingActionButton(onClick = { lastAction = "Add" }) { Text("+") }
        },
    )

    Text(
        text = "Last action: $lastAction",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
