# Changelog

All notable changes to FormaUI are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) — with the pre-1.0 caveat that
**every public API is annotated `@ExperimentalFormaUiApi` and may change in any release** until the
surface is proven.

Artifacts are `io.github.devsnackio:core` and `io.github.devsnackio:components`. The Kotlin package
is `dev.formaui.*` in both — the group/package split is intentional.

## [0.1.0-beta04] — unreleased

The first public release. Nothing prior to this was published to Maven Central, so everything below
is new to consumers; earlier `beta01`–`beta03` version numbers were internal-only bumps.

### Added

- **40 components**, each shipping with full variant/state coverage, KDoc on every public API, a
  `@Preview` covering its variants, and a Robolectric UI test.
  - *Input & selection* — Button, TextField, Checkbox, RadioButton, Switch, Slider, RangeSlider,
    SegmentedButton, Chip, SearchBar, ExposedDropdownMenu.
  - *Containment & content* — Card, ListItem, Avatar, Badge, Divider, EmptyState, Carousel.
  - *Navigation* — TopAppBar, BottomAppBar, NavigationBar, NavigationRail, NavigationDrawer, TabRow,
    FloatingActionButton, IconButton, DropdownMenu.
  - *Overlays & feedback* — Dialog, BottomSheet, Snackbar, Tooltip, LoadingIndicator.
  - *Gesture* — PullToRefresh, SwipeToDismiss.
  - *Pickers* — DatePickerSheet, DateRangePickerSheet, TimePickerSheet.
  - *Charts* — BarChart, DonutChart, LineChart, drawn with pure Compose `Canvas` and no
    chart-library dependency.
- **`FormaTheme`** and the token layer in `:core` — `FormaColorScheme` (light + dark warm-editorial
  brand palettes), `FormaTypography` (Public Sans with an editorial display scale and a
  tabular-figures `numeric` style), `FormaShapes`, and `FormaSpacing` (a 4dp grid).
- **Material You dynamic color** as an opt-in (`FormaTheme(dynamicColor = true)`) on Android 12+,
  with a static brand-palette fallback on older Android and on `wasmJs`.
- **`wasmJs` target** for `:core` and `:components`, compiled so the docs site can embed live
  previews of the real components. Only Android artifacts are published.

### Changed

- **Breaking (pre-release): warm-editorial rebrand.** Design tokens were renamed as part of the
  visual language defined in [`docs/DESIGN.md`](docs/DESIGN.md). Typography remains Public Sans — no
  serif or Inter was adopted. Since nothing was ever published, no consumer migration is required.

### Notes

- Requires Android `minSdk 24`; built against compile/target SDK 37.
- Built with Kotlin 2.4.10, Compose Multiplatform 1.11.1, Material 3 1.9.0, AGP 9.3.0, Gradle 9.6.
- Opt in at every use site: `@OptIn(ExperimentalFormaUiApi::class)`.

[0.1.0-beta04]: https://github.com/devsnackio/forma-ui/releases/tag/v0.1.0-beta04
