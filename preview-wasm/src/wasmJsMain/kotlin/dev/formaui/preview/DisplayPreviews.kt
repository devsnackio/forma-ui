/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.formaui.components.avatar.FormaAvatar
import dev.formaui.components.avatar.FormaAvatarSize
import dev.formaui.components.badge.FormaBadge
import dev.formaui.components.badge.FormaBadgedBox
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.card.FormaCard
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.components.divider.FormaDivider
import dev.formaui.components.divider.FormaDividerOrientation
import dev.formaui.components.emptystate.FormaEmptyState
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.carousel.FormaCarousel
import dev.formaui.components.carousel.FormaCarouselVariant
import dev.formaui.components.loading.FormaLoadingIndicator
import dev.formaui.components.loading.FormaLoadingIndicatorVariant
import dev.formaui.components.pulltorefresh.FormaPullToRefresh
import dev.formaui.components.slider.FormaSlider
import dev.formaui.components.swipedismiss.FormaSwipeToDismiss
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Live preview for `card`: all three [FormaCardVariant]s, each clickable with the press-scale dip. */
@Composable
internal fun ColumnScope.CardPreview() {
    var clicks by remember { mutableIntStateOf(0) }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
    ) {
        FormaCardVariant.entries.forEach { variant ->
            FormaCard(
                variant = variant,
                onClick = { clicks++ },
                header = { Text(variant.name, style = MaterialTheme.typography.titleMedium) },
            ) {
                Text("Tap this card.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    Text(
        text = "Card clicks: $clicks — watch the press-scale dip on press.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `badge`: a numeric badge you can increment past the 99+ cap, plus a dot badge. */
@Composable
internal fun ColumnScope.BadgePreview() {
    var count by remember { mutableIntStateOf(5) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xl),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormaBadgedBox(badge = { FormaBadge(count = count) }) {
            Text("Inbox", style = MaterialTheme.typography.titleMedium)
        }
        FormaBadgedBox(badge = { FormaBadge() }) {
            Text("Updates", style = MaterialTheme.typography.titleMedium)
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs)) {
        FormaButton(onClick = { count++ }, variant = FormaButtonVariant.Tonal) { Text("+1") }
        FormaButton(onClick = { count += 50 }, variant = FormaButtonVariant.Tonal) { Text("+50") }
        FormaButton(onClick = { count = 0 }, variant = FormaButtonVariant.Outlined) { Text("Reset") }
    }

    Text(
        text = "Counts above 99 cap at 99+.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `avatar`: all three [FormaAvatarSize]s with initials, plus slot content. */
@Composable
internal fun ColumnScope.AvatarPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormaAvatar(initials = "AB", size = FormaAvatarSize.Small)
        FormaAvatar(initials = "CD")
        FormaAvatar(initials = "EF", size = FormaAvatarSize.Large)
        FormaAvatar(size = FormaAvatarSize.Large) { Text("🙂") }
    }

    Text(
        text = "Small · Medium · Large initials, plus arbitrary slot content.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `divider`: a horizontal rule and a vertical rule inside an intrinsic-height row. */
@Composable
internal fun ColumnScope.DividerPreview() {
    Text("Above the rule", style = MaterialTheme.typography.bodyMedium)
    FormaDivider()
    Text("Below the rule", style = MaterialTheme.typography.bodyMedium)

    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Left", style = MaterialTheme.typography.bodyMedium)
        FormaDivider(orientation = FormaDividerOrientation.Vertical)
        Text("Right", style = MaterialTheme.typography.bodyMedium)
    }
}

/** Live preview for `list-item`: single-, two-, and three-line rows, each clickable. */
@Composable
internal fun ColumnScope.ListItemPreview() {
    var lastClicked by remember { mutableStateOf("nothing yet") }

    Column {
        FormaListItem(
            headline = "Single line",
            trailing = { Text("›") },
            onClick = { lastClicked = "Single line" },
        )
        FormaDivider()
        FormaListItem(
            headline = "Ada Lovelace",
            supporting = "ada@example.com",
            leading = { FormaAvatar(initials = "AL", size = FormaAvatarSize.Small) },
            onClick = { lastClicked = "Ada Lovelace" },
        )
        FormaDivider()
        FormaListItem(
            overline = "Overline",
            headline = "Three-line item",
            supporting = "Supporting text turns this into a taller row.",
            onClick = { lastClicked = "Three-line item" },
        )
    }

    Text(
        text = "Last clicked: $lastClicked",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `loading-indicator`: indeterminate and determinate indicators driven by a slider. */
@Composable
internal fun ColumnScope.LoadingIndicatorPreview() {
    var progress by remember { mutableFloatStateOf(0.6f) }
    val percent = (progress * 100).toInt()

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormaLoadingIndicator(contentDescription = "Loading")
        FormaLoadingIndicator(progress = progress, contentDescription = "$percent percent loaded")
    }
    FormaLoadingIndicator(
        modifier = Modifier.fillMaxWidth(),
        variant = FormaLoadingIndicatorVariant.Linear,
        progress = progress,
        contentDescription = "$percent percent loaded",
    )
    FormaSlider(
        value = progress,
        onValueChange = { progress = it },
        modifier = Modifier.fillMaxWidth(),
    )
    Text(
        text = "Drag the slider to drive the determinate indicators ($percent%).",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `empty-state`: illustration, title, description, and a working action slot. */
@Composable
internal fun ColumnScope.EmptyStatePreview() {
    var actionClicks by remember { mutableIntStateOf(0) }

    FormaEmptyState(
        title = "No transactions yet",
        description = "Your transactions will appear here once you make your first payment.",
        icon = { Text("🧾", style = MaterialTheme.typography.headlineLarge) },
        action = { FormaButton(onClick = { actionClicks++ }) { Text("Add payment") } },
    )

    Text(
        text = if (actionClicks == 0) {
            "The action slot is a real button."
        } else {
            "Action clicked $actionClicks times."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `carousel`: the multi-browse and uncontained variants with colored items. */
@Composable
internal fun ColumnScope.CarouselPreview() {
    val colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.errorContainer,
        MaterialTheme.colorScheme.surfaceVariant,
    )

    Text("Multi-browse", style = MaterialTheme.typography.titleSmall)
    val multiState = rememberCarouselState(itemCount = { colors.size })
    FormaCarousel(
        state = multiState,
        itemWidth = 200.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
    ) { index ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors[index]),
            contentAlignment = Alignment.Center,
        ) {
            Text("${index + 1}", style = MaterialTheme.typography.titleMedium)
        }
    }

    Text("Uncontained", style = MaterialTheme.typography.titleSmall)
    val uncontainedState = rememberCarouselState(itemCount = { colors.size })
    FormaCarousel(
        state = uncontainedState,
        itemWidth = 140.dp,
        variant = FormaCarouselVariant.Uncontained,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
    ) { index ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors[index]),
            contentAlignment = Alignment.Center,
        ) {
            Text("${index + 1}", style = MaterialTheme.typography.titleMedium)
        }
    }
}

/** Live preview for `pull-to-refresh`: pull the list down to trigger a simulated refresh. */
@Composable
internal fun ColumnScope.PullToRefreshPreview() {
    var refreshing by remember { mutableStateOf(false) }
    var refreshCount by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    FormaPullToRefresh(
        isRefreshing = refreshing,
        onRefresh = {
            refreshing = true
            scope.launch {
                delay(1200)
                refreshCount++
                refreshing = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            repeat(6) { i ->
                FormaListItem(
                    headline = "Item ${i + 1}",
                    supporting = "Refreshed $refreshCount time(s)",
                )
                FormaDivider()
            }
        }
    }

    Text(
        text = "Pull the list down to refresh (refreshed $refreshCount times).",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `swipe-to-dismiss`: swipe a row to reveal the delete background and dismiss it. */
@Composable
internal fun ColumnScope.SwipeToDismissPreview() {
    val items = remember { mutableStateListOf("Ada Lovelace", "Alan Turing", "Grace Hopper") }

    Column {
        items.forEach { item ->
            key(item) {
                val state = rememberSwipeToDismissBoxState()
                FormaSwipeToDismiss(
                    state = state,
                    onDismiss = { items.remove(item) },
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = FormaTheme.spacing.md),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text("Delete", color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    },
                ) {
                    FormaListItem(headline = item, supporting = "Swipe me away")
                }
            }
        }
    }

    Text(
        text = if (items.isEmpty()) {
            "All dismissed — reload to reset."
        } else {
            "${items.size} item(s) — swipe a row to dismiss it."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
