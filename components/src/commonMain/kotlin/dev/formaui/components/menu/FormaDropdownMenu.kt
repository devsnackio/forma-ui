/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.menu

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI dropdown menu — a transient surface of choices anchored to a control, delegating to
 * Material 3's `DropdownMenu`.
 *
 * Place [FormaDropdownMenu] as a sibling of its anchor inside a `Box`; like the underlying
 * `DropdownMenu`, it renders in its own popup window and does not take up any space in the layout
 * itself. State is hoisted: [expanded] controls visibility, and [onDismissRequest] fires when the
 * user taps outside the menu or presses back.
 *
 * ```
 * var expanded by remember { mutableStateOf(false) }
 * Box {
 *     FormaIconButton(onClick = { expanded = true }) {
 *         Icon(Icons.Default.MoreVert, contentDescription = "More options")
 *     }
 *     FormaDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
 *         FormaDropdownMenuItem(text = "Share", onClick = { expanded = false })
 *         FormaDropdownMenuItem(text = "Delete", onClick = { expanded = false })
 *     }
 * }
 * ```
 *
 * @param expanded whether the menu is currently shown.
 * @param onDismissRequest called when the user requests to dismiss the menu — an outside tap or
 *   the back gesture.
 * @param modifier the [Modifier] applied to the menu's content.
 * @param content the menu's items, laid out in a scrollable [ColumnScope] (typically
 *   [FormaDropdownMenuItem]s).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        content = content,
    )
}

/**
 * A single choice in a [FormaDropdownMenu], delegating to Material 3's `DropdownMenuItem`.
 *
 * @param text the item's label.
 * @param onClick called when the item is clicked. Not invoked while [enabled] is `false`. Callers
 *   typically also dismiss the menu from here.
 * @param modifier the [Modifier] applied to the item.
 * @param leadingIcon optional slot at the start of the item.
 * @param trailingIcon optional slot at the end of the item (can also hold `Text` for a keyboard
 *   shortcut hint).
 * @param enabled whether the item is interactive.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    DropdownMenuItem(
        text = { Text(text) },
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
    )
}
