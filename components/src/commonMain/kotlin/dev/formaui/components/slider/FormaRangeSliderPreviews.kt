/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
 * Preview of [FormaRangeSlider]: a continuous range and a stepped (discrete) range.
 */
@Preview
@Composable
private fun FormaRangeSliderPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                var continuous by remember { mutableStateOf(0.2f..0.8f) }
                Text("Continuous: ${(continuous.start * 100).toInt()}%–${(continuous.endInclusive * 100).toInt()}%")
                FormaRangeSlider(
                    value = continuous,
                    onValueChange = { continuous = it },
                    modifier = Modifier.fillMaxWidth(),
                )

                var stepped by remember { mutableStateOf(2f..6f) }
                Text("Stepped: ${stepped.start.toInt()}–${stepped.endInclusive.toInt()}")
                FormaRangeSlider(
                    value = stepped,
                    onValueChange = { stepped = it },
                    valueRange = 0f..8f,
                    steps = 7,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
