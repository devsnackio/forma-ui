/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.carousel

import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The layout strategy of a [FormaCarousel], mirroring Material 3's two horizontal carousel families.
 */
@ExperimentalFormaUiApi
enum class FormaCarouselVariant {
    /**
     * Large, medium, and small items are shown together so the user can "browse" many items at
     * once; item widths flex around [FormaCarousel]'s `itemWidth`. The default. Best for a gallery
     * of peers (photos, cards).
     */
    MultiBrowse,

    /**
     * Every item keeps exactly the requested width and is not resized as it scrolls off-screen. Best
     * for a hero/feature strip where each item should stay a consistent size.
     */
    Uncontained,
}

/**
 * A FormaUI carousel — a horizontally scrolling, snapping strip of items, delegating to Material 3's
 * `HorizontalMultiBrowseCarousel` / `HorizontalUncontainedCarousel` via [variant].
 *
 * State is hoisted: the caller creates the [state] with Material 3's `rememberCarouselState`,
 * supplying the item count. Items are a slot indexed by position:
 *
 * ```
 * val state = rememberCarouselState(itemCount = { photos.size })
 * FormaCarousel(state = state, itemWidth = 220.dp, modifier = Modifier.height(200.dp)) { index ->
 *     val photo = photos[index]
 *     Image(
 *         painter = photo.painter,
 *         contentDescription = photo.description,
 *         modifier = Modifier.fillMaxSize().maskClip(FormaTheme.shapes.lg),
 *         contentScale = ContentScale.Crop,
 *     )
 * }
 * ```
 *
 * [itemWidth] is the single width knob: it maps to Material 3's `preferredItemWidth` for
 * [FormaCarouselVariant.MultiBrowse] (a target that flexes to fit the small/medium/large keylines)
 * and to the exact `itemWidth` for [FormaCarouselVariant.Uncontained]. Carousel has no `*Colors`
 * surface — the styling surface is the item slot plus [itemSpacing] / [contentPadding]; use the
 * `CarouselItemScope` mask modifiers (e.g. `Modifier.maskClip`) inside [content] to shape items.
 *
 * @param state the hoisted [CarouselState]; create it with `rememberCarouselState(itemCount = { … })`.
 * @param itemWidth the item width — the preferred (flexing) width for [FormaCarouselVariant.MultiBrowse],
 *   the exact width for [FormaCarouselVariant.Uncontained].
 * @param modifier the [Modifier] applied to the carousel (typically sets its height).
 * @param variant the layout strategy (defaults to [FormaCarouselVariant.MultiBrowse]).
 * @param itemSpacing the gap between items (defaults to [FormaCarouselDefaults.itemSpacing], an
 *   opinionated small gap; pass `0.dp` for Material 3's flush default).
 * @param contentPadding padding around the whole content strip (defaults to none).
 * @param flingBehavior the post-scroll fling behavior. When `null`, the Material 3 default for the
 *   [variant] is used (a single-advance snap for MultiBrowse, a no-snap fling for Uncontained).
 * @param userScrollEnabled whether the user can scroll the carousel by gesture.
 * @param content the item slot, invoked per item with its index, in a `CarouselItemScope`.
 */
@ExperimentalFormaUiApi
@Composable
fun FormaCarousel(
    state: CarouselState,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    variant: FormaCarouselVariant = FormaCarouselVariant.MultiBrowse,
    itemSpacing: Dp = FormaCarouselDefaults.itemSpacing,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    flingBehavior: TargetedFlingBehavior? = null,
    userScrollEnabled: Boolean = true,
    content: @Composable CarouselItemScope.(index: Int) -> Unit,
) {
    when (variant) {
        FormaCarouselVariant.MultiBrowse -> HorizontalMultiBrowseCarousel(
            state = state,
            preferredItemWidth = itemWidth,
            modifier = modifier,
            itemSpacing = itemSpacing,
            flingBehavior = flingBehavior ?: CarouselDefaults.singleAdvanceFlingBehavior(state = state),
            userScrollEnabled = userScrollEnabled,
            contentPadding = contentPadding,
            content = content,
        )

        FormaCarouselVariant.Uncontained -> HorizontalUncontainedCarousel(
            state = state,
            itemWidth = itemWidth,
            modifier = modifier,
            itemSpacing = itemSpacing,
            flingBehavior = flingBehavior ?: CarouselDefaults.noSnapFlingBehavior(),
            userScrollEnabled = userScrollEnabled,
            contentPadding = contentPadding,
            content = content,
        )
    }
}

/**
 * Default values used by [FormaCarousel].
 */
@ExperimentalFormaUiApi
object FormaCarouselDefaults {
    /**
     * The default gap between carousel items: FormaUI's `xs` spacing token — a small opinionated gap
     * (Material 3's own default is `0.dp`, flush).
     */
    val itemSpacing: Dp
        @Composable
        @ReadOnlyComposable
        get() = FormaTheme.spacing.xs
}
