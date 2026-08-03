/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

/**
 * Live preview for `button` — the reference implementation for host-driven [PreviewControls].
 *
 * Two shapes, because this preview is used two ways. Embedded in a docs page whose control bar is
 * naming a variant, it shows that one specimen and honours the `enabled = false` toggle: the tabs
 * are the gallery, so repeating every variant beneath them would just be noise. Uncontrolled —
 * opened standalone, or embedded by a site that doesn't drive controls — it falls back to the
 * every-variant gallery, which is the only way the bundle is worth opening on its own.
 */
@Composable
internal fun ColumnScope.ButtonPreview() {
    var clicks by remember { mutableIntStateOf(0) }
    val controls = LocalPreviewControls.current
    val selected = controls.variantOf<FormaButtonVariant>()

    if (selected == null) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        ) {
            FormaButtonVariant.entries.forEach { variant ->
                FormaButton(onClick = { clicks++ }, variant = variant) {
                    Text(variant.name)
                }
            }
            FormaButton(onClick = {}, enabled = false) {
                Text("Disabled")
            }
        }

        Text(
            text = "Clicked $clicks times — this is the real component running in your browser.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        FormaButton(onClick = { clicks++ }, variant = selected, enabled = controls.enabled) {
            Text(selected.name)
        }

        Text(
            text = if (controls.enabled) {
                "${selected.name} — clicked $clicks times. This is the real component running in your browser."
            } else {
                "${selected.name}, disabled — clicks are ignored, which is the component's own behaviour, not a mock."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Live preview for `icon-button`: every [FormaIconButtonVariant], counting clicks. */
@Composable
internal fun ColumnScope.IconButtonPreview() {
    var clicks by remember { mutableIntStateOf(0) }
    val controls = LocalPreviewControls.current
    val shown = controls.shownVariants<FormaIconButtonVariant>()

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        shown.forEach { variant ->
            FormaIconButton(
                onClick = { clicks++ },
                variant = variant,
                enabled = controls.enabled,
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    }

    // Built from `shown` rather than written out, so narrowing to one variant
    // can't leave the caption naming four.
    Text(
        text = shown.joinToString(" · ") { it.name } +
            if (controls.enabled) " — clicked $clicks times." else " — disabled, so clicks do nothing.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `floating-action-button`: all three [FormaFabSize]s plus a collapsible extended FAB. */
@Composable
internal fun ColumnScope.FloatingActionButtonPreview() {
    var clicks by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(true) }

    // The inventory lists a fourth "Extended" variant, but it is a different
    // composable rather than a FormaFabSize, so only the three real enum
    // entries are offered as tabs; the extended FAB below always renders.
    val shown = LocalPreviewControls.current.shownVariants<FormaFabSize>()

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        shown.forEach { size ->
            FormaFloatingActionButton(onClick = { clicks++ }, size = size) {
                Text("+")
            }
        }
    }

    FormaExtendedFloatingActionButton(
        text = "Compose",
        icon = { Text("+") },
        onClick = { expanded = !expanded },
        expanded = expanded,
    )

    Text(
        text = shown.joinToString(" · ") { it.name } +
            " ($clicks clicks) — tap the extended FAB to collapse or expand it.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `chip`: all four [FormaChipVariant]s; Filter and Input toggle their selection. */
@Composable
internal fun ColumnScope.ChipPreview() {
    var assistClicks by remember { mutableIntStateOf(0) }
    var filterSelected by remember { mutableStateOf(false) }
    var inputSelected by remember { mutableStateOf(true) }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
    ) {
        FormaChip(label = "Assist", onClick = { assistClicks++ })
        FormaChip(
            label = "Filter",
            onClick = { filterSelected = !filterSelected },
            variant = FormaChipVariant.Filter,
            selected = filterSelected,
        )
        FormaChip(
            label = "Input",
            onClick = { inputSelected = !inputSelected },
            variant = FormaChipVariant.Input,
            selected = inputSelected,
            trailingIcon = { Icon(Icons.Default.Close, contentDescription = null) },
        )
        FormaChip(label = "Suggestion", onClick = {}, variant = FormaChipVariant.Suggestion)
    }

    Text(
        text = "Assist clicked $assistClicks times; Filter and Input toggle their selection.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `segmented-button`: a single-select row and a multi-select row, both interactive. */
@Composable
internal fun ColumnScope.SegmentedButtonPreview() {
    val periods = remember { listOf("Day", "Week", "Month") }
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

    val styles = remember { listOf("Bold", "Italic", "Underline") }
    val checkedStyles = remember { mutableStateListOf(true, false, false) }

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

    val checkedLabels = styles.filterIndexed { index, _ -> checkedStyles[index] }
    Text(
        text = "Single-select: ${periods[selectedPeriod]} · Multi-select: " +
            checkedLabels.ifEmpty { listOf("none") }.joinToString(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
