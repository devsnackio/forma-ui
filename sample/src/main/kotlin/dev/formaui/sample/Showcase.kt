/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import dev.formaui.components.card.FormaCard
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * The showcase's 5 top-level categories, driving both the bottom-nav destinations and the
 * top app bar title. Every existing component demo lives in exactly one category.
 *
 * [navLabel] is a short label for the bottom-nav item — kept separate from [title] because a
 * 5-item nav bar has less room per label than the top app bar; [title] stays the fuller name
 * used up top.
 */
enum class SampleCategory(
    val title: String,
    val navLabel: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Actions("Actions", "Actions", Icons.Filled.Bolt, Icons.Outlined.Bolt),
    Inputs("Inputs", "Inputs", Icons.Filled.Edit, Icons.Outlined.Edit),
    Containment("Containment", "Display", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
    Navigation("Navigation", "Navigation", Icons.Filled.Menu, Icons.Outlined.Menu),
    Feedback("Feedback", "Feedback", Icons.Filled.Notifications, Icons.Outlined.Notifications),
}

/**
 * The scrollable body for one [SampleCategory]: every component demo in that category, each
 * wrapped in a [ComponentShowcase] card. Width-constrained and centered so the gallery doesn't
 * stretch edge-to-edge on large screens/tablets.
 *
 * @param contentPadding the insets contributed by the surrounding `Scaffold` (top app bar / bottom
 *   nav bar); merged with FormaUI's own outer spacing.
 */
@Composable
fun CategoryScreen(
    category: SampleCategory,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current
    val listPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection) + FormaTheme.spacing.md,
        top = contentPadding.calculateTopPadding() + FormaTheme.spacing.md,
        end = contentPadding.calculateEndPadding(layoutDirection) + FormaTheme.spacing.md,
        bottom = contentPadding.calculateBottomPadding() + FormaTheme.spacing.md,
    )

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().widthIn(max = 640.dp),
            contentPadding = listPadding,
            verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
        ) {
            when (category) {
                SampleCategory.Actions -> {
                    item { ButtonShowcase() }
                    item { IconButtonShowcase() }
                    item { FabShowcase() }
                    item { ChipShowcase() }
                    item { SegmentedButtonShowcase() }
                }
                SampleCategory.Inputs -> {
                    item { TextFieldShowcase() }
                    item { SearchBarShowcase() }
                    item { SelectionShowcase() }
                    item { SliderShowcase() }
                    item { DatePickerSheetShowcase() }
                    item { DateRangePickerSheetShowcase() }
                }
                SampleCategory.Containment -> {
                    item { CardShowcase() }
                    item { ListItemShowcase() }
                    item { AvatarShowcase() }
                    item { BadgeShowcase() }
                    item { TooltipShowcase() }
                    item { DropdownMenuShowcase() }
                    item { BarChartShowcase() }
                    item { DonutChartShowcase() }
                    item { LineChartShowcase() }
                }
                SampleCategory.Navigation -> {
                    item { TopAppBarShowcase() }
                    item { TabsShowcase() }
                    item { NavigationBarShowcase() }
                    item { NavigationRailShowcase() }
                    item { NavigationDrawerShowcase() }
                    item { BottomAppBarShowcase() }
                }
                SampleCategory.Feedback -> {
                    item { DialogShowcase() }
                    item { BottomSheetShowcase() }
                    item { SnackbarShowcase() }
                    item { LoadingShowcase() }
                    item { EmptyStateShowcase() }
                }
            }
        }
    }
}

/**
 * A single component's demo: a name, a one-line description, then the live demo itself — all on
 * a consistent outlined surface. This is the main polish lever for the showcase, replacing the
 * old bare section title.
 */
@Composable
fun ComponentShowcase(
    name: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    FormaCard(variant = FormaCardVariant.Outlined, modifier = modifier.fillMaxWidth()) {
        Text(name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(FormaTheme.spacing.xs))
        content()
    }
}
