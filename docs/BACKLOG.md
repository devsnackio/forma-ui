# FormaUI — Component Backlog

Tracks the remaining Material 3 surface not yet wrapped by FormaUI, so the "close the core M3 gap"
work stays visible. As of this writing the library ships **40 components** (18 PRD + 22 extra — see
`docs/component-inventory.json`). Toolchain baseline: CMP 1.11.1, `composeMaterial3` 1.9.0.

## Available in CMP / material3 1.9.0 — deferred by choice

These components exist in the pinned `composeMaterial3` 1.9.0 and could be wrapped today; they were
consciously deferred, not blocked. Revisit when prioritizing the next batch.

- **Scaffold** (`Scaffold`) — structural screen skeleton (top bar / bottom bar / FAB / content).
  Open question: does a structural layout primitive belong in an opinionated *component* library, or
  is it better left to the app? Decide the scope before wrapping.
- **Wide navigation rail** (`WideNavigationRail`, `ModalWideNavigationRail`) — the expanded
  medium/large-window rail; complements the existing `FormaNavigationRail`.
- **Short navigation bar** (`ShortNavigationBar`) — the compact bottom bar variant; complements the
  existing `FormaNavigationBar`.
- **Navigation drawer variants** — the current drawer wraps only the modal drawer. Add the
  `Dismissible` and `Permanent` drawer variants to round out the family.

## Blocked on a `composeMaterial3` version bump (not in 1.9.0)

Only the `tokens/*Tokens.kt` for these were ported into 1.9.0 — the actual composables are not yet
present in the pinned material3. **Revisit when `composeMaterial3` is bumped past 1.9.0.**

- **Button group**
- **Split button**
- **FAB menu**
- **Loading indicator** (the new shape-morphing M3 expressive indicator — distinct from the current
  `FormaLoadingIndicator`, which wraps the classic circular/linear progress indicators)
- **Floating toolbars**
- **Toggle button**

## Scope question to resolve with the user

- **Charts** — `FormaBarChart`, `FormaDonutChart`, and `FormaLineChart` currently ship in the free
  `:components` module, but the PRD says charts belong in a **paid vertical kit**. This is a
  positioning/packaging decision, not a code cleanup — **flag for the user to decide**; do not move
  anything until that ruling lands.
