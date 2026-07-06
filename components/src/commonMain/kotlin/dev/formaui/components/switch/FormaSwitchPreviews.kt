/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.switch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaSwitch]: a live toggle plus enabled-on and disabled states.
 */
@Preview
@Composable
private fun FormaSwitchStatesPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                var on by remember { mutableStateOf(false) }
                FormaSwitch(checked = on, onCheckedChange = { on = it })
                FormaSwitch(checked = true, onCheckedChange = {})
                FormaSwitch(checked = true, onCheckedChange = {}, enabled = false)
                FormaSwitch(checked = false, onCheckedChange = {}, enabled = false)
                Text("Tap the first switch to toggle it.")
            }
        }
    }
}
