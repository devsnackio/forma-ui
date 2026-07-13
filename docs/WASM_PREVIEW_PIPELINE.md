# Wasm live-preview pipeline — de-risk spike (Button)

**Status: ✅ VALIDATED.** A real FormaUI component compiled to `wasmJs` renders and is fully
interactive in the browser. This resolves the PRD §7.3/§10 "single riskiest unknown" and the §10
decision: **proceed with live Wasm previews — the static-screenshot fallback is not needed.**

Evidence: `FormaButton` (all five variants) rendered in headless Chromium via WebAssembly, with a
working light/dark theme toggle and click-to-increment state, zero console/page errors.

## The `:preview-wasm` module

A dedicated, **unpublished** harness module renders real components in the browser. It deliberately
does **not** apply `formaui.kmp.library` (no Android target/host tests) — just a `wasmJs` target with
`binaries.executable()`, depending on `:core` + `:components`. Entry point:
`ComposeViewport(document.body!!) { … }` (the current CMP entry point; `CanvasBasedWindow` is
deprecated). See `preview-wasm/build.gradle.kts` and `preview-wasm/src/wasmJsMain/`.

## Key finding: skip webpack; assemble + static-serve instead

The standard `wasmJsBrowser*Distribution` (webpack) path is a **non-starter in this environment**:
- `settings.gradle.kts` sets `RepositoriesMode.FAIL_ON_PROJECT_REPOS`, which rejects Kotlin adding
  the `https://nodejs.org/dist` repo → `:kotlinWasmNodeJsSetup` fails at configuration time.
- It would then need the Kotlin-managed Node + yarn + Binaryen downloads (blocked by the SSL proxy).

We don't need it. The **compile → assemble → static-serve** path works fully offline and maps
cleanly onto how the Next.js docs site would embed a preview (static assets, optionally in an iframe).

### Recipe (reproduced end-to-end in the spike)

1. **Compile + sync the executable (offline, no Node/binaryen):**
   ```
   export JAVA_HOME=…/jbr-17.0.14/Contents/Home
   ./gradlew :preview-wasm:wasmJsDevelopmentExecutableCompileSync :preview-wasm:unpackSkikoWasmRuntime --offline
   ```
2. **Assemble a serve directory** with:
   - the Kotlin output from
     `preview-wasm/build/compileSync/wasmJs/main/developmentExecutable/kotlin/`
     (`forma-ui-preview-wasm.mjs` + `.wasm` + `.import-object.mjs` + `.js-builtins.mjs` +
     `custom-formatters.js`),
   - the Skia runtime `skiko.mjs` + `skiko.wasm` from
     `preview-wasm/build/compose/skiko-for-web-runtime/`,
   - the Compose resources dir `preview-wasm/build/processedResources/wasmJs/main/composeResources/`
     — **required**: the theme's Public Sans fonts are fetched at runtime; without it you get
     `MissingResourceException: …/font/public_sans_medium.ttf`,
   - an `index.html` that loads `./forma-ui-preview-wasm.mjs` as a module.
3. **Provide the one npm dependency via an import map.** The emitted module has exactly one bare
   import: `@js-joda/core` (transitive of `kotlinx-datetime-wasm-js`). The **npm registry is
   reachable through the proxy** (only Maven is blocked), so `npm install @js-joda/core` works; map it
   in `index.html`:
   ```html
   <script type="importmap">
   { "imports": { "@js-joda/core": "./node_modules/@js-joda/core/dist/js-joda.esm.js" } }
   </script>
   ```
4. **Serve statically** (`python3 -m http.server`, or the docs site's `public/`). The wasm loads via
   `WebAssembly.instantiateStreaming(fetch('./forma-ui-preview-wasm.wasm'))`.

Verified headlessly with Playwright + the cached `chromium_headless_shell` (WebGL via
`--use-angle=swiftshader`).

## Notes / follow-ups for the docs site
- **Bundle size:** the *development* executable is unoptimized (~21 MB wasm + ~8.6 MB skiko). For
  production, use the optimized/production executable (needs Binaryen `wasm-opt`, a Kotlin-managed
  download — run it in unproxied CI) and/or lazy-load previews. Not a blocker for the spike.
- **One entry per component, or one parameterized entry** (select component via URL query) — either
  fits this recipe.
- **Browser requirement:** the wasm uses WasmGC + JS-string-builtins; needs a recent Chromium.
- Webpack/Node is only needed if we want its dev server or bundling; the import-map static approach
  avoids it entirely and is simpler to embed.
