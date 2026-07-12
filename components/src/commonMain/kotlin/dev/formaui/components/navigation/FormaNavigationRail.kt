/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.components.badge.FormaBadge
import dev.formaui.components.badge.FormaBadgedBox
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI navigation rail — top-level destination switching for tablet/desktop-width screens,
 * delegating to Material 3's `NavigationRail`. Populate it with [FormaNavigationRailItem]s.
 *
 * Unlike [FormaNavigationBar] (bottom navigation for phone-width screens), the rail is a vertical
 * strip typically pinned to the start edge of the screen. [header] is an optional slot above the
 * items — typically a [dev.formaui.components.fab.FormaFloatingActionButton] or a logo.
 *
 * ```
 * FormaNavigationRail(
 *     header = { FormaFloatingActionButton(onClick = { /* compose */ }) { Text("+") } },
 * ) {
 *     FormaNavigationRailItem(selected = tab == HOME, onClick = { tab = HOME },
 *         icon = { Icon(Icons.Default.Home, contentDescription = "Home") }, label = "Home")
 *     FormaNavigationRailItem(selected = tab == ALERTS, onClick = { tab = ALERTS },
 *         icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
 *         label = "Alerts", badgeCount = 3)
 * }
 * ```
 *
 * @param modifier the [Modifier] applied to the rail.
 * @param header optional content shown above the items (typically a FAB or a logo).
 * @param content the rail's items, laid out in a [ColumnScope] (typically 3-7
 *   [FormaNavigationRailItem]s).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(modifier = modifier, header = header, content = content)
}

/**
 * A single destination in a [FormaNavigationRail], with built-in per-item badge support.
 *
 * The badge is chosen from [badgeCount] / [showBadgeDot]: a non-null [badgeCount] shows a numeric
 * [FormaBadge] on the icon; otherwise [showBadgeDot]` = true` shows a dot badge; if neither, no
 * badge. The 48dp touch target and selection semantics come from Material 3's `NavigationRailItem`.
 *
 * Note: unlike [FormaNavigationBarItem] (which is scoped to `RowScope` because Material 3's
 * `NavigationBarItem` requires it), Material 3's `NavigationRailItem` takes no scope receiver, so
 * this composable takes none either — call it directly inside [FormaNavigationRail]'s [content].
 *
 * @param selected whether this item is the current destination.
 * @param onClick called when the item is tapped.
 * @param icon the item's icon (supply a `contentDescription` on it for accessibility).
 * @param modifier the [Modifier] applied to the item.
 * @param label optional text label shown under the icon.
 * @param enabled whether the item is interactive.
 * @param badgeCount optional unread count to show as a numeric badge on the icon.
 * @param showBadgeDot when `true` and [badgeCount] is null, shows a dot badge on the icon.
 * @param alwaysShowLabel whether the label is shown even when the item is unselected.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    badgeCount: Int? = null,
    showBadgeDot: Boolean = false,
    alwaysShowLabel: Boolean = true,
) {
    val iconContent: @Composable () -> Unit = when {
        badgeCount != null -> {
            { FormaBadgedBox(badge = { FormaBadge(count = badgeCount) }) { icon() } }
        }
        showBadgeDot -> {
            { FormaBadgedBox(badge = { FormaBadge() }) { icon() } }
        }
        else -> icon
    }

    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = iconContent,
        modifier = modifier,
        enabled = enabled,
        label = label?.let { value -> { Text(value) } },
        alwaysShowLabel = alwaysShowLabel,
    )
}
