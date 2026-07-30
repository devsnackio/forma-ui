---
name: Feature or component request
about: Request a new component, variant, or customization parameter
title: ''
labels: enhancement
assignees: ''
---

## What you need

<!-- The component, variant, or parameter, and the UI you're trying to build with it. -->

## What you're doing today

<!--
How you work around its absence — dropping to raw Material 3, hand-rolling it, forking a Forma*
component. This is the strongest signal for prioritization.
-->

## Proposed API

<!-- Optional, but very welcome. A sketch of the signature you'd want to call. -->

```kotlin
```

## Checks

- [ ] I checked [`docs/BACKLOG.md`](../../docs/BACKLOG.md) — it tracks the Material 3 surface that
      is deliberately deferred or blocked on a `composeMaterial3` bump.
- [ ] I checked [`docs/formaui-reference.md`](../../docs/formaui-reference.md) — the full catalog of
      what already ships, including customization parameters that may already cover this.
- [ ] This fits a lean, dependency-free component library. Anything needing a heavy third-party
      dependency or platform APIs (camera, file access, maps) is out of scope for `:core` and
      `:components`.

## Material 3 reference

<!-- If Material 3 specifies this component, link the spec — FormaUI stays M3-native by default. -->
