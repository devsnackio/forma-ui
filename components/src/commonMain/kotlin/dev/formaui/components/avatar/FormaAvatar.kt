/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.avatar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The diameter of a [FormaAvatar].
 *
 * @property dp the avatar's width and height.
 */
@ExperimentalFormaUiApi
enum class FormaAvatarSize(val dp: Dp) {
    /** 32dp — dense lists, inline mentions. */
    Small(32.dp),

    /** 40dp — the default; list items, app bars. */
    Medium(40.dp),

    /** 56dp — profile headers, prominent placement. */
    Large(56.dp),
}

/**
 * A FormaUI avatar container — a circular (by default) tinted surface that centers its [content].
 *
 * This is the base of the avatar's three usages:
 * - **Image**: fill [content] with an `Image` (clipped to [shape]).
 * - **Icon**: fill [content] with an `Icon`.
 * - **Initials**: use the [FormaAvatar] `initials` overload, which is the typical fallback when no
 *   image is available.
 *
 * The container is decorative and non-interactive; wrap it in a clickable/`Modifier.semantics`
 * parent (with a person's name as the description) when it represents a tappable user.
 *
 * @param modifier the [Modifier] applied to the avatar.
 * @param size the avatar diameter (defaults to [FormaAvatarSize.Medium]).
 * @param shape the clip shape (defaults to [FormaAvatarDefaults.shape], a full circle).
 * @param containerColor the background color (defaults to the M3 `primaryContainer`).
 * @param contentColor the color applied to icon/text content (defaults to M3 `onPrimaryContainer`).
 * @param content the centered content (an `Image`, `Icon`, or text).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaAvatar(
    modifier: Modifier = Modifier,
    size: FormaAvatarSize = FormaAvatarSize.Medium,
    shape: Shape = FormaAvatarDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier.size(size.dp),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = content,
        )
    }
}

/**
 * A FormaUI avatar showing [initials] — the standard fallback when a user has no profile image.
 *
 * @param initials the initials to display (typically 1–2 characters).
 * @param modifier the [Modifier] applied to the avatar.
 * @param size the avatar diameter (defaults to [FormaAvatarSize.Medium]).
 * @param shape the clip shape (defaults to [FormaAvatarDefaults.shape], a full circle).
 * @param containerColor the background color (defaults to the M3 `primaryContainer`).
 * @param contentColor the initials color (defaults to M3 `onPrimaryContainer`).
 * @param textStyle the initials [TextStyle] (defaults to [FormaAvatarDefaults.textStyle] for
 * [size], which keeps the type at roughly 35–40% of the avatar diameter).
 */
@ExperimentalFormaUiApi
@Composable
fun FormaAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: FormaAvatarSize = FormaAvatarSize.Medium,
    shape: Shape = FormaAvatarDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    textStyle: TextStyle = FormaAvatarDefaults.textStyle(size),
) {
    FormaAvatar(
        modifier = modifier,
        size = size,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = initials,
            style = textStyle,
        )
    }
}

/**
 * Default values used by [FormaAvatar].
 */
@ExperimentalFormaUiApi
object FormaAvatarDefaults {
    /** The default avatar [Shape]: FormaUI's full (circular) corner tier. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.full

    /**
     * The default initials [TextStyle] for the given avatar [size], pulled from the theme's
     * [MaterialTheme.typography] type scale so the type sits at roughly 35–40% of the diameter:
     * - [FormaAvatarSize.Small] (32dp) → `labelMedium`
     * - [FormaAvatarSize.Medium] (40dp) → `titleSmall`
     * - [FormaAvatarSize.Large] (56dp) → `titleLarge`
     *
     * @param size the avatar diameter the style is scaled for.
     */
    @Composable
    @ReadOnlyComposable
    fun textStyle(size: FormaAvatarSize): TextStyle = when (size) {
        FormaAvatarSize.Small -> MaterialTheme.typography.labelMedium
        FormaAvatarSize.Medium -> MaterialTheme.typography.titleSmall
        FormaAvatarSize.Large -> MaterialTheme.typography.titleLarge
    }
}
