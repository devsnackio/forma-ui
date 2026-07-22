<div align="center">

# FormaUI

**Opinionated, Material You-native Jetpack Compose components that look great with zero styling work.**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.11.1-4285F4.svg)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-unreleased-lightgrey.svg)](https://central.sonatype.com/)

[Documentation (coming soon)](https://formaui.dev) · [Components](#components) · [Theming](#theming)

</div>

---

## What is FormaUI?

FormaUI is a [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) component library built as a **themed layer on top of Material 3** — production-ready components with better defaults, so you can ship fast instead of building a design system from scratch.

**The positioning wedge:** Unlike headless/unstyled toolkits (e.g. Composables UI — "Compose without Material"), FormaUI is deliberately **opinionated and Material 3-native**. Components ship with a distinct brand look — Public Sans with an editorial display scale over a warm-editorial palette — yet still feel like the Material 3 you already know (and Material You dynamic color is one flag away), so anyone productive in Compose Material 3 is productive in FormaUI within minutes.

- **Distinctive out of the box** — ships with the **Public Sans** typeface, an editorial display scale (64sp display tier, negative tracking, Medium-weight labels), and a warm-editorial brand palette (cream canvas, coral primary, warm ink text, dark-navy dark scheme) on by default, so components look like *FormaUI*, not stock Material.
- **Zero-config, fully overridable** — every component works with just its required params, but exposes `modifier`, `colors`, `shape`, and more where it matters.
- **Material You, one flag away** — the brand palette is the default; opt into wallpaper-based dynamic color with `FormaTheme(dynamicColor = true)` on Android 12+.
- **Accessibility built in** — 48dp touch targets, correct semantics roles, and content descriptions are not optional.
- **Slot-based & state-hoisted** — standard Compose conventions, no unfamiliar parallel API.

> **Pre-1.0.** All public APIs are annotated `@ExperimentalFormaUiApi` while the surface stabilizes. Expect breaking changes before `1.0.0`.

## Install

FormaUI will publish to Maven Central under the `io.github.devsnackio` group (`0.1.0-beta01` release pending; the `dev.formaui` namespace may be adopted at release time once the domain is verified). Add it to your version catalog / Gradle build:

```kotlin
dependencies {
    implementation("io.github.devsnackio:components:0.1.0-beta01") // components transitively brings in :core
    // or depend on the theming engine alone:
    // implementation("io.github.devsnackio:core:0.1.0-beta01")
}
```

> **Not yet published.** The `0.1.0-beta01` artifacts aren't on Maven Central yet — these are the intended coordinates for the pending first release. Stable `0.1.0` follows once the beta has proven itself.

**Requirements:** Android `minSdk 24`+, Kotlin 2.4.x, Compose Multiplatform 1.11.x (or AndroidX Compose with a compatible Material 3).

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
                Text("Cards, chips, sheets, and 26 more — all themed to match.")
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

All 29 Phase 1 components, each with variants/states, KDoc, `@Preview`s, and UI tests:

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
| **TabRow** — primary/secondary, fixed/scrollable | **Tooltip** — plain + rich | |

## Try it — sample app

A runnable showcase app demonstrates every component live, with light/dark and dynamic-color toggles:

```bash
./gradlew :sample:installDebug   # with an emulator/device connected, then launch "FormaUI Sample"
```

Or open the project in Android Studio and run the **`sample`** configuration. The sample screen is itself built entirely from FormaUI components — it doubles as a reference usage example.

## Project structure

```
core/          # Theming engine: FormaTheme, color/typography/spacing/shape tokens (zero FormaUI deps)
components/    # The 29 components (depends on :core)
sample/        # Runnable Android showcase app
build-logic/   # Gradle convention plugins
```

Targets: **Android** (the published artifact) and **`wasmJs`** (compiled for future embedded web previews).

## License

FormaUI `core` and `components` are licensed under the [Apache License 2.0](LICENSE).

---

<div align="center">
Built by <a href="https://formaui.dev">DevSnack</a> · Docs & live previews at <a href="https://formaui.dev">formaui.dev</a> (coming soon)
</div>
