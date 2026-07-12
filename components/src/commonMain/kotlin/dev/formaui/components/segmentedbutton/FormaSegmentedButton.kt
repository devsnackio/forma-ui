/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.segmentedbutton

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.MultiChoiceSegmentedButtonRowScope
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The scope exposed inside a [FormaSegmentedButtonRow]'s `content` lambda.
 *
 * Material 3 models single- and multi-select segmented button rows as two distinct receiver
 * scopes ([SingleChoiceSegmentedButtonRowScope] and [MultiChoiceSegmentedButtonRowScope]) so the
 * compiler can pick the correctly-accessible `SegmentedButton` overload (`RadioButton` semantics
 * for single-select, toggle semantics for multi-select). Neither scope declares any member beyond
 * [RowScope] itself, so [FormaSegmentedButtonRowScope] safely extends both — this lets
 * [FormaSegmentedButtonRow] expose a single `content` lambda type regardless of its `multiSelect`
 * flag, while [FormaSegmentedButton] still resolves to the real, correctly-scoped Material 3
 * `SegmentedButton` overload underneath.
 */
@ExperimentalFormaUiApi
interface FormaSegmentedButtonRowScope :
    SingleChoiceSegmentedButtonRowScope,
    MultiChoiceSegmentedButtonRowScope

private class FormaSegmentedButtonRowScopeWrapper(scope: RowScope) :
    FormaSegmentedButtonRowScope,
    RowScope by scope

/**
 * A FormaUI segmented button row — a set of connected, mutually-styled buttons for view switching
 * or option selection, delegating to Material 3's `SingleChoiceSegmentedButtonRow` /
 * `MultiChoiceSegmentedButtonRow`.
 *
 * One entry point covers both Material 3 selection modes via [multiSelect]: `false` (the default)
 * for mutually-exclusive selection (`RadioButton` semantics — exactly one segment selected, e.g. a
 * view switcher), `true` for independent per-segment toggles (checkbox-like semantics, e.g. a text
 * style toolbar). Populate [content] with [FormaSegmentedButton]s, choosing the `selected`/`onClick`
 * overload for single-select rows or the `checked`/`onCheckedChange` overload for multi-select rows:
 *
 * ```
 * var selectedPeriod by remember { mutableStateOf(0) }
 * val periods = listOf("Day", "Week", "Month")
 * FormaSegmentedButtonRow(multiSelect = false) {
 *     periods.forEachIndexed { index, label ->
 *         FormaSegmentedButton(
 *             selected = selectedPeriod == index,
 *             onClick = { selectedPeriod = index },
 *             index = index,
 *             count = periods.size,
 *             label = { Text(label) },
 *         )
 *     }
 * }
 * ```
 *
 * @param multiSelect `false` for mutually-exclusive (single-choice) selection, `true` for
 *   independent per-segment toggles (multi-choice).
 * @param modifier the [Modifier] applied to the row.
 * @param content the row's segments, typically [FormaSegmentedButton]s.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSegmentedButtonRow(
    multiSelect: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable FormaSegmentedButtonRowScope.() -> Unit,
) {
    if (multiSelect) {
        MultiChoiceSegmentedButtonRow(modifier = modifier) {
            val scope = remember(this) { FormaSegmentedButtonRowScopeWrapper(this) }
            scope.content()
        }
    } else {
        SingleChoiceSegmentedButtonRow(modifier = modifier) {
            val scope = remember(this) { FormaSegmentedButtonRowScopeWrapper(this) }
            scope.content()
        }
    }
}

/**
 * A single-choice segment in a [FormaSegmentedButtonRow] (`multiSelect = false`), delegating to
 * Material 3's `SegmentedButton` with `RadioButton` semantics.
 *
 * @param selected whether this segment is the currently selected one.
 * @param onClick called when the segment is clicked. Not invoked while [enabled] is `false`.
 * @param index this segment's position within the row (`0`-based) — used to compute the correct
 *   start/middle/end corner shape.
 * @param count the total number of segments in the row — used to compute the correct
 *   start/middle/end corner shape.
 * @param modifier the [Modifier] applied to the segment.
 * @param enabled whether the segment is interactive.
 * @param icon the segment's leading icon slot. Defaults to Material 3's crossfading checkmark,
 *   shown only while [selected].
 * @param label the segment's content, typically a `Text`.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSegmentedButtonRowScope.FormaSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(selected) },
    label: @Composable () -> Unit,
) {
    SegmentedButton(
        selected = selected,
        onClick = onClick,
        shape = SegmentedButtonDefaults.itemShape(index = index, count = count),
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        label = label,
    )
}

/**
 * A multi-choice segment in a [FormaSegmentedButtonRow] (`multiSelect = true`), delegating to
 * Material 3's `SegmentedButton` with independent toggle semantics.
 *
 * @param checked whether this segment is currently checked.
 * @param onCheckedChange called with the requested new checked state when the segment is clicked.
 *   Not invoked while [enabled] is `false`.
 * @param index this segment's position within the row (`0`-based) — used to compute the correct
 *   start/middle/end corner shape.
 * @param count the total number of segments in the row — used to compute the correct
 *   start/middle/end corner shape.
 * @param modifier the [Modifier] applied to the segment.
 * @param enabled whether the segment is interactive.
 * @param icon the segment's leading icon slot. Defaults to Material 3's crossfading checkmark,
 *   shown only while [checked].
 * @param label the segment's content, typically a `Text`.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSegmentedButtonRowScope.FormaSegmentedButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(checked) },
    label: @Composable () -> Unit,
) {
    SegmentedButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        shape = SegmentedButtonDefaults.itemShape(index = index, count = count),
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        label = label,
    )
}
