/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.badge

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.formaui.core.annotation.ExperimentalFormaUiApi

private const val DefaultMaxCount = 99

/**
 * A FormaUI badge — a small status marker for indicating unread counts or presence.
 *
 * Two forms, chosen by [count]:
 * - **Dot** ([count]` == null`): a small filled dot with no text — signals "something new" without
 *   a number.
 * - **Numeric** ([count]` != null`): shows the number, capped at [maxCount] with a trailing `+`
 *   (e.g. `99+`).
 *
 * A badge is decorative on its own; place it against the element it annotates using Material 3's
 * `BadgedBox`. For accessibility, describe the count on the **annotated element** (e.g. the icon's
 * `contentDescription` = "Messages, 5 unread") rather than on the badge, so screen readers announce
 * it in context.
 *
 * @param modifier the [Modifier] applied to the badge.
 * @param count the number to display, or `null` for a dot badge.
 * @param maxCount the largest number shown verbatim; above it the badge shows `"$maxCount+"`
 *   (defaults to 99). Ignored for dot badges.
 * @param containerColor the badge background color (defaults to the M3 `error` color).
 * @param contentColor the badge text color (defaults to the content color for [containerColor]).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaBadge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    maxCount: Int = DefaultMaxCount,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = contentColorFor(containerColor),
) {
    if (count == null) {
        Badge(
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
        )
    } else {
        Badge(
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
            Text(if (count > maxCount) "$maxCount+" else count.toString())
        }
    }
}

/**
 * Anchors a badge to the top-end corner of some [content] — the FormaUI wrapper over Material 3's
 * `BadgedBox`.
 *
 * Use this to attach a [FormaBadge] (or any badge composable) to an icon, avatar, or label without
 * reaching into Material 3 directly:
 *
 * ```
 * FormaBadgedBox(badge = { FormaBadge(count = unread) }) {
 *     Icon(Icons.Default.Notifications, contentDescription = "Notifications, $unread unread")
 * }
 * ```
 *
 * The badge is decorative, so put the meaningful description (including any count) on the
 * [content] element's `contentDescription` — not on the badge — so it is announced in context.
 *
 * @param badge the badge to draw at the content's top-end corner (typically a [FormaBadge]).
 * @param modifier the [Modifier] applied to the box.
 * @param content the element the badge is anchored to.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    BadgedBox(badge = badge, modifier = modifier, content = content)
}
