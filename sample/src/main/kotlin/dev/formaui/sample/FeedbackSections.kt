/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.dialog.FormaAlertDialog
import dev.formaui.components.dialog.FormaFullScreenDialog
import dev.formaui.components.divider.FormaDivider
import dev.formaui.components.emptystate.FormaEmptyState
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.loading.FormaLoadingIndicator
import dev.formaui.components.loading.FormaLoadingIndicatorVariant
import dev.formaui.components.pulltorefresh.FormaPullToRefresh
import dev.formaui.components.snackbar.FormaSnackbar
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** The **Feedback** category: Dialog, BottomSheet, Snackbar, LoadingIndicator, EmptyState. */

@Composable
fun DialogShowcase() {
    ComponentShowcase(
        name = "Dialog",
        description = "An alert dialog and a full-screen dialog for focused tasks.",
    ) {
        var showAlert by remember { mutableStateOf(false) }
        var showFullScreen by remember { mutableStateOf(false) }

        Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs)) {
            FormaButton(onClick = { showAlert = true }, variant = FormaButtonVariant.Outlined) {
                Text("Alert dialog")
            }
            FormaButton(onClick = { showFullScreen = true }, variant = FormaButtonVariant.Outlined) {
                Text("Full-screen")
            }
        }

        if (showAlert) {
            FormaAlertDialog(
                onDismissRequest = { showAlert = false },
                title = "Delete payment?",
                text = "This action can't be undone.",
                confirmButton = {
                    FormaButton(onClick = { showAlert = false }) { Text("Delete") }
                },
                dismissButton = {
                    FormaButton(onClick = { showAlert = false }, variant = FormaButtonVariant.Text) {
                        Text("Cancel")
                    }
                },
            )
        }

        if (showFullScreen) {
            FormaFullScreenDialog(
                onDismissRequest = { showFullScreen = false },
                title = "New payment",
                confirmAction = {
                    FormaButton(onClick = { showFullScreen = false }, variant = FormaButtonVariant.Text) {
                        Text("Save")
                    }
                },
            ) {
                Text("A full-screen dialog for focused, multi-step tasks.")
                FormaTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = "Amount",
                    singleLine = true,
                )
            }
        }
    }
}

@Composable
fun BottomSheetShowcase() {
    ComponentShowcase(
        name = "BottomSheet",
        description = "A modal sheet that slides up from the bottom.",
    ) {
        var showSheet by remember { mutableStateOf(false) }
        FormaButton(onClick = { showSheet = true }, variant = FormaButtonVariant.Outlined) {
            Text("Show bottom sheet")
        }
        if (showSheet) {
            FormaBottomSheet(onDismissRequest = { showSheet = false }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                ) {
                    Text("Payment options", style = MaterialTheme.typography.titleMedium)
                    Text("Choose how you'd like to pay.")
                    FormaButton(
                        onClick = { showSheet = false },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}

@Composable
fun SnackbarShowcase() {
    ComponentShowcase(
        name = "Snackbar",
        description = "Brief, transient feedback, with an optional action.",
    ) {
        FormaSnackbar(message = "Changes saved.")
        FormaSnackbar(message = "Message deleted.", actionLabel = "Undo", onAction = {})
    }
}

@Composable
fun LoadingShowcase() {
    ComponentShowcase(
        name = "LoadingIndicator",
        description = "Circular and linear, both determinate and indeterminate.",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
            FormaLoadingIndicator(contentDescription = "Loading")
            FormaLoadingIndicator(progress = 0.6f, contentDescription = "60 percent loaded")
        }
        FormaLoadingIndicator(
            modifier = Modifier.fillMaxWidth(),
            variant = FormaLoadingIndicatorVariant.Linear,
            contentDescription = "Loading",
        )
        FormaLoadingIndicator(
            modifier = Modifier.fillMaxWidth(),
            variant = FormaLoadingIndicatorVariant.Linear,
            progress = 0.6f,
            contentDescription = "60 percent loaded",
        )
    }
}

@Composable
fun PullToRefreshShowcase() {
    ComponentShowcase(
        name = "PullToRefresh",
        description = "Pull the list down to trigger a refresh; it resolves after a moment.",
    ) {
        var refreshing by remember { mutableStateOf(false) }
        var refreshCount by remember { mutableIntStateOf(0) }
        val scope = rememberCoroutineScope()

        FormaPullToRefresh(
            isRefreshing = refreshing,
            onRefresh = {
                refreshing = true
                scope.launch {
                    delay(1200)
                    refreshCount++
                    refreshing = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                repeat(6) { i ->
                    FormaListItem(
                        headline = "Item ${i + 1}",
                        supporting = "Refreshed $refreshCount time(s)",
                    )
                    FormaDivider()
                }
            }
        }
    }
}

@Composable
fun EmptyStateShowcase() {
    ComponentShowcase(
        name = "EmptyState",
        description = "A placeholder for a screen with no content yet.",
    ) {
        FormaEmptyState(
            title = "No transactions yet",
            description = "Your transactions will appear here once you make your first payment.",
            modifier = Modifier.fillMaxWidth(),
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                    contentDescription = "No transactions",
                    modifier = Modifier.size(48.dp),
                )
            },
            action = {
                FormaButton(onClick = {}) { Text("Add payment") }
            },
        )
    }
}
