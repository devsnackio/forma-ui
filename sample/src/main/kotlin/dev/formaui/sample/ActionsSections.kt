/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.chip.FormaChip
import dev.formaui.components.chip.FormaChipVariant
import dev.formaui.components.fab.FormaExtendedFloatingActionButton
import dev.formaui.components.fab.FormaFabSize
import dev.formaui.components.fab.FormaFloatingActionButton
import dev.formaui.components.iconbutton.FormaIconButton
import dev.formaui.components.iconbutton.FormaIconButtonVariant
import dev.formaui.components.segmentedbutton.FormaSegmentedButton
import dev.formaui.components.segmentedbutton.FormaSegmentedButtonRow
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/** The **Actions** category: Button, IconButton, FloatingActionButton, Chip, SegmentedButton. */

@Composable
fun ButtonShowcase() {
    ComponentShowcase(
        name = "Button",
        description = "Five emphasis levels, from filled to text-only. Press and hold to feel the press-scale.",
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        ) {
            FormaButtonVariant.entries.forEach { variant ->
                FormaButton(onClick = {}, variant = variant) {
                    Text(variant.name)
                }
            }
        }
    }
}

@Composable
fun IconButtonShowcase() {
    ComponentShowcase(
        name = "IconButton",
        description = "Four emphasis levels, plus a disabled state.",
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        ) {
            FormaIconButtonVariant.entries.forEach { variant ->
                FormaIconButton(onClick = {}, variant = variant) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                }
            }
            FormaIconButton(onClick = {}, enabled = false) {
                Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
            }
        }
    }
}

@Composable
fun FabShowcase() {
    ComponentShowcase(
        name = "FloatingActionButton",
        description = "Three sizes, plus an extended FAB — tap it to toggle expansion.",
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            verticalAlignment = Alignment.Bottom,
        ) {
            FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Small) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
            FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Regular) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
            FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Large) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }

        var expanded by remember { mutableStateOf(true) }
        FormaExtendedFloatingActionButton(
            text = "Compose",
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
            onClick = { expanded = !expanded },
            expanded = expanded,
        )
    }
}

@Composable
fun ChipShowcase() {
    ComponentShowcase(
        name = "Chip",
        description = "Assist, suggestion, filter, and input variants.",
    ) {
        var filterOn by remember { mutableStateOf(true) }
        var inputOn by remember { mutableStateOf(false) }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        ) {
            FormaChip(label = "Assist", onClick = {}, variant = FormaChipVariant.Assist)
            FormaChip(label = "Suggestion", onClick = {}, variant = FormaChipVariant.Suggestion)
            FormaChip(
                label = "Filter",
                onClick = { filterOn = !filterOn },
                variant = FormaChipVariant.Filter,
                selected = filterOn,
            )
            FormaChip(
                label = "Input",
                onClick = { inputOn = !inputOn },
                variant = FormaChipVariant.Input,
                selected = inputOn,
            )
        }
    }
}

@Composable
fun SegmentedButtonShowcase() {
    ComponentShowcase(
        name = "SegmentedButton",
        description = "Single-choice and multi-choice segmented rows.",
    ) {
        val periods = listOf("Day", "Week", "Month")
        var selectedPeriod by remember { mutableIntStateOf(0) }
        FormaSegmentedButtonRow(multiSelect = false) {
            periods.forEachIndexed { index, label ->
                FormaSegmentedButton(
                    selected = selectedPeriod == index,
                    onClick = { selectedPeriod = index },
                    index = index,
                    count = periods.size,
                    label = { Text(label) },
                )
            }
        }

        val styles = listOf("Bold", "Italic", "Underline")
        val checkedStyles = remember { mutableStateListOf(false, false, false) }
        FormaSegmentedButtonRow(multiSelect = true) {
            styles.forEachIndexed { index, label ->
                FormaSegmentedButton(
                    checked = checkedStyles[index],
                    onCheckedChange = { checkedStyles[index] = it },
                    index = index,
                    count = styles.size,
                    label = { Text(label) },
                )
            }
        }
    }
}
