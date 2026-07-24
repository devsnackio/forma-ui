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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.formaui.components.autocomplete.FormaExposedDropdownMenu
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.checkbox.FormaCheckbox
import dev.formaui.components.datepicker.FormaDatePickerSheet
import dev.formaui.components.datepicker.FormaDateRangePickerSheet
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.radiobutton.FormaRadioButton
import dev.formaui.components.search.FormaSearchBar
import dev.formaui.components.search.FormaSearchBarVariant
import dev.formaui.components.slider.FormaRangeSlider
import dev.formaui.components.slider.FormaSlider
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.components.textfield.FormaTextFieldVariant
import dev.formaui.components.timepicker.FormaTimePickerSheet
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * The **Inputs** category: TextField, SearchBar, Switch/Checkbox/RadioButton, Slider, and the
 * date picker sheets.
 */

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
fun ExposedDropdownMenuShowcase() {
    ComponentShowcase(
        name = "ExposedDropdownMenu",
        description = "An autocomplete: type to filter, or a read-only tap-to-select field.",
    ) {
        val fruits = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape")

        var query by remember { mutableStateOf("") }
        var editableExpanded by remember { mutableStateOf(false) }
        val matches = remember(query) {
            if (query.isBlank()) fruits else fruits.filter { it.contains(query, ignoreCase = true) }
        }
        FormaExposedDropdownMenu(
            expanded = editableExpanded,
            onExpandedChange = { editableExpanded = it },
            value = query,
            onValueChange = { query = it },
            options = matches,
            onOptionSelected = {
                query = it
                editableExpanded = false
            },
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
            onOptionSelected = {
                selected = it
                selectExpanded = false
            },
            optionLabel = { it },
            modifier = Modifier.fillMaxWidth(),
            variant = FormaTextFieldVariant.Filled,
            editable = false,
            label = "Fruit (tap to select)",
        )
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

@Composable
fun RangeSliderShowcase() {
    ComponentShowcase(
        name = "RangeSlider",
        description = "A two-thumb slider selecting a start and end value.",
    ) {
        var price by remember { mutableStateOf(20f..80f) }
        Text(
            "Price: \$${price.start.toInt()} – \$${price.endInclusive.toInt()}",
            style = MaterialTheme.typography.bodySmall,
        )
        FormaRangeSlider(
            value = price,
            onValueChange = { price = it },
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth(),
        )

        var rating by remember { mutableStateOf(1f..3f) }
        Text(
            "Rating: ${rating.start.toInt()} – ${rating.endInclusive.toInt()}",
            style = MaterialTheme.typography.bodySmall,
        )
        FormaRangeSlider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 0f..5f,
            steps = 4,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Formats a UTC-epoch-millis date the picker states report (dates are UTC-anchored). */
private fun formatUtcDate(millis: Long): String =
    SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("UTC") }
        .format(Date(millis))

@Composable
fun DatePickerSheetShowcase() {
    ComponentShowcase(
        name = "DatePickerSheet",
        description = "The Material date picker hosted in a modal bottom sheet.",
    ) {
        var showSheet by remember { mutableStateOf(false) }
        var confirmedMillis by remember { mutableStateOf<Long?>(null) }
        val state = rememberDatePickerState()

        FormaButton(onClick = { showSheet = true }, variant = FormaButtonVariant.Outlined) {
            Text("Pick a date")
        }
        Text(
            text = confirmedMillis?.let { "Selected: ${formatUtcDate(it)}" } ?: "No date selected",
            style = MaterialTheme.typography.bodySmall,
        )
        if (showSheet) {
            FormaDatePickerSheet(
                onDismissRequest = { showSheet = false },
                state = state,
                confirmButton = {
                    FormaButton(
                        onClick = {
                            confirmedMillis = state.selectedDateMillis
                            showSheet = false
                        },
                        enabled = state.selectedDateMillis != null,
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    FormaButton(
                        onClick = { showSheet = false },
                        variant = FormaButtonVariant.Text,
                    ) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}

@Composable
fun TimePickerSheetShowcase() {
    ComponentShowcase(
        name = "TimePickerSheet",
        description = "The Material time picker (clock dial + text input) hosted in a modal bottom sheet.",
    ) {
        var showSheet by remember { mutableStateOf(false) }
        var confirmed by remember { mutableStateOf<String?>(null) }
        val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)

        FormaButton(onClick = { showSheet = true }, variant = FormaButtonVariant.Outlined) {
            Text("Pick a time")
        }
        Text(
            text = confirmed?.let { "Selected: $it" } ?: "No time selected",
            style = MaterialTheme.typography.bodySmall,
        )
        if (showSheet) {
            FormaTimePickerSheet(
                onDismissRequest = { showSheet = false },
                state = state,
                confirmButton = {
                    FormaButton(
                        onClick = {
                            confirmed = "%02d:%02d".format(state.hour, state.minute)
                            showSheet = false
                        },
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    FormaButton(
                        onClick = { showSheet = false },
                        variant = FormaButtonVariant.Text,
                    ) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}

@Composable
fun DateRangePickerSheetShowcase() {
    ComponentShowcase(
        name = "DateRangePickerSheet",
        description = "The Material date-range picker hosted in a modal bottom sheet.",
    ) {
        var showSheet by remember { mutableStateOf(false) }
        var confirmedRange by remember { mutableStateOf<Pair<Long, Long>?>(null) }
        val state = rememberDateRangePickerState()

        FormaButton(onClick = { showSheet = true }, variant = FormaButtonVariant.Outlined) {
            Text("Pick a date range")
        }
        Text(
            text = confirmedRange
                ?.let { (start, end) -> "Selected: ${formatUtcDate(start)} – ${formatUtcDate(end)}" }
                ?: "No range selected",
            style = MaterialTheme.typography.bodySmall,
        )
        if (showSheet) {
            FormaDateRangePickerSheet(
                onDismissRequest = { showSheet = false },
                state = state,
                confirmButton = {
                    FormaButton(
                        onClick = {
                            val start = state.selectedStartDateMillis
                            val end = state.selectedEndDateMillis
                            if (start != null && end != null) confirmedRange = start to end
                            showSheet = false
                        },
                        enabled = state.selectedEndDateMillis != null,
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    FormaButton(
                        onClick = { showSheet = false },
                        variant = FormaButtonVariant.Text,
                    ) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}
