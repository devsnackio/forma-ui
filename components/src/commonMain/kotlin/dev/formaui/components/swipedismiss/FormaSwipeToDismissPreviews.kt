/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.swipedismiss

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of [FormaSwipeToDismiss]: a row whose content sits over a colored "delete" background slot
 * that is revealed as the row is swiped.
 */
@Preview
@Composable
private fun FormaSwipeToDismissPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                val state = rememberSwipeToDismissBoxState()
                FormaSwipeToDismiss(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = FormaTheme.spacing.md),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                "Delete",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                        }
                    },
                ) {
                    Surface {
                        Text(
                            "Swipe to dismiss",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(FormaTheme.spacing.md),
                        )
                    }
                }
            }
        }
    }
}
