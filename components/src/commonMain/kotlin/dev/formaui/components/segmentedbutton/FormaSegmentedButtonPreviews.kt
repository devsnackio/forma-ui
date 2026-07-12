/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.segmentedbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaSegmentedButtonRow]: a single-choice row (`multiSelect = false`), a
 * multi-choice row (`multiSelect = true`), and a row with a disabled segment.
 */
@Preview
@Composable
private fun FormaSegmentedButtonRowPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
            ) {
                val periods = listOf("Day", "Week", "Month")
                var selectedPeriod by remember { mutableStateOf(0) }
                FormaSegmentedButtonRow(multiSelect = false) {
                    periods.forEachIndexed { index, label ->
                        FormaSegmentedButton(
                            selected = selectedPeriod == index,
                            onClick = { selectedPeriod = index },
                            index = index,
                            count = periods.size,
                            label = { Text(label) },
                        )
                    }
                }

                val styles = listOf("Bold", "Italic", "Underline")
                val checkedStyles = remember { mutableStateListOf(true, false, false) }
                FormaSegmentedButtonRow(multiSelect = true) {
                    styles.forEachIndexed { index, label ->
                        FormaSegmentedButton(
                            checked = checkedStyles[index],
                            onCheckedChange = { checkedStyles[index] = it },
                            index = index,
                            count = styles.size,
                            label = { Text(label) },
                        )
                    }
                }

                FormaSegmentedButtonRow(multiSelect = false) {
                    FormaSegmentedButton(
                        selected = true,
                        onClick = {},
                        index = 0,
                        count = 2,
                        label = { Text("Enabled") },
                    )
                    FormaSegmentedButton(
                        selected = false,
                        onClick = {},
                        index = 1,
                        count = 2,
                        enabled = false,
                        label = { Text("Disabled") },
                    )
                }
            }
        }
    }
}
