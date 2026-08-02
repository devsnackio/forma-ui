<div align="center">

# FormaUI

**Opinionated, Material You-native Jetpack Compose components that look great with zero styling work.**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-minSdk%2024-3DDC84.svg?logo=android&logoColor=white)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/dev.formaui/components?label=Maven%20Central)](https://central.sonatype.com/artifact/dev.formaui/components)

[Documentation](https://formaui.dev) · [Components](#components) · [Theming](#theming) · [Changelog](CHANGELOG.md) · [Contributing](CONTRIBUTING.md)

</div>

---

## What is FormaUI?

FormaUI is a [Jetpack Compose](https://developer.android.com/jetpack/compose) component library for **Android**, built as a **themed layer on top of Material 3** — production-ready components with better defaults, so you can ship fast instead of building a design system from scratch.

**The positioning wedge:** Unlike headless/unstyled toolkits (e.g. Composables UI — "Compose without Material"), FormaUI is deliberately **opinionated and Material 3-native**. Components ship with a distinct brand look — Public Sans with an editorial display scale over a warm-editorial palette — yet still feel like the Material 3 you already know (and Material You dynamic color is one flag away), so anyone productive in Compose Material 3 is productive in FormaUI within minutes.

- **Distinctive out of the box** — ships with the **Public Sans** typeface, an editorial display scale (64sp display tier, negative tracking, Medium-weight labels), and a warm-editorial brand palette (cream canvas, coral primary, warm ink text, dark-navy dark scheme) on by default, so components look like *FormaUI*, not stock Material.
- **Zero-config, fully overridable** — every component works with just its required params, but exposes `modifier`, `colors`, `shape`, and more where it matters.
- **Material You, one flag away** — the brand palette is the default; opt into wallpaper-based dynamic color with `FormaTheme(dynamicColor = true)` on Android 12+.
- **Accessibility built in** — 48dp touch targets, correct semantics roles, and content descriptions are not optional.
- **Slot-based & state-hoisted** — standard Compose conventions, no unfamiliar parallel API.

> **Pre-1.0.** All public APIs are annotated `@ExperimentalFormaUiApi` while the surface stabilizes. Expect breaking changes before `1.0.0`.

## Install

FormaUI publishes to Maven Central under the `dev.formaui` group. Add it to your version catalog / Gradle build:

```kotlin
dependencies {
    implementation("dev.formaui:components:0.2.0-beta01") // components transitively brings in :core
    // or depend on the theming engine alone:
    // implementation("dev.formaui:core:0.2.0-beta01")
}
```

> `0.2.0-beta01` is the current release — it verifies the `dev.formaui` namespace end-to-end on
> Central. `0.2.0` stable follows from the same code; until it lands, a bare `0.2.0` will not
> resolve. The Maven Central badge above always shows what is actually published.

> **Moved from `io.github.devsnackio` in `0.2.0`.** Releases up to `0.1.0` shipped under that group, before the `formaui.dev` domain was owned. Only the coordinate changed — imports were always `dev.formaui.*`, so migrating is a one-line edit to your dependency. Relocation POMs keep old builds resolving, but the retired group gets no further releases.

**Requirements:** Android `minSdk 24`+, Kotlin 2.4.x, Material 3. **Android is the only published target.** The artifacts are built with Compose Multiplatform 1.11.x (`org.jetbrains.compose`), so they also work in a Compose Multiplatform app's Android source set; no other target is published.

Because the APIs are experimental pre-1.0, opt in where you use them:

```kotlin
@OptIn(ExperimentalFormaUiApi::class)
@Composable
fun MyScreen() { /* … */ }
```

## Quick start

Wrap your app in `FormaTheme` and drop in components:

```kotlin
@OptIn(ExperimentalFormaUiApi::class)
@Composable
fun App() {
    FormaTheme {                       // brand palette by default; pass dynamicColor = true for Material You on Android 12+
        Column {
            FormaButton(onClick = { /* … */ }) { Text("Get started") }

            var query by remember { mutableStateOf("") }
            FormaTextField(
                value = query,
                onValueChange = { query = it },
                label = "Search",
            )

            FormaCard(variant = FormaCardVariant.Elevated) {
                Text("Cards, chips, sheets, and 37 more — all themed to match.")
            }
        }
    }
}
```

## Theming

`FormaTheme` layers FormaUI tokens on top of Material 3's `ColorScheme`:

```kotlin
@Composable
fun FormaTheme(
    colorScheme: FormaColorScheme = FormaTheme.defaultColorScheme(),
    typography: FormaTypography = FormaTheme.defaultTypography(),
    shapes: FormaShapes = FormaTheme.defaultShapes(),
    dynamicColor: Boolean = false,              // brand palette by default; true = Material You on Android 12+
    darkTheme: Boolean = isSystemInDarkTheme(),  // force light/dark, or follow the system
    content: @Composable () -> Unit,
)
```

Read tokens anywhere inside the theme via `FormaTheme.colorScheme`, `FormaTheme.typography`, `FormaTheme.shapes`, and `FormaTheme.spacing`. Design tokens:

- **`FormaSpacing`** — a 4dp grid (`xxs` 4 / `xs` 8 / `sm` 12 / `md` 16 / `lg` 24 / `xl` 32 / `xxl` 48 / `section` 96); components use these internally, never hardcoded dp.
- **`FormaShapes`** — corner tiers `none` / `xs` 4 / `sm` 6 / `md` 8 / `lg` 12 / `xl` 16 / `pill` / `full`.
- **`FormaTypography`** — an editorial Public Sans type scale plus a tabular-figures `numeric` style for financial/data display.
- **`FormaColorScheme`** — light + dark brand palettes, fully overridable.

## Components

All 40 components (the 18 in the original Phase 1 scope, plus 22 extras), each with variants/states, KDoc, `@Preview`s, and UI tests:

| | | |
|---|---|---|
| **Button** — filled/outlined/text/elevated/tonal | **TextField** — outlined/filled, error/disabled, icons, helper text | **Card** — elevated/outlined/filled, header/content/footer, clickable |
| **Chip** — assist/filter/input/suggestion | **Badge** — dot/numeric/overflow + `FormaBadgedBox` | **Switch** |
| **Checkbox** | **RadioButton** | **Dialog** — alert + full-screen |
| **BottomSheet** — modal | **NavigationBar** — with per-item badges | **ListItem** — one/two/three-line, slots, clickable |
| **Avatar** — initials/icon/image slot, sized | **Divider** — horizontal/vertical | **LoadingIndicator** — circular/linear, determinate/indeterminate |
| **EmptyState** — icon + title + description + action | **Snackbar** — standard + action, `FormaSnackbarHost` | **Slider** |
| **TopAppBar** — small/center-aligned/medium/large | **FloatingActionButton** — small/regular/large + extended | **IconButton** — standard/filled/tonal/outlined |
| **BottomAppBar** — actions + optional FAB | **DropdownMenu** — items with leading/trailing icons | **NavigationDrawer** — modal, slot-based items |
| **NavigationRail** — with badges + optional header | **SearchBar** — docked + full-screen | **SegmentedButton** — single/multi-select |
| **TabRow** — primary/secondary, fixed/scrollable | **Tooltip** — plain + rich | **ExposedDropdownMenu** — autocomplete, editable/tap-to-select |
| **DatePickerSheet** — calendar + text-input, in a sheet | **DateRangePickerSheet** — start/end range, in a sheet | **TimePickerSheet** — clock dial + text input, in a sheet |
| **RangeSlider** — two-thumb, continuous/stepped | **Carousel** — multi-browse/uncontained, snapping | **PullToRefresh** — swipe-down refresh, indicator slot |
| **SwipeToDismiss** — per-direction, background slot | **BarChart** — gridlines, value labels, entry animation | **DonutChart** — arc segments, center slot, legend |
| **LineChart** — smooth/straight, area fill, markers | | |

## Try it — sample app

A runnable showcase app demonstrates every component live, with light/dark and dynamic-color toggles:

```bash
./gradlew :sample:installDebug   # with an emulator/device connected, then launch "FormaUI Sample"
```

Or open the project in Android Studio and run the **`sample`** configuration. The sample screen is itself built entirely from FormaUI components — it doubles as a reference usage example.

## Project structure

```
core/          # Theming engine: FormaTheme, color/typography/spacing/shape tokens (zero FormaUI deps)
components/    # The 40 components (depends on :core)
sample/        # Runnable Android showcase app
build-logic/   # Gradle convention plugins
```

Targets: **Android** (the published artifact) and **`wasmJs`** (compiled for the live component previews embedded in the docs site).

## License

FormaUI `core` and `components` are licensed under the [Apache License 2.0](LICENSE).

---

<div align="center">
Built by <a href="https://github.com/devsnackio">DevSnack</a> · Docs & live previews at <a href="https://formaui.dev">formaui.dev</a>
</div>
