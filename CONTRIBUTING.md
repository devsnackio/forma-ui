# Contributing to FormaUI

Thanks for helping out. This repo has a handful of non-obvious conventions that will bite you if you
follow standard Compose-library instincts — they are all listed below. Read the **Gotchas** section
before your first change; it is short and it is the part that actually matters.

## What FormaUI is (and what belongs here)

FormaUI is a **themed layer on top of Material 3**, not a design system built from scratch. The
positioning is "M3 with better defaults": components should look distinctly like FormaUI while
staying recognizable to anyone already productive in Compose Material 3. Don't break M3 conventions.

`:core` and `:components` are deliberately **lean** — no heavy third-party dependencies. Anything
requiring platform APIs (camera, file pickers, maps) or a large dependency does not belong here.

## Build and test

Every `./gradlew` invocation needs the JetBrains Runtime on `JAVA_HOME` first. **Bare `./gradlew`
fails** — this is the single most common setup mistake:

```bash
export JAVA_HOME=~/Library/Java/JavaVirtualMachines/jbr-17.0.14/Contents/Home
```

There is no standalone `gradle` or `kotlin` CLI in this project — use the wrapper.

These three commands together are the **authoritative QA gate**. All three must pass before a PR
is mergeable; CI runs exactly this set on every pull request:

```bash
./gradlew :core:testAndroidHostTest :components:testAndroidHostTest   # unit gate (Robolectric)
./gradlew :core:compileKotlinWasmJs :components:compileKotlinWasmJs   # wasm must compile
./gradlew :sample:assembleDebug                                       # sample app builds
```

Toolchain, all pinned in `gradle/libs.versions.toml`: Gradle 9.6 · Kotlin 2.4.10 · Compose
Multiplatform 1.11.1 · AGP 9.3.1 · Material 3 1.9.0 (version-decoupled as `composeMaterial3`).
`minSdk 24`, compile/target SDK 37.

## Module layout

```
core/          # Theming engine: FormaTheme + color/typography/spacing/shape tokens. ZERO FormaUI deps.
components/    # The 40 components. Depends on :core.
sample/        # Android showcase app (not published).
preview-wasm/  # Wasm live-preview harness for the docs site (not published).
build-logic/   # Gradle convention plugins.
```

No circular dependencies, ever.

## Gotchas

These are the things that are easy to get wrong:

- **Tests live in `src/androidHostTest`, not `commonTest`.** Compose UI tests run on Robolectric and
  need `@Config(sdk = [34])`. The `wasmJs` target carries no tests — it only has to compile.
- **JUnit asserts only.** `kotlin.test` is not a dependency. Reference suite:
  `components/src/androidHostTest/kotlin/dev/formaui/components/button/FormaButtonTest.kt`.
- **Shared code goes in `commonMain`, and must stay platform-free.** One `android.content.Context`
  import and the `wasmJs` target stops compiling. Dynamic color is Android-only — it falls back to
  the static brand palette elsewhere.
- **No hardcoded `dp` in component internals.** Use `FormaSpacing` tokens (a 4dp grid).
- **`:core` and `:components` use AGP 9's `com.android.kotlin.multiplatform.library` plugin**, not
  `com.android.library`. Shared config lives in
  `build-logic/src/main/kotlin/formaui.kmp.library.gradle.kts`.
- **`:sample` uses AGP 9's built-in Kotlin support** — do not apply `kotlin.android` to it.
- **`docs/formaui-reference.md` is generated. Never hand-edit it.** Edit
  `docs/component-inventory.json`, then regenerate:
  ```bash
  python3 docs/gen-reference.py .
  ```
  The generator reads the version straight out of `build.gradle.kts`, so the header cannot drift.
- **`settings.gradle.kts` contains a content-scoped `mavenLocal()` workaround** for
  `ui-tooling-preview` behind an SSL-inspecting proxy. Don't remove it until the proxy CA is trusted.

## Adding or changing a component

Every public API is annotated `@ExperimentalFormaUiApi` and **requires KDoc** — this is a public
library. State should be hoisted (stateless where possible) and APIs slot-based. Accessibility is
not optional: 48dp minimum touch targets, `contentDescription` on icon-only content, correct
semantics roles.

A new component is **not done** until all of the following exist:

1. Implementation in `components/src/commonMain/…` with full variant and state coverage.
2. KDoc on every public declaration, including `@param` for each parameter.
3. A `@Preview` covering all variants (conventionally a sibling `*Previews.kt`).
4. A Robolectric UI test in `components/src/androidHostTest/…` that renders the component **and**
   asserts on at least one state change.
5. An entry in `docs/component-inventory.json`, and the regenerated `docs/formaui-reference.md`.
6. Registration in `preview-wasm/src/wasmJsMain/kotlin/dev/formaui/preview/PreviewRegistry.kt`,
   keyed by the same `id` used in the inventory.
7. A section in the `:sample` showcase app.

Every `Forma*` wrapper should forward the underlying M3 `colors` and `textStyle` parameters so
callers can customize without dropping to raw Material 3.

`docs/BACKLOG.md` tracks the Material 3 surface not yet wrapped — a good place to find work.

## Pull requests

- Branch off `main`; keep PRs focused on one concern.
- Run the full three-command gate locally before pushing.
- Update `CHANGELOG.md` under the unreleased heading for anything user-visible.
- The PR template mirrors the Definition of Done above — fill it in honestly; an unchecked box with
  a reason is far more useful than a checked one that isn't true.

## Reporting bugs and requesting components

Use the issue templates. For bugs, the FormaUI version, Compose/Kotlin versions, and target
(Android or `wasmJs`) are what make a report actionable.

## License

By contributing you agree that your contributions are licensed under the
[Apache License 2.0](LICENSE), the same license as the project.
