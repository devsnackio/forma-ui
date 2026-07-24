/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.autocomplete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.formaui.components.textfield.FormaTextFieldVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of [FormaExposedDropdownMenu]: an editable type-to-filter autocomplete and a read-only
 * tap-to-select field, both collapsed (the expanded menu is a popup that does not render in a
 * static `@Preview`, so these show the anchor field with its rotating chevron).
 */
@Preview
@Composable
private fun FormaExposedDropdownMenuPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                val fruits = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry")

                var query by remember { mutableStateOf("") }
                var editableExpanded by remember { mutableStateOf(false) }
                val matches = fruits.filter { it.contains(query, ignoreCase = true) }
                FormaExposedDropdownMenu(
                    expanded = editableExpanded,
                    onExpandedChange = { editableExpanded = it },
                    value = query,
                    onValueChange = { query = it },
                    options = matches,
                    onOptionSelected = { query = it; editableExpanded = false },
                    optionLabel = { it },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Fruit (type to filter)",
                )

                var selected by remember { mutableStateOf("Banana") }
                var selectExpanded by remember { mutableStateOf(false) }
                FormaExposedDropdownMenu(
                    expanded = selectExpanded,
                    onExpandedChange = { selectExpanded = it },
                    value = selected,
                    onValueChange = {},
                    options = fruits,
                    onOptionSelected = { selected = it; selectExpanded = false },
                    optionLabel = { it },
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaTextFieldVariant.Filled,
                    editable = false,
                    label = "Fruit (tap to select)",
                )
            }
        }
    }
}
