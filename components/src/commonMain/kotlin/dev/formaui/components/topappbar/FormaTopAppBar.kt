/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.topappbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The layout of a [FormaTopAppBar], mirroring Material 3's four top app bar sizes.
 */
@ExperimentalFormaUiApi
enum class FormaTopAppBarVariant {
    /** A single-row bar with a start-aligned title. The most common top app bar. */
    Small,

    /** A single-row bar with a horizontally centered title. */
    CenterAligned,

    /** A two-row bar whose title grows larger when expanded, shrinking to [Small] on scroll. */
    Medium,

    /** A two-row bar with the most prominent expanded title, shrinking to [Small] on scroll. */
    Large,
}

/**
 * A FormaUI top app bar — "Material 3 with better defaults".
 *
 * Because [scrollBehavior]'s type (`TopAppBarScrollBehavior`) is still `@ExperimentalMaterial3Api`
 * in the underlying Material 3 library, that marker propagates through this signature: call sites
 * need `@OptIn(ExperimentalMaterial3Api::class)` (in addition to
 * `@OptIn(ExperimentalFormaUiApi::class)`) until Material 3 stabilizes top app bar scrolling.
 *
 * One entry point covers all four Material 3 top app bar sizes via [variant]. Pair a [Medium] or
 * [Large] variant with a [scrollBehavior] (e.g. `TopAppBarDefaults.exitUntilCollapsedScrollBehavior()`)
 * and `Modifier.nestedScroll` on the scrolling content to get the standard collapsing-header
 * behavior:
 *
 * ```
 * val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
 * Scaffold(
 *     modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
 *     topBar = {
 *         FormaTopAppBar(
 *             title = "Inbox",
 *             variant = FormaTopAppBarVariant.Large,
 *             scrollBehavior = scrollBehavior,
 *         )
 *     },
 * ) { ... }
 * ```
 *
 * @param title the bar's title text.
 * @param modifier the [Modifier] applied to the bar.
 * @param variant the bar's layout (defaults to [FormaTopAppBarVariant.Small]).
 * @param navigationIcon optional slot at the start of the bar (typically an `IconButton`, e.g.
 *   [dev.formaui.components.iconbutton.FormaIconButton], for a back or menu action).
 * @param actions the actions displayed at the end of the bar, laid out in a [RowScope] (typically
 *   `IconButton`s).
 * @param scrollBehavior an optional [TopAppBarScrollBehavior] that ties this bar's height/color
 *   changes to a scrolling content's [androidx.compose.ui.input.nestedscroll.NestedScrollConnection].
 * @param colors the container/content colors. When `null`, the Material 3 default colors for the
 *   chosen [variant] are used.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    variant: FormaTopAppBarVariant = FormaTopAppBarVariant.Small,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors? = null,
) {
    val titleSlot: @Composable () -> Unit = { Text(title) }
    val navigationIconSlot: @Composable () -> Unit = navigationIcon ?: {}

    when (variant) {
        FormaTopAppBarVariant.Small -> TopAppBar(
            title = titleSlot,
            modifier = modifier,
            navigationIcon = navigationIconSlot,
            actions = actions,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )

        FormaTopAppBarVariant.CenterAligned -> CenterAlignedTopAppBar(
            title = titleSlot,
            modifier = modifier,
            navigationIcon = navigationIconSlot,
            actions = actions,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )

        FormaTopAppBarVariant.Medium -> MediumTopAppBar(
            title = titleSlot,
            modifier = modifier,
            navigationIcon = navigationIconSlot,
            actions = actions,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )

        FormaTopAppBarVariant.Large -> LargeTopAppBar(
            title = titleSlot,
            modifier = modifier,
            navigationIcon = navigationIconSlot,
            actions = actions,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )
    }
}
