/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.topappbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaTopAppBar]: all four variants, with a navigation icon and an action slot.
 */
@Preview
@Composable
private fun FormaTopAppBarVariantsPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
            ) {
                FormaTopAppBar(
                    title = "Small",
                    variant = FormaTopAppBarVariant.Small,
                    navigationIcon = { Text("‹") },
                    actions = { Text("⋮") },
                )
                FormaTopAppBar(
                    title = "Center-aligned",
                    variant = FormaTopAppBarVariant.CenterAligned,
                    navigationIcon = { Text("‹") },
                    actions = { Text("⋮") },
                )
                FormaTopAppBar(
                    title = "Medium",
                    variant = FormaTopAppBarVariant.Medium,
                    navigationIcon = { Text("‹") },
                    actions = { Text("⋮") },
                )
                FormaTopAppBar(
                    title = "Large",
                    variant = FormaTopAppBarVariant.Large,
                    navigationIcon = { Text("‹") },
                    actions = { Text("⋮") },
                )
            }
        }
    }
}
