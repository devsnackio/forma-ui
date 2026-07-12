/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.bottomappbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI bottom app bar — navigation and key actions anchored to the bottom of small screens,
 * delegating to Material 3's `BottomAppBar`. Pairs with
 * [dev.formaui.components.topappbar.FormaTopAppBar] and
 * [dev.formaui.components.fab.FormaFloatingActionButton].
 *
 * ```
 * FormaBottomAppBar(
 *     actions = {
 *         FormaIconButton(onClick = { }) { Text("🔍") }
 *         FormaIconButton(onClick = { }) { Text("⋮") }
 *     },
 *     floatingActionButton = {
 *         FormaFloatingActionButton(onClick = { }) { Text("+") }
 *     },
 * )
 * ```
 *
 * @param actions the bar's icon content, laid out in a [RowScope] (typically `IconButton`s).
 * @param modifier the [Modifier] applied to the bar.
 * @param floatingActionButton optional FAB embedded at the end of the bar (typically a
 *   [dev.formaui.components.fab.FormaFloatingActionButton]).
 * @param containerColor the background color of the bar.
 * @param contentColor the preferred content color, derived from [containerColor] by default.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaBottomAppBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: (@Composable () -> Unit)? = null,
    containerColor: Color = FormaBottomAppBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
) {
    BottomAppBar(
        actions = actions,
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

/**
 * Default values used by [FormaBottomAppBar].
 */
@ExperimentalFormaUiApi
object FormaBottomAppBarDefaults {
    /** The default container [Color] for a [FormaBottomAppBar]. */
    val containerColor: Color
        @Composable get() = BottomAppBarDefaults.containerColor
}
