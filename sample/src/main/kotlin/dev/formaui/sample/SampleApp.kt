/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.formaui.components.avatar.FormaAvatar
import dev.formaui.components.avatar.FormaAvatarSize
import dev.formaui.components.badge.FormaBadge
import dev.formaui.components.badge.FormaBadgedBox
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.card.FormaCard
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.components.checkbox.FormaCheckbox
import dev.formaui.components.chip.FormaChip
import dev.formaui.components.chip.FormaChipVariant
import dev.formaui.components.dialog.FormaAlertDialog
import dev.formaui.components.dialog.FormaFullScreenDialog
import dev.formaui.components.divider.FormaDivider
import dev.formaui.components.emptystate.FormaEmptyState
import dev.formaui.components.fab.FormaExtendedFloatingActionButton
import dev.formaui.components.fab.FormaFabSize
import dev.formaui.components.fab.FormaFloatingActionButton
import dev.formaui.components.iconbutton.FormaIconButton
import dev.formaui.components.iconbutton.FormaIconButtonVariant
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.loading.FormaLoadingIndicator
import dev.formaui.components.loading.FormaLoadingIndicatorVariant
import dev.formaui.components.navigation.FormaNavigationBar
import dev.formaui.components.navigation.FormaNavigationBarItem
import dev.formaui.components.radiobutton.FormaRadioButton
import dev.formaui.components.slider.FormaSlider
import dev.formaui.components.snackbar.FormaSnackbar
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.components.topappbar.FormaTopAppBar
import dev.formaui.components.topappbar.FormaTopAppBarVariant
import dev.formaui.components.textfield.FormaTextFieldVariant
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * How the showcase forces its color scheme, independent of the system setting.
 */
private enum class ThemeMode { System, Light, Dark }

/**
 * Root of the FormaUI showcase app: hosts the theme-mode + dynamic-color state and wraps the
 * [ShowcaseScreen] in [FormaTheme].
 *
 * The in-app light/dark selector maps to `FormaTheme`'s `darkTheme` parameter, which drives both
 * the static brand palette and (on Android 12+) the dynamic-color path.
 */
@Composable
fun SampleApp() {
    var themeMode by remember { mutableStateOf(ThemeMode.System) }
    var dynamicColor by remember { mutableStateOf(false) }

    val darkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    FormaTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ShowcaseScreen(
                themeMode = themeMode,
                onThemeModeChange = { themeMode = it },
                dynamicColor = dynamicColor,
                onDynamicColorChange = { dynamicColor = it },
            )
        }
    }
}

@Composable
private fun ShowcaseScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(FormaTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.lg),
    ) {
        Text("FormaUI", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Component showcase — 21 components, live and interactive.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        ThemeControls(themeMode, onThemeModeChange, dynamicColor, onDynamicColorChange)

        ButtonSection()
        IconButtonSection()
        FabSection()
        TextFieldSection()
        CardSection()
        ChipSection()
        BadgeSection()
        SelectionSection()
        AvatarSection()
        ListItemSection()
        LoadingSection()
        SliderSection()
        SnackbarSection()
        TopAppBarSection()
        NavigationBarSection()
        DialogSection()
        BottomSheetSection()
        EmptyStateSection()
    }
}

/** A titled group with consistent vertical rhythm. */
@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        content()
    }
}

@Composable
private fun ThemeControls(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
) {
    FormaCard(
        variant = FormaCardVariant.Filled,
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Theme", style = MaterialTheme.typography.titleMedium) },
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
            ThemeMode.entries.forEach { mode ->
                FormaChip(
                    label = mode.name,
                    onClick = { onThemeModeChange(mode) },
                    variant = FormaChipVariant.Filter,
                    selected = mode == themeMode,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaSwitch(checked = dynamicColor, onCheckedChange = onDynamicColorChange)
            Text("Dynamic color (Android 12+)")
        }
    }
}

@Composable
private fun ButtonSection() {
    Section("Button") {
        var clicks by remember { mutableIntStateOf(0) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
            FormaButtonVariant.entries.forEach { variant ->
                FormaButton(onClick = { clicks++ }, variant = variant) {
                    Text(variant.name)
                }
            }
        }
        Text("Clicked $clicks times", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun IconButtonSection() {
    Section("IconButton") {
        var likes by remember { mutableIntStateOf(0) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
            FormaIconButtonVariant.entries.forEach { variant ->
                FormaIconButton(onClick = { likes++ }, variant = variant) {
                    Text("★")
                }
            }
            FormaIconButton(onClick = {}, enabled = false) {
                Text("★")
            }
        }
        Text("Liked $likes times", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun FabSection() {
    Section("FloatingActionButton") {
        var taps by remember { mutableIntStateOf(0) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            verticalAlignment = Alignment.Bottom,
        ) {
            FormaFloatingActionButton(onClick = { taps++ }, size = FormaFabSize.Small) {
                Text("+")
            }
            FormaFloatingActionButton(onClick = { taps++ }, size = FormaFabSize.Regular) {
                Text("+")
            }
            FormaFloatingActionButton(onClick = { taps++ }, size = FormaFabSize.Large) {
                Text("+")
            }
        }

        var expanded by remember { mutableStateOf(true) }
        FormaExtendedFloatingActionButton(
            text = "Compose",
            icon = { Text("+") },
            onClick = { expanded = !expanded },
            expanded = expanded,
        )
        Text("Tapped $taps times · tap the extended FAB to toggle expansion", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun TextFieldSection() {
    Section("TextField") {
        var text by remember { mutableStateOf("") }
        FormaTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Outlined",
            placeholder = "Type something",
            helperText = "Helper text",
            singleLine = true,
        )
        FormaTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            variant = FormaTextFieldVariant.Filled,
            label = "Filled",
            singleLine = true,
        )
        FormaTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = "With validation",
            isError = text.isBlank(),
            errorText = "This field is required",
            helperText = "Cannot be empty",
            singleLine = true,
        )
    }
}

@Composable
private fun CardSection() {
    Section("Card") {
        FormaCardVariant.entries.forEach { variant ->
            FormaCard(
                variant = variant,
                modifier = Modifier.fillMaxWidth(),
                header = { Text("${variant.name} card", style = MaterialTheme.typography.titleMedium) },
                footer = {
                    FormaButton(onClick = {}, variant = FormaButtonVariant.Text) { Text("Action") }
                },
            ) {
                Text("Balance", style = MaterialTheme.typography.labelMedium)
                Text("$1,240.00", style = FormaTheme.typography.numeric)
            }
        }

        // A clickable card — the whole surface is one target.
        var taps by remember { mutableIntStateOf(0) }
        FormaCard(
            variant = FormaCardVariant.Elevated,
            modifier = Modifier.fillMaxWidth(),
            onClick = { taps++ },
            header = { Text("Clickable card", style = MaterialTheme.typography.titleMedium) },
            footer = { Text("Tap anywhere on the card", style = MaterialTheme.typography.labelMedium) },
        ) {
            Text("Tapped $taps times", style = FormaTheme.typography.numeric)
        }
    }
}

@Composable
private fun ChipSection() {
    Section("Chip") {
        var filterOn by remember { mutableStateOf(true) }
        var inputOn by remember { mutableStateOf(false) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
            FormaChip(label = "Assist", onClick = {}, variant = FormaChipVariant.Assist)
            FormaChip(label = "Suggestion", onClick = {}, variant = FormaChipVariant.Suggestion)
            FormaChip(
                label = "Filter",
                onClick = { filterOn = !filterOn },
                variant = FormaChipVariant.Filter,
                selected = filterOn,
            )
            FormaChip(
                label = "Input",
                onClick = { inputOn = !inputOn },
                variant = FormaChipVariant.Input,
                selected = inputOn,
            )
        }
    }
}

@Composable
private fun BadgeSection() {
    Section("Badge") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xl)) {
            FormaBadgedBox(badge = { FormaBadge() }) { Text("Dot") }
            FormaBadgedBox(badge = { FormaBadge(count = 5) }) { Text("Count") }
            FormaBadgedBox(badge = { FormaBadge(count = 150) }) { Text("Overflow") }
        }
    }
}

@Composable
private fun SelectionSection() {
    Section("Switch · Checkbox · RadioButton") {
        var switchOn by remember { mutableStateOf(true) }
        var checked by remember { mutableStateOf(true) }
        val options = listOf("Standard", "Priority", "Overnight")
        var selected by remember { mutableStateOf(options.first()) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaSwitch(checked = switchOn, onCheckedChange = { switchOn = it })
            Text("Notifications ${if (switchOn) "on" else "off"}")
        }

        Row(
            modifier = Modifier.toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = { checked = it },
            ),
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaCheckbox(checked = checked, onCheckedChange = null)
            Text("I agree to the terms")
        }

        Column(modifier = Modifier.selectableGroup()) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = option == selected,
                            role = Role.RadioButton,
                            onClick = { selected = option },
                        ),
                    horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FormaRadioButton(selected = option == selected, onClick = null)
                    Text(option)
                }
            }
        }
    }
}

@Composable
private fun AvatarSection() {
    Section("Avatar") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaAvatar(initials = "AB", size = FormaAvatarSize.Small)
            FormaAvatar(initials = "CD", size = FormaAvatarSize.Medium)
            FormaAvatar(initials = "EF", size = FormaAvatarSize.Large)
            FormaAvatar(size = FormaAvatarSize.Medium) { Text("★") }
        }
    }
}

@Composable
private fun ListItemSection() {
    Section("ListItem · Divider") {
        FormaListItem(
            headline = "Single line",
            trailing = { Text("›") },
            onClick = {},
        )
        FormaDivider()
        FormaListItem(
            headline = "Ada Lovelace",
            supporting = "ada@example.com",
            leading = { FormaAvatar(initials = "AL", size = FormaAvatarSize.Small) },
            trailing = { Text("›") },
            onClick = {},
        )
        FormaDivider()
        FormaListItem(
            headline = "Payment received",
            overline = "Today, 09:41",
            supporting = "$1,240.00 from Grace Hopper",
            leading = { FormaAvatar(initials = "GH", size = FormaAvatarSize.Small) },
        )
    }
}

@Composable
private fun LoadingSection() {
    Section("LoadingIndicator") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaLoadingIndicator(contentDescription = "Loading")
            FormaLoadingIndicator(progress = 0.6f, contentDescription = "60 percent loaded")
            Text("Circular (indeterminate + 60%)", style = MaterialTheme.typography.bodySmall)
        }
        FormaLoadingIndicator(
            modifier = Modifier.fillMaxWidth(),
            variant = FormaLoadingIndicatorVariant.Linear,
            contentDescription = "Loading",
        )
        FormaLoadingIndicator(
            modifier = Modifier.fillMaxWidth(),
            variant = FormaLoadingIndicatorVariant.Linear,
            progress = 0.6f,
            contentDescription = "60 percent loaded",
        )
    }
}

@Composable
private fun EmptyStateSection() {
    Section("EmptyState") {
        FormaCard(variant = FormaCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            FormaEmptyState(
                title = "No transactions yet",
                description = "Your transactions will appear here once you make your first payment.",
                modifier = Modifier.fillMaxWidth(),
                icon = { Text("🧾", style = MaterialTheme.typography.headlineLarge) },
                action = {
                    FormaButton(onClick = {}) { Text("Add payment") }
                },
            )
        }
    }
}

@Composable
private fun SliderSection() {
    Section("Slider") {
        var continuous by remember { mutableFloatStateOf(0.4f) }
        Text("Volume: ${(continuous * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
        FormaSlider(
            value = continuous,
            onValueChange = { continuous = it },
            modifier = Modifier.fillMaxWidth(),
        )

        var stepped by remember { mutableFloatStateOf(2f) }
        Text("Rating: ${stepped.toInt()}", style = MaterialTheme.typography.bodySmall)
        FormaSlider(
            value = stepped,
            onValueChange = { stepped = it },
            valueRange = 0f..4f,
            steps = 3,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SnackbarSection() {
    Section("Snackbar") {
        FormaSnackbar(message = "Changes saved.")
        FormaSnackbar(message = "Message deleted.", actionLabel = "Undo", onAction = {})
    }
}

@Composable
private fun TopAppBarSection() {
    Section("TopAppBar") {
        var variant by remember { mutableStateOf(FormaTopAppBarVariant.Small) }
        var navigationTaps by remember { mutableIntStateOf(0) }

        FlowRow(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
            FormaTopAppBarVariant.entries.forEach { entry ->
                FormaChip(
                    label = entry.name,
                    onClick = { variant = entry },
                    variant = FormaChipVariant.Filter,
                    selected = entry == variant,
                )
            }
        }

        FormaCard(variant = FormaCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            FormaTopAppBar(
                title = "Inbox",
                modifier = Modifier.fillMaxWidth(),
                variant = variant,
                navigationIcon = {
                    FormaIconButton(onClick = { navigationTaps++ }) { Text("‹") }
                },
                actions = {
                    FormaIconButton(onClick = {}) { Text("⋮") }
                },
            )
        }
        Text("Navigation icon tapped $navigationTaps times", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun NavigationBarSection() {
    Section("NavigationBar") {
        var tab by remember { mutableIntStateOf(0) }
        FormaNavigationBar {
            FormaNavigationBarItem(
                selected = tab == 0,
                onClick = { tab = 0 },
                icon = { Text("🏠") },
                label = "Home",
            )
            FormaNavigationBarItem(
                selected = tab == 1,
                onClick = { tab = 1 },
                icon = { Text("🔔") },
                label = "Alerts",
                badgeCount = 3,
            )
            FormaNavigationBarItem(
                selected = tab == 2,
                onClick = { tab = 2 },
                icon = { Text("👤") },
                label = "Profile",
                showBadgeDot = true,
            )
        }
    }
}

@Composable
private fun DialogSection() {
    Section("Dialog") {
        var showAlert by remember { mutableStateOf(false) }
        var showFullScreen by remember { mutableStateOf(false) }

        Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.sm)) {
            FormaButton(onClick = { showAlert = true }, variant = FormaButtonVariant.Outlined) {
                Text("Alert dialog")
            }
            FormaButton(onClick = { showFullScreen = true }, variant = FormaButtonVariant.Outlined) {
                Text("Full-screen")
            }
        }

        if (showAlert) {
            FormaAlertDialog(
                onDismissRequest = { showAlert = false },
                title = "Delete payment?",
                text = "This action can't be undone.",
                confirmButton = {
                    FormaButton(onClick = { showAlert = false }) { Text("Delete") }
                },
                dismissButton = {
                    FormaButton(onClick = { showAlert = false }, variant = FormaButtonVariant.Text) {
                        Text("Cancel")
                    }
                },
            )
        }

        if (showFullScreen) {
            FormaFullScreenDialog(
                onDismissRequest = { showFullScreen = false },
                title = "New payment",
                confirmAction = {
                    FormaButton(onClick = { showFullScreen = false }, variant = FormaButtonVariant.Text) {
                        Text("Save")
                    }
                },
            ) {
                Text("A full-screen dialog for focused, multi-step tasks.")
                FormaTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = "Amount",
                    singleLine = true,
                )
            }
        }
    }
}

@Composable
private fun BottomSheetSection() {
    Section("BottomSheet") {
        var showSheet by remember { mutableStateOf(false) }
        FormaButton(onClick = { showSheet = true }, variant = FormaButtonVariant.Outlined) {
            Text("Show bottom sheet")
        }
        if (showSheet) {
            FormaBottomSheet(onDismissRequest = { showSheet = false }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(FormaTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
                ) {
                    Text("Payment options", style = MaterialTheme.typography.titleMedium)
                    Text("Choose how you'd like to pay.")
                    FormaButton(
                        onClick = { showSheet = false },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}
