/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.formaui.components.badge.FormaBadge
import dev.formaui.components.badge.FormaBadgedBox
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI bottom navigation bar — top-level destination switching, delegating to Material 3's
 * `NavigationBar`. Populate it with [FormaNavigationBarItem]s.
 *
 * ```
 * FormaNavigationBar {
 *     FormaNavigationBarItem(selected = tab == HOME, onClick = { tab = HOME },
 *         icon = { Icon(Icons.Default.Home, contentDescription = "Home") }, label = "Home")
 *     FormaNavigationBarItem(selected = tab == ALERTS, onClick = { tab = ALERTS },
 *         icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
 *         label = "Alerts", badgeCount = 3)
 * }
 * ```
 *
 * @param modifier the [Modifier] applied to the bar.
 * @param containerColor the bar's background color (defaults to the M3 navigation-bar container,
 * themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param contentColor the preferred content color for items (defaults to the color matching
 * [containerColor]).
 * @param content the bar's items, laid out in a [RowScope] (typically [FormaNavigationBarItem]s).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

/**
 * A single destination in a [FormaNavigationBar], with built-in per-item badge support.
 *
 * The badge is chosen from [badgeCount] / [showBadgeDot]: a non-null [badgeCount] shows a numeric
 * [FormaBadge] on the icon; otherwise [showBadgeDot]` = true` shows a dot badge; if neither, no
 * badge. The 48dp touch target and selection semantics come from Material 3's `NavigationBarItem`.
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
 * @param colors the item colors — icon, label, and selection-indicator colors for the selected,
 * unselected, and disabled states (e.g. `NavigationBarItemDefaults.colors(selectedTextColor = ...)`).
 * Defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme].
 */
@ExperimentalFormaUiApi
@Composable
fun RowScope.FormaNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    badgeCount: Int? = null,
    showBadgeDot: Boolean = false,
    alwaysShowLabel: Boolean = true,
    colors: NavigationBarItemColors? = null,
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

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = iconContent,
        modifier = modifier,
        enabled = enabled,
        label = label?.let { value -> { Text(value) } },
        alwaysShowLabel = alwaysShowLabel,
        colors = colors ?: NavigationBarItemDefaults.colors(),
    )
}
