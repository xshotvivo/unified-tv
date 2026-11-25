# UTV Live Mobile V1.0 Delivery Plan

**Status:** Engineering branch initialized; first Android application scaffold added. Build and device validation are pending.
**Branch:** `feat/utv-live-mobile-v1`
**Primary validation device:** vivo V2157A, Android 11.
**Secondary target:** Toshiba REGZA 75R700SF, Android 12; not in the first mobile acceptance gate.

## Evidence labels

- **A:** repository files and current official dependency documentation inspected.
- **B:** device or CI measurement. None recorded yet.
- **C:** engineering inference.
- **D:** delivery target, not achieved performance.
- **U:** unknown / pending verification.

## Scope

The mobile app is an independently runnable Android client. Domain and data contracts should remain reusable by a future Android TV UI. Do not couple the shared Live core to touch-only navigation or mobile lifecycle assumptions.

### First build gate

- Android application module exists.
- Debug APK builds in CI.
- APK installs and launches on Android 11.
- Media3 starts a user-entered HTTPS stream and reports playback state/errors.
- No credentials, playlist URLs, cookies or tokens are committed.

### V1.0 functional scope

1. M3U/M3U8 and TXT import from local files and user-supplied URLs.
2. Channel catalog, search, categories, favorites and history.
3. XMLTV EPG import, mapping, time-zone handling and retention.
4. Media3 playback behind a stable PlaybackEngine contract.
5. Local persistence and transactional playlist refresh.
6. Source diagnostics, bounded retry and controlled failover among user-configured candidates.
7. Sanitized backup and restore.
8. Phone UI first; TV UI/platform adapter follows shared core validation.

## Milestones

| Gate | Target from build environment ready | Exit evidence |
|---|---:|---|
| M0 Engineering scaffold | Day 0-1 | Gradle project and CI build |
| M1 First installable APK | Day 2-5 | CI artifact + V2157A install/launch |
| M2 Source and channel loop | Day 5-8 | Parser tests + local catalog |
| M3 Playback integration | Day 7-10 | HTTPS/HLS fixture and real authorized source |
| M4 EPG and persistence | Day 10-14 | XMLTV tests and database migration tests |
| M5 Diagnostics and backup | Day 12-17 | failure/recovery and restore tests |
| M6 V1.0 candidate | Day 14-21 | regression report, limitations and signed release decision |

Targets are estimates, not guarantees. External blockers include CI permissions, Android SDK availability, real authorized test streams and user-side device installation.

## Current implementation state

- [x] Dedicated feature branch created from main.
- [x] Android application scaffold authored.
- [x] Media3 1.11.1 selected as stable baseline based on Android Developers release information checked 2026-11-26.
- [x] CI workflow authored to build, test and publish a debug APK artifact.
- [ ] CI build executed and passed.
- [ ] APK installed on V2157A.
- [ ] Playlist parser and channel catalog.
- [ ] EPG.
- [ ] Persistent storage.
- [ ] Diagnostics and backup/restore.
- [ ] Sustained playback and regression tests.

## Security constraints

- HTTPS only in the first scaffold. Cleartext HTTP is intentionally rejected until a per-source consent and credential-handling policy is implemented.
- Never store secrets in source control or plain diagnostic exports.
- No authentication, DRM, paywall or geographic-access bypass.
- Importing a playlist does not establish rights to redistribute its channels.
- Preserve the last known-good catalog on failed refresh.

## Build

CI invokes Gradle 8.13 with JDK 17 and Android SDK, then runs:

```bash
gradle --no-daemon testDebugUnitTest assembleDebug
```

Expected artifact path:

```
app/build/outputs/apk/debug/app-debug.apk
```

A successful CI build is not device acceptance or release readiness.
