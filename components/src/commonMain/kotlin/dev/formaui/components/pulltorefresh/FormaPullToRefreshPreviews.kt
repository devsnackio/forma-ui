/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.pulltorefresh

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of [FormaPullToRefresh]: the idle state (no refresh in progress) and the refreshing state
 * (indicator spinning).
 */
@Preview
@Composable
private fun FormaPullToRefreshPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
            ) {
                Text("Idle")
                FormaPullToRefresh(
                    isRefreshing = false,
                    onRefresh = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FormaTheme.spacing.xxl),
                ) {
                    Column(Modifier.fillMaxSize().padding(FormaTheme.spacing.sm)) {
                        Text("Pull down to refresh")
                    }
                }

                Text("Refreshing")
                FormaPullToRefresh(
                    isRefreshing = true,
                    onRefresh = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FormaTheme.spacing.xxl),
                ) {
                    Column(Modifier.fillMaxSize().padding(FormaTheme.spacing.sm)) {
                        Text("Refreshing…")
                    }
                }
            }
        }
    }
}
