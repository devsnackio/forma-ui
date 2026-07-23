/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.components.checkbox.FormaCheckbox
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.radiobutton.FormaRadioButton
import dev.formaui.components.search.FormaSearchBar
import dev.formaui.components.slider.FormaSlider
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.components.textfield.FormaTextFieldVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/** Live preview for `text-field`: an editable Outlined field and a Filled field with live error state. */
@Composable
internal fun ColumnScope.TextFieldPreview() {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val amountInvalid = amount.isNotEmpty() && amount.toFloatOrNull() == null

    FormaTextField(
        value = name,
        onValueChange = { name = it },
        modifier = Modifier.fillMaxWidth(),
        label = "Name",
        placeholder = "Ada Lovelace",
        helperText = "Outlined (default) — type to see live state.",
        singleLine = true,
    )
    FormaTextField(
        value = amount,
        onValueChange = { amount = it },
        modifier = Modifier.fillMaxWidth(),
        variant = FormaTextFieldVariant.Filled,
        label = "Amount",
        isError = amountInvalid,
        helperText = "Filled variant — numbers only.",
        errorText = "That's not a number.",
        singleLine = true,
    )
}

/** Live preview for `switch`: an interactive toggle plus a disabled example. */
@Composable
internal fun ColumnScope.SwitchPreview() {
    var on by remember { mutableStateOf(true) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormaSwitch(checked = on, onCheckedChange = { on = it })
        Text(
            text = if (on) "Notifications on" else "Notifications off",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormaSwitch(checked = true, onCheckedChange = null, enabled = false)
        Text(
            text = "Disabled",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Live preview for `checkbox`: three independently toggleable rows with a live selection count. */
@Composable
internal fun ColumnScope.CheckboxPreview() {
    val labels = remember { listOf("Email updates", "Push alerts", "Monthly report") }
    val checked = remember { mutableStateListOf(true, false, false) }

    labels.forEachIndexed { index, label ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaCheckbox(checked = checked[index], onCheckedChange = { checked[index] = it })
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }

    Text(
        text = "${checked.count { it }} of ${labels.size} selected.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `radio-button`: a three-option single-choice group. */
@Composable
internal fun ColumnScope.RadioButtonPreview() {
    val options = remember { listOf("Standard", "Priority", "Overnight") }
    var selected by remember { mutableStateOf(options.first()) }

    Column(modifier = Modifier.selectableGroup()) {
        options.forEach { option ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FormaRadioButton(selected = option == selected, onClick = { selected = option })
                Text(option, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    Text(
        text = "Shipping: $selected",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `slider`: a continuous slider and a stepped slider, both draggable. */
@Composable
internal fun ColumnScope.SliderPreview() {
    var value by remember { mutableFloatStateOf(0.4f) }
    FormaSlider(
        value = value,
        onValueChange = { value = it },
        modifier = Modifier.fillMaxWidth(),
    )
    Text(
        text = "Continuous: ${(value * 100).toInt()}%",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    var stepped by remember { mutableFloatStateOf(2f) }
    FormaSlider(
        value = stepped,
        onValueChange = { stepped = it },
        modifier = Modifier.fillMaxWidth(),
        valueRange = 0f..4f,
        steps = 3,
    )
    Text(
        text = "Stepped: ${stepped.toInt()} of 4",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Live preview for `search-bar`: a Docked search bar with a filterable suggestion list. */
@Composable
internal fun ColumnScope.SearchBarPreview() {
    val contacts = remember { listOf("Ada Lovelace", "Alan Turing", "Grace Hopper", "Katherine Johnson") }
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    FormaSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { expanded = false },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        placeholder = { Text("Search contacts") },
        leadingIcon = { Text("🔍") },
    ) {
        contacts.filter { it.contains(query, ignoreCase = true) }.forEach { contact ->
            FormaListItem(
                headline = contact,
                onClick = {
                    query = contact
                    expanded = false
                },
            )
        }
    }

    Text(
        text = "Docked variant — focus the field to expand the suggestions.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
