/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.listitem

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI list item — a single row for lists, menus, and settings, delegating to Material 3's
 * `ListItem`.
 *
 * The single- / two- / three-line layout is derived automatically from which text is supplied:
 * - **single-line**: [headline] only.
 * - **two-line**: [headline] plus one of [overline] / [supporting].
 * - **three-line**: [headline] plus [overline] and [supporting].
 *
 * [leading] and [trailing] are optional composable slots (icon, [FormaAvatar][dev.formaui.components.avatar.FormaAvatar],
 * [FormaSwitch][dev.formaui.components.switch.FormaSwitch], etc.). Pass [onClick] to make the whole
 * row a single clickable/focusable target with `Role.Button` semantics; leave it `null` for a
 * static row. M3 list items already meet the 48dp minimum height.
 *
 * @param headline the primary text of the row.
 * @param modifier the [Modifier] applied to the row.
 * @param overline optional label shown above [headline].
 * @param supporting optional secondary text shown below [headline].
 * @param leading optional slot at the start of the row.
 * @param trailing optional slot at the end of the row.
 * @param onClick optional click handler; when non-null the whole row becomes clickable.
 * @param enabled whether the row responds to clicks. Only meaningful when [onClick] is non-null.
 * @param colors the row colors (defaults to the M3 defaults, themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaListItem(
    headline: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    supporting: String? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: ListItemColors? = null,
) {
    val rowModifier = if (onClick != null) {
        modifier.clickable(enabled = enabled, onClick = onClick)
    } else {
        modifier
    }

    ListItem(
        headlineContent = { Text(headline) },
        modifier = rowModifier,
        overlineContent = overline?.let { text -> { Text(text) } },
        supportingContent = supporting?.let { text -> { Text(text) } },
        leadingContent = leading,
        trailingContent = trailing,
        colors = colors ?: ListItemDefaults.colors(),
    )
}
