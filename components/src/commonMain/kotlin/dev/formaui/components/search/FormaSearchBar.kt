/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * The surface a [FormaSearchBar] expands into, mirroring Material 3's two search-bar hosts.
 */
@ExperimentalFormaUiApi
enum class FormaSearchBarVariant {
    /**
     * A search bar that expands in place; results are shown in a bounded panel docked directly
     * below it. Prefer this on wide layouts (tablets, desktop) where a full-screen takeover would
     * feel heavy-handed.
     */
    Docked,

    /**
     * A search bar that grows to fill the entire screen (in a dedicated overlay) once expanded.
     * This is the standard Material 3 search pattern on phones.
     */
    FullScreen,
}

/**
 * A FormaUI search bar — "Material 3 with better defaults", wrapping Material 3's
 * `SearchBar`/`DockedSearchBar`.
 *
 * Material 3 1.9.0 ships two parallel search-bar surfaces that both expand from the same
 * collapsed input field: [FormaSearchBarVariant.Docked] shows results in a bounded panel beneath
 * the field, while [FormaSearchBarVariant.FullScreen] takes over the screen. One entry point
 * covers both via [variant], so switching between them (e.g. for a tablet/phone layout split) is
 * a single-parameter change rather than a rewrite. Query state and expansion are both hoisted:
 *
 * ```
 * var query by remember { mutableStateOf("") }
 * var expanded by remember { mutableStateOf(false) }
 * val results = remember(query) { people.filter { it.contains(query, ignoreCase = true) } }
 *
 * FormaSearchBar(
 *     query = query,
 *     onQueryChange = { query = it },
 *     onSearch = { expanded = false },
 *     expanded = expanded,
 *     onExpandedChange = { expanded = it },
 *     placeholder = { Text("Search people") },
 *     leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
 * ) {
 *     results.forEach { name ->
 *         FormaListItem(headline = name, onClick = { query = name; expanded = false })
 *     }
 * }
 * ```
 *
 * @param query the current query text shown in the input field.
 * @param onQueryChange called with the updated text as the user types.
 * @param onSearch called with the current [query] when the input service triggers the search
 *   (IME) action.
 * @param expanded whether the search bar is expanded and showing [content].
 * @param onExpandedChange called with the requested new expanded state — e.g. `true` when the
 *   input field gains focus, `false` on dismiss request or back navigation.
 * @param modifier the [Modifier] applied to the search bar in its collapsed layout position.
 * @param variant which Material 3 surface to expand into (defaults to
 *   [FormaSearchBarVariant.Docked]).
 * @param enabled whether the input field is interactive.
 * @param placeholder the placeholder shown in the input field when [query] is empty.
 * @param leadingIcon the input field's leading icon slot, typically a search glyph.
 * @param trailingIcon the input field's trailing icon slot, e.g. a clear button shown when
 *   [query] is not empty.
 * @param shape the collapsed search bar's container shape. When `null`, the Material 3 default
 *   for the chosen [variant] is used ([FormaSearchBarDefaults.dockedShape] or
 *   [FormaSearchBarDefaults.fullScreenCollapsedShape]).
 * @param colors the container/divider/input-field colors. When `null`, the Material 3 default
 *   colors are used.
 * @param tonalElevation the tonal elevation applied when [colors]' container color is the
 *   surface color (defaults to [FormaSearchBarDefaults.TonalElevation]).
 * @param shadowElevation the shadow elevation below the collapsed search bar (defaults to
 *   [FormaSearchBarDefaults.ShadowElevation]).
 * @param interactionSource the [MutableInteractionSource] for observing/emitting interactions on
 *   the input field. When `null`, one is remembered internally.
 * @param content the search results (or suggestions) shown below the input field while
 *   [expanded], laid out in a [ColumnScope].
 */
@ExperimentalFormaUiApi
@Composable
fun FormaSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaSearchBarVariant = FormaSearchBarVariant.Docked,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape? = null,
    colors: SearchBarColors? = null,
    tonalElevation: Dp = FormaSearchBarDefaults.TonalElevation,
    shadowElevation: Dp = FormaSearchBarDefaults.ShadowElevation,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val resolvedColors = colors ?: SearchBarDefaults.colors()
    val inputField: @Composable () -> Unit = {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            enabled = enabled,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = resolvedColors.inputFieldColors,
            interactionSource = interactionSource,
        )
    }

    when (variant) {
        FormaSearchBarVariant.Docked -> DockedSearchBar(
            inputField = inputField,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = modifier,
            shape = shape ?: FormaSearchBarDefaults.dockedShape,
            colors = resolvedColors,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
            content = content,
        )

        FormaSearchBarVariant.FullScreen -> SearchBar(
            inputField = inputField,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = modifier,
            shape = shape ?: FormaSearchBarDefaults.fullScreenCollapsedShape,
            colors = resolvedColors,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
            content = content,
        )
    }
}

/**
 * Default values used by [FormaSearchBar]. Override any of these per call site, or read them to
 * build a customised search bar that still inherits FormaUI's defaults.
 */
@ExperimentalFormaUiApi
object FormaSearchBarDefaults {
    /** The default collapsed shape for [FormaSearchBarVariant.Docked]. */
    val dockedShape: Shape
        @Composable
        get() = SearchBarDefaults.dockedShape

    /**
     * The default collapsed shape for [FormaSearchBarVariant.FullScreen]. When expanded, Material
     * 3 always switches to its own full-screen container shape regardless of this value.
     */
    val fullScreenCollapsedShape: Shape
        @Composable
        get() = SearchBarDefaults.inputFieldShape

    /** Default tonal elevation for a collapsed search bar. */
    val TonalElevation: Dp = SearchBarDefaults.TonalElevation

    /** Default shadow elevation for a collapsed search bar. */
    val ShadowElevation: Dp = SearchBarDefaults.ShadowElevation
}
