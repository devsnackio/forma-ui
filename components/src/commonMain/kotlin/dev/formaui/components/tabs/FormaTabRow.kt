/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.tabs

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The indicator style of a [FormaTabRow], mirroring Material 3's two tab row emphases.
 */
@ExperimentalFormaUiApi
enum class FormaTabRowVariant {
    /** A bold, pill-shaped active indicator. Use for top-level navigation between destinations. */
    Primary,

    /** A slim underline active indicator. Use to switch between views within one destination. */
    Secondary,
}

/**
 * A FormaUI tab row — switches between a small number of related content destinations, delegating
 * to Material 3's `PrimaryTabRow`/`SecondaryTabRow` (or their scrollable equivalents).
 *
 * One entry point covers both Material 3 tab row emphases via [variant] and both the fixed
 * (evenly-spaced) and [scrollable] layouts via a single flag, instead of choosing between four
 * separate Material 3 functions. Populate [tabs] with [FormaTab]s:
 *
 * ```
 * var selectedTab by remember { mutableStateOf(0) }
 * FormaTabRow(selectedTabIndex = selectedTab) {
 *     FormaTab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Chat") })
 *     FormaTab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Calls") })
 * }
 * ```
 *
 * The row is stateless: [selectedTabIndex] and tab selection are hoisted to the caller.
 *
 * @param selectedTabIndex the index of the currently selected tab, used to position the active
 *   indicator.
 * @param modifier the [Modifier] applied to the row.
 * @param variant the indicator style (defaults to [FormaTabRowVariant.Primary]).
 * @param scrollable when `true`, tabs are laid out from the start edge and scroll if they overflow
 *   — use this for a large or dynamic number of tabs. When `false` (the default), tabs are fixed
 *   and evenly spaced across the row's width — use this for a small, stable set of tabs.
 * @param tabs the row's tabs, typically [FormaTab]s.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    variant: FormaTabRowVariant = FormaTabRowVariant.Primary,
    scrollable: Boolean = false,
    tabs: @Composable () -> Unit,
) {
    when (variant) {
        FormaTabRowVariant.Primary -> if (scrollable) {
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                tabs = tabs,
            )
        } else {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                tabs = tabs,
            )
        }

        FormaTabRowVariant.Secondary -> if (scrollable) {
            SecondaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                tabs = tabs,
            )
        } else {
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                tabs = tabs,
            )
        }
    }
}

/**
 * A single tab in a [FormaTabRow], delegating to Material 3's `Tab` (text and/or icon slots) or,
 * when both [text] and [icon] are supplied, to `LeadingIconTab` — Material 3 lays those two out
 * differently (icon stacked above text vs. icon inline before text), so the icon+text combination
 * is dispatched to the inline layout automatically.
 *
 * @param selected whether this tab is the currently active destination.
 * @param onClick called when the tab is clicked. Not invoked while [enabled] is `false`.
 * @param modifier the [Modifier] applied to the tab.
 * @param text optional text label.
 * @param icon optional icon. Should be 24dp.
 * @param enabled whether the tab is interactive.
 * @param selectedContentColor the color for the tab's content (and its ripple) when [selected].
 *   When `null`, [LocalContentColor] is used.
 * @param unselectedContentColor the color for the tab's content when not [selected]. When `null`,
 *   defaults to [selectedContentColor].
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selectedContentColor: Color? = null,
    unselectedContentColor: Color? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedSelectedColor = selectedContentColor ?: LocalContentColor.current
    val resolvedUnselectedColor = unselectedContentColor ?: resolvedSelectedColor

    if (text != null && icon != null) {
        LeadingIconTab(
            selected = selected,
            onClick = onClick,
            text = text,
            icon = icon,
            modifier = modifier,
            enabled = enabled,
            selectedContentColor = resolvedSelectedColor,
            unselectedContentColor = resolvedUnselectedColor,
            interactionSource = interactionSource,
        )
    } else {
        Tab(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            text = text,
            icon = icon,
            selectedContentColor = resolvedSelectedColor,
            unselectedContentColor = resolvedUnselectedColor,
            interactionSource = interactionSource,
        )
    }
}
