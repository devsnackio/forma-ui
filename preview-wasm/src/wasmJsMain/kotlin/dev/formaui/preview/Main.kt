/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalComposeUiApi::class)

package dev.formaui.preview

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window

/**
 * Browser entry point for the Wasm preview harness. Renders one live, interactive FormaUI
 * component — the real Compose code running in the browser via the `wasmJs` target — selected by
 * the `?component=<id>` URL query parameter, where `<id>` is the kebab-case component id from
 * `docs/component-inventory.json` (e.g. `button`, `icon-button`, `date-picker-sheet`).
 *
 * A missing or empty parameter falls back to [DefaultComponentId] (back-compat with the original
 * Button-only harness); an unrecognized id renders an in-canvas [UnknownComponentMessage]. Every
 * known preview shares the [PreviewScaffold] chrome: an in-canvas light/dark toggle driving
 * `FormaTheme(darkTheme = …)`, the component name heading, then the interactive preview content.
 *
 * Before composition starts, `document.title` is set to `formaui-preview:<id>` (or
 * `formaui-preview:unknown:<id>`) so embedders and e2e tests can assert which component rendered.
 */
fun main() {
    val requestedId = componentQueryParam() ?: DefaultComponentId
    val entry = PreviewRegistry[requestedId]
    document.title =
        if (entry != null) "formaui-preview:$requestedId" else "formaui-preview:unknown:$requestedId"
    ComposeViewport(document.body!!) {
        if (entry != null) {
            PreviewScaffold(title = entry.title, content = entry.content)
        } else {
            UnknownComponentMessage(id = requestedId)
        }
    }
}

/**
 * Returns the value of the `component` query parameter in the current page URL, or null when the
 * parameter is absent or blank. Inventory ids are plain kebab-case, so no URL decoding is needed.
 */
private fun componentQueryParam(): String? =
    window.location.search
        .removePrefix("?")
        .split('&')
        .firstOrNull { it.substringBefore('=') == "component" }
        ?.substringAfter('=', missingDelimiterValue = "")
        ?.takeIf { it.isNotBlank() }
