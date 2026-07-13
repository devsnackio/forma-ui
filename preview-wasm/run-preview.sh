#!/usr/bin/env bash
#
# Run the FormaUI :preview-wasm live preview locally, then open http://127.0.0.1:8791 in a browser.
#
# Why not `./gradlew :preview-wasm:wasmJsBrowserDevelopmentRun`? That webpack/Node dev-server path is
# blocked in this environment (settings.gradle.kts uses FAIL_ON_PROJECT_REPOS, which rejects Kotlin's
# nodejs.org repo, and the Node/binaryen downloads are proxy-blocked). This script uses the offline
# compile -> assemble -> static-serve path instead. See docs/WASM_PREVIEW_PIPELINE.md.
#
# Usage:   ./preview-wasm/run-preview.sh          (serves on port 8791)
#          PORT=9000 ./preview-wasm/run-preview.sh
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

# Gradle needs the JetBrains Runtime 17. Honor an already-valid JAVA_HOME; otherwise use the JBR.
JBR="/Users/ly.sokchanbo/Library/Java/JavaVirtualMachines/jbr-17.0.14/Contents/Home"
if [ ! -x "${JAVA_HOME:-}/bin/java" ]; then export JAVA_HOME="$JBR"; fi
echo "==> JAVA_HOME=$JAVA_HOME"

echo "==> Compiling wasm executable + unpacking the Skia runtime (offline, no Node)…"
./gradlew :preview-wasm:wasmJsDevelopmentExecutableCompileSync :preview-wasm:unpackSkikoWasmRuntime --offline

BUILD="$ROOT/preview-wasm/build"
KOTLIN="$BUILD/compileSync/wasmJs/main/developmentExecutable/kotlin"
SKIKO="$BUILD/compose/skiko-for-web-runtime"
RES="$BUILD/processedResources/wasmJs/main/composeResources"
SERVE="$BUILD/preview-serve"

echo "==> Assembling serve dir: $SERVE"
rm -rf "$SERVE"; mkdir -p "$SERVE"
cp "$KOTLIN"/forma-ui-preview-wasm.mjs \
   "$KOTLIN"/forma-ui-preview-wasm.wasm \
   "$KOTLIN"/forma-ui-preview-wasm.import-object.mjs \
   "$KOTLIN"/forma-ui-preview-wasm.js-builtins.mjs \
   "$KOTLIN"/custom-formatters.js "$SERVE"/
cp "$SKIKO"/skiko.mjs "$SKIKO"/skiko.wasm "$SERVE"/
cp -R "$RES" "$SERVE"/composeResources          # Public Sans fonts — required at runtime
cp "$ROOT/preview-wasm/src/wasmJsMain/resources/index.html" "$SERVE"/index.html

echo "==> Fetching @js-joda/core (the one npm module the wasm imports; registry is reachable)…"
( cd "$SERVE" && npm install @js-joda/core@6.1.0 --no-audit --no-fund --loglevel=error )

PORT="${PORT:-8791}"
echo ""
echo "==> Ready. Open:  http://127.0.0.1:$PORT   (needs a recent Chrome/Edge — WasmGC)"
echo "    Press Ctrl-C to stop the server."
cd "$SERVE" && exec python3 -m http.server "$PORT" --bind 127.0.0.1
