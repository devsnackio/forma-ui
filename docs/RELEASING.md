# Releasing FormaUI to Maven Central

The single publishing doc: one-time setup, the per-version runbook, and the background on why the
pipeline is shaped this way.

Two artifacts ship, under the `io.github.devsnackio` group:

| Artifact | Coordinates | Depends on |
|----------|-------------|------------|
| Theming core | `io.github.devsnackio:core:<version>` | — |
| Components | `io.github.devsnackio:components:<version>` | `core` (transitive) |

Current version: **`0.1.0-beta04`**.

> The Kotlin package is always `dev.formaui.*` regardless of the Maven coordinate — see
> [Group ID vs. Kotlin package](#group-id-vs-kotlin-package).

> **Nothing has shipped yet.** There are no git tags and no GitHub releases; Central returns no
> results for the group. `0.1.0-beta04` will be the first.

Two routes reach the same outcome:

- **[Route A — via CI](#route-a--release-via-ci-preferred)** — dispatch `release.yml`. Preferred; no
  local signing setup needed.
- **[Route B — from a local machine](#route-b--release-from-a-local-machine-fallback)** — build the
  bundle yourself and upload it in the browser. Always available, and the fallback when Route A's
  API call fails.

---

## How publishing works here

Legacy OSSRH (`s01.oss.sonatype.org`) is decommissioned, and the Sonatype Central Portal does not
accept a direct Gradle upload from plain `maven-publish`. This repo deliberately uses only Gradle's
built-in `maven-publish` + `signing` plugins — **no external publishing plugin** — so the build keeps
working behind SSL-inspecting proxies that block fresh plugin-portal downloads. The trade-off:
releasing means producing a **signed bundle** and handing it to the Portal, rather than
`./gradlew publish` to a remote.

> Don't "fix" this by adding a publishing plugin. The proxy constraint is the reason it looks
> unusual.

That handoff is automated by [`release.yml`](../.github/workflows/release.yml) (Route A), which
POSTs the bundle to the Central Publisher API as `USER_MANAGED` — so the only manual act left is
reviewing the validated file list and pressing **Publish**.

The publishing logic lives in the `formaui.publishing` convention plugin
([`build-logic/src/main/kotlin/formaui.publishing.gradle.kts`](../build-logic/src/main/kotlin/formaui.publishing.gradle.kts)),
applied to `:core` and `:components`. It configures the full POM, a javadoc jar (KMP already emits
sources jars), PGP signing, and a shared local `central-bundle` repository. The `wasmJs` publication
is deliberately excluded.

---

## One-time setup (do NOT repeat each release)

### Account-level (shared by every route)

- **Namespace** `io.github.devsnackio` verified in the Central Portal. `io.github.<username>`
  namespaces are verified by GitHub ownership: the portal gives you a code, you create a temporary
  **public repo** named with that code, then click *Verify*. No domain, no DNS, no cost.
- **Group ID + POM** wired to `io.github.devsnackio` / `github.com/devsnackio/forma-ui`, in two
  places: `build.gradle.kts` (root) and
  `build-logic/src/main/kotlin/formaui.publishing.gradle.kts`.
  ⚠ The POM's `url`/`scm` must **resolve** — confirm the repository actually lives at
  `github.com/devsnackio/forma-ui` before the first upload. A dead SCM URL in a published POM is
  permanent.
- **GPG key** `EDA3EDC9AD612D91` generated **and published** to `keyserver.ubuntu.com`. Back up the
  **private key + passphrase** offline.

Generating one from scratch, if ever needed — Maven Central requires every artifact to be PGP-signed:

```bash
# Generate a key (RSA 4096, no expiry or a long one). Note the KEY_ID it prints.
gpg --full-generate-key

# Publish the PUBLIC key so Central can verify signatures.
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>

# Export the ASCII-armored PRIVATE key that Gradle will sign with.
gpg --armor --export-secret-keys <KEY_ID>
```

### For Route A — four GitHub repository secrets

`release.yml` will not run without all four (Settings → Secrets and variables → Actions):

| Secret | What it is |
|---|---|
| `SIGNING_KEY` | the ASCII-armored PGP **private** key, multiline, pasted as-is |
| `SIGNING_PASSWORD` | its passphrase |
| `CENTRAL_TOKEN_USERNAME` | Central Portal user-token **name** |
| `CENTRAL_TOKEN_PASSWORD` | Central Portal user-token **password** |

> The user token is **not** your portal login. Generate it at
> [central.sonatype.com](https://central.sonatype.com) → your account → **Generate User Token**; it
> is shown once. This pair is required only by the CI route and is easy to miss — earlier revisions
> of this runbook never mentioned it.

### For Route B — release machine only

These live on the **release machine**, never in the repo, and do not follow you between computers.
Put the key in your **machine-global** `~/.gradle/gradle.properties` (not this repo's committed
`gradle.properties`) — see [`gradle.properties.template`](../gradle.properties.template):

```properties
# ~/.gradle/gradle.properties
# The armored private key on a SINGLE line, with real newlines escaped as \n.
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n...\n-----END PGP PRIVATE KEY BLOCK-----
signingInMemoryKeyPassword=<passphrase>
```

Equivalent env vars: `SIGNING_KEY` and `SIGNING_PASSWORD`. No Central Portal credentials are needed
at the Gradle level for a manual upload — you authenticate in the browser at upload time.

Also required locally: **`local.properties`** in the repo root with `sdk.dir=…` (gitignored), and
`JAVA_HOME` on a JetBrains Runtime — JBR 21 from Android Studio on the Windows box
(`C:\Users\User\AppData\Local\Programs\Android Studio\jbr`), JBR 17 on macOS
(`~/Library/Java/JavaVirtualMachines/jbr-17.0.14/Contents/Home`).

> **Releasing from a second machine?** The signing key and `~/.gradle/gradle.properties` exist only
> where they were created. Either import the private key and recreate that file, or skip Route B
> entirely and use **Route A** — CI holds the key as a secret, so no local setup is needed.

All commands below run from the repo root.

---

## Step 1 — Bump the version (both routes)

The version is hard-coded in **three** places. Keeping them in sync is the #1 release mistake, and
Route A's guard will reject a mismatch:

| File | Field |
|------|-------|
| `build.gradle.kts` (root) | `version = "0.2.0"` |
| `build-logic/src/main/kotlin/formaui.publishing.gradle.kts` | `version = "0.2.0"` — **authoritative for the published artifact** |
| `sample/build.gradle.kts` | `versionName` (cosmetic; keep aligned) |

Before going further:

- [ ] **Green build gate** — unit tests + `wasmJs` compile + `sample` assemble all pass.
- [ ] **README accuracy.** The artifact is about to be public. Check the badges still match
      [`gradle/libs.versions.toml`](../gradle/libs.versions.toml), the component count still matches
      `docs/component-inventory.json`, and — once the first version actually lands on Central — that
      the "not yet published" caveat is **removed**.
- [ ] **`CHANGELOG.md`** has an entry for this version.

Examples below release `0.2.0` — substitute your version.

---

## Route A — release via CI (preferred)

Trigger [`release.yml`](../.github/workflows/release.yml) from the Actions tab → **Release to Maven
Central** → *Run workflow*, with the `version` input (e.g. `0.1.0-beta04`).

It runs a **version guard** (the input must equal the version in both build files — a typo aborts
before anything irreversible), the full test gate, the signed bundle build (it refuses to proceed
unsigned), the zip-root assertion, then uploads the bundle **as a workflow artifact** and POSTs it
to the Central Publisher API as `USER_MANAGED`, polling status for up to 10 minutes.

It deliberately **does not tag and does not press Publish** — both stay with you; continue at
[Post-release](#post-release).

> ⚠ **This workflow has never been run.** Its header comment flags that the Publisher API
> endpoint/auth shape should be re-verified against
> <https://central.sonatype.org/publish/publish-portal-api/> before the first release. If the API
> step fails, download the `central-bundle-<version>` workflow artifact and continue from
> [step B4](#b4-upload) — the manual path always remains available.

---

## Route B — release from a local machine (fallback)

### B1. Validate locally (dry run — catch problems before the irreversible upload)

No signing key needed for this:

```bash
./gradlew publishToMavenLocal --console=plain
```

Confirm the artifacts landed under `~/.m2/repository/io/github/devsnackio/{core,components}/<version>/`.
Each should contain the main `.aar`/`.jar` (per target), a `-sources.jar`, a `-javadoc.jar`, and a
`.pom`. Also run the full gates: unit tests + `wasmJs` compile + `sample` assemble.

### B2. Build the signed bundle (clear the old one first)

```bash
rm -rf build/central-bundle
./gradlew publishAllPublicationsToCentralBundleRepository --console=plain
```

<details><summary>PowerShell equivalent</summary>

```powershell
Remove-Item -Recurse -Force build\central-bundle -ErrorAction SilentlyContinue
.\gradlew.bat publishAllPublicationsToCentralBundleRepository --console=plain
```
</details>

Produces a signed Maven-repo tree under `build/central-bundle/` — both modules, `.asc` signatures
and checksums, in one shared directory so `core` and `components` ship as a single deployment.

> **Delete any `maven-metadata.xml` before zipping.** Gradle sometimes emits it into the repo tree,
> and the Central Portal rejects bundles that contain it:
> ```bash
> find build/central-bundle -name 'maven-metadata*' -delete
> ```

### B3. Zip with `io/` at the root

Zip the *contents* of `build/central-bundle/` so `io/github/devsnackio/...` sits at the zip root —
**not** the `central-bundle/` folder itself. Git Bash has no `zip`, so use the JDK's `jar` (it
produces clean forward-slash entries):

```bash
cd build/central-bundle && "$JAVA_HOME/bin/jar" -cMf ../formaui-0.2.0-bundle.zip io
```

> A wrong zip root is the most common upload rejection. Verify the first entries:
> ```bash
> "$JAVA_HOME/bin/jar" -tf build/formaui-0.2.0-bundle.zip | head
> ```

### B4. Upload

1. Go to [central.sonatype.com](https://central.sonatype.com) → **Publish Component**, authenticate
   in the browser.
2. Upload `build/formaui-0.2.0-bundle.zip`.
3. Validation runs — PGP signatures resolve against your published public key, sources and javadoc
   jars are present, and the POM is complete (name, description, url, license, developer, SCM — all
   supplied by the convention plugin). The namespace is already verified, so it should pass. If it
   fails, **Drop** the deployment, fix, and re-upload.
4. Status flows `VALIDATED → PUBLISHING → PUBLISHED`. Once it reaches **PUBLISHED**, it's out of
   your hands.

---

## Post-release

### Tag it (this also ships the docs-site previews)

```bash
git tag v0.2.0 && git push origin v0.2.0
```

Pushing a `v*` tag fires [`previews.yml`](../.github/workflows/previews.yml), which builds the wasm
preview bundle, creates the GitHub release if absent, and uploads `previews-0.2.0.tar.gz` — the
asset the `formaui-site` Vercel build downloads. Tag last and this happens for free.

> **Ordering gotcha.** `previews.yml` also has a `workflow_dispatch` trigger that needs no secrets,
> so it can be run *before* a Maven publish to unblock the docs site. But dispatching it **creates
> the tag itself** — and a tag that already exists will not fire the push trigger again. If you take
> that shortcut, re-dispatch `previews.yml` manually after the publish whenever the bundle needs
> rebuilding.

### Verify it went live

Propagation to `repo1.maven.org` takes ~15 min to a few hours after **PUBLISHED** (the search index
lags longer):

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

```kotlin
@OptIn(ExperimentalFormaUiApi::class)
@Composable
fun Demo() {
    FormaTheme {
        FormaButton(onClick = {}) { Text("It works") }
    }
}
```

---

## The one rule that bites

**Published versions are immutable.** Once `0.2.0` is on Central it can *never* be re-uploaded —
found a bug? Ship `0.2.1`. Always dry-run before you upload.

---

## Appendix

### Group ID vs. Kotlin package

The Maven **group ID** is only the coordinate string consumers write in Gradle. It is independent of
the **Kotlin package** (`dev.formaui.*`), which is compiled into the source and never changes. So
`import dev.formaui.components.button.FormaButton` is identical whether the artifact is
`io.github.devsnackio:components` or `dev.formaui:components`.

The split is deliberate: `io.github.devsnackio` is verified via GitHub account ownership (no domain,
no cost), while `dev.formaui` would require owning `formaui.dev`.

### Migrating to `dev.formaui` later — the caveats

Registering the custom-domain namespace `dev.formaui` requires proving control of `formaui.dev`: the
portal issues a verification key that you publish as a **DNS TXT record**, then click *Verify*.
Uploads are rejected until it shows **Verified**.

Published coordinates are **permanent and immutable**, so switching group is not a rename — it mints
a new artifact:

- `io.github.devsnackio:components:0.1.0` exists forever.
- `dev.formaui:components:0.2.0` is a *different* artifact. When consumers upgrade they must edit
  their dependency line from the old group to the new one — it is not a transparent version bump.

The migration itself:

1. Change the group in the two build files, and point the POM `url` at `https://formaui.dev`.
2. Publish a one-time **relocation POM** under the old coordinates so tools redirect:

```xml
<distributionManagement>
  <relocation>
    <groupId>dev.formaui</groupId>
    <artifactId>components</artifactId>
    <version>0.2.0</version>
    <message>FormaUI moved to the dev.formaui namespace.</message>
  </relocation>
</distributionManagement>
```

Because the cost of this migration grows with every release and every consumer, prefer securing
`formaui.dev` and publishing under `dev.formaui` as early as you reasonably can.
