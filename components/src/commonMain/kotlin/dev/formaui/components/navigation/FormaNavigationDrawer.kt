/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI modal navigation drawer — an off-canvas panel of destinations that slides in over
 * [content] and is dismissed with a scrim tap, delegating to Material 3's `ModalNavigationDrawer`.
 *
 * Better default over the raw Material 3 API: [drawerContent] is automatically wrapped in a
 * `ModalDrawerSheet`, so callers just supply the drawer's items (typically
 * [FormaNavigationDrawerItem]s) rather than assembling the sheet themselves.
 *
 * [drawerState] is hoisted — create it with `rememberDrawerState(DrawerValue.Closed)` and drive it
 * from a menu icon's `onClick`:
 *
 * ```
 * val drawerState = rememberDrawerState(DrawerValue.Closed)
 * val scope = rememberCoroutineScope()
 *
 * FormaModalNavigationDrawer(
 *     drawerState = drawerState,
 *     drawerContent = {
 *         FormaNavigationDrawerItem(label = "Inbox", selected = true, onClick = { })
 *         FormaNavigationDrawerItem(label = "Sent", selected = false, onClick = { })
 *     },
 * ) {
 *     FormaTopAppBar(
 *         title = "Mail",
 *         navigationIcon = {
 *             FormaIconButton(onClick = { scope.launch { drawerState.open() } }) { Text("☰") }
 *         },
 *     )
 * }
 * ```
 *
 * @param drawerState the drawer's hoisted open/closed state (e.g.
 *   `rememberDrawerState(DrawerValue.Closed)`).
 * @param drawerContent the drawer's items, laid out in a [ColumnScope] inside a `ModalDrawerSheet`
 *   (typically [FormaNavigationDrawerItem]s).
 * @param modifier the [Modifier] applied to the drawer.
 * @param gesturesEnabled whether the drawer can be opened/closed by a swipe gesture.
 * @param scrimColor the color of the scrim that obscures [content] while the drawer is open
 *   (defaults to the M3 default scrim).
 * @param drawerContainerColor the background color of the drawer sheet (defaults to the M3 modal
 *   drawer container, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param drawerContentColor the preferred content color inside the drawer sheet (defaults to the
 *   color matching [drawerContainerColor]).
 * @param content the screen content behind the drawer.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaModalNavigationDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    drawerContainerColor: Color = DrawerDefaults.modalContainerColor,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = drawerContainerColor,
                drawerContentColor = drawerContentColor,
                content = drawerContent,
            )
        },
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        scrimColor = scrimColor,
        content = content,
    )
}

/**
 * A single destination in a [FormaModalNavigationDrawer], delegating to Material 3's
 * `NavigationDrawerItem`.
 *
 * [badge] renders as trailing text at the item's end (e.g. an unread count like `"24"`) — the
 * common Material 3 drawer usage — rather than a [dev.formaui.components.badge.FormaBadge] dot/chip,
 * which reads awkwardly inline with a label in a drawer row.
 *
 * @param label the item's text label.
 * @param selected whether this item is the current destination.
 * @param onClick called when the item is tapped.
 * @param modifier the [Modifier] applied to the item.
 * @param icon optional leading icon (supply a `contentDescription` on it for accessibility).
 * @param badge optional trailing text (e.g. an unread count) shown at the item's end.
 * @param colors the item colors — text, icon, badge, and selection-container colors for the
 * selected and unselected states (e.g. `NavigationDrawerItemDefaults.colors(selectedContainerColor = ...)`).
 * Defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme].
 * @param labelTextStyle optional [TextStyle] override for [label], merged on top of the M3 label
 * style so a partial override (e.g. only `fontWeight`) keeps the M3 defaults for everything else.
 * @param badgeTextStyle optional [TextStyle] override for [badge], merged on top of the M3 badge
 * style.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaNavigationDrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    badge: String? = null,
    colors: NavigationDrawerItemColors? = null,
    labelTextStyle: TextStyle? = null,
    badgeTextStyle: TextStyle? = null,
) {
    NavigationDrawerItem(
        label = { Text(label, style = LocalTextStyle.current.merge(labelTextStyle)) },
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        badge = badge?.let { value ->
            { Text(value, style = LocalTextStyle.current.merge(badgeTextStyle)) }
        },
        colors = colors ?: NavigationDrawerItemDefaults.colors(),
    )
}
