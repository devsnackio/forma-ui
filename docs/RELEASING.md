# Releasing a new FormaUI version

Quick per-version checklist. For the full background (one-time setup, why the upload is manual,
`io.github` vs `dev.formaui`), see [`PUBLISHING.md`](./PUBLISHING.md).

Current coordinates (temporary namespace until `formaui.dev` is owned):

- `io.github.devsnackio:core:<version>`
- `io.github.devsnackio:components:<version>` (depends transitively on `core`)

> The Kotlin package is always `dev.formaui.*` regardless of the Maven coordinate.

---

## Already set up once (do NOT repeat each release)

These persist on this machine / account — skip them:

- **GPG key** `EDA3EDC9AD612D91` generated **and published** to `keyserver.ubuntu.com`.
- **Signing credentials** in `~/.gradle/gradle.properties` (`signingInMemoryKey` as a **single line**
  with `\n` escapes + `signingInMemoryKeyPassword`). Back up the **private key + passphrase** offline.
- **`local.properties`** in the repo root with `sdk.dir=C:/Users/User/AppData/Local/Android/Sdk`
  (gitignored, machine-local).
- **Namespace** `io.github.devsnackio` verified in the Central Portal.
- **Group ID + POM** wired to `io.github.devsnackio` / `github.com/devsnackio/forma-ui`.

Toolchain: `JAVA_HOME` points at Android Studio's bundled **JBR 21**
(`C:\Users\User\AppData\Local\Programs\Android Studio\jbr`). All commands below run from the repo root.

---

## Per-version steps

Example below releases `0.2.0` — substitute your version.

### 1. Bump the version (3 places — keep in sync)

This duplication is the #1 release mistake. Update all three:

| File | Field |
|------|-------|
| `build.gradle.kts` (root) | `version = "0.2.0"` |
| `build-logic/src/main/kotlin/formaui.publishing.gradle.kts` | `version = "0.2.0"` — **authoritative for the published artifact** |
| `sample/build.gradle.kts` | `versionName` (cosmetic; keep aligned) |

### 2. Validate locally (dry run — catch problems before the irreversible upload)

```powershell
.\gradlew.bat publishToMavenLocal --console=plain
```

Ideally also run the full gates: unit tests + `wasmJs` compile + `sample` assemble.

### 3. Build the signed bundle (clear the old one first)

```powershell
Remove-Item -Recurse -Force build\central-bundle -ErrorAction SilentlyContinue
.\gradlew.bat publishAllPublicationsToCentralBundleRepository --console=plain
```

Produces a signed Maven-repo tree under `build/central-bundle/` (both modules, `.asc` signatures + checksums).

### 4. Zip with `io/` at the root

Git Bash here has no `zip`, so use the JDK's `jar` (produces clean forward-slash entries). Run in **Git Bash**:

```bash
cd build/central-bundle && "$JAVA_HOME/bin/jar" -cMf ../formaui-0.2.0-bundle.zip io
```

> A wrong zip root is the most common upload rejection. Verify the first entries are
> `io/github/devsnackio/...`:
> ```bash
> "$JAVA_HOME/bin/jar" -tf build/formaui-0.2.0-bundle.zip | head
> ```

### 5. Upload

1. Go to [central.sonatype.com](https://central.sonatype.com) → **Publish Component**, authenticate in the browser.
2. Upload `build/formaui-0.2.0-bundle.zip`.
3. Validation runs (signatures, sources/javadoc, POM completeness). The namespace is already verified,
   so it should pass. If it fails, **Drop**, fix, re-upload.
4. Status flows: `VALIDATED → PUBLISHING → PUBLISHED`. Once it reaches **PUBLISHED**, it's out of your hands.

### 6. Post-release

```bash
git tag v0.2.0 && git push origin v0.2.0
```

---

## Verify it went live

Propagation to `repo1.maven.org` takes ~15 min to a few hours after **PUBLISHED**
(the search index lags longer). Check with:

```bash
for a in components core; do
  curl -sS -o /dev/null -w "%{http_code}  $a\n" \
    "https://repo1.maven.org/maven2/io/github/devsnackio/$a/0.2.0/$a-0.2.0.pom"
done
# 200 = live, 404 = still propagating
```

Consumer sanity check — in a throwaway project with `mavenCentral()`:

```kotlin
dependencies { implementation("io.github.devsnackio:components:0.2.0") }
```

---

## The one rule that bites

**Published versions are immutable.** Once `0.2.0` is on Central it can *never* be re-uploaded —
found a bug? Ship `0.2.1`. Always run step 2 before step 5.

---

## Later: migrating to `dev.formaui`

When `formaui.dev` is owned and the `dev.formaui` namespace is verified:

1. Change the group back to `dev.formaui` in the two build files (and point POM `url` at `https://formaui.dev`).
2. Publish a one-time **relocation POM** under `io.github.devsnackio` so existing consumers are redirected
   (see [`PUBLISHING.md`](./PUBLISHING.md) → "Migrating to `dev.formaui` later").
