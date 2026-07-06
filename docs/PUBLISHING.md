# Publishing FormaUI to Maven Central

The maintainer runbook for cutting a FormaUI release. Follow it top to bottom.

## Overview

FormaUI publishes two artifacts to Maven Central under the `dev.formaui` group:

| Artifact | Coordinates | Depends on |
|----------|-------------|------------|
| Theming core | `dev.formaui:core:<version>` | — |
| Components | `dev.formaui:components:<version>` | `core` (transitive) |

Current version: **`0.1.0`**.

**Why the upload is manual.** Legacy OSSRH (`s01.oss.sonatype.org`) is decommissioned, and the
Sonatype Central Portal does not accept a direct Gradle upload from plain `maven-publish`. This
repo deliberately uses only Gradle's built-in `maven-publish` + `signing` plugins — no external
publishing plugin — so the build keeps working behind SSL-inspecting proxies that block fresh
plugin-portal downloads. The trade-off: releasing is a **manual signed-bundle upload**, not an
automated `./gradlew publish` to a remote. The build produces an upload-ready bundle; you push it
by hand in the browser.

The publishing logic lives in the `formaui.publishing` convention plugin
([`build-logic/src/main/kotlin/formaui.publishing.gradle.kts`](../build-logic/src/main/kotlin/formaui.publishing.gradle.kts)),
applied to `:core` and `:components`. It configures the full POM, a javadoc jar (KMP already emits
sources jars), PGP signing, and a shared local `central-bundle` repository.

---

## One-time prerequisites

Do these once per machine / account — not per release.

### 1. Verify the `dev.formaui` namespace in the Central Portal

Sign in at [central.sonatype.com](https://central.sonatype.com) and register the `dev.formaui`
namespace. Because this is a custom domain namespace (not `io.github.<user>`), verification
requires proving control of `formaui.dev`: the portal issues a verification key that you publish as
a **DNS TXT record** on the domain, then click *Verify*. Uploads for the namespace are rejected
until this shows **Verified**.

> **No `formaui.dev` domain yet?** You can ship under an `io.github.<username>` namespace instead,
> which is verified with a GitHub account (no domain, no cost), and migrate to `dev.formaui` in a
> later version. The Kotlin package stays `dev.formaui.*` either way — only the Maven coordinate
> changes. See [Appendix: Publishing under `io.github`](#appendix-publishing-under-iogithub) before
> you decide, and read the migration caveats there — published coordinates are permanent.

### 2. Generate and publish a GPG signing key

Maven Central requires every artifact to be PGP-signed.

```bash
# Generate a key (RSA 4096, no expiry or a long one). Note the KEY_ID it prints.
gpg --full-generate-key

# Publish the PUBLIC key so Central can verify signatures.
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>

# Export the ASCII-armored PRIVATE key that Gradle will sign with.
gpg --armor --export-secret-keys <KEY_ID>
```

### 3. Place signing credentials (never in the repo)

Gradle reads the key from a Gradle property or an environment variable. Put them in your
**machine-global** `~/.gradle/gradle.properties` (not this repo's committed `gradle.properties`),
or export the env vars. See [`gradle.properties.template`](../gradle.properties.template).

```properties
# ~/.gradle/gradle.properties
# The armored private key on a single line, with real newlines escaped as \n.
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n...\n-----END PGP PRIVATE KEY BLOCK-----
signingInMemoryKeyPassword=<passphrase>
```

Equivalent env vars: `SIGNING_KEY` and `SIGNING_PASSWORD`.

No Central Portal credentials are needed at the Gradle level — you authenticate in the browser at
upload time. (A user token is only required if you later automate uploads via the Publisher API;
see [Future automation](#future-automation).)

---

## Pre-release checklist

Run through this before building the bundle for a given version.

- [ ] **Version is correct and consistent.** The version is hard-coded in **three** places — keep
      them in sync (this duplication is a known gotcha):
  - `build.gradle.kts` (root)
  - `build-logic/src/main/kotlin/formaui.publishing.gradle.kts` (line 40)
  - `sample/build.gradle.kts` (`versionName`)
- [ ] **Artifacts are immutable once published** — a version can never be re-uploaded. Double-check
      before you commit to it.
- [ ] **README accuracy** (the artifact is about to be public — reconcile the docs first):
  - Version badges read Kotlin `2.1.21` / CMP `1.8.0`, but the catalog
    ([`gradle/libs.versions.toml`](../gradle/libs.versions.toml)) is Kotlin **2.4.0** /
    CMP **1.11.1** / Material3 **1.9.0**.
  - The README states the library is on Maven Central at `dev.formaui:components:0.1.0` — that is
    only true **after** the upload below completes and propagates.
- [ ] **Green build gate.** Run the authoritative gates (unit tests + `wasmJs` compile + sample
      assemble) and confirm they pass before releasing.

> All `./gradlew` invocations below require the JetBrains Runtime 17 on `JAVA_HOME`:
> ```bash
> export JAVA_HOME=<path-to-JBR-17>
> ```

---

## Build the signed bundle

### Dry run (no signing key needed)

Verify the artifacts, POM, and jars locally first:

```bash
./gradlew publishToMavenLocal
```

Then confirm the artifacts landed:

```
~/.m2/repository/dev/formaui/core/<version>/
~/.m2/repository/dev/formaui/components/<version>/
```

Each should contain the main `.aar`/`.jar` (per target), a `-sources.jar`, a `-javadoc.jar`, and a
`.pom`.

### Real bundle (signed)

With signing credentials in place (see prerequisites):

```bash
./gradlew publishAllPublicationsToCentralBundleRepository
```

This writes a signed Maven-repo tree — including `.asc` signature files — to a single shared
directory so `core` and `components` ship in one deployment:

```
build/central-bundle/
```

---

## Package and upload

1. **Zip the *contents* of `build/central-bundle/`**, so that `dev/formaui/...` sits at the **root**
   of the zip — do **not** zip the `central-bundle/` folder itself (a wrong zip root is the most
   common upload rejection).

   ```bash
   cd build/central-bundle
   zip -r ../formaui-<version>-bundle.zip dev
   ```

2. Go to [central.sonatype.com](https://central.sonatype.com) → **Publish Component**, authenticate
   in the browser, and upload the zip.

3. The portal runs validation — PGP signatures resolve against your published public key, sources
   and javadoc jars are present, and the POM is complete (name, description, url, license,
   developer, SCM — all supplied by the convention plugin). If validation fails, **Drop** the
   deployment, fix, and re-upload. If it passes, choose **Publish**.

4. Propagation to `search.maven.org` and the CDN can take from minutes up to a few hours after
   publishing.

---

## Post-release

- **Tag the release** in git:
  ```bash
  git tag v<version>
  git push origin v<version>
  ```
- **Consumer sanity check.** In a throwaway project with `mavenCentral()` in its repositories, pull
  the artifact and confirm it resolves and compiles:
  ```kotlin
  dependencies {
      implementation("dev.formaui:components:<version>")
  }
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

## Future automation

The manual upload can later be replaced by an automated push to the Central Portal Publisher API
(authenticated with a user token generated in your Central Portal account). That is intentionally
out of scope today — the manual bundle flow is what keeps the build proxy-friendly. Revisit only if
release cadence makes the manual step a bottleneck.

---

## Appendix: Publishing under `io.github`

If you don't own `formaui.dev` yet, publish under an `io.github.<username>` namespace now and move
to `dev.formaui` later.

**What does and doesn't change.** The Maven **group ID** is only the coordinate string consumers
write in Gradle. It is independent of the **Kotlin package** (`dev.formaui.*`), which is compiled
into the source and stays the same. So `import dev.formaui.components.button.FormaButton` is
identical whether the artifact is `io.github.<you>:components` or `dev.formaui:components`.

### Verify the `io.github` namespace

At [central.sonatype.com](https://central.sonatype.com), register `io.github.<username>`. It is
verified by GitHub ownership: the portal gives you a code, you create a temporary **public repo**
named with that code, then click *Verify*. No domain, no DNS, no cost.

### Point the build at the new group

Change the group ID in **two** places (the version stays wherever it already is):

- `build.gradle.kts` (root): `group = "io.github.<username>"`
- `build-logic/src/main/kotlin/formaui.publishing.gradle.kts` (line 39): `group = "io.github.<username>"`

The POM `url`/`scm`/developer fields can keep pointing at `github.com/formaui` and `formaui.dev` —
those are metadata, not the coordinate. Everything else in this guide (signing, bundle build, zip,
upload) is unchanged.

### Migrating to `dev.formaui` later — the caveats

Published coordinates are **permanent and immutable**. Switching group is not a rename; it mints a
new artifact:

- `io.github.<you>:components:0.1.0` exists forever.
- `dev.formaui:components:0.2.0` is a *different* artifact. When consumers upgrade, they must edit
  their dependency line from the old group to the new one — it is not a transparent version bump.

To smooth that transition, publish a one-time **relocation POM** under the old coordinates that
redirects tools to the new group:

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

Because the cost of this migration only grows with each release and each consumer, prefer securing
`formaui.dev` and publishing under `dev.formaui` from `0.1.0` if you reasonably can.
