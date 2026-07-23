/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.avatar

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Compose UI tests for [FormaAvatar], hosted on the JVM via Robolectric. Covers the initials
 * fallback overload, the arbitrary-content (icon) slot form, the three sizes, and the
 * size-scaled default [FormaAvatarDefaults.textStyle] plus its `textStyle` override.
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

    @Test
    fun defaultTextStyle_scalesWithAvatarSize() {
        var expectedSmall: TextUnit? = null
        var expectedMedium: TextUnit? = null
        var expectedLarge: TextUnit? = null

        composeRule.setContent {
            FormaTheme {
                // Capture the theme's own scale in-composition, so the expectation always
                // tracks whatever typography FormaTheme installs.
                expectedSmall = MaterialTheme.typography.labelMedium.fontSize
                expectedMedium = MaterialTheme.typography.titleSmall.fontSize
                expectedLarge = MaterialTheme.typography.titleLarge.fontSize
                Row {
                    FormaAvatar(initials = "S", size = FormaAvatarSize.Small)
                    FormaAvatar(initials = "M", size = FormaAvatarSize.Medium)
                    FormaAvatar(initials = "L", size = FormaAvatarSize.Large)
                }
            }
        }

        assertEquals(expectedSmall, fetchTextLayoutFontSize("S"))
        assertEquals(expectedMedium, fetchTextLayoutFontSize("M"))
        assertEquals(expectedLarge, fetchTextLayoutFontSize("L"))
    }

    @Test
    fun textStyleOverride_isHonored() {
        composeRule.setContent {
            FormaTheme {
                FormaAvatar(
                    initials = "AB",
                    textStyle = TextStyle(fontSize = 9.sp),
                )
            }
        }

        composeRule.onNodeWithText("AB").assertIsDisplayed()
        assertEquals(9.sp, fetchTextLayoutFontSize("AB"))
    }

    /**
     * Returns the resolved font size the initials [text] was actually laid out with, via the
     * text node's [SemanticsActions.GetTextLayoutResult] action.
     */
    private fun fetchTextLayoutFontSize(text: String): TextUnit {
        val results = mutableListOf<TextLayoutResult>()
        composeRule.onNodeWithText(text)
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(results) }
        return results.first().layoutInput.style.fontSize
    }
}
