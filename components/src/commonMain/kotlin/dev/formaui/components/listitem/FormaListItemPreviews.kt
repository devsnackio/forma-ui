/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.formaui.components.avatar.FormaAvatar
import dev.formaui.components.avatar.FormaAvatarSize
import dev.formaui.components.divider.FormaDivider
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaListItem]: single-, two-, and three-line rows with leading/trailing slots,
 * separated by [FormaDivider]s.
 */
@Preview
@Composable
private fun FormaListItemPreview() {
    FormaTheme {
        Surface {
            Column(modifier = Modifier) {
                FormaListItem(
                    headline = "Single line",
                    trailing = { Text("›") },
                    onClick = {},
                )
                FormaDivider()
                FormaListItem(
                    headline = "Ada Lovelace",
                    supporting = "ada@example.com",
                    leading = { FormaAvatar(initials = "AL", size = FormaAvatarSize.Small) },
                    trailing = { Text("›") },
                    onClick = {},
                )
                FormaDivider()
                FormaListItem(
                    headline = "Payment received",
                    overline = "Today, 09:41",
                    supporting = "$1,240.00 from Grace Hopper",
                    leading = { FormaAvatar(initials = "GH", size = FormaAvatarSize.Small) },
                )
            }
        }
    }
}
