# FormaUI — Product Requirements Document

**Author:** Chanbo (DevSnack)
**Purpose:** Build-ready spec for handoff to Claude Code
**Status:** Phase 1 (Core + Free Component Library)

---

## 1. Vision

FormaUI is a Jetpack Compose component ecosystem with a two-axis open-core business model:

1. **Component-level**: free core components + premium (paid) components
2. **Kit-level**: complete vertical UI kits (fintech, e-commerce, etc.), some free, some paid — the free/paid split per kit is a strategic lever, not a fixed rule

**Positioning wedge:** Unlike Composables UI (headless, unstyled, "Compose without Material"), FormaUI is **opinionated and Material You-native** — production-ready components that look great with zero styling work. Target audience: developers who want to ship fast, not build a design system from scratch.

**Phase 1 scope (this document):** Build `core` and `components` (15–18 free components) as Compose Multiplatform modules (Android + Web/Wasm targets) in the `formaui/formaui` repo, publish the Android artifact to Maven Central, AND build a documentation/showcase site (`formaui-site`, Next.js + Tailwind, deployed to formaui.dev) in parallel — with genuinely interactive, embedded live previews of the real components (via the Wasm target), in the spirit of Composables.com. Docs are treated as a first-class deliverable alongside the components themselves, not an afterthought.

**Explicitly out of scope for Phase 1:** premium components, kit modules (`:kit-fintech`, `:kit-ecommerce`), licensing/gating infrastructure, purchase flows. These are documented in Section 9 for context but not to be built yet.

---

## 2. Tech Stack

- **Language:** Kotlin (latest stable)
- **UI:** Compose Multiplatform (JetBrains), not AndroidX-only Compose. Targets in Phase 1: **Android** (primary — this is the shipped, published artifact devs consume) and **Web/Wasm** (`wasmJs` target — used only to compile interactive component previews for embedding on the docs site, not published/marketed as a standalone web UI product yet). Write shared component code once in `commonMain`; avoid Android-only APIs (e.g. direct `android.content.Context` dependencies) in shared code so both targets compile cleanly.
- **Min SDK:** 24 (Android 7.0) — matches broad device coverage typical of CodeCanyon buyer base
- **Target/Compile SDK:** latest stable
- **Build system:** Gradle with Kotlin DSL (`build.gradle.kts`), version catalogs (`libs.versions.toml`), Kotlin Multiplatform plugin
- **Theming base:** Material 3 via Compose Multiplatform's Material 3 implementation, extended with FormaUI-specific tokens — NOT a from-scratch design system, but a themed layer on top of M3
- **Documentation:** KDoc on all public APIs (required, not optional — this is a public library)
- **Testing:** Compose UI testing for each component — at minimum, a render test and a state-change test per component, run on the Android target (Wasm target only needs to build successfully, not carry full test coverage in Phase 1)
- **Package namespace:** `dev.formaui.*`
- **Publishing target:** Maven Central (Sonatype) — group ID `dev.formaui`, publishing the Android artifact as the primary consumable dependency

---

## 3. Repository & Module Architecture

FormaUI is split across multiple repositories by product and visibility, not bundled into one monorepo. This keeps the free library's public repo clean and discoverable, lets each kit be independently marketable, and makes gating paid content a matter of repo access rather than custom licensing infrastructure.

### 3.1 Repository layout

```
formaui/formaui           (PUBLIC)   — the core library
├── core/                             # Design tokens, theme, foundational utilities
├── components/                       # Free component library (18 components)
├── build-logic/                      # Convention plugins for shared Gradle config
└── settings.gradle.kts

formaui/formaui-site        (PUBLIC)  — docs/showcase site (Next.js, separate codebase)
└── embeds live Wasm-compiled component previews built by formaui/formaui's CI (Section 7.2)

formaui/formaui-pro        (PRIVATE)  — premium components
└── components-pro/                   # Gated behind purchase; depends on formaui/formaui

formaui/kit-fintech         (PUBLIC)  — free kit, standalone repo
└── depends on formaui/formaui via Maven Central, not source copy

formaui/kit-ecommerce       (PRIVATE) — paid kit, standalone repo
└── depends on formaui/formaui via Maven Central, not source copy
```

**For Phase 1, build `formaui/formaui`** (the `core` and `components` modules) **and `formaui/formaui-site`** (the Next.js docs site, per Section 7). Do not create the `formaui-pro`, `kit-fintech`, or `kit-ecommerce` repos yet — those are separate repos created in later phases.

### 3.2 Cross-repo dependency rules

- `formaui-pro` and every `kit-*` repo depend on `formaui/formaui`'s published Maven Central artifacts (`dev.formaui:core`, `dev.formaui:components`) — never by copying or forking source
- Each kit repo is independently versioned and released; a kit release does not require a `core`/`components` version bump and vice versa
- A kit's public/private visibility is a per-kit business decision, not a structural constraint — a kit can change from private to public (or vice versa) later without restructuring anything, since it's already its own repo
- `formaui-pro` is a single private repo for all premium _components_ (add-ons to the core library) — kits are a separate concept from premium components and always live in their own repo, whether free or paid
- `core` has zero dependencies on any other FormaUI module or repo (it's the foundation)
- No circular dependencies, ever — enforce this at the Gradle level within each repo

---

## 4. `:core` Module Spec

This module is the theming engine. Everything else builds on it.

### 4.1 Color tokens

- Full Material You dynamic color support (`androidx.compose.material3.dynamicColorScheme` on Android 12+, with a static fallback palette for older Android devices, and the same static fallback used on the Wasm target, since dynamic color is an Android-only OS feature)
- FormaUI ships a default brand color scheme (light + dark) as the fallback — pick a palette that reads as "professional fintech-adjacent" (blues/teals with a clear accent), but keep this easily overridable
- Expose a `FormaTheme` composable wrapper, analogous to `MaterialTheme`, that layers FormaUI tokens on top of M3's `ColorScheme`

### 4.2 Typography scale

- Extend M3's `Typography` with FormaUI-specific text styles where useful (e.g., a `numeric` style variant with tabular figures for financial/data display — this matters given the fintech kit lineage)
- Document the full type scale in KDoc with visual examples referenced in the showcase app later

### 4.3 Spacing scale

- Define a consistent spacing token set (e.g., `FormaSpacing.xs/sm/md/lg/xl`, backed by dp values on a 4dp grid)
- All components in `:components` MUST use these tokens internally — no hardcoded dp values in component internals

### 4.4 Shape tokens

- Define a shape scale (corner radius tiers: none/small/medium/large/full) consistent with Material You's shape system, exposed as `FormaShapes`

### 4.5 API surface

```kotlin
@Composable
fun FormaTheme(
    colorScheme: FormaColorScheme = FormaTheme.defaultColorScheme(),
    typography: FormaTypography = FormaTheme.defaultTypography(),
    shapes: FormaShapes = FormaTheme.defaultShapes(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
)
```

---

## 5. `:components` Module Spec

### 5.1 Component list (Phase 1 — build in this order)

Priority order matters: build in this sequence so early testers always have a usable subset, even if later components slip.

1. **Button** — variants: filled, outlined, text, elevated, tonal (mirror M3's button variants but with FormaUI's opinionated default styling)
2. **TextField** — variants: outlined, filled; states: default, error, disabled, focused; support for leading/trailing icons and helper/error text
3. **Card** — variants: elevated, outlined, filled; support for a header/content/footer slot API
4. **Chip** — variants: assist, filter, input, suggestion
5. **Badge** — numeric and dot variants
6. **Switch**
7. **Checkbox**
8. **RadioButton**
9. **Dialog** — standard alert dialog + a full-screen dialog variant
10. **BottomSheet** — modal bottom sheet wrapper with FormaUI styling
11. **NavigationBar** (bottom nav) — with badge support per item
12. **ListItem** — single-line, two-line, three-line variants with leading/trailing slots
13. **Avatar** — image, initials-fallback, and icon variants
14. **Divider**
15. **LoadingIndicator** — circular + linear variants
16. **EmptyState** — icon/illustration slot + title + description + optional action button
17. **SnackBar** — standard + action-button variant
18. **Slider**

### 5.2 API design philosophy (apply to every component)

- **Slot-based APIs** where composability matters (e.g., `Card { header = {...}; content = {...} }` pattern or trailing-lambda content slots) — avoid rigid data-class-only APIs that block customization
- **Sensible defaults, full override capability** — every component must work with zero configuration beyond required params, but expose `modifier`, `colors`, `shape`, and `textStyle`-equivalent overrides where applicable
- **State hoisting** — components are stateless where possible; state (e.g., `TextFieldState`, `checked: Boolean`) is hoisted to the caller, following standard Compose conventions
- **No breaking Material 3 conventions** — FormaUI components should feel like "M3 components with better defaults," not an unfamiliar parallel API. Anyone who knows Compose Material 3 should be productive in FormaUI within minutes.
- **Accessibility is not optional** — every interactive component needs proper `contentDescription`, minimum touch target sizing (48dp), and correct semantics roles. This is a real differentiator to call out in docs.

### 5.3 Per-component deliverable checklist

For each of the 18 components, "done" means:

- [ ] Composable implemented with full variant/state coverage per spec above
- [ ] KDoc on the public composable function and all public parameters
- [ ] At least one `@Preview` composable showing all variants/states
- [ ] Compose UI test: renders without crashing + responds correctly to at least one state change
- [ ] Doc page + live embedded preview added to `formaui-site` (per Section 7.1) same day the component is finished

---

## 6. Non-Functional Requirements

- **Package size:** keep `:core` and `:components` lean — no heavy third-party dependencies. If a component needs something non-trivial (e.g., a charting library), that belongs in a kit module or `:components-pro`, not the free core.
- **API stability:** Phase 1 is pre-1.0 — mark all public APIs with `@ExperimentalFormaUiApi` opt-in annotations until the API surface is proven, so breaking changes pre-1.0 don't count as semver violations
- **Versioning:** Semantic versioning starting at `0.1.0`, targeting `1.0.0` once the full Phase 1 component set is stable and has real external usage
- **License:** Apache 2.0 for `:core` and `:components` (permissive, matches expectations for a free adoption-driving library)
- **Minimum Android Studio / AGP compatibility:** target whatever is current stable at build time; do not chase bleeding-edge alpha tooling

---

## 7. Documentation & Showcase Site (built in parallel with components, not after)

Docs are a first-class Phase 1 deliverable. The goal is a live site at formaui.dev from day one with genuinely interactive component previews — closer to Composables.com's experience — using a Next.js shell for the site itself, with live Wasm-compiled previews of the real components embedded in it (see 7.2 for how the hybrid pipeline works).

### 7.1 Per-component doc content

Each component needs a doc page containing:

- One-sentence description of what it's for
- Code sample (minimal, copy-pasteable, with a "copy" button on the site)
- Full parameter table
- **Live interactive preview** — the real component, compiled to Wasm via the `commonMain`/`wasmJs` target (Section 2), embedded in the page and genuinely clickable/toggleable, not a static screenshot
- Accessibility notes if non-default behavior applies

Write this content the same day the component itself is finished — do not batch documentation to the end.

### 7.2 Showcase site (hybrid: Next.js shell + embedded live Wasm previews)

- **Site shell:** Next.js + Tailwind CSS (`formaui-site`, separate repo) — handles routing, layout, landing page, doc content (MDX), matching your existing stack on other projects
- **Interactive previews:** each component in `:components` is compiled to a small Wasm bundle (via the `wasmJs` target) that renders just that component, embedded into its doc page as an iframe or custom element. This is the actual Compose component running in-browser — real state, real clicks — not a reimplementation
- **Build pipeline:** `formaui/formaui`'s CI produces per-component Wasm preview bundles as build artifacts; `formaui-site` fetches/embeds the latest bundles at build time (or via a simple static asset sync step) — keep this as simple as possible in Phase 1 (a manual or scripted copy step is fine; don't over-engineer automated cross-repo CI until the pattern is proven on a few components)
- **Hosting:** Vercel for the Next.js shell; Wasm preview bundles can be served as static assets from the same deployment
- **Content source:** each component's doc metadata (description, code sample, params) lives as a markdown/MDX file per component in `formaui-site`, referencing its corresponding embedded preview bundle by component ID

### 7.3 Sequencing within Phase 1

Recommended build order to avoid the showcase site blocking on a fully finished component library, and to de-risk the Wasm embedding pipeline early rather than discovering problems late:

1. Build Button first, and get its full pipeline working end-to-end — Compose Multiplatform component → Android target compiles/tests pass → Wasm target compiles → preview bundle embeds correctly in a bare-bones Next.js page. This is the highest-risk unknown in the whole plan (Wasm embedding of Compose Multiplatform components in Next.js is a less common pattern), so validate it on one component before committing to the approach across all 18.
2. Once the pipeline is proven, build TextField and Card next, standing up the real `formaui-site` skeleton (landing page + component index) alongside them.
3. Add each remaining component's doc page + embedded live preview to the site as soon as that component is "done" per the Section 5.3 checklist.
4. By the time all 18 components are complete, the site is complete too — not a separate push at the end.

**Fallback:** if the Wasm embedding pipeline proves unexpectedly difficult or slow during step 1, fall back to static rendered preview images (screenshots via Compose screenshot testing) for the remaining components rather than letting this block the whole library's progress — an imperfect but shipped Phase 1 beats a stalled one.

---

## 8. Acceptance Criteria for Phase 1 Completion

Phase 1 is done when:

1. `core` and `components` modules (in `formaui/formaui`) build cleanly with no warnings, on both the Android and Wasm targets
2. All 18 components in Section 5.1 meet the per-component checklist in Section 5.3
3. `formaui-site` (Next.js) is live at formaui.dev with a landing page, component index, and a complete doc page (per Section 7.1) for every one of the 18 components, each with a genuinely interactive embedded live preview
4. Light/dark theme and dynamic color on/off are demonstrated correctly within the live previews themselves
5. The Android artifacts (`core` and `components`) are successfully published to Maven Central (or at minimum, to Sonatype staging, ready for release)
6. README.md at `formaui/formaui` root explains: what FormaUI is, install instructions, the differentiation pitch (opinionated/Material You-native vs. headless alternatives), and a link to formaui.dev

---

## 9. Future Phases (context only — do not build yet)

- **Phase 2:** `:kit-fintech` — free kit, porting/generalizing components from the existing DevSnack CodeCanyon fintech UI kit (CardNumberField, OTPInput, PINPad, BalanceCard) as a top-of-funnel product feeding the paid CodeCanyon listing. Gets its own doc pages on the already-live showcase site.
- **Phase 3:** Licensing infrastructure — Paddle/Gumroad purchase flow + private repo access (manual initially) for gated content
- **Phase 4:** `:kit-ecommerce` — first paid kit, built only after Phases 1–2 show real adoption signal (GitHub stars, Maven downloads, inbound interest)

---

## 10. Notes for Claude Code

- Set up `build-logic` convention plugins early so Gradle config isn't duplicated across `core` and `components` — this pays off the moment more modules are added in later phases
- **De-risk the Wasm preview pipeline first, on Button alone, before touching any other component.** This is the single riskiest unknown in this PRD (per Section 7.3) — validate Compose Multiplatform → Android build/test → Wasm build → embed-in-Next.js end-to-end on one component before parallelizing. If it stalls, fall back to static screenshot previews (per the Section 7.3 fallback) rather than blocking all 18 components on it.
- Ask before making any decision that would be expensive to reverse later (package structure, public API shape on `FormaTheme`, min SDK, and especially whether to keep pursuing live Wasm previews vs. falling back to static images) — everything else, use best judgment and proceed
