/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.avatar

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaAvatar], hosted on the JVM via Robolectric. Covers the initials
 * fallback overload, the arbitrary-content (icon) slot form, and the three sizes.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FormaAvatarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun initialsOverload_showsInitials() {
        composeRule.setContent {
            FormaTheme {
                FormaAvatar(initials = "AB")
            }
        }

        composeRule.onNodeWithText("AB").assertIsDisplayed()
    }

    @Test
    fun slotForm_rendersProvidedContent() {
        composeRule.setContent {
            FormaTheme {
                // The icon/image usage: any composable fills the avatar container.
                FormaAvatar(size = FormaAvatarSize.Medium) {
                    Text("★")
                }
            }
        }

        composeRule.onNodeWithText("★").assertIsDisplayed()
    }

    @Test
    fun allSizes_render() {
        composeRule.setContent {
            FormaTheme {
                Row {
                    FormaAvatar(initials = "S", size = FormaAvatarSize.Small)
                    FormaAvatar(initials = "M", size = FormaAvatarSize.Medium)
                    FormaAvatar(initials = "L", size = FormaAvatarSize.Large)
                }
            }
        }

        composeRule.onNodeWithText("S").assertIsDisplayed()
        composeRule.onNodeWithText("M").assertIsDisplayed()
        composeRule.onNodeWithText("L").assertIsDisplayed()
    }
}
