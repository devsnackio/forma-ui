/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI modal bottom sheet — content that slides up from the bottom over a scrim, delegating
 * to Material 3's `ModalBottomSheet`.
 *
 * Shown while composed; dismissed via [onDismissRequest] (scrim tap, back press, or swipe down).
 * Drive visibility by conditionally composing it:
 *
 * ```
 * if (open) {
 *     FormaBottomSheet(onDismissRequest = { open = false }) {
 *         // sheet content, laid out in a ColumnScope
 *     }
 * }
 * ```
 *
 * The Material 3 `SheetState` is created and managed internally so callers only need FormaUI's
 * opt-in (not `ExperimentalMaterial3Api`). Set [skipPartiallyExpanded] for tall sheets that should
 * open straight to fully-expanded.
 *
 * @param onDismissRequest called when the sheet is dismissed.
 * @param modifier the [Modifier] applied to the sheet.
 * @param skipPartiallyExpanded when `true`, the sheet has no half-expanded state and opens fully.
 * @param shape the sheet container shape. When `null`, Material 3's top-rounded default is used.
 * @param containerColor the sheet's background color (defaults to the M3 bottom-sheet container,
 * themed by [FormaTheme][dev.formaui.core.theme.FormaTheme]).
 * @param contentColor the preferred content color inside the sheet (defaults to the color matching
 * [containerColor]).
 * @param scrimColor the color of the scrim that obscures content behind the sheet (defaults to the
 * M3 default scrim).
 * @param content the sheet's content, laid out in a [ColumnScope].
 */
@ExperimentalFormaUiApi
@Composable
fun FormaBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = false,
    shape: Shape? = null,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape ?: BottomSheetDefaults.ExpandedShape,
        containerColor = containerColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
        content = content,
    )
}
