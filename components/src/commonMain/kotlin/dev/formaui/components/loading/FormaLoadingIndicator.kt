/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.components.loading

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The shape of a [FormaLoadingIndicator].
 */
@ExperimentalFormaUiApi
enum class FormaLoadingIndicatorVariant {
    /** A circular spinner. Use for contained or centered loading. */
    Circular,

    /** A horizontal bar. Use at the top of a region or above content being loaded. */
    Linear,
}

/**
 * A FormaUI loading indicator — circular or linear, determinate or indeterminate — delegating to
 * Material 3's progress indicators.
 *
 * Determinacy is chosen by [progress]:
 * - **Indeterminate** ([progress]` == null`): an endless animation for unknown-duration work.
 * - **Determinate** ([progress]` != null`): the fraction complete, in `0f..1f`.
 *
 * For accessibility, supply [contentDescription] (e.g. "Loading transactions") so screen readers
 * announce the busy state; a bare indicator is otherwise silent.
 *
 * @param modifier the [Modifier] applied to the indicator.
 * @param variant circular or linear (defaults to [FormaLoadingIndicatorVariant.Circular]).
 * @param progress the completion fraction `0f..1f`, or `null` for an indeterminate indicator.
 * @param contentDescription optional accessibility description of what is loading.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaLoadingIndicator(
    modifier: Modifier = Modifier,
    variant: FormaLoadingIndicatorVariant = FormaLoadingIndicatorVariant.Circular,
    progress: Float? = null,
    contentDescription: String? = null,
) {
    val describedModifier = if (contentDescription != null) {
        modifier.semantics { this.contentDescription = contentDescription }
    } else {
        modifier
    }

    when (variant) {
        FormaLoadingIndicatorVariant.Circular -> {
            if (progress == null) {
                CircularProgressIndicator(modifier = describedModifier)
            } else {
                CircularProgressIndicator(progress = { progress }, modifier = describedModifier)
            }
        }

        FormaLoadingIndicatorVariant.Linear -> {
            if (progress == null) {
                LinearProgressIndicator(modifier = describedModifier)
            } else {
                LinearProgressIndicator(progress = { progress }, modifier = describedModifier)
            }
        }
    }
}
