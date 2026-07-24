/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.tooltip

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.PopupPositionProvider
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The visual weight of a [FormaTooltip], mirroring Material 3's two tooltip surfaces.
 */
@ExperimentalFormaUiApi
enum class FormaTooltipVariant {
    /** A single line of supporting text. Use for a brief label or hint. */
    Plain,

    /**
     * A larger surface that can carry an optional title and action alongside the body text (see
     * [FormaTooltip]'s `title`/`action` parameters). Use when the tooltip needs to do more than
     * label the anchor.
     */
    Rich,
}

/**
 * A FormaUI tooltip — "Material 3 with better defaults", wrapping Material 3's `TooltipBox` +
 * `PlainTooltip`/`RichTooltip`.
 *
 * [content] is the anchor: the element the tooltip attaches to and that triggers it on long-press
 * or hover. [text] is the tooltip's message, shown for both variants; [FormaTooltipVariant.Rich]
 * additionally accepts an optional [title] and [action]. Visibility is hoisted through [state] —
 * pass a remembered [TooltipState] to inspect or drive it programmatically (e.g. `state.dismiss()`
 * after the [action] fires):
 *
 * ```
 * val tooltipState = rememberTooltipState()
 * FormaTooltip(
 *     text = "Adds the item to your cart",
 *     state = tooltipState,
 * ) {
 *     FormaIconButton(onClick = {}) {
 *         Icon(Icons.Default.AddShoppingCart, contentDescription = "Add to cart")
 *     }
 * }
 *
 * FormaTooltip(
 *     text = "You can undo this for the next 10 seconds.",
 *     variant = FormaTooltipVariant.Rich,
 *     title = { Text("Item archived") },
 *     action = { FormaButton(onClick = { tooltipState.dismiss() }) { Text("Undo") } },
 * ) {
 *     FormaIconButton(onClick = {}) {
 *         Icon(Icons.Default.Archive, contentDescription = "Archive")
 *     }
 * }
 * ```
 *
 * @param text the tooltip's message.
 * @param modifier the [Modifier] applied to the anchor ([content]).
 * @param variant the tooltip surface (defaults to [FormaTooltipVariant.Plain]).
 * @param state hoisted visibility state; controls and reports whether the tooltip is currently
 *   shown (defaults to a freshly remembered, non-persistent [TooltipState]).
 * @param title an optional title shown above [text]. Only used when [variant] is
 *   [FormaTooltipVariant.Rich]; ignored for [FormaTooltipVariant.Plain].
 * @param action an optional action (typically a `TextButton`) shown below [text]. Only used when
 *   [variant] is [FormaTooltipVariant.Rich]; ignored for [FormaTooltipVariant.Plain]. When
 *   non-null, the tooltip becomes persistent-friendly: Material 3 keeps it focusable so
 *   accessibility services can reach the action.
 * @param showCaret whether a caret pointing at the anchor is drawn on the tooltip container.
 *   FormaUI defaults this to `true` (Material 3's own default is no caret) since it makes the
 *   anchor relationship unambiguous.
 * @param positionProvider controls where the tooltip is placed relative to [content]. When `null`,
 *   it is placed above the anchor via `TooltipDefaults.rememberTooltipPositionProvider`.
 * @param onDismissRequest called when the user clicks outside the tooltip while it is shown. When
 *   `null`, Material 3's default dismiss-on-outside-click behavior applies.
 * @param enableUserInput whether long-press and hover on [content] trigger the tooltip through
 *   [state]. Set to `false` if you drive [state] entirely programmatically.
 * @param colors the container/content/title/action colors used by [FormaTooltipVariant.Rich].
 *   When `null`, the Material 3 default rich tooltip colors are used. Has no effect on
 *   [FormaTooltipVariant.Plain].
 * @param containerColor the tooltip's background color for [FormaTooltipVariant.Plain] (defaults to
 *   the M3 plain-tooltip container). Has no effect on [FormaTooltipVariant.Rich] — use [colors] there.
 * @param contentColor the tooltip's content color for [FormaTooltipVariant.Plain] (defaults to the
 *   M3 plain-tooltip content color). Has no effect on [FormaTooltipVariant.Rich] — use [colors] there.
 * @param textStyle optional [TextStyle] override for the tooltip's body [text] (both variants),
 *   merged on top of the M3 tooltip body style.
 * @param content the anchor content that the tooltip attaches to.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTooltip(
    text: String,
    modifier: Modifier = Modifier,
    variant: FormaTooltipVariant = FormaTooltipVariant.Plain,
    state: TooltipState = rememberTooltipState(),
    title: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    showCaret: Boolean = true,
    positionProvider: PopupPositionProvider? = null,
    onDismissRequest: (() -> Unit)? = null,
    enableUserInput: Boolean = true,
    colors: RichTooltipColors? = null,
    containerColor: Color = TooltipDefaults.plainTooltipContainerColor,
    contentColor: Color = TooltipDefaults.plainTooltipContentColor,
    textStyle: TextStyle? = null,
    content: @Composable () -> Unit,
) {
    val resolvedPositionProvider = positionProvider
        ?: TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above)
    val caretShape: Shape? = if (showCaret) FormaTooltipDefaults.caretShape else null

    TooltipBox(
        positionProvider = resolvedPositionProvider,
        state = state,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        enableUserInput = enableUserInput,
        hasAction = variant == FormaTooltipVariant.Rich && action != null,
        tooltip = {
            when (variant) {
                FormaTooltipVariant.Plain -> PlainTooltip(
                    caretShape = caretShape,
                    contentColor = contentColor,
                    containerColor = containerColor,
                ) {
                    Text(text, style = LocalTextStyle.current.merge(textStyle))
                }

                FormaTooltipVariant.Rich -> RichTooltip(
                    title = title,
                    action = action,
                    caretShape = caretShape,
                    colors = colors ?: TooltipDefaults.richTooltipColors(),
                ) {
                    Text(text, style = LocalTextStyle.current.merge(textStyle))
                }
            }
        },
        content = content,
    )
}

/**
 * Default values used by [FormaTooltip]. Override any of these per call site, or read them to
 * build a customised tooltip that still inherits FormaUI's defaults.
 */
@ExperimentalFormaUiApi
object FormaTooltipDefaults {
    /** The default caret shape drawn when `showCaret` is `true`. */
    val caretShape: Shape
        get() = TooltipDefaults.caretShape()

    /** The default maximum width for [FormaTooltipVariant.Plain] tooltips. */
    val plainMaxWidth: Dp = TooltipDefaults.plainTooltipMaxWidth

    /** The default maximum width for [FormaTooltipVariant.Rich] tooltips. */
    val richMaxWidth: Dp = TooltipDefaults.richTooltipMaxWidth
}
