<!--
Thanks for the PR. Fill in what applies and delete what doesn't.
An unchecked box with a one-line reason is far more useful than a checked one that isn't true.
-->

## What this changes

<!-- One or two sentences. Link the issue it closes, if any. -->

## QA gate

All three must pass — CI runs exactly this set. Run them locally first
(`export JAVA_HOME=<JBR>` before `./gradlew`, or the wrapper fails):

- [ ] `./gradlew :core:testAndroidHostTest :components:testAndroidHostTest`
- [ ] `./gradlew :core:compileKotlinWasmJs :components:compileKotlinWasmJs`
- [ ] `./gradlew :sample:assembleDebug`

## Conventions

- [ ] KDoc on every public declaration, `@param` on every parameter
- [ ] New public APIs annotated `@ExperimentalFormaUiApi`
- [ ] No hardcoded `dp` — `FormaSpacing` tokens only
- [ ] Nothing platform-specific in `commonMain` (it would break the `wasmJs` target)
- [ ] Accessibility: 48dp touch targets, `contentDescription` on icon-only content, correct
      semantics roles
- [ ] Screenshots or a recording for anything visual (light **and** dark)

## For a new or changed component

Delete this section for docs-only or infrastructure changes.

- [ ] Full variant and state coverage
- [ ] `@Preview` covering all variants
- [ ] Robolectric test in `src/androidHostTest` (`@Config(sdk = [34])`, JUnit asserts) that renders
      **and** asserts on at least one state change
- [ ] `docs/component-inventory.json` updated, and `docs/formaui-reference.md` regenerated with
      `python3 docs/gen-reference.py .` — never hand-edited
- [ ] Registered in `preview-wasm/…/PreviewRegistry.kt` under the same `id`
- [ ] Added to the `:sample` showcase
- [ ] Underlying M3 `colors` / `textStyle` parameters forwarded
- [ ] `README.md` component table and count updated

## Anything user-visible

- [ ] `CHANGELOG.md` updated under the unreleased heading
- [ ] Breaking change? Say so explicitly here, and what callers must do:
