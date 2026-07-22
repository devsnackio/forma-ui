/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.formaui.components.checkbox.FormaCheckbox
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.radiobutton.FormaRadioButton
import dev.formaui.components.search.FormaSearchBar
import dev.formaui.components.search.FormaSearchBarVariant
import dev.formaui.components.slider.FormaSlider
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.components.textfield.FormaTextFieldVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/** The **Inputs** category: TextField, SearchBar, Switch/Checkbox/RadioButton, Slider. */

@Composable
fun TextFieldShowcase() {
    ComponentShowcase(
        name = "TextField",
        description = "Outlined and filled variants, plus inline validation.",
    ) {
        var text by remember { mutableStateOf("") }
        FormaTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Outlined",
            placeholder = "Type something",
            helperText = "Helper text",
            singleLine = true,
        )
        FormaTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            variant = FormaTextFieldVariant.Filled,
            label = "Filled",
            singleLine = true,
        )
        FormaTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = "With validation",
            isError = text.isBlank(),
            errorText = "This field is required",
            helperText = "Cannot be empty",
            singleLine = true,
        )
    }
}

@Composable
fun SearchBarShowcase() {
    ComponentShowcase(
        name = "SearchBar",
        description = "A docked search bar that expands into a results list.",
    ) {
        val contacts = listOf("Ada Lovelace", "Alan Turing", "Grace Hopper", "Katherine Johnson")
        var query by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        val results = remember(query) {
            if (query.isBlank()) contacts else contacts.filter { it.contains(query, ignoreCase = true) }
        }

        FormaSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { expanded = false },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth(),
            variant = FormaSearchBarVariant.Docked,
            placeholder = { Text("Search contacts") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        ) {
            results.forEach { name ->
                FormaListItem(
                    headline = name,
                    onClick = {
                        query = name
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun SelectionShowcase() {
    ComponentShowcase(
        name = "Switch · Checkbox · RadioButton",
        description = "The three binary/exclusive selection controls.",
    ) {
        var switchOn by remember { mutableStateOf(true) }
        var checked by remember { mutableStateOf(true) }
        val options = listOf("Standard", "Priority", "Overnight")
        var selected by remember { mutableStateOf(options.first()) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaSwitch(checked = switchOn, onCheckedChange = { switchOn = it })
            Text("Notifications ${if (switchOn) "on" else "off"}")
        }

        Row(
            modifier = Modifier.toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = { checked = it },
            ),
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaCheckbox(checked = checked, onCheckedChange = null)
            Text("I agree to the terms")
        }

        Column(modifier = Modifier.selectableGroup()) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = option == selected,
                            role = Role.RadioButton,
                            onClick = { selected = option },
                        ),
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FormaRadioButton(selected = option == selected, onClick = null)
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun SliderShowcase() {
    ComponentShowcase(
        name = "Slider",
        description = "A continuous slider and a stepped, discrete slider.",
    ) {
        var continuous by remember { mutableFloatStateOf(0.4f) }
        Text("Volume: ${(continuous * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
        FormaSlider(
            value = continuous,
            onValueChange = { continuous = it },
            modifier = Modifier.fillMaxWidth(),
        )

        var stepped by remember { mutableFloatStateOf(2f) }
        Text("Rating: ${stepped.toInt()}", style = MaterialTheme.typography.bodySmall)
        FormaSlider(
            value = stepped,
            onValueChange = { stepped = it },
            valueRange = 0f..4f,
            steps = 3,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
