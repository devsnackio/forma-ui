/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.dialog.FormaAlertDialog
import dev.formaui.components.dialog.FormaFullScreenDialog
import dev.formaui.components.menu.FormaDropdownMenu
import dev.formaui.components.menu.FormaDropdownMenuItem
import dev.formaui.components.snackbar.FormaSnackbar
import dev.formaui.components.snackbar.FormaSnackbarHost
import dev.formaui.components.tooltip.FormaTooltip
import dev.formaui.components.tooltip.FormaTooltipVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.launch

/** Live preview for `dialog`: buttons open a dismissible alert dialog and a full-screen dialog. */
@Composable
internal fun ColumnScope.DialogPreview() {
    var showAlert by remember { mutableStateOf(false) }
    var showFullScreen by remember { mutableStateOf(false) }
    var lastResult by remember { mutableStateOf("none yet") }

    Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs)) {
        FormaButton(onClick = { showAlert = true }) { Text("Alert dialog") }
        FormaButton(onClick = { showFullScreen = true }, variant = FormaButtonVariant.Tonal) {
            Text("Full-screen")
        }
    }
    Text(
        text = "Last result: $lastResult",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (showAlert) {
        FormaAlertDialog(
            onDismissRequest = {
                showAlert = false
                lastResult = "dismissed"
            },
            title = "Delete payment?",
            text = "This action can't be undone.",
            confirmButton = {
                FormaButton(onClick = {
                    showAlert = false
                    lastResult = "deleted"
                }) { Text("Delete") }
            },
            dismissButton = {
                FormaButton(
                    onClick = {
                        showAlert = false
                        lastResult = "cancelled"
                    },
                    variant = FormaButtonVariant.Text,
                ) { Text("Cancel") }
            },
        )
    }

    if (showFullScreen) {
        FormaFullScreenDialog(
            onDismissRequest = {
                showFullScreen = false
                lastResult = "closed"
            },
            title = "New payment",
            confirmAction = {
                FormaButton(onClick = {
                    showFullScreen = false
                    lastResult = "saved"
                }) { Text("Save") }
            },
        ) {
            Text("A focused, full-screen task lives here.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/** Live preview for `bottom-sheet`: a button opens a modal sheet; scrim tap or swipe dismisses it. */
@Composable
internal fun ColumnScope.BottomSheetPreview() {
    var open by remember { mutableStateOf(false) }

    FormaButton(onClick = { open = true }) { Text("Show sheet") }
    Text(
        text = "Opens a modal sheet over this canvas; tap the scrim or swipe down to dismiss.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (open) {
        FormaBottomSheet(onDismissRequest = { open = false }) {
            Text("Payment options", style = MaterialTheme.typography.titleMedium)
            Text("Choose how you'd like to pay.", style = MaterialTheme.typography.bodyMedium)
            FormaButton(onClick = { open = false }, modifier = Modifier.fillMaxWidth()) {
                Text("Continue")
            }
        }
    }
}

/** Live preview for `snackbar`: the static visual plus a host that queues real transient snackbars. */
@Composable
internal fun ColumnScope.SnackbarPreview() {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var shown by remember { mutableIntStateOf(0) }

    FormaSnackbar(message = "Message deleted.", actionLabel = "Undo", onAction = {})

    FormaButton(onClick = {
        shown++
        scope.launch { hostState.showSnackbar("Saved draft #$shown.", actionLabel = "Undo") }
    }) { Text("Show snackbar") }

    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
        FormaSnackbarHost(
            hostState = hostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

/** Live preview for `dropdown-menu`: an anchored menu with icons and a disabled item. */
@Composable
internal fun ColumnScope.DropdownMenuPreview() {
    var expanded by remember { mutableStateOf(false) }
    var lastAction by remember { mutableStateOf("none yet") }

    Box {
        FormaButton(onClick = { expanded = true }, variant = FormaButtonVariant.Tonal) {
            Text("Open menu")
        }
        FormaDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            FormaDropdownMenuItem(
                text = "Share",
                onClick = {
                    lastAction = "Share"
                    expanded = false
                },
                leadingIcon = { Text("🔗") },
            )
            FormaDropdownMenuItem(
                text = "Rename",
                onClick = {
                    lastAction = "Rename"
                    expanded = false
                },
            )
            FormaDropdownMenuItem(text = "Archive", onClick = {}, enabled = false)
            FormaDropdownMenuItem(
                text = "Delete",
                onClick = {
                    lastAction = "Delete"
                    expanded = false
                },
                leadingIcon = { Text("🗑") },
            )
        }
    }

    Text(
        text = "Last action: $lastAction",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `tooltip`: Plain and Rich tooltips shown on hover, long-press, or button click. */
@Composable
internal fun ColumnScope.TooltipPreview() {
    val scope = rememberCoroutineScope()
    val plainState = rememberTooltipState()
    val richState = rememberTooltipState(isPersistent = true)

    Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
        FormaTooltip(text = "A plain tooltip.", state = plainState) {
            FormaButton(
                onClick = { scope.launch { plainState.show() } },
                variant = FormaButtonVariant.Tonal,
            ) { Text("Plain") }
        }
        FormaTooltip(
            text = "Rich tooltips take a title and an action.",
            variant = FormaTooltipVariant.Rich,
            state = richState,
            title = { Text("Rich tooltip") },
            action = {
                FormaButton(onClick = { richState.dismiss() }, variant = FormaButtonVariant.Text) {
                    Text("Got it")
                }
            },
        ) {
            FormaButton(
                onClick = { scope.launch { richState.show() } },
                variant = FormaButtonVariant.Tonal,
            ) { Text("Rich") }
        }
    }

    Text(
        text = "Hover, long-press, or click either button to show its tooltip.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
