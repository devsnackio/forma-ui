/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import dev.formaui.components.bottomappbar.FormaBottomAppBar
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.card.FormaCard
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.components.chip.FormaChip
import dev.formaui.components.chip.FormaChipVariant
import dev.formaui.components.fab.FormaFabSize
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
import dev.formaui.components.topappbar.FormaTopAppBar
import dev.formaui.components.topappbar.FormaTopAppBarVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.launch

/**
 * The **Navigation** category: TopAppBar, Tabs, NavigationBar, NavigationRail, NavigationDrawer,
 * BottomAppBar.
 */

@Composable
fun TopAppBarShowcase() {
    ComponentShowcase(
        name = "TopAppBar",
        description = "All four Material 3 top app bar sizes.",
    ) {
        var variant by remember { mutableStateOf(FormaTopAppBarVariant.Small) }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
        ) {
            FormaTopAppBarVariant.entries.forEach { entry ->
                FormaChip(
                    label = entry.name,
                    onClick = { variant = entry },
                    variant = FormaChipVariant.Filter,
                    selected = entry == variant,
                )
            }
        }

        FormaCard(variant = FormaCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            FormaTopAppBar(
                title = "Inbox",
                modifier = Modifier.fillMaxWidth(),
                variant = variant,
                navigationIcon = {
                    FormaIconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FormaIconButton(onClick = {}) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                },
            )
        }
    }
}

@Composable
fun TabsShowcase() {
    ComponentShowcase(
        name = "Tabs",
        description = "A row of mutually-exclusive destinations.",
    ) {
        var selectedTab by remember { mutableIntStateOf(0) }
        val destinations = listOf("Chat", "Calls", "Contacts")
        FormaTabRow(selectedTabIndex = selectedTab) {
            destinations.forEachIndexed { index, label ->
                FormaTab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(label) },
                )
            }
        }
    }
}

@Composable
fun NavigationBarShowcase() {
    ComponentShowcase(
        name = "NavigationBar",
        description = "Bottom navigation with per-item badges.",
    ) {
        var tab by remember { mutableIntStateOf(0) }
        FormaNavigationBar {
            FormaNavigationBarItem(
                selected = tab == 0,
                onClick = { tab = 0 },
                icon = {
                    Icon(
                        imageVector = if (tab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                    )
                },
                label = "Home",
            )
            FormaNavigationBarItem(
                selected = tab == 1,
                onClick = { tab = 1 },
                icon = {
                    Icon(
                        imageVector = if (tab == 1) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Alerts",
                    )
                },
                label = "Alerts",
                badgeCount = 3,
            )
            FormaNavigationBarItem(
                selected = tab == 2,
                onClick = { tab = 2 },
                icon = {
                    Icon(
                        imageVector = if (tab == 2) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Profile",
                    )
                },
                label = "Profile",
                showBadgeDot = true,
            )
        }
    }
}

@Composable
fun NavigationRailShowcase() {
    ComponentShowcase(
        name = "NavigationRail",
        description = "Side navigation for wider layouts, with a header FAB.",
    ) {
        var tab by remember { mutableIntStateOf(0) }
        FormaCard(variant = FormaCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            FormaNavigationRail(
                header = {
                    FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Small) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                },
            ) {
                FormaNavigationRailItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (tab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home",
                        )
                    },
                    label = "Home",
                )
                FormaNavigationRailItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (tab == 1) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                            contentDescription = "Alerts",
                        )
                    },
                    label = "Alerts",
                    badgeCount = 5,
                )
                FormaNavigationRailItem(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (tab == 2) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile",
                        )
                    },
                    label = "Profile",
                    showBadgeDot = true,
                )
            }
        }
    }
}

@Composable
fun NavigationDrawerShowcase() {
    ComponentShowcase(
        name = "NavigationDrawer",
        description = "A modal drawer of destinations, opened from an edge swipe or button.",
    ) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItem by remember { mutableStateOf("Inbox") }

        FormaButton(
            onClick = { scope.launch { drawerState.open() } },
            variant = FormaButtonVariant.Outlined,
        ) {
            Text("Open drawer")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
        ) {
            FormaModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    FormaNavigationDrawerItem(
                        label = "Inbox",
                        selected = selectedItem == "Inbox",
                        onClick = {
                            selectedItem = "Inbox"
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Filled.Inbox, contentDescription = "Inbox") },
                        badge = "24",
                    )
                    FormaNavigationDrawerItem(
                        label = "Sent",
                        selected = selectedItem == "Sent",
                        onClick = {
                            selectedItem = "Sent"
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Sent") },
                    )
                    FormaNavigationDrawerItem(
                        label = "Trash",
                        selected = selectedItem == "Trash",
                        onClick = {
                            selectedItem = "Trash"
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Filled.Delete, contentDescription = "Trash") },
                    )
                },
            ) {
                FormaCard(variant = FormaCardVariant.Outlined, modifier = Modifier.fillMaxSize()) {
                    Text("Selected: $selectedItem")
                }
            }
        }
    }
}

@Composable
fun BottomAppBarShowcase() {
    ComponentShowcase(
        name = "BottomAppBar",
        description = "A bottom bar with actions and an embedded FAB.",
    ) {
        FormaCard(variant = FormaCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            FormaBottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    FormaIconButton(onClick = {}) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    FormaIconButton(onClick = {}) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                },
                floatingActionButton = {
                    FormaFloatingActionButton(onClick = {}) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                },
            )
        }
    }
}
