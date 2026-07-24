/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.autocomplete

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.components.textfield.FormaTextFieldVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * A FormaUI exposed dropdown menu (autocomplete) — a [FormaTextField] anchor that drops a menu of
 * [options] below it, delegating to Material 3's `ExposedDropdownMenuBox`.
 *
 * Fully stateless / hoisted — the caller owns all three pieces of state:
 * - the **query text** ([value] / [onValueChange]),
 * - the **expanded** state ([expanded] / [onExpandedChange]),
 * - and the **already-filtered** [options] list (do your filtering against [value] in the caller).
 *
 * FormaUI provides only the wiring and styling: the anchor field, the rotating chevron trailing
 * icon, the menu, and the per-option rows. Two interaction modes via [editable]:
 * - `editable = true` (default) — **type-to-filter** autocomplete: the field is editable, uses the
 *   `PrimaryEditable` anchor, and the caller narrows [options] as [value] changes.
 * - `editable = false` — **tap-to-select**: the field is read-only, uses the `PrimaryNotEditable`
 *   anchor, and the menu shows the full [options] list. Set [value] from the selection.
 *
 * On selecting a row, [onOptionSelected] is called and the menu collapses; the caller is
 * responsible for reflecting the choice into [value] (e.g. `value = optionLabel(it)`). When
 * [options] is empty while [expanded], a single disabled "No matches" row is shown.
 *
 * ```
 * var text by remember { mutableStateOf("") }
 * var expanded by remember { mutableStateOf(false) }
 * val matches = countries.filter { it.contains(text, ignoreCase = true) }
 * FormaExposedDropdownMenu(
 *     expanded = expanded,
 *     onExpandedChange = { expanded = it },
 *     value = text,
 *     onValueChange = { text = it },
 *     options = matches,
 *     onOptionSelected = { text = it },
 *     optionLabel = { it },
 *     label = "Country",
 * )
 * ```
 *
 * @param T the option type; each option is rendered via [optionLabel].
 * @param expanded whether the menu is currently open.
 * @param onExpandedChange called when the menu requests to open or close (anchor tap, dismiss).
 * @param value the current query / selected text shown in the anchor field.
 * @param onValueChange called as the user edits the query. Only fires while [editable] is `true`.
 * @param options the options to show, **already filtered** by the caller for the current [value].
 * @param onOptionSelected called with the chosen option when a row is tapped; the menu then collapses.
 * @param optionLabel maps an option to its display (and menu-row) text.
 * @param modifier the [Modifier] applied to the `ExposedDropdownMenuBox`.
 * @param variant the anchor field's container style (defaults to [FormaTextFieldVariant.Outlined]).
 * @param editable whether the field is a type-to-filter autocomplete (`true`) or a read-only
 *   tap-to-select field (`false`).
 * @param enabled whether the control is interactive (disables both the field and the menu anchor).
 * @param label optional floating label on the anchor field.
 * @param placeholder optional placeholder shown while the field is empty.
 * @param isError whether the anchor field is in the error state.
 * @param helperText optional supporting text shown below the field when not in error.
 * @param errorText optional supporting text shown below the field while [isError] is `true`.
 * @param leadingIcon optional slot at the start of the anchor field.
 * @param colors the anchor field colors. When `null`, the M3 defaults for the [variant] are used.
 * @param noMatchesText the label of the single disabled row shown when [options] is empty while
 *   [expanded] (defaults to [FormaExposedDropdownMenuDefaults.NoMatchesText]).
 */
@ExperimentalFormaUiApi
@Composable
fun <T> FormaExposedDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    variant: FormaTextFieldVariant = FormaTextFieldVariant.Outlined,
    editable: Boolean = true,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    helperText: String? = null,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors? = null,
    noMatchesText: String = FormaExposedDropdownMenuDefaults.NoMatchesText,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
    ) {
        val anchorType = if (editable) {
            ExposedDropdownMenuAnchorType.PrimaryEditable
        } else {
            ExposedDropdownMenuAnchorType.PrimaryNotEditable
        }

        FormaTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .menuAnchor(anchorType, enabled)
                .fillMaxWidth(),
            variant = variant,
            enabled = enabled,
            readOnly = !editable,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            isError = isError,
            helperText = helperText,
            errorText = errorText,
            singleLine = true,
            colors = colors,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            if (options.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(noMatchesText) },
                    onClick = {},
                    enabled = false,
                )
            } else {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionLabel(option)) },
                        onClick = {
                            onOptionSelected(option)
                            onExpandedChange(false)
                        },
                    )
                }
            }
        }
    }
}

/**
 * Default values used by [FormaExposedDropdownMenu].
 */
@ExperimentalFormaUiApi
object FormaExposedDropdownMenuDefaults {
    /** The default label of the disabled row shown when the options list is empty while expanded. */
    const val NoMatchesText: String = "No matches"
}
