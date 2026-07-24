/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.preview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

/** Default component id rendered when the URL has no (or an empty) `component` parameter. */
internal const val DefaultComponentId: String = "button"

/** One renderable preview: the heading shown above it plus its content composable. */
internal class PreviewEntry(
    val title: String,
    val content: @Composable ColumnScope.() -> Unit,
)

/**
 * Every supported component preview, keyed by the kebab-case component id from
 * `docs/component-inventory.json` — the same key the docs site passes as `?component=<id>`.
 * Insertion order mirrors the inventory (the 18 PRD components first, the extras after).
 */
internal val PreviewRegistry: Map<String, PreviewEntry> = linkedMapOf(
    "button" to PreviewEntry("FormaButton") { ButtonPreview() },
    "text-field" to PreviewEntry("FormaTextField") { TextFieldPreview() },
    "card" to PreviewEntry("FormaCard") { CardPreview() },
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
    "divider" to PreviewEntry("FormaDivider") { DividerPreview() },
    "loading-indicator" to PreviewEntry("FormaLoadingIndicator") { LoadingIndicatorPreview() },
    "empty-state" to PreviewEntry("FormaEmptyState") { EmptyStatePreview() },
    "snackbar" to PreviewEntry("FormaSnackbar") { SnackbarPreview() },
    "slider" to PreviewEntry("FormaSlider") { SliderPreview() },
    "bottom-app-bar" to PreviewEntry("FormaBottomAppBar") { BottomAppBarPreview() },
    "dropdown-menu" to PreviewEntry("FormaDropdownMenu") { DropdownMenuPreview() },
    "floating-action-button" to PreviewEntry("FormaFloatingActionButton") { FloatingActionButtonPreview() },
    "icon-button" to PreviewEntry("FormaIconButton") { IconButtonPreview() },
    "navigation-drawer" to PreviewEntry("FormaNavigationDrawer") { NavigationDrawerPreview() },
    "navigation-rail" to PreviewEntry("FormaNavigationRail") { NavigationRailPreview() },
    "search-bar" to PreviewEntry("FormaSearchBar") { SearchBarPreview() },
    "segmented-button" to PreviewEntry("FormaSegmentedButton") { SegmentedButtonPreview() },
    "tab-row" to PreviewEntry("FormaTabRow") { TabRowPreview() },
    "tooltip" to PreviewEntry("FormaTooltip") { TooltipPreview() },
    "top-app-bar" to PreviewEntry("FormaTopAppBar") { TopAppBarPreview() },
    "bar-chart" to PreviewEntry("FormaBarChart") { BarChartPreview() },
    "donut-chart" to PreviewEntry("FormaDonutChart") { DonutChartPreview() },
    "line-chart" to PreviewEntry("FormaLineChart") { LineChartPreview() },
    "date-picker-sheet" to PreviewEntry("FormaDatePickerSheet") { DatePickerSheetPreview() },
    "date-range-picker-sheet" to PreviewEntry("FormaDateRangePickerSheet") { DateRangePickerSheetPreview() },
    "pull-to-refresh" to PreviewEntry("FormaPullToRefresh") { PullToRefreshPreview() },
    "swipe-to-dismiss" to PreviewEntry("FormaSwipeToDismiss") { SwipeToDismissPreview() },
    "range-slider" to PreviewEntry("FormaRangeSlider") { RangeSliderPreview() },
    "time-picker-sheet" to PreviewEntry("FormaTimePickerSheet") { TimePickerSheetPreview() },
    "carousel" to PreviewEntry("FormaCarousel") { CarouselPreview() },
    "exposed-dropdown-menu" to PreviewEntry("FormaExposedDropdownMenu") { ExposedDropdownMenuPreview() },
)
