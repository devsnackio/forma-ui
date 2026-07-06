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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaSlider]: a continuous slider and a stepped (discrete) slider.
 */
@Preview
@Composable
private fun FormaSliderPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                var continuous by remember { mutableFloatStateOf(0.4f) }
                Text("Continuous: ${(continuous * 100).toInt()}%")
                FormaSlider(
                    value = continuous,
                    onValueChange = { continuous = it },
                    modifier = Modifier.fillMaxWidth(),
                )

                var stepped by remember { mutableFloatStateOf(2f) }
                Text("Stepped: ${stepped.toInt()}")
                FormaSlider(
                    value = stepped,
                    onValueChange = { stepped = it },
                    valueRange = 0f..4f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
