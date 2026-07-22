# FormaUI → formaui-site handoff pack

**Audience:** the Claude Code session (or human) building `formaui-site` — the Next.js + Tailwind
docs/showcase site (separate repo, deployed to formaui.dev).
**Generated:** 2026-07-22 from `forma-ui` @ `0794836`. Regenerate `component-inventory.json` from a
`forma-ui` session whenever components change (it is extracted from source/KDoc, not hand-written).

## How to use this pack

Launch the site session with read access to this repo:

```bash
cd ~/AndroidStudioProjects/formaui-site
claude --add-dir ../forma-ui
```

(Persist via `permissions.additionalDirectories: ["../forma-ui"]` in the site repo's
`.claude/settings.json`.) Note `--add-dir` does **not** load this repo's CLAUDE.md — this file and
the pointers below are the contract.

Source-of-truth pointers (all relative to the `forma-ui` repo root):

| What | Where |
|---|---|
| Product spec, site requirements | `docs/PRD.md` §7 (doc content), §8.3–8.4 (acceptance) |
| Component API inventory (machine-readable) | `docs/component-inventory.json` |
| Wasm preview build recipe (validated) | `docs/WASM_PREVIEW_PIPELINE.md` |
| Preview harness source | `preview-wasm/` (`src/wasmJsMain/kotlin/dev/formaui/preview/Main.kt`) |
| Component sources / KDoc | `components/src/commonMain/kotlin/dev/formaui/components/` |
| Theme engine | `core/src/commonMain/kotlin/dev/formaui/core/theme/` |

**Ground rule:** the site never copies Kotlin source. It documents the API (from the inventory) and
embeds built wasm bundles. If the inventory looks stale vs. source, regenerate it — don't hand-patch.

## What FormaUI is (landing-page pitch)

FormaUI is an opinionated, Material You-native Jetpack Compose / Compose Multiplatform component
library: production-ready components that look great with zero styling work. It is the
anti-"headless/unstyled" stance — where headless libraries (e.g. Composables UI) give you behavior
and make you build a design system, FormaUI gives you **M3 with better defaults**: a distinctive
brand theme (Public Sans, blue/teal palette, SemiBold button labels), 4dp-grid spacing tokens,
accessibility built in (48dp touch targets, semantics roles), and full override capability
(`modifier`, `colors`, `shape` on everything). Target user: developers who want to ship fast, not
build a design system. Apache 2.0.

- **29 components** (18 from the PRD roadmap + 11 extras) — see the inventory JSON.
- Install (NOT yet published — don't show a copy-paste install as if live until 0.1.0 ships):

```kotlin
dependencies {
    implementation("io.github.devsnackio:components:0.1.0") // brings core transitively
}
```

> Coordinates note: group is `io.github.devsnackio` for now; `dev.formaui` may be adopted at
> release time. Keep coordinates in ONE site constant so a namespace switch is a one-line change.

## `component-inventory.json` schema

Array of 29 objects, sorted PRD components first (`prdOrder` 1–18), then extras (`prdOrder: null`,
`tier: "extra"`, alphabetical). Per object:

```
id            kebab-case slug — use as route (/components/<id>) and preview bundle key
name          display name (e.g. "Button")
package       Kotlin package
sourceFile    repo-relative path to the impl file (KDoc source of truth)
prdOrder      1–18 or null · tier "prd" | "extra"
description   one-sentence, for the doc page header and index cards
composables   every public @Composable: name, summary, params[{name,type,default,description}]
              (default: null ⇒ required parameter — render params tables from this)
supportingApi enums/defaults objects/state holders: name, kind, summary
variants      user-facing variant axis values (drive preview/tab structure per page)
codeSample    minimal copy-pasteable Kotlin snippet (\n-escaped) — the §7.1 code sample
accessibility notes for the §7.1 accessibility section, or null
```

## Per-component doc page (PRD §7.1 checklist)

Each page: one-sentence description · copy-pasteable code sample **with copy button** · full
parameter table · **live interactive preview** (real component, wasm — not a screenshot) ·
accessibility notes when non-default. MDX file per component, referencing its preview by `id`.

## Live preview embed contract

The pipeline is **validated end-to-end** (see `docs/WASM_PREVIEW_PIPELINE.md` for the full recipe
and evidence). The short version the site needs:

1. **Built in `forma-ui`** (not in the site): `:preview-wasm` compiles real components to a wasm
   bundle. Offline-friendly recipe: `wasmJsDevelopmentExecutableCompileSync` +
   `unpackSkikoWasmRuntime`, then assemble a static dir of: Kotlin `.mjs`/`.wasm` output + `skiko.mjs`/
   `skiko.wasm` + `composeResources/` (required — Public Sans fonts load at runtime) + `index.html`.
2. **One bare npm import**: `@js-joda/core`, provided via an import map (`npm install @js-joda/core`
   works through the proxy). No webpack, no Node toolchain.
3. **Site side**: bundles land in `public/previews/<id>/` and embed as an `<iframe>` per doc page.
   Everything is static assets — Vercel-compatible, no server component.
4. **Sync**: a `scripts/sync-previews.sh` in the site repo copying from
   `../forma-ui/preview-wasm/build/...` (manual/scripted copy is the Phase 1 design — no cross-repo
   CI yet).
5. **Theme demo inside previews** (PRD §8.4): each preview should include a light/dark toggle (the
   harness already demonstrates this pattern — `FormaTheme(darkTheme = …)`). Dynamic color is an
   **Android-only OS feature**: on wasm, document it and show the static brand palette; do not fake
   Material You in the browser.

Caveats to design around:
- Current bundle is the **development** executable: ~21 MB wasm + ~8.6 MB skiko. Lazy-load previews
  (click-to-load or IntersectionObserver); the production `wasm-opt` build needs unproxied CI and
  is a later optimization, not a blocker.
- Needs recent Chromium-class browsers (WasmGC + JS-string-builtins). Provide a graceful fallback
  message; screenshots optional.
- Open design choice: one wasm entry per component vs. one parameterized entry selected by URL
  query (`?component=<id>`). The recipe supports both; parameterized keeps total bytes down since
  skiko/runtime is shared — recommended starting point. The current harness renders Button + Switch
  only; extending it per-component is `forma-ui`-side work to request as pages get built.

## Theming facts to document (core module)

```kotlin
@Composable
fun FormaTheme(
    colorScheme: FormaColorScheme = FormaTheme.defaultColorScheme(), // light/dark brand pair
    typography: FormaTypography = FormaTheme.defaultTypography(),    // Public Sans, tabular-figure numeric style
    shapes: FormaShapes = FormaTheme.defaultShapes(),                // none/small/medium/large/full
    dynamicColor: Boolean = false,   // Material You OPT-IN (brand palette is the default)
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
)
```

Token access inside content: `FormaTheme.colorScheme` / `.typography` / `.shapes` / `.spacing`
(xs/sm/md/lg/xl on a 4dp grid). All public APIs are `@ExperimentalFormaUiApi` (pre-1.0 opt-in) —
show the opt-in in setup docs. Deliberate PRD deviation worth documenting: `dynamicColor` defaults
to **false** so the brand look ships out of the box.

## Site acceptance criteria (PRD §8.3–8.4, adapted)

- Live at formaui.dev: landing page + component index + a complete §7.1 doc page per component
  (18 PRD components are the bar; the 11 extras use the same template).
- Light/dark demonstrated **inside** the live previews; dynamic color documented as Android-only.
- Add each new component's page the same day the component lands (PRD §5.3 item 5 — currently
  0/29 pages exist; this pack is the starting gun).
