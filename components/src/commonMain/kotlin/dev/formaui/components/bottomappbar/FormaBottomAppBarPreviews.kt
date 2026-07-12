/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.bottomappbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.components.fab.FormaFabSize
import dev.formaui.components.fab.FormaFloatingActionButton
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaBottomAppBar] with actions and an embedded FAB.
 */
@Preview
@Composable
private fun FormaBottomAppBarPreview() {
    FormaTheme {
        Surface {
            FormaBottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    Text("🔍")
                    Text("⋮")
                },
                floatingActionButton = {
                    FormaFloatingActionButton(onClick = {}, size = FormaFabSize.Regular) {
                        Text("+")
                    }
                },
            )
        }
    }
}
