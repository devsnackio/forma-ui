/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalComposeUiApi::class)

package dev.formaui.preview

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
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
 *
 * ## Host-driven controls
 *
 * When embedded by formaui-site, the docs page draws the variant tabs, the `enabled = false`
 * toggle and the theme pill in its own DOM — outside this canvas — and drives them in here by
 * postMessage. Two state holders below own everything that arrives that way. They are created
 * before [ComposeViewport] and written from a JS event callback, which is safe here: wasm is
 * single-threaded, so the write lands on the same thread the Compose recomposer observes, and the
 * global snapshot picks it up like any other state write.
 *
 * Both are deliberately null/default until a host actually says otherwise, so opening this bundle
 * directly — no iframe, no docs site — still renders the full self-contained preview it always
 * did. See [PreviewControls] and [installPreviewControlBridge] for the protocol.
 */
fun main() {
    val requestedId = componentQueryParam() ?: DefaultComponentId
    val entry = PreviewRegistry[requestedId]
    document.title =
        if (entry != null) "formaui-preview:$requestedId" else "formaui-preview:unknown:$requestedId"

    val controls = mutableStateOf(PreviewControls())
    // Null means "no host is driving theme", which is what keeps PreviewScaffold's own in-canvas
    // light/dark switch on screen. Once a host sends a theme the switch steps aside rather than
    // sitting next to the host's pill doing the same job — see PreviewScaffold's `hostDark`.
    val hostDark = mutableStateOf<Boolean?>(null)

    if (entry != null) {
        installPreviewControlBridge(requestedId) { message ->
            val update = decodePreviewControls(message)
            controls.value = update.applyTo(controls.value)
            update.dark?.let { hostDark.value = it }
        }
        announcePreviewControls(
            component = requestedId,
            variants = entry.controls.variants.joinToString(ControlFieldSeparator),
            supportsEnabled = entry.controls.supportsEnabled,
        )
    }

    ComposeViewport(document.body!!) {
        if (entry != null) {
            CompositionLocalProvider(LocalPreviewControls provides controls.value) {
                PreviewScaffold(
                    title = entry.title,
                    hostDark = hostDark.value,
                    content = entry.content,
                )
            }
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
