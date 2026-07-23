# CLAUDE.md — FormaUI

> Source of truth: **`docs/PRD.md`** (product) + **`docs/DESIGN.md`** (visual language).
> This file is the fast map; the docs have the detail. Publishing: `docs/PUBLISHING.md` /
> `docs/RELEASING.md`. Docs-site handoff: `docs/SITE_HANDOFF.md`.

## What this is

FormaUI is a Jetpack Compose / Compose Multiplatform component library with a two-axis
open-core model (free + premium components; free + paid vertical kits). Positioning wedge:
**opinionated and Material You-native** — production-ready components that look great with
zero styling work. This is the anti-"headless/unstyled" (Composables UI) stance. Target
user: developers who want to ship fast, not build a design system.

## Status

**Phase 1 library work is DONE: 29/29 components built + tested** (18 PRD + 11 extra —
see `docs/component-inventory.json`). Warm-editorial rebrand per `docs/DESIGN.md` landed
2026-07-22 (breaking token renames; typography stays Public Sans — no serif/Inter).
Remaining Phase 1: the `formaui-site` docs site + first Maven Central publish.

⚠ **Open decision:** Maven group is currently `io.github.devsnackio` (`build.gradle.kts`,
publishing convention plugin) while code packages are `dev.formaui.*` — resolve before
publishing; ask the user.

Out of scope (do NOT build): premium components, kit modules, licensing/purchase flows.

## Build & test (memorize this — bare `./gradlew` fails)

```bash
export JAVA_HOME=~/Library/Java/JavaVirtualMachines/jbr-17.0.14/Contents/Home
./gradlew :core:testAndroidHostTest :components:testAndroidHostTest  # unit gate (Robolectric)
./gradlew :core:compileKotlinWasmJs :components:compileKotlinWasmJs  # wasm must compile
./gradlew :sample:assembleDebug                                      # sample app builds
```

All three together = the authoritative QA gate. Wrapper only — there is no standalone
`gradle`/`kotlin` CLI. Toolchain: Gradle 9.6 · Kotlin 2.4.10 · CMP 1.11.1 · AGP 9.3.0
(pinned in `gradle/libs.versions.toml`; material3 is version-decoupled: `composeMaterial3`).

## Tech stack

- Kotlin (latest stable), **Compose Multiplatform (JetBrains)** — not AndroidX-only Compose.
- Targets: **Android** (the shipped, published artifact) + **`wasmJs`** (docs previews only).
- Material 3 base, extended with FormaUI tokens — a themed layer on M3, not from scratch.
- min SDK **24**; compile/target SDK 37.
- Code namespace **`dev.formaui.*`** (Maven group: see open decision above).
- Gradle Kotlin DSL, `libs.versions.toml` version catalog, KMP plugin.

## Module architecture (this repo)

```
core/          # theming engine: FormaTheme, color/typography/spacing/shape tokens. ZERO FormaUI deps.
components/    # the 29 free components; depends on core.
sample/        # Android showcase app (not published). AGP 9 built-in Kotlin — do NOT apply kotlin.android.
preview-wasm/  # wasm live-preview harness for the docs site (not published). Pipeline validated —
               # see docs/WASM_PREVIEW_PIPELINE.md + docs/SITE_HANDOFF.md.
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

## Testing gotchas

- Compose UI tests live in **`src/androidHostTest`** (Robolectric, `@Config(sdk = [34])`) —
  NOT `commonTest`. wasmJs carries no tests; it only has to compile.
- JUnit asserts only (`kotlin.test` is not a dependency). Reference test:
  `components/src/androidHostTest/.../button/FormaButtonTest.kt`.
- `:core`/`:components` use AGP 9's **`com.android.kotlin.multiplatform.library`** plugin
  (NOT `com.android.library`); config lives in `build-logic/.../formaui.kmp.library.gradle.kts`.
- `settings.gradle.kts` has a content-scoped `mavenLocal()` SSL-proxy workaround for
  `ui-tooling-preview` — don't remove it until the proxy CA is trusted.

## "Done" bar for any new component

Impl with full variant/state coverage · KDoc · a `@Preview` covering all variants · a
Robolectric UI test (renders + responds to ≥1 state change) · entry in
`docs/component-inventory.json` · doc page + live preview the same day the site exists.

## Docs site is first-class (parallel, not after)

`formaui-site` (Next.js + Tailwind, separate repo) with **live Wasm-compiled previews** of the
real components embedded. The preview pipeline is **already validated** (`:preview-wasm`) —
start from `docs/SITE_HANDOFF.md` and `docs/WASM_PREVIEW_PIPELINE.md`, don't re-derisk it.

## Ask before deciding (PRD §10)

Ask the user before anything expensive to reverse: **package structure**, the **`FormaTheme`
public API shape**, **min SDK**, the **Maven group / namespace split** (see Status), and
**whether to keep live Wasm previews vs. the static fallback**. Everything else — best
judgment, proceed.
