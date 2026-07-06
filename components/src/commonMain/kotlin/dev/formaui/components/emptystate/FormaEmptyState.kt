/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.emptystate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A FormaUI empty state — the centered "nothing here yet / no results" placeholder with an
 * optional illustration, a title, a description, and an optional action.
 *
 * Composed from FormaUI tokens (no Material 3 equivalent): the slots stack and center, spaced by
 * [FormaSpacing.md][dev.formaui.core.theme.FormaSpacing.md] and inset by
 * [FormaSpacing.lg][dev.formaui.core.theme.FormaSpacing.lg]. Only [title] is required.
 *
 * ```
 * FormaEmptyState(
 *     title = "No transactions yet",
 *     description = "Your transactions will appear here once you make your first payment.",
 *     icon = { Icon(Icons.Outlined.Receipt, contentDescription = null) },
 *     action = { FormaButton(onClick = ::addPayment) { Text("Add payment") } },
 * )
 * ```
 *
 * @param title the primary message (required).
 * @param modifier the [Modifier] applied to the empty state.
 * @param description optional secondary explanatory text.
 * @param icon optional illustration/icon slot shown above the [title].
 * @param action optional action slot shown below the text (typically a
 *   [FormaButton][dev.formaui.components.button.FormaButton]).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
    action: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(FormaTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
    ) {
        icon?.invoke()

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        action?.invoke()
    }
}
