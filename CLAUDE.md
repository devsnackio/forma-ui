# CLAUDE.md — FormaUI

> Source of truth: **`docs/PRD.md`**. This file is the fast map; the PRD has the detail.

## What this is

FormaUI is a Jetpack Compose / Compose Multiplatform component library with a two-axis
open-core model (free + premium components; free + paid vertical kits). Positioning wedge:
**opinionated and Material You-native** — production-ready components that look great with
zero styling work. This is the anti-"headless/unstyled" (Composables UI) stance. Target
user: developers who want to ship fast, not build a design system.

## Status

**Phase 1, nothing built yet.** This repo will become `formaui/formaui` (public).
In scope: `core` + `components` (18 free components) + parallel docs site.
Out of scope (do NOT build): premium components, kit modules, licensing/purchase flows.

## Tech stack

- Kotlin (latest stable), **Compose Multiplatform (JetBrains)** — not AndroidX-only Compose.
- Targets: **Android** (the shipped, published artifact) + **`wasmJs`** (docs previews only).
- Material 3 base, extended with FormaUI tokens — a themed layer on M3, not from scratch.
- min SDK **24**; compile/target SDK latest stable.
- Namespace **`dev.formaui.*`**; Maven Central group **`dev.formaui`**.
- Gradle Kotlin DSL, `libs.versions.toml` version catalog, KMP plugin.

## Module architecture (this repo)

```
core/          # theming engine: FormaTheme, color/typography/spacing/shape tokens. ZERO FormaUI deps.
components/    # the 18 free components; depends on core.
build-logic/   # convention plugins — set up early to avoid duplicated Gradle config.
```

No circular deps, ever. Other repos (`formaui-site`, `formaui-pro`, `kit-*`) live elsewhere —
they depend on this repo's **published Maven Central artifacts**, never source copies.

## Hard rules (easy to get wrong)

- **Shared code goes in `commonMain`**; no Android-only APIs (e.g. `android.content.Context`)
  in shared code, or the `wasmJs` target won't compile. Dynamic color is Android-only → static
  fallback palette on old Android and on Wasm.
- **No hardcoded dp** in component internals — use `FormaSpacing` tokens (4dp grid).
- All public APIs marked **`@ExperimentalFormaUiApi`** opt-in until the surface is proven.
- **KDoc required** on every public API (this is a public library).
- **Accessibility is not optional**: 48dp min touch targets, `contentDescription`, correct
  semantics roles.
- **State hoisting** (stateless where possible) + **slot-based APIs** for composability.
- Feel like "**M3 with better defaults**" — don't break Material 3 conventions.
- Keep `core`/`components` **lean** — no heavy third-party deps (charts etc. belong in kits/pro).
- License **Apache 2.0**; semver starting **`0.1.0`**.

## Build order & "done" bar

Build in PRD §5.1 priority order: **Button → TextField → Card → Chip → Badge → Switch →
Checkbox → RadioButton → Dialog → BottomSheet → NavigationBar → ListItem → Avatar → Divider →
LoadingIndicator → EmptyState → SnackBar → Slider.**

A component is "done" only when: impl with full variant/state coverage · KDoc · a `@Preview`
covering all variants · a Compose UI test (renders + responds to ≥1 state change) · doc page +
live preview added the **same day** it's finished.

## Docs site is first-class (parallel, not after)

`formaui-site` (Next.js + Tailwind, separate repo) with **live Wasm-compiled previews** of the
real components embedded. **De-risk the Wasm preview pipeline on Button ALONE first** — it's the
single riskiest unknown. Fall back to static screenshot previews if it stalls rather than
blocking the whole library.

## Ask before deciding (PRD §10)

Ask the user before anything expensive to reverse: **package structure**, the **`FormaTheme`
public API shape**, **min SDK**, and **whether to keep live Wasm previews vs. the static
fallback**. Everything else — best judgment, proceed.
