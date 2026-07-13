/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.components.avatar.FormaAvatar
import dev.formaui.components.avatar.FormaAvatarSize
import dev.formaui.components.badge.FormaBadge
import dev.formaui.components.badge.FormaBadgedBox
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.card.FormaCard
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.components.divider.FormaDivider
import dev.formaui.components.iconbutton.FormaIconButton
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.menu.FormaDropdownMenu
import dev.formaui.components.menu.FormaDropdownMenuItem
import dev.formaui.components.tooltip.FormaTooltip
import dev.formaui.components.tooltip.FormaTooltipVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/** The **Containment** category: Card, ListItem, Avatar, Badge, Divider, Tooltip, DropdownMenu. */

@Composable
fun CardShowcase() {
    ComponentShowcase(
        name = "Card",
        description = "Elevated, outlined, and filled surfaces, with an optional header/footer.",
    ) {
        FormaCardVariant.entries.forEach { variant ->
            FormaCard(
                variant = variant,
                modifier = Modifier.fillMaxWidth(),
                header = { Text("${variant.name} card", style = MaterialTheme.typography.titleMedium) },
                footer = {
                    FormaButton(onClick = {}, variant = FormaButtonVariant.Text) { Text("Action") }
                },
            ) {
                Text("Balance", style = MaterialTheme.typography.labelMedium)
                Text("$1,240.00", style = FormaTheme.typography.numeric)
            }
        }

        FormaCard(
            variant = FormaCardVariant.Elevated,
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            header = { Text("Clickable card", style = MaterialTheme.typography.titleMedium) },
        ) {
            Text(
                text = "Tap anywhere on this card — the whole surface is one target.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun ListItemShowcase() {
    ComponentShowcase(
        name = "ListItem · Divider",
        description = "Single, two, and three-line rows, separated by dividers.",
    ) {
        FormaListItem(
            headline = "Single line",
            trailing = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Open") },
            onClick = {},
        )
        FormaDivider()
        FormaListItem(
            headline = "Ada Lovelace",
            supporting = "ada@example.com",
            leading = { FormaAvatar(initials = "AL", size = FormaAvatarSize.Small) },
            trailing = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Open") },
            onClick = {},
        )
        FormaDivider()
        FormaListItem(
            headline = "Payment received",
            overline = "Today, 09:41",
            supporting = "$1,240.00 from Grace Hopper",
            leading = { FormaAvatar(initials = "GH", size = FormaAvatarSize.Small) },
        )
    }
}

@Composable
fun AvatarShowcase() {
    ComponentShowcase(
        name = "Avatar",
        description = "Initials in three sizes, plus a custom icon slot.",
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaAvatar(initials = "AB", size = FormaAvatarSize.Small)
            FormaAvatar(initials = "CD", size = FormaAvatarSize.Medium)
            FormaAvatar(initials = "EF", size = FormaAvatarSize.Large)
            FormaAvatar(size = FormaAvatarSize.Medium) {
                Icon(Icons.Filled.Star, contentDescription = "Featured")
            }
        }
    }
}

@Composable
fun BadgeShowcase() {
    ComponentShowcase(
        name = "Badge",
        description = "A dot badge, a numeric badge, and an overflowing count.",
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
        ) {
            FormaBadgedBox(badge = { FormaBadge() }) { Text("Dot") }
            FormaBadgedBox(badge = { FormaBadge(count = 5) }) { Text("Count") }
            FormaBadgedBox(badge = { FormaBadge(count = 150) }) { Text("Overflow") }
        }
    }
}

@Composable
fun TooltipShowcase() {
    ComponentShowcase(
        name = "Tooltip",
        description = "Plain and rich tooltips — long-press or hover an icon to show one.",
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaTooltip(text = "Search") {
                FormaIconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }

            val undoTooltipState = remember { TooltipState() }
            FormaTooltip(
                text = "This item was moved to Archive. It'll be permanently deleted in 30 days.",
                variant = FormaTooltipVariant.Rich,
                title = { Text("Archived") },
                action = {
                    FormaButton(
                        onClick = { undoTooltipState.dismiss() },
                        variant = FormaButtonVariant.Text,
                    ) { Text("Undo") }
                },
                state = undoTooltipState,
            ) {
                FormaIconButton(onClick = {}) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun DropdownMenuShowcase() {
    ComponentShowcase(
        name = "DropdownMenu",
        description = "A contextual menu of actions, anchored to a button.",
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            FormaButton(onClick = { expanded = true }, variant = FormaButtonVariant.Outlined) {
                Text("Options")
            }
            FormaDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                FormaDropdownMenuItem(
                    text = "Share",
                    onClick = { expanded = false },
                    leadingIcon = { Icon(Icons.Filled.Share, contentDescription = "Share") },
                )
                FormaDropdownMenuItem(
                    text = "Rename",
                    onClick = { expanded = false },
                    leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = "Rename") },
                )
                FormaDropdownMenuItem(
                    text = "Delete",
                    onClick = { expanded = false },
                    leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Delete") },
                )
            }
        }
    }
}
