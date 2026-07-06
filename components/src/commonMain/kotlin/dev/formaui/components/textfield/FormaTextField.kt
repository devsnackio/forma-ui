/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The container style of a [FormaTextField], mirroring Material 3's two text-field families.
 */
@ExperimentalFormaUiApi
enum class FormaTextFieldVariant {
    /** A transparent field bounded by a full outline. The default; reads cleanly on any surface. */
    Outlined,

    /** A filled container with a bottom indicator line. Higher visual weight than [Outlined]. */
    Filled,
}

/**
 * A FormaUI text field — "Material 3 with better defaults".
 *
 * One entry point covers both Material 3 text-field families via [variant]. State is fully
 * hoisted per standard Compose convention: the caller owns [value] and reacts to
 * [onValueChange]. The four PRD states map to plain parameters — **default** (the base case),
 * **disabled** ([enabled]` = false`), **error** ([isError]` = true`), and **focused** (handled
 * automatically by the underlying M3 field via its [interactionSource]).
 *
 * Leading/trailing content are nullable [Composable] slots (typically an `Icon`); supply a
 * `contentDescription` on any icon that conveys meaning. Helper and error text share one line
 * below the field: while [isError] is `true` and [errorText] is non-null, [errorText] is shown
 * (and colored as an error by M3, which also surfaces the error to accessibility services);
 * otherwise [helperText] is shown when present.
 *
 * Opinionated defaults over bare Material 3:
 * - The outlined variant uses FormaUI's [small][dev.formaui.core.theme.FormaShapes.small] corner
 *   tier; the filled variant keeps M3's top-rounded shape. Override via [shape].
 * - Colors fall back to the M3 defaults for the chosen [variant]; override via [colors].
 *
 * @param value the current text to display.
 * @param onValueChange called with the updated text as the user edits. The caller must hoist and
 *   persist the new value for it to appear.
 * @param modifier the [Modifier] applied to the field.
 * @param variant the container style (defaults to [FormaTextFieldVariant.Outlined]).
 * @param enabled whether the field is editable and focusable. When `false` it is visually
 *   de-emphasised and ignores input.
 * @param readOnly whether the field is read-only: it shows [value] and is focusable/selectable
 *   but not editable.
 * @param label optional floating label describing the field.
 * @param placeholder optional placeholder shown while the field is empty.
 * @param leadingIcon optional slot rendered at the start of the field.
 * @param trailingIcon optional slot rendered at the end of the field.
 * @param isError whether the field is in the error state (error styling + accessibility signal).
 * @param helperText optional supporting text shown below the field when not in error.
 * @param errorText optional supporting text shown below the field while [isError] is `true`;
 *   takes precedence over [helperText].
 * @param singleLine whether the field is constrained to a single line.
 * @param textStyle the [TextStyle] applied to the input text (defaults to the ambient text style;
 *   pass [FormaTheme.typography]`.numeric` for tabular numeric entry).
 * @param visualTransformation transforms the visual representation of [value] (e.g. password dots).
 * @param keyboardOptions software-keyboard configuration (type, IME action, capitalization).
 * @param keyboardActions callbacks for IME actions.
 * @param shape the container shape. When `null`, the variant's sensible default is used
 *   ([FormaTextFieldDefaults.outlinedShape] for outlined, M3's shape for filled).
 * @param colors the field colors. When `null`, the M3 default colors for the [variant] are used.
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions
 *   (focus, press). When `null`, one is remembered internally.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaTextFieldVariant = FormaTextFieldVariant.Outlined,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    helperText: String? = null,
    errorText: String? = null,
    singleLine: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape? = null,
    colors: TextFieldColors? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val labelSlot: (@Composable () -> Unit)? = label?.let { text -> { Text(text) } }
    val placeholderSlot: (@Composable () -> Unit)? = placeholder?.let { text -> { Text(text) } }
    val supportingSlot: (@Composable () -> Unit)? = when {
        isError && errorText != null -> {
            { Text(errorText) }
        }
        helperText != null -> {
            { Text(helperText) }
        }
        else -> null
    }

    when (variant) {
        FormaTextFieldVariant.Outlined -> OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = labelSlot,
            placeholder = placeholderSlot,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingSlot,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            shape = shape ?: FormaTextFieldDefaults.outlinedShape,
            colors = colors ?: OutlinedTextFieldDefaults.colors(),
            interactionSource = interactionSource,
        )

        FormaTextFieldVariant.Filled -> TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = labelSlot,
            placeholder = placeholderSlot,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingSlot,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            shape = shape ?: TextFieldDefaults.shape,
            colors = colors ?: TextFieldDefaults.colors(),
            interactionSource = interactionSource,
        )
    }
}

/**
 * Default values used by [FormaTextField].
 */
@ExperimentalFormaUiApi
object FormaTextFieldDefaults {
    /** The default shape for the outlined variant: FormaUI's small corner tier. */
    val outlinedShape: Shape
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.shapes.small
}
