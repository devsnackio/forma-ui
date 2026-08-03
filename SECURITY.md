# Security Policy

## Supported versions

FormaUI is pre-1.0. Only the most recent release receives fixes — there are no long-term support
branches, and patches are not backported to older `0.x` versions.

| Version | Supported |
|---|---|
| Latest `0.1.x` release | ✅ |
| Anything older | ❌ — upgrade to the latest |

## Reporting a vulnerability

**Please do not open a public issue for a security problem.**

Report it privately through GitHub: go to the repository's **Security** tab → **Report a
vulnerability** (GitHub private vulnerability reporting). This opens a channel visible only to you
and the maintainers.

If private reporting is unavailable to you, open a regular issue asking for a private contact and
**omit all technical detail** — the maintainer will follow up with a channel.

Useful things to include when you do report:

- the FormaUI version, and your Android API level,
- what an attacker can achieve, and under what preconditions,
- a minimal reproduction — a composable and the state that triggers it.

## What to expect

You should get an acknowledgement within a week. FormaUI is maintained by one person, so please
allow reasonable time for a fix before public disclosure. Fixes ship as a new patch release with the
issue described in `CHANGELOG.md`, and reporters are credited unless they ask not to be.

## Scope

FormaUI is a UI component library: it renders components and holds no credentials, performs no
network I/O, and reads no files. Realistic issues are things like a component leaking sensitive text
through accessibility semantics, a state-handling bug that discloses data across recompositions, or
a crash reachable from untrusted input rendered in a component.

Out of scope: vulnerabilities in Kotlin, Compose Multiplatform, or Material 3 themselves — report
those upstream. Findings in the `:sample` app or `:preview-wasm` harness are also out of scope;
neither is published.
