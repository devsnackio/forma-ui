/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import dev.formaui.components.bottomsheet.FormaBottomSheet
import dev.formaui.components.iconbutton.FormaIconButton
import dev.formaui.components.navigation.FormaNavigationBar
import dev.formaui.components.navigation.FormaNavigationBarItem
import dev.formaui.components.segmentedbutton.FormaSegmentedButton
import dev.formaui.components.segmentedbutton.FormaSegmentedButtonRow
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.components.topappbar.FormaTopAppBar
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * How the showcase forces its color scheme, independent of the system setting.
 */
private enum class ThemeMode { System, Light, Dark }

/**
 * Root of the FormaUI showcase app: hosts the theme-mode + dynamic-color state and wraps
 * [ShowcaseScaffold] in [FormaTheme].
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
            ShowcaseScaffold(
                themeMode = themeMode,
                onThemeModeChange = { themeMode = it },
                dynamicColor = dynamicColor,
                onDynamicColorChange = { dynamicColor = it },
            )
        }
    }
}

/**
 * The showcase's app shell: a pinned [FormaTopAppBar] + [FormaNavigationBar] around a
 * [CategoryScreen], plus a settings [FormaBottomSheet] for theme controls.
 */
@Composable
private fun ShowcaseScaffold(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
) {
    var selectedCategory by remember { mutableStateOf(SampleCategory.Actions) }
    var showSettings by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FormaTopAppBar(
                title = selectedCategory.title,
                actions = {
                    FormaIconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            FormaNavigationBar {
                SampleCategory.entries.forEach { category ->
                    val isSelected = category == selectedCategory
                    FormaNavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) category.selectedIcon else category.unselectedIcon,
                                contentDescription = category.navLabel,
                            )
                        },
                        label = category.navLabel,
                    )
                }
            }
        },
    ) { scaffoldPadding ->
        CategoryScreen(category = selectedCategory, contentPadding = scaffoldPadding)
    }

    if (showSettings) {
        FormaBottomSheet(onDismissRequest = { showSettings = false }) {
            SettingsSheet(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                dynamicColor = dynamicColor,
                onDynamicColorChange = onDynamicColorChange,
            )
        }
    }
}

/** The settings sheet content: an app blurb, the theme-mode picker, and the dynamic-color toggle. */
@Composable
private fun SettingsSheet(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(FormaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
    ) {
        Text("FormaUI", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "A Material 3-native component showcase, live and interactive.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text("Theme", style = MaterialTheme.typography.titleMedium)
        FormaSegmentedButtonRow(multiSelect = false) {
            ThemeMode.entries.forEachIndexed { index, mode ->
                FormaSegmentedButton(
                    selected = mode == themeMode,
                    onClick = { onThemeModeChange(mode) },
                    index = index,
                    count = ThemeMode.entries.size,
                    label = { Text(mode.name) },
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormaSwitch(checked = dynamicColor, onCheckedChange = onDynamicColorChange)
            Text("Dynamic color (Android 12+)")
        }
    }
}
