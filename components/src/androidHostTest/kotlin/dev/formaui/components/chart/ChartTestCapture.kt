/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.components.chart

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import kotlin.math.roundToInt

/**
 * Captures the pixels a semantics node actually painted, under Robolectric, by software-drawing
 * the node's host compose view into a [Bitmap] via [android.view.View.draw] and cropping to the
 * node's bounds in the root.
 *
 * Why not `captureToImage()`? That API captures the *window*: its `forceRedraw` step registers a
 * frame-commit/`onDraw` callback and waits for a frame-driven draw pass to fire it — and under
 * Robolectric frame-driven draw passes never happen (the compose team's own note in
 * `RobolectricIdlingStrategy`: "Draw passes don't happen"), so it always times out with
 * `ComposeTimeoutException: Condition still not satisfied after 2000 ms`, for every component,
 * regardless of `GraphicsMode` or `robolectric.pixelCopyRenderMode`. Calling `View.draw`
 * directly dispatches a real draw pass synchronously, which works under Robolectric's NATIVE
 * graphics.
 *
 * The test class MUST be annotated `@GraphicsMode(GraphicsMode.Mode.NATIVE)`: in this project
 * Robolectric runs LEGACY (shadow) graphics by default, where `drawPath`/`drawArc`/`drawLine`
 * are raster no-ops and rect fills rasterize as hairline outlines — every capture then reads
 * blank regardless of what the component draws (verified empirically with a raw
 * `android.graphics.Canvas` probe, no compose involved). NATIVE mode rasterizes all ops
 * correctly, using the already-cached `org.robolectric:nativeruntime` artifacts.
 *
 * This is the pixel-verification bar for canvas-drawn components (charts and friends):
 * semantics-only assertions cannot tell a painted chart from an empty band, because a node's
 * layout bounds can be coerced to a plausible size even when its draw size is zero and nothing
 * is ever drawn.
 */
internal fun SemanticsNodeInteraction.captureNodeToImage(): ImageBitmap {
    val node = fetchSemanticsNode("Failed to capture the node to a bitmap.")
    val view = (node.root as ViewRootForTest).view
    check(view.width > 0 && view.height > 0) {
        "Cannot capture: host view has degenerate size ${view.width}x${view.height}."
    }

    val fullBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    view.draw(Canvas(fullBitmap))

    val bounds = node.boundsInRoot
    check(bounds.width >= 1f && bounds.height >= 1f) {
        "Cannot capture: node bounds are degenerate ($bounds)."
    }
    val left = bounds.left.roundToInt().coerceIn(0, view.width - 1)
    val top = bounds.top.roundToInt().coerceIn(0, view.height - 1)
    val width = bounds.width.roundToInt().coerceAtMost(view.width - left)
    val height = bounds.height.roundToInt().coerceAtMost(view.height - top)
    return Bitmap.createBitmap(fullBitmap, left, top, width, height).asImageBitmap()
}
