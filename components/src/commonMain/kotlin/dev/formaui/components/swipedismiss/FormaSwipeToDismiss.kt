/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.swipedismiss

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI swipe-to-dismiss row — lets the user swipe a row horizontally to dismiss it (e.g. to
 * delete or archive), delegating to Material 3's `SwipeToDismissBox`.
 *
 * Stateless with hoisted state: the caller creates the [state] via
 * `rememberSwipeToDismissBoxState(...)` and reads its `dismissDirection` / `currentValue` (or handles
 * [onDismiss]) to react to a completed swipe. The [backgroundContent] slot is revealed behind
 * [content] as the user swipes — use [state] to show a different background per direction. Both
 * slots are laid out in a [RowScope].
 *
 * @param state the [SwipeToDismissBoxState] tracking the swipe (create via `rememberSwipeToDismissBoxState`).
 * @param backgroundContent the content stacked behind [content] and revealed while swiping, laid out
 * in a [RowScope]; use [state] to vary it per swipe direction.
 * @param modifier the [Modifier] applied to the row.
 * @param enableDismissFromStartToEnd whether the row can be dismissed by swiping start-to-end (defaults to `true`).
 * @param enableDismissFromEndToStart whether the row can be dismissed by swiping end-to-start (defaults to `true`).
 * @param gesturesEnabled whether the swipe gesture is interactive (defaults to `true`).
 * @param onDismiss called when [content] is dismissed, with the resulting [SwipeToDismissBoxValue]
 * direction (defaults to a no-op).
 * @param content the dismissable content, laid out in a [RowScope].
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSwipeToDismiss(
    state: SwipeToDismissBoxState,
    backgroundContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true,
    gesturesEnabled: Boolean = true,
    onDismiss: (SwipeToDismissBoxValue) -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    SwipeToDismissBox(
        state = state,
        backgroundContent = backgroundContent,
        modifier = modifier,
        enableDismissFromStartToEnd = enableDismissFromStartToEnd,
        enableDismissFromEndToStart = enableDismissFromEndToStart,
        gesturesEnabled = gesturesEnabled,
        onDismiss = onDismiss,
        content = content,
    )
}
