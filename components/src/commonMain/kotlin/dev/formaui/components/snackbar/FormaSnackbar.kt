/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.snackbar

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * A FormaUI snackbar — a brief, transient message at the bottom of the screen, delegating to
 * Material 3's `Snackbar`. Supports a **standard** (message-only) and an **action** variant.
 *
 * This composable renders the snackbar visual directly. To actually surface transient messages,
 * pair a [SnackbarHostState] with [FormaSnackbarHost] (which renders queued messages as
 * `FormaSnackbar`s) and trigger it from a coroutine:
 *
 * ```
 * val hostState = remember { SnackbarHostState() }
 * Scaffold(snackbarHost = { FormaSnackbarHost(hostState) }) { ... }
 * // elsewhere: scope.launch { hostState.showSnackbar("Saved.", actionLabel = "Undo") }
 * ```
 *
 * @param message the text to display.
 * @param modifier the [Modifier] applied to the snackbar.
 * @param actionLabel optional action button label; the action is shown only when both this and
 *   [onAction] are provided.
 * @param onAction optional action callback.
 * @param shape the snackbar container shape (defaults to [FormaSnackbarDefaults.shape]).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    shape: Shape = FormaSnackbarDefaults.shape,
) {
    val action: (@Composable () -> Unit)? = if (actionLabel != null && onAction != null) {
        {
            TextButton(
                onClick = onAction,
                colors = ButtonDefaults.textButtonColors(contentColor = SnackbarDefaults.actionColor),
            ) {
                Text(actionLabel)
            }
        }
    } else {
        null
    }

    Snackbar(
        modifier = modifier,
        action = action,
        shape = shape,
    ) {
        Text(message)
    }
}

/**
 * A host that displays snackbars queued through [hostState], rendering each as a FormaUI-styled
 * [FormaSnackbar] (including its action button when the message has an `actionLabel`).
 *
 * Place it in a `Scaffold`'s `snackbarHost` slot (or anywhere in your layout), then show messages
 * via `hostState.showSnackbar(...)` from a coroutine.
 *
 * @param hostState the [SnackbarHostState] whose current message is displayed.
 * @param modifier the [Modifier] applied to the host.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        FormaSnackbar(
            message = data.visuals.message,
            actionLabel = data.visuals.actionLabel,
            onAction = data.visuals.actionLabel?.let { { data.performAction() } },
        )
    }
}

/**
 * Default values used by [FormaSnackbar].
 */
@ExperimentalFormaUiApi
object FormaSnackbarDefaults {
    /** The default snackbar [Shape]: FormaUI's small corner tier. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.small
}
