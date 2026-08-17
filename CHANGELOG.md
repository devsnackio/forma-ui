# Changelog

All notable changes to FormaUI are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) — with the pre-1.0 caveat that
**every public API is annotated `@ExperimentalFormaUiApi` and may change in any release** until the
surface is proven.

Artifacts are `dev.formaui:core` and `dev.formaui:components`, matching the `dev.formaui.*` Kotlin
package. Releases up to and including `0.1.0` shipped under the old `io.github.devsnackio` group —
see [0.2.0](#020--2026-08-03).

## [Unreleased]

### Added

- **`FormaTheme` takes a `spacing: FormaSpacing` parameter**, defaulting to the 4dp grid, alongside
  the existing `colorScheme` / `typography` / `shapes`. `FormaTheme.defaultSpacing()` returns the
  default scale, matching the other three `default*()` accessors.

  This closes a documented-but-absent capability rather than adding a new one: `FormaSpacing`'s
  KDoc said "every value is overridable" and `FormaTheme`'s told you to provide
  `LocalFormaSpacing` yourself, but that composition local is `internal` and `FormaTheme`
  hardcoded `FormaSpacing()` over anything supplied from outside. Constructing a custom scale was
  possible; installing one was not. The parameter is additive and source-compatible.

### Fixed

- The `-javadoc.jar` published for `core` and `components` was **empty** — the `Jar` task set a
  classifier but never a source. It is now packed from Dokka's HTML output, so the library's KDoc
  reaches Maven Central instead of being discarded at publish time. Affects every release up to and
  including `0.2.0`, whose javadoc jars are 261-byte stubs.

- `SECURITY.md` listed `0.1.x` as the supported line after `0.2.0` shipped.

### Removed

- The `mavenLocal()` repository from `settings.gradle.kts`. It was a workaround for an
  SSL-inspecting proxy on one machine, scoped to `org.jetbrains.compose.ui:ui-tooling-preview`, and
  had become dead code — those artifacts resolve from Maven Central and were not present in the
  local `~/.m2` at all. The full gate passes with `--refresh-dependencies` without it.

## [0.2.0] — 2026-08-03

### Changed

- **Breaking: the Maven group is now `dev.formaui`.** With the `formaui.dev` domain acquired, the
  coordinates finally match the Kotlin package. Update your dependency:

  ```diff
  - implementation("io.github.devsnackio:components:0.1.0")
  + implementation("dev.formaui:components:0.2.0")
  ```

  **No code changes are required** — imports were always `dev.formaui.*` and are untouched. Only the
  coordinate string moves.

  Relocation POMs are published under `io.github.devsnackio:*:0.2.0`, so an un-migrated build still
  resolves and warns rather than silently pinning to `0.1.0`. They are a migration aid, not a
  supported channel: the old group receives no further releases.

- The published POM's `url` now points at `https://formaui.dev` (the docs site). `scm` continues to
  point at `github.com/devsnackio/forma-ui`.

- **The POM description is now Android-first**: "opinionated, Material You-native Compose UI
  components **for Android**, built as a themed layer on Material 3." It previously said "Compose
  Multiplatform UI components", which implied the artifacts resolve on non-Android targets. They do
  not — Android is the only published target. The library is still authored against Compose
  Multiplatform (`org.jetbrains.compose`), and the `wasmJs` target still exists for the docs-site
  previews, but neither is consumable from Maven. README and the component reference say the same.

## [0.2.0-beta01] — 2026-08-01

First artifact published under `dev.formaui`, cut to verify that the new namespace resolves
end-to-end on Maven Central before `0.2.0` stable. Library content is identical to
[0.2.0](#020--2026-08-03) — no component, API, or token differs between the two, so there is no
reason to depend on it over `0.2.0`. It stays on Central permanently, as every release does.

Relocation POMs for the retired `io.github.devsnackio` coordinates were **not** part of this
release; they point at stable `0.2.0` and ship only once it resolves.

## [0.1.0] — 2026-07-27

First stable release, published to Maven Central as `io.github.devsnackio:{core,components}:0.1.0`.
Preceded by `0.1.0-beta01`–`beta04` under the same group.

> Not reproducible from a tag: no commit bumped the build files to `0.1.0`, and none was tagged
> `v0.1.0`. Recorded here for accuracy — see `docs/RELEASING.md`.

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
  previews of the real components. Android is the supported consumer target; `*-wasm-js` klibs were
  published at this version by accident and should not be depended on. `0.2.0` onward excludes them.

### Changed

- **Breaking (beta): warm-editorial rebrand.** Design tokens were renamed as part of the move to
  the warm editorial visual language. Typography remains Public Sans — no serif
  or Inter was adopted. Landed during the beta line, so only `0.1.0-beta0x` consumers were affected.

### Notes

- Requires Android `minSdk 24`; built against compile/target SDK 37.
- Built with Kotlin 2.4.10, Compose Multiplatform 1.11.1, Material 3 1.9.0, AGP 9.3.1, Gradle 9.6.
- Opt in at every use site: `@OptIn(ExperimentalFormaUiApi::class)`.

[0.2.0]: https://github.com/devsnackio/forma-ui/releases/tag/v0.2.0
[0.2.0-beta01]: https://github.com/devsnackio/forma-ui/releases/tag/v0.2.0-beta01
[0.1.0]: https://central.sonatype.com/artifact/io.github.devsnackio/components/0.1.0
