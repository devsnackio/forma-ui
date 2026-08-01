# Releasing FormaUI to Maven Central

The single publishing doc: one-time setup, the per-version runbook, and the background on why the
pipeline is shaped this way.

Two artifacts ship, under the `dev.formaui` group:

| Artifact | Coordinates | Depends on |
|----------|-------------|------------|
| Theming core | `dev.formaui:core:<version>` | — |
| Components | `dev.formaui:components:<version>` | `core` (transitive) |

Current version: **`0.2.0-beta01`** — a namespace-verification beta. It exists to prove the
`dev.formaui` coordinates resolve end-to-end on Central before `0.2.0` stable is cut from the same
tree. Nothing about the library differs between them.

> The Kotlin package is always `dev.formaui.*` regardless of the Maven coordinate — see
> [Group ID vs. Kotlin package](#group-id-vs-kotlin-package).

### Release history

`0.2.0-beta01` is the first release under `dev.formaui`. Everything before it shipped under the
retired `io.github.devsnackio` group and **still exists on Central permanently** — those coordinates
can never be withdrawn:

| Version | Group | Note |
|---|---|---|
| `0.1.0-beta01` … `0.1.0-beta04` | `io.github.devsnackio` | betas |
| `0.1.0` | `io.github.devsnackio` | stable; published 2026-07-27 |
| `0.2.0-beta01` | `dev.formaui` | namespace verification on the owned-domain group |
| `0.2.0` | `dev.formaui` | stable; cut once the beta resolves on `repo1` |

Old coordinates redirect via the relocation POMs in [`relocation/`](../relocation/build.gradle.kts)
— see [Appendix → The `dev.formaui` migration](#the-devformaui-migration).

> ⚠ **Checking what's published: use `repo1.maven.org`, never `search.maven.org`.** The solrsearch
> index lags publishes by a long way and happily returns `numFound: 0` for coordinates that already
> resolve. It reported zero for `io.github.devsnackio` for days *after* `0.1.0` went live, and this
> runbook, `README.md` and `CHANGELOG.md` all carried "nothing has shipped yet" as a
> result. `repo1` **is** the repository; the index is a cache of it.
>
> ```bash
> curl -sI https://repo1.maven.org/maven2/dev/formaui/components/0.2.0/components-0.2.0.pom | head -1
> ```

> ⚠ **`0.1.0` is not reproducible from any commit.** No commit ever bumped the build files to
> `0.1.0` — they read `0.1.0-beta04` throughout, so it was cut via Route B from an uncommitted local
> version edit, and there is no `v0.1.0` tag. Don't retro-tag a commit that doesn't build it. The
> rule that prevents a repeat: **never release a version that isn't committed in both build files.**
> Route A's version guard enforces this automatically; Route B has no such guard, so check by hand.

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

- **Namespace** `dev.formaui` verified in the Central Portal, by proving control of the
  `formaui.dev` domain. The portal issues a **Verification Key**; publish it as a **TXT record on
  the apex** (`@`) of `formaui.dev`, wait for it to resolve, *then* click **Verify**. Central checks
  the exact domain — `formaui.dev`, not `maven-central.formaui.dev` or any other variant — and once
  verified, every subgroup (`dev.formaui.*`) is covered too.
  ⚠ Don't click Verify before `nslookup -type=TXT formaui.dev` shows the key: a premature check
  caches an NXDOMAIN and stalls verification. Leave the TXT record in place afterward.
  (The retired `io.github.devsnackio` namespace remains verified via GitHub account ownership —
  needed only to publish the relocation POMs.)
- **Group ID + POM** wired to `dev.formaui`, in two places: `build.gradle.kts` (root) and
  `build-logic/src/main/kotlin/formaui.publishing.gradle.kts`.
  ⚠ The POM's `url` (`https://formaui.dev`) and `scm` (`github.com/devsnackio/forma-ui`) must both
  **resolve** — a dead URL in a published POM is permanent. They deliberately differ: `url` is the
  docs site, `scm` is where the source lives.
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
`JAVA_HOME` on a JetBrains Runtime — the Android Studio JBR on the Windows box
(`C:\Users\User\AppData\Local\Programs\Android Studio\jbr`, currently JBR 25), JBR 17 on macOS
(`~/Library/Java/JavaVirtualMachines/jbr-17.0.14/Contents/Home`). Don't hardcode the JBR *version*
anywhere — Android Studio bumps it, and the path is what stays stable.

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
| `build.gradle.kts` (root) | `version = "0.2.0-beta01"` |
| `build-logic/src/main/kotlin/formaui.publishing.gradle.kts` | `version = "0.2.0-beta01"` — **authoritative for the published artifact** |
| `sample/build.gradle.kts` | `versionName` (cosmetic; keep aligned) |

`relocation/build.gradle.kts` carries its own `relocationVersion` and is **deliberately not** part
of this bump — it stays pinned to stable `0.2.0`, the coordinate old builds should land on. Publish
it only after `0.2.0` itself resolves.

Before going further:

- [ ] **Green build gate** — unit tests + `wasmJs` compile + `sample` assemble all pass.
- [ ] **README accuracy.** Check the badges still match
      [`gradle/libs.versions.toml`](../gradle/libs.versions.toml), the component count still matches
      `docs/component-inventory.json`, and the install snippet shows the version you're releasing.
- [ ] **`CHANGELOG.md`** has an entry for this version.
- [ ] **`formaui-site`** — bump `MAVEN_VERSION` in `src/lib/constants.ts` *after* the release lands,
      since its Vercel build downloads the matching `previews-<version>.tar.gz` release asset.

Examples below release `0.2.0` — substitute your version.

---

## Route A — release via CI (preferred)

Trigger [`release.yml`](../.github/workflows/release.yml) from the Actions tab → **Release to Maven
Central** → *Run workflow*, with the `version` input (e.g. `0.2.0`).

It runs a **version guard** (the input must equal the version in both build files — a typo aborts
before anything irreversible), the full test gate, the signed bundle build (it refuses to proceed
unsigned), the zip-root assertion, then uploads the bundle **as a workflow artifact** and POSTs it
to the Central Publisher API as `USER_MANAGED`, polling status for up to 10 minutes.

It deliberately **does not tag and does not press Publish** — both stay with you; continue at
[Post-release](#post-release).

> ⚠ **A green run is not a publish.** The workflow uploads as `USER_MANAGED`, which parks the
> deployment in the Portal awaiting your click — the "Upload" and "Wait for Portal validation" steps
> both pass without anything reaching consumers. Treating green as done is exactly how this repo's
> docs came to claim nothing had shipped. Finish the job at
> <https://central.sonatype.com/publishing/deployments>.
>
> If the API step itself fails, download the `central-bundle-<version>` workflow artifact and
> continue from [step B4](#b4-upload) — the manual path always remains available.

---

## Route B — release from a local machine (fallback)

### B1. Validate locally (dry run — catch problems before the irreversible upload)

No signing key needed for this:

```bash
./gradlew publishToMavenLocal --console=plain
```

Confirm the artifacts landed under `~/.m2/repository/dev/formaui/{core,components}/<version>/`.
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

### B3. Zip with `dev/` at the root

Zip the *contents* of `build/central-bundle/` so `dev/formaui/...` sits at the zip root —
**not** the `central-bundle/` folder itself. Git Bash has no `zip`, so use the JDK's `jar` (it
produces clean forward-slash entries):

```bash
cd build/central-bundle && "$JAVA_HOME/bin/jar" -cMf ../formaui-0.2.0-bundle.zip dev
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
    "https://repo1.maven.org/maven2/dev/formaui/$a/0.2.0/$a-0.2.0.pom"
done
# 200 = live, 404 = still propagating
```

Consumer sanity check — in a throwaway project with `mavenCentral()`:

```kotlin
dependencies { implementation("dev.formaui:components:0.2.0") }
```

> **Only after this returns 200** — if the retired `io.github.devsnackio` coordinates still need to
> redirect, publish the relocation bundle now. See
> [Publishing the relocation bundle](#publishing-the-relocation-bundle). Doing it earlier points
> consumers at an artifact that isn't live yet.

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

For most of the project's life these differed: the group was `io.github.devsnackio` (verified via
GitHub account ownership — no domain, no cost) because `formaui.dev` wasn't owned yet. Since `0.2.0`
they match.

### The `dev.formaui` migration

**Done — this section is a record, not a to-do.** `formaui.dev` was acquired on 2026-08-01 and the
group moved to `dev.formaui` for `0.2.0`.

Published coordinates are permanent, so a group switch is not a rename — it mints a new artifact:

- `io.github.devsnackio:components:0.1.0` exists forever and can never be withdrawn.
- `dev.formaui:components:0.2.0` is a *different* artifact. Consumers must edit their dependency
  line; it is not a transparent version bump.

The relocation POMs bridge that gap. [`relocation/`](../relocation/build.gradle.kts) publishes
nothing but `.pom` files under the **old** group at `0.2.0`, each carrying:

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

Gradle and Maven both follow it: a build still asking for `io.github.devsnackio:components:0.2.0`
resolves through to the new coordinates and prints a warning naming the replacement.

#### Publishing the relocation bundle

**Order matters: do this only after the real release resolves on `repo1.maven.org`.** A relocation
POM pointing at an artifact that isn't live yet advertises a redirect to a 404.

```bash
./gradlew :relocation:publishAllPublicationsToRelocationBundleRepository --console=plain
find build/relocation-bundle -name 'maven-metadata*' -delete
cd build/relocation-bundle && "$JAVA_HOME/bin/jar" -cMf ../formaui-0.2.0-relocation.zip io
```

Note the **`io`** at the end — this bundle's root is the *old* group. Upload it as a second, separate
deployment at [central.sonatype.com](https://central.sonatype.com) → **Publish Component**. It writes
to `build/relocation-bundle/`, and its task name deliberately differs from the release task, so a
normal release run never picks it up.

This is a one-shot. Don't republish it every release — one relocation POM per retired artifact is
enough, and the module can be deleted once consumers have moved.
