/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview of [FormaAvatar]: initials fallback across the three sizes, plus an icon-content avatar.
 */
@Preview
@Composable
private fun FormaAvatarPreview() {
    FormaTheme {
        Surface {
            Row(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FormaAvatar(initials = "AB", size = FormaAvatarSize.Small)
                FormaAvatar(initials = "CD", size = FormaAvatarSize.Medium)
                FormaAvatar(initials = "EF", size = FormaAvatarSize.Large)
                // Icon-content usage: any composable can fill the avatar container.
                FormaAvatar(size = FormaAvatarSize.Medium) {
                    Text("★")
                }
            }
        }
    }
}
