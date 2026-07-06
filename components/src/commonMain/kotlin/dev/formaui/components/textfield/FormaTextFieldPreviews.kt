/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaTextField]: both variants across the default, error, and disabled states, with
 * label/placeholder, leading/trailing content, and helper/error supporting text.
 */
@Preview
@Composable
private fun FormaTextFieldStatesPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                // Outlined — default with label, helper text, and a leading affordance.
                FormaTextField(
                    value = "1,240.00",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaTextFieldVariant.Outlined,
                    label = "Amount",
                    leadingIcon = { Text("$") },
                    helperText = "Available balance: $5,000.00",
                    singleLine = true,
                )

                // Outlined — error state with error text (takes precedence over helper).
                FormaTextField(
                    value = "abc",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaTextFieldVariant.Outlined,
                    label = "Account number",
                    isError = true,
                    helperText = "Digits only",
                    errorText = "Enter a valid account number",
                    singleLine = true,
                )

                // Filled — default with placeholder and a trailing affordance.
                FormaTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaTextFieldVariant.Filled,
                    label = "Search",
                    placeholder = "Search transactions",
                    trailingIcon = { Text("⌕") },
                    singleLine = true,
                )

                // Filled — disabled.
                FormaTextField(
                    value = "Locked field",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = FormaTextFieldVariant.Filled,
                    label = "Reference",
                    enabled = false,
                    singleLine = true,
                )
            }
        }
    }
}
