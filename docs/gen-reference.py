#!/usr/bin/env python3
"""Generate docs/formaui-reference.md from docs/component-inventory.json.

A consumer-facing catalog for projects that depend on the published FormaUI
artifacts (e.g. the fintech app repo). Regenerate whenever the inventory changes.
"""
import json
import re
import sys
from pathlib import Path

repo = Path(sys.argv[1]) if len(sys.argv) > 1 else Path.cwd()
# Encoding is explicit everywhere: Python defaults to the locale codec, which is cp1252 on the
# Windows dev box and blows up on the UTF-8 punctuation in these files.
inv = json.loads((repo / "docs/component-inventory.json").read_text(encoding="utf-8"))

# Read the group and version from the root build script so the header can't drift from the
# artifacts. The group was hardcoded here once and silently went stale through a namespace move.
root_build = (repo / "build.gradle.kts").read_text(encoding="utf-8")

version_match = re.search(r'^version = "(.+)"', root_build, re.M)
if not version_match:
    sys.exit("could not read `version = \"…\"` from build.gradle.kts")
version = version_match.group(1)

group_match = re.search(r'^group = "(.+)"', root_build, re.M)
if not group_match:
    sys.exit("could not read `group = \"…\"` from build.gradle.kts")
GROUP = group_match.group(1)

# The retired namespace, kept only to explain the move. Nothing new ships here.
OLD_GROUP = "io.github.devsnackio"

out = []
w = out.append

w("# FormaUI Component Reference")
w("")
w("> Generated from `docs/component-inventory.json` in the forma-ui repo — regenerate there")
w("> when the library version is bumped. Do not edit by hand.")
w("")
w(f"**Artifacts** (Maven Central): `{GROUP}:core` and `{GROUP}:components`, version `{version}`.")
w("")
w("```kotlin")
w("dependencies {")
w(f"    implementation(\"{GROUP}:components:{version}\") // brings :core transitively")
w("}")
w("```")
w("")
w(f"> Released through `0.1.0` under the retired `{OLD_GROUP}` group; `{GROUP}` since `0.2.0`.")
w("> Old coordinates still resolve via relocation POMs, but get no further releases.")
w("")
w(f"Code packages are `dev.formaui.*`, matching the Maven group `{GROUP}` since `0.2.0`.")
w("Import from `dev.formaui.*`.")
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
dest.write_text("\n".join(out), encoding="utf-8")
print(f"wrote {dest} ({len(out)} lines, {dest.stat().st_size // 1024} KB)")
