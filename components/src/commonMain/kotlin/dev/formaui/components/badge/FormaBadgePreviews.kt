/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaBadge]: a dot badge, a numeric badge, and an overflowing count, each anchored
 * to an element with a `BadgedBox`.
 */
@Preview
@Composable
private fun FormaBadgeVariantsPreview() {
    FormaTheme {
        Surface {
            Row(
                modifier = Modifier.padding(FormaTheme.spacing.lg),
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xl),
            ) {
                FormaBadgedBox(badge = { FormaBadge() }) {
                    Text("Updates")
                }
                FormaBadgedBox(badge = { FormaBadge(count = 5) }) {
                    Text("Inbox")
                }
                FormaBadgedBox(badge = { FormaBadge(count = 150) }) {
                    Text("Alerts")
                }
            }
        }
    }
}
