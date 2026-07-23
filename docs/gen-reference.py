#!/usr/bin/env python3
"""Generate docs/formaui-reference.md from docs/component-inventory.json.

A consumer-facing catalog for projects that depend on the published FormaUI
artifacts (e.g. the fintech app repo). Regenerate whenever the inventory changes.
"""
import json
import sys
from pathlib import Path

repo = Path(sys.argv[1]) if len(sys.argv) > 1 else Path.cwd()
inv = json.loads((repo / "docs/component-inventory.json").read_text())

out = []
w = out.append

w("# FormaUI Component Reference")
w("")
w("> Generated from `docs/component-inventory.json` in the forma-ui repo — regenerate there")
w("> when the library version is bumped. Do not edit by hand.")
w("")
w("**Artifacts** (Maven Central): `io.github.devsnackio:core` and `io.github.devsnackio:components`.")
w("Code packages are `dev.formaui.*` — the group/package mismatch is intentional (the Maven group")
w("is namespace-verified as `io.github.devsnackio`); import from `dev.formaui.*`, never \"correct\" it.")
w("")
w("**Setup rules:**")
w("- Wrap every screen (or the app root) in `FormaTheme { ... }` (from `dev.formaui.core.theme`).")
w("  Dynamic color is opt-in (`dynamicColor = true`); default palette is FormaUI's warm-editorial brand.")
w("- Every public API requires `@OptIn(ExperimentalFormaUiApi::class)` (usually file-level).")
w("- Always prefer `Forma*` components over raw Material 3 equivalents.")
w("- min SDK 24; Compose Multiplatform (JetBrains) with Material 3.")
w("")
w(f"**{len(inv)} components.** Index:")
w("")
w("| Component | Package | Summary |")
w("|---|---|---|")
for e in inv:
    anchor = e["name"].lower().replace(" ", "-")
    desc = e["description"].split(" — ")[-1] if " — " in e["description"] else e["description"]
    desc = (desc[:110] + "…") if len(desc) > 112 else desc
    w(f"| [{e['name']}](#{anchor}) | `{e['package'].split('.')[-1]}` | {desc} |")
w("")
w("---")
w("")


def signature(comp):
    """Render a Kotlin-ish signature from the param list."""
    lines = [f"fun {comp['name']}("]
    for p in comp.get("params", []):
        default = p.get("default")
        if default is None:
            lines.append(f"    {p['name']}: {p['type']},")
        else:
            lines.append(f"    {p['name']}: {p['type']} = {default},")
    lines.append(")")
    return "\n".join(lines)


for e in inv:
    w(f"## {e['name']}")
    w("")
    w(f"`{e['package']}` · tier: {e['tier']}")
    w("")
    w(e["description"])
    w("")
    for comp in e.get("composables", []):
        if comp.get("summary"):
            w(f"**`{comp['name']}`** — {comp['summary']}")
            w("")
        w("```kotlin")
        w(signature(comp))
        w("```")
        w("")
        params = comp.get("params", [])
        noteworthy = [p for p in params if p.get("description")]
        if noteworthy:
            for p in noteworthy:
                w(f"- `{p['name']}` — {p['description']}")
            w("")
    sa = e.get("supportingApi") or []
    if sa:
        w("**Supporting API:**")
        for s in sa:
            w(f"- `{s['name']}` ({s['kind']}) — {s['summary']}")
        w("")
    variants = e.get("variants") or []
    if variants:
        w(f"**Variants:** {', '.join(variants)}")
        w("")
    if e.get("codeSample"):
        w("**Example:**")
        w("")
        w("```kotlin")
        w(e["codeSample"].rstrip())
        w("```")
        w("")
    if e.get("accessibility"):
        w(f"**Accessibility:** {e['accessibility']}")
        w("")
    w("---")
    w("")

dest = repo / "docs/formaui-reference.md"
dest.write_text("\n".join(out))
print(f"wrote {dest} ({len(out)} lines, {dest.stat().st_size // 1024} KB)")
