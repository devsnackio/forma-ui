/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.chip

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.InputChip
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The kind of a [FormaChip], mirroring Material 3's four chip types.
 */
@ExperimentalFormaUiApi
enum class FormaChipVariant {
    /** A helper action related to nearby content (e.g. "Add to calendar"). Not selectable. */
    Assist,

    /** A toggleable filter within a set. Honors the [selected][FormaChip] state. */
    Filter,

    /** Represents a discrete user-entered piece of information (e.g. an entered contact). Selectable. */
    Input,

    /** A dynamically-generated suggestion (e.g. a smart reply). Not selectable; no trailing icon. */
    Suggestion,
}

/**
 * A FormaUI chip — "Material 3 with better defaults".
 *
 * One entry point covers all four Material 3 chip types via [variant]. The [selected] state is
 * honored only by the selectable variants ([FormaChipVariant.Filter] and
 * [FormaChipVariant.Input]); it is ignored by [FormaChipVariant.Assist] and
 * [FormaChipVariant.Suggestion]. Because [FormaChipVariant.Suggestion] supports only a single
 * leading icon, [trailingIcon] is ignored for that variant.
 *
 * Chips keep Material 3's compact visual height while still exposing a **48dp touch target**
 * automatically (via M3's minimum interactive component size) — so no explicit height is forced.
 * The corner shape defaults to FormaUI's
 * [small][dev.formaui.core.theme.FormaShapes.small] tier; per-variant container colors use the
 * M3 defaults (their color types differ across variants, so they are not exposed as one param —
 * restyle via [FormaTheme] instead).
 *
 * @param label the chip's text label.
 * @param onClick called when the chip is clicked. For selectable variants, toggle [selected] in
 *   response.
 * @param modifier the [Modifier] applied to the chip.
 * @param variant the chip kind (defaults to [FormaChipVariant.Assist]).
 * @param selected whether the chip is selected. Only meaningful for [FormaChipVariant.Filter] and
 *   [FormaChipVariant.Input].
 * @param enabled whether the chip is interactive.
 * @param leadingIcon optional slot at the start of the chip (for [FormaChipVariant.Suggestion]
 *   this is the chip's single icon).
 * @param trailingIcon optional slot at the end of the chip. Ignored for [FormaChipVariant.Suggestion].
 * @param shape the chip's container shape (defaults to [FormaChipDefaults.shape]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions.
 *   When `null`, one is remembered internally.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaChipVariant = FormaChipVariant.Assist,
    selected: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val chipShape = shape ?: FormaChipDefaults.shape
    val labelSlot: @Composable () -> Unit = { Text(label) }

    when (variant) {
        FormaChipVariant.Assist -> AssistChip(
            onClick = onClick,
            label = labelSlot,
            modifier = modifier,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = chipShape,
            interactionSource = interactionSource,
        )

        FormaChipVariant.Filter -> FilterChip(
            selected = selected,
            onClick = onClick,
            label = labelSlot,
            modifier = modifier,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = chipShape,
            interactionSource = interactionSource,
        )

        FormaChipVariant.Input -> InputChip(
            selected = selected,
            onClick = onClick,
            label = labelSlot,
            modifier = modifier,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = chipShape,
            interactionSource = interactionSource,
        )

        FormaChipVariant.Suggestion -> SuggestionChip(
            onClick = onClick,
            label = labelSlot,
            modifier = modifier,
            enabled = enabled,
            icon = leadingIcon,
            shape = chipShape,
            interactionSource = interactionSource,
        )
    }
}

/**
 * Default values used by [FormaChip].
 */
@ExperimentalFormaUiApi
object FormaChipDefaults {
    /** The default chip [Shape]: FormaUI's small corner tier. */
    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.small
}
