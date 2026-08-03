---
name: Bug report
about: A FormaUI component renders wrong, crashes, or behaves unexpectedly
title: ''
labels: bug
assignees: ''
---

## What happened

<!-- What you saw. A screenshot or screen recording helps a lot for visual bugs. -->

## What you expected

## Reproduction

<!--
A minimal composable we can paste and run. Please include the FormaTheme wrapper and any state
the component depends on.
-->

```kotlin
@OptIn(ExperimentalFormaUiApi::class)
@Composable
fun Repro() {
    FormaTheme {
        // …
    }
}
```

## Environment

| | |
|---|---|
| FormaUI version | e.g. `0.2.0` |
| Target | Android / `wasmJs` |
| `minSdk` / device API level | e.g. minSdk 24, running on API 34 |
| Kotlin version | e.g. 2.4.10 |
| Compose | Compose Multiplatform 1.11.1 / AndroidX Compose + Material 3 `<version>` |

## Anything else

<!--
Useful if applicable:
- Does it reproduce in the :sample app?
- Does it happen with dynamicColor = true, false, or both?
- Light theme, dark theme, or both?
- Does the equivalent raw Material 3 component behave the same way? (Tells us whether the bug is
  in FormaUI's layer or upstream.)
-->
