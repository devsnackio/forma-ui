/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.preview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.components.carousel.FormaCarouselVariant
import dev.formaui.components.divider.FormaDividerOrientation
import dev.formaui.components.fab.FormaFabSize
import dev.formaui.components.iconbutton.FormaIconButtonVariant
import dev.formaui.components.tooltip.FormaTooltipVariant
import dev.formaui.components.topappbar.FormaTopAppBarVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/** Default component id rendered when the URL has no (or an empty) `component` parameter. */
internal const val DefaultComponentId: String = "button"

/**
 * Which host-driven controls a preview honours. This is the opt-in switch for the docs site's
 * control bar: [announcePreviewControls] reports it to the embedder, which renders tabs and the
 * disabled toggle only for what is declared here, so a control can never appear for a preview
 * that would ignore it.
 *
 * Declaring a spec is only half the job — the preview's own composable has to read
 * [LocalPreviewControls] and act on it. [ButtonPreview] is the reference implementation.
 *
 * Theme is not listed: [PreviewScaffold] applies it around every preview, so it needs no per-entry
 * opt-in and is announced unconditionally.
 */
internal class PreviewControlSpec(
    /**
     * Variant names offered as tabs, matched to the preview's enum case-insensitively (see
     * [variantOf]). Derive them from the enum rather than writing them out, so a new variant in
     * the library can't silently go missing from the docs site's tab row.
     */
    val variants: List<String> = emptyList(),
    /** Whether the preview honours [PreviewControls.enabled]. */
    val supportsEnabled: Boolean = false,
)

/**
 * One renderable preview: the heading shown above it, which host-driven [controls] it honours, and
 * its content composable.
 */
internal class PreviewEntry(
    val title: String,
    val controls: PreviewControlSpec = PreviewControlSpec(),
    val content: @Composable ColumnScope.() -> Unit,
)

/**
 * Every supported component preview, keyed by the kebab-case component id from
 * `docs/component-inventory.json` — the same key the docs site passes as `?component=<id>`.
 * Insertion order mirrors the inventory (the 18 PRD components first, the extras after).
 */
internal val PreviewRegistry: Map<String, PreviewEntry> = linkedMapOf(
    "button" to PreviewEntry(
        title = "FormaButton",
        controls = PreviewControlSpec(
            variants = FormaButtonVariant.entries.map { it.name },
            supportsEnabled = true,
        ),
    ) { ButtonPreview() },
    "text-field" to PreviewEntry("FormaTextField") { TextFieldPreview() },
    "card" to PreviewEntry(
        title = "FormaCard",
        controls = PreviewControlSpec(
            variants = FormaCardVariant.entries.map { it.name },
            supportsEnabled = true,
        ),
    ) { CardPreview() },
    "chip" to PreviewEntry("FormaChip") { ChipPreview() },
    "badge" to PreviewEntry("FormaBadge") { BadgePreview() },
    "switch" to PreviewEntry("FormaSwitch") { SwitchPreview() },
    "checkbox" to PreviewEntry("FormaCheckbox") { CheckboxPreview() },
    "radio-button" to PreviewEntry("FormaRadioButton") { RadioButtonPreview() },
    "dialog" to PreviewEntry("FormaDialog") { DialogPreview() },
    "bottom-sheet" to PreviewEntry("FormaBottomSheet") { BottomSheetPreview() },
    "navigation-bar" to PreviewEntry("FormaNavigationBar") { NavigationBarPreview() },
    "list-item" to PreviewEntry("FormaListItem") { ListItemPreview() },
    "avatar" to PreviewEntry("FormaAvatar") { AvatarPreview() },
    "divider" to PreviewEntry(
        title = "FormaDivider",
        controls = PreviewControlSpec(
            variants = FormaDividerOrientation.entries.map { it.name },
        ),
    ) { DividerPreview() },
    "loading-indicator" to PreviewEntry("FormaLoadingIndicator") { LoadingIndicatorPreview() },
    "empty-state" to PreviewEntry("FormaEmptyState") { EmptyStatePreview() },
    "snackbar" to PreviewEntry("FormaSnackbar") { SnackbarPreview() },
    "slider" to PreviewEntry("FormaSlider") { SliderPreview() },
    "bottom-app-bar" to PreviewEntry("FormaBottomAppBar") { BottomAppBarPreview() },
    "dropdown-menu" to PreviewEntry("FormaDropdownMenu") { DropdownMenuPreview() },
    "floating-action-button" to PreviewEntry(
        title = "FormaFloatingActionButton",
        controls = PreviewControlSpec(
            variants = FormaFabSize.entries.map { it.name },
        ),
    ) { FloatingActionButtonPreview() },
    "icon-button" to PreviewEntry(
        title = "FormaIconButton",
        controls = PreviewControlSpec(
            variants = FormaIconButtonVariant.entries.map { it.name },
            supportsEnabled = true,
        ),
    ) { IconButtonPreview() },
    "navigation-drawer" to PreviewEntry("FormaNavigationDrawer") { NavigationDrawerPreview() },
    "navigation-rail" to PreviewEntry("FormaNavigationRail") { NavigationRailPreview() },
    "search-bar" to PreviewEntry("FormaSearchBar") { SearchBarPreview() },
    "segmented-button" to PreviewEntry("FormaSegmentedButton") { SegmentedButtonPreview() },
    "tab-row" to PreviewEntry("FormaTabRow") { TabRowPreview() },
    "tooltip" to PreviewEntry(
        title = "FormaTooltip",
        controls = PreviewControlSpec(
            variants = FormaTooltipVariant.entries.map { it.name },
        ),
    ) { TooltipPreview() },
    "top-app-bar" to PreviewEntry(
        title = "FormaTopAppBar",
        controls = PreviewControlSpec(
            variants = FormaTopAppBarVariant.entries.map { it.name },
        ),
    ) { TopAppBarPreview() },
    "bar-chart" to PreviewEntry("FormaBarChart") { BarChartPreview() },
    "donut-chart" to PreviewEntry("FormaDonutChart") { DonutChartPreview() },
    "line-chart" to PreviewEntry("FormaLineChart") { LineChartPreview() },
    "date-picker-sheet" to PreviewEntry("FormaDatePickerSheet") { DatePickerSheetPreview() },
    "date-range-picker-sheet" to PreviewEntry("FormaDateRangePickerSheet") { DateRangePickerSheetPreview() },
    "pull-to-refresh" to PreviewEntry("FormaPullToRefresh") { PullToRefreshPreview() },
    "swipe-to-dismiss" to PreviewEntry("FormaSwipeToDismiss") { SwipeToDismissPreview() },
    "range-slider" to PreviewEntry("FormaRangeSlider") { RangeSliderPreview() },
    "time-picker-sheet" to PreviewEntry("FormaTimePickerSheet") { TimePickerSheetPreview() },
    "carousel" to PreviewEntry(
        title = "FormaCarousel",
        controls = PreviewControlSpec(
            variants = FormaCarouselVariant.entries.map { it.name },
        ),
    ) { CarouselPreview() },
    "exposed-dropdown-menu" to PreviewEntry("FormaExposedDropdownMenu") { ExposedDropdownMenuPreview() },
)
