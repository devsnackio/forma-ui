/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
// The two js() bridges at the bottom of this file are Kotlin/Wasm JS interop,
// which is still experimental. Opted in at file scope rather than per call so
// the annotation sits with the explanation instead of being repeated.
@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.formaui.preview

import androidx.compose.runtime.compositionLocalOf
import kotlin.js.ExperimentalWasmJsInterop

/**
 * Controls the embedding docs site is currently driving for the previewed component.
 *
 * The harness is embedded in an `<iframe>` by formaui-site's `PreviewEmbed`, which renders a
 * control bar (variant tabs + an `enabled = false` toggle) around it. Those widgets live in the
 * host page's DOM, so the only way they can move a real Compose component is by posting a message
 * in — see [installPreviewControlBridge] for the protocol and [LocalPreviewControls] for how a
 * preview reads the result.
 *
 * [variant] is null until the host names one, and stays null forever when the harness is opened
 * directly rather than embedded. Previews MUST treat that as "no host is driving me" and render
 * their own full gallery, so the bundle is still worth opening standalone.
 */
internal data class PreviewControls(
    val variant: String? = null,
    val enabled: Boolean = true,
)

/**
 * The controls in force for the preview being composed.
 *
 * Deliberately a CompositionLocal rather than a [PreviewEntry.content] parameter: 40 previews
 * exist and only a handful will ever declare controls, so this lets one opt in without touching
 * the other 39's signatures. A preview that never reads this local behaves exactly as it did
 * before Phase 2.
 */
internal val LocalPreviewControls = compositionLocalOf { PreviewControls() }

/**
 * Resolves [PreviewControls.variant] to an entry of enum [T], or null when no variant is being
 * driven (or the host named one this enum doesn't have).
 *
 * Matching is deliberately **case-insensitive**. The host's variant names come from the site's
 * `component-inventory.json`, whose `variants` arrays are not consistently cased against the
 * Kotlin enums they describe — `button` lists `["filled", "tonal", …]` while `icon-button` lists
 * `["Standard", "Filled", …]`, and both are meant to name `FormaButtonVariant.Filled` /
 * `FormaIconButtonVariant.Standard`. Case-sensitive matching would silently ignore every tab on
 * roughly half the components, which reads as "the tabs don't work" rather than as a data bug.
 */
internal inline fun <reified T : Enum<T>> PreviewControls.variantOf(): T? =
    variant?.let { requested ->
        enumValues<T>().firstOrNull { it.name.equals(requested, ignoreCase = true) }
    }

/**
 * The variants of [T] a preview should currently render: just the host-selected one, or all of
 * them when nothing is driving this preview.
 *
 * This is the shape most previews want, and it is why opting one in is usually a two-line change.
 * A preview that renders one labelled section per variant keeps that structure verbatim and simply
 * iterates this instead of `Enum.entries` — uncontrolled it is the same gallery it always was,
 * controlled it narrows to a single specimen. An unrecognised variant name falls back to the full
 * gallery rather than rendering nothing, so a site/bundle version skew degrades to today's
 * behaviour instead of an empty canvas.
 */
internal inline fun <reified T : Enum<T>> PreviewControls.shownVariants(): List<T> =
    variantOf<T>()?.let { listOf(it) } ?: enumValues<T>().toList()

/**
 * One `set-controls` message, decoded. Every field is nullable and means "the host said nothing
 * about this axis" — the protocol's fields are all optional, so an update is a patch over the
 * current [PreviewControls], never a replacement (see [applyTo]).
 */
internal class PreviewControlUpdate(
    val variant: String?,
    val enabled: Boolean?,
    /** True for `"dark"`, false for `"light"`, null when the host isn't driving theme. */
    val dark: Boolean?,
)

/**
 * Decodes the `variant|enabled|theme` string [installPreviewControlBridge] hands back, where an
 * empty field means absent. See that function for why the bridge flattens the message to a string
 * instead of passing the JS object through.
 */
internal fun decodePreviewControls(encoded: String): PreviewControlUpdate {
    val fields = encoded.split(ControlFieldSeparator)
    return PreviewControlUpdate(
        variant = fields.getOrNull(0)?.takeIf { it.isNotEmpty() },
        enabled = when (fields.getOrNull(1)) {
            "true" -> true
            "false" -> false
            else -> null
        },
        dark = when (fields.getOrNull(2)) {
            "dark" -> true
            "light" -> false
            else -> null
        },
    )
}

/** Applies this patch over [current], leaving axes the host didn't mention untouched. */
internal fun PreviewControlUpdate.applyTo(current: PreviewControls): PreviewControls =
    PreviewControls(
        variant = variant ?: current.variant,
        enabled = enabled ?: current.enabled,
    )

/**
 * Separates the three fields in the encoded control message, and the variant names in
 * [announcePreviewControls]'s argument. Safe as a delimiter because every value crossing this
 * boundary is a Kotlin enum entry name (an identifier) or one of the literals above — and the
 * bridge rejects an inbound variant containing it rather than mis-splitting.
 */
internal const val ControlFieldSeparator: String = "|"

/**
 * Starts listening for the host page's `set-controls` messages, invoking [onControls] with an
 * encoded `variant|enabled|theme` string for each valid one. Call once, before composition.
 *
 * The protocol (host → iframe), mirroring the existing iframe → host `ready`/`error` shape:
 *
 * ```
 * { source: "formaui-preview-host", type: "set-controls",
 *   component: "<id>", variant?: string, enabled?: boolean, theme?: "light" | "dark" }
 * ```
 *
 * Validation lives in JavaScript on purpose. The message is attacker-shaped data — any page on the
 * origin can post anything — and JS is where "is this field a string" is a total, non-throwing
 * question. Reaching into an arbitrary object through Kotlin/Wasm external interfaces would be
 * both more code and more ways to fault on a malformed message. What crosses back into Kotlin is
 * one plain [String], so nothing here can throw into the Compose frame.
 *
 * `component` is checked against the id this harness actually rendered, so a message meant for a
 * different (e.g. just-unmounted) preview can't move this one. Unknown extra fields are ignored
 * rather than fatal — the site and the published bundle version independently, so a newer site
 * must be able to send fields an older bundle has never heard of.
 */
internal fun installPreviewControlBridge(component: String, onControls: (String) -> Unit): Unit =
    js(
        """{
    window.addEventListener('message', function (event) {
        if (event.origin !== window.location.origin) return;
        if (event.source !== window.parent) return;

        var data = event.data;
        if (data === null || typeof data !== 'object') return;
        if (data.source !== 'formaui-preview-host') return;
        if (data.type !== 'set-controls') return;
        if (data.component !== component) return;

        var variant = typeof data.variant === 'string' && data.variant.indexOf('|') < 0
            ? data.variant
            : '';
        var enabled = data.enabled === true ? 'true' : (data.enabled === false ? 'false' : '');
        var theme = (data.theme === 'light' || data.theme === 'dark') ? data.theme : '';

        onControls(variant + '|' + enabled + '|' + theme);
    });
}""",
    )

/**
 * Tells the host which controls this bundle can actually honour for [component]:
 *
 * ```
 * { source: "formaui-preview", type: "controls",
 *   component: "<id>", variants: string[], enabled: boolean, theme: true }
 * ```
 *
 * This is what lets the site render a control bar without a hand-maintained "which components
 * understand set-controls" allowlist that would drift the moment a preview opts in. The bundle
 * describes itself, so a control only ever appears when the running harness will move for it —
 * which is the handoff's stated bar ("don't ship visible controls that don't move the preview").
 *
 * [variants] arrives as a [ControlFieldSeparator]-joined string, empty for none, because that is
 * one fewer interop type to marshal than a list.
 *
 * Posted before `ready` (the host page posts that once this module's import resolves, and this
 * runs during it), which is fine: the site attaches its listener when it mounts the iframe, well
 * before either. It is also posted for every known component — theme is handled by
 * [PreviewScaffold] rather than by individual previews, so it works everywhere, while variant
 * tabs light up only where a preview has opted in.
 *
 * Standalone (not in an iframe) `window.parent` is this window, so this posts to itself; the
 * bridge above drops it on the `source` check.
 */
internal fun announcePreviewControls(
    component: String,
    variants: String,
    supportsEnabled: Boolean,
): Unit =
    js(
        """{
    window.parent.postMessage({
        source: 'formaui-preview',
        type: 'controls',
        component: component,
        variants: variants.length === 0 ? [] : variants.split('|'),
        enabled: supportsEnabled,
        theme: true
    }, window.location.origin);
}""",
    )
