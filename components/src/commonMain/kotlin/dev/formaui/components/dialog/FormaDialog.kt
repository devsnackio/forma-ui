/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A FormaUI alert dialog — a modal that interrupts the user for a decision or acknowledgement,
 * delegating to Material 3's `AlertDialog`.
 *
 * State is hoisted: the dialog is shown while it is composed, and [onDismissRequest] fires when
 * the user taps outside it or presses back. Supply [confirmButton] (and optionally [dismissButton])
 * — typically FormaUI [FormaButton]s.
 *
 * ```
 * if (showDialog) {
 *     FormaAlertDialog(
 *         onDismissRequest = { showDialog = false },
 *         title = "Delete payment?",
 *         text = "This action can't be undone.",
 *         confirmButton = { FormaButton(onClick = ::delete) { Text("Delete") } },
 *         dismissButton = { FormaButton(onClick = { showDialog = false }, variant = FormaButtonVariant.Text) { Text("Cancel") } },
 *     )
 * }
 * ```
 *
 * @param onDismissRequest called when the dialog is dismissed by tapping outside or pressing back.
 * @param confirmButton the primary action (required).
 * @param modifier the [Modifier] applied to the dialog surface.
 * @param dismissButton the optional secondary/cancel action.
 * @param icon optional icon shown above the title.
 * @param title optional dialog title.
 * @param text optional supporting body text.
 * @param shape the dialog container shape (defaults to [FormaDialogDefaults.shape]).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    title: String? = null,
    text: String? = null,
    shape: Shape = FormaDialogDefaults.shape,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        icon = icon,
        title = title?.let { value -> { Text(value) } },
        text = text?.let { value -> { Text(value) } },
        shape = shape,
    )
}

/**
 * A FormaUI full-screen dialog — a modal that takes over the whole screen for a focused,
 * multi-step task (e.g. a create/edit form), per the Material full-screen dialog pattern.
 *
 * A top bar hosts a **Close** action ([onDismissRequest]), the [title], and an optional
 * [confirmAction] (e.g. "Save"); [content] is laid out in a scrollable [ColumnScope] below.
 *
 * @param onDismissRequest called when the user closes the dialog (Close button or back press).
 * @param title the dialog's title, shown in the top bar.
 * @param modifier the [Modifier] applied to the dialog surface.
 * @param confirmAction optional top-bar action slot at the end (e.g. a "Save" [FormaButton]).
 * @param content the dialog body, laid out in a scrollable [ColumnScope].
 */
@ExperimentalFormaUiApi
@Composable
fun FormaFullScreenDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    confirmAction: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = FormaTheme.spacing.sm,
                            vertical = FormaTheme.spacing.xs,
                        ),
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FormaButton(
                            onClick = onDismissRequest,
                            variant = FormaButtonVariant.Text,
                        ) {
                            Text("Close")
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f),
                        )
                        confirmAction?.invoke()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(FormaTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                    content = content,
                )
            }
        }
    }
}

/**
 * Default values used by the FormaUI dialogs.
 */
@ExperimentalFormaUiApi
object FormaDialogDefaults {
    /** The default alert-dialog [Shape]: FormaUI's large corner tier. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.large
}
