/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.pulltorefresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI pull-to-refresh container — wraps a scrollable layout and adds a swipe-down-to-refresh
 * gesture, delegating to Material 3's `PullToRefreshBox`.
 *
 * Stateless with hoisted state: the caller owns [isRefreshing] (flip it to `true` when your refresh
 * starts and back to `false` when it completes) and reacts to the gesture in [onRefresh]. The
 * [content] slot is a [BoxScope] and is expected to be a scrollable layout such as a `LazyColumn` or
 * a `Column` with `Modifier.verticalScroll`.
 *
 * The [indicator] slot is the styling passthrough: it defaults to Material 3's
 * [PullToRefreshDefaults.Indicator] — the same spinner `PullToRefreshBox` draws by default, aligned
 * to the top-center — so callers get the themed indicator for free but can supply a custom one (e.g.
 * [PullToRefreshDefaults.LoadingIndicator]) when needed.
 *
 * @param isRefreshing whether a refresh is currently occurring; drives the indicator's spinning state.
 * @param onRefresh called when the user's pull crosses the threshold, requesting a refresh.
 * @param modifier the [Modifier] applied to the container.
 * @param state the [PullToRefreshState] tracking pull distance (defaults to a `rememberPullToRefreshState()`).
 * @param contentAlignment the [Alignment] of children inside the container (defaults to [Alignment.TopStart]).
 * @param indicator the indicator drawn on top of the content while pulling or refreshing (defaults to
 * Material 3's [PullToRefreshDefaults.Indicator], themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param content the scrollable content of the container, laid out in a [BoxScope].
 */
@ExperimentalFormaUiApi
@Composable
fun FormaPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        PullToRefreshDefaults.Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state,
        )
    },
    content: @Composable BoxScope.() -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        contentAlignment = contentAlignment,
        indicator = indicator,
        content = content,
    )
}
