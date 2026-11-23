# OriginOSTV V1.0 — R700 Delivery Control Plan

**Status:** Active delivery baseline  
**Branch:** `v1.0/r700-delivery`  
**Target device:** Toshiba REGZA 75R700SF, Android 12  
**Release type:** User-space Android TV application suite; not a custom ROM or independent operating system  
**Evidence rule:** A = authoritative/source inspected; B = measured on named hardware; C = evidence-based inference; D = target; U = unknown.

## 1. Product outcome

Deliver an installable, remote-first OriginOSTV V1.0 that makes the target R700 useful for everyday home media:

- A clean home/launcher entry with reliable D-pad focus and app navigation.
- A unified playback boundary used by Live TV and Media Center.
- Live TV with user-provided M3U and XMLTV sources, channel identity, EPG, favorites and bounded recovery.
- A local Media Center with scanning, metadata/artwork where available, categories, playback history and resume.
- Safe install, update, backup/export and rollback procedures.
- Repeatable device acceptance with logs and an evidence bundle.

The V1.0 target includes at least an 8-hour continuous playback soak as a **D target**, not a passed test.

## 2. Explicit non-goals for V1.0

- No custom Android ROM, root, bootloader changes or firmware flashing.
- No NAS purchase, cloud drive, remote streaming server or mandatory account.
- No AI agent performing autonomous device/configuration changes.
- No multi-brand compatibility claim.
- No sports center, retro-game ecosystem or advanced music product.
- No unauthorized channel lists, credential harvesting, DRM/HDCP bypass or bundled copyrighted streams.
- No replacement/removal of OEM system applications as an installation prerequisite.

## 3. Six-module delivery chain

| ID | Module | V1.0 responsibility | Exit evidence |
|---|---|---|---|
| M1 | Engineering Foundation | Build/release pipeline, versioned settings, storage model/migrations, structured logs, error taxonomy, backup/restore primitives and security controls | Reproducible build, migration/config tests, artifact hash, backup/restore test |
| M2 | UTOS TV Foundation | Home entry, D-pad navigation, focus restoration, app launch, Back behavior and user-controlled launcher activation | Install/launch/navigation/reboot tests on exact R700 build |
| M3 | UTOS Super Player | Stable engine-neutral session/control/error contract; engine adapter(s); metrics and lifecycle cleanup | Playback contract tests and same-fixture engine/device results |
| M4 | UTV Live TV | M3U import, channel identity, source candidates, XMLTV EPG, favorites, playback handoff and bounded retry/failover | Import/refresh/EPG/playback/fault-recovery tests |
| M5 | UTV Media Center | Local media scan, stable media identity, library browsing, metadata/artwork provenance, favorites, history and resume | Local fixture scan/browse/play/resume tests |
| M6 | Real Device Acceptance | Device inventory, deployment, regression, soak, fault injection, evidence and rollback | Signed-off evidence bundle; no substitute with documentation |

Dependency direction:
`M1 → M2 → M3 → (M4, M5) → M6`

M1 also supplies build, audit and recovery controls to M6. M2 is a direct runtime dependency of M4 and M5. Guardian is not a seventh delivery module; its minimum controls are embedded in M1.

## 4. Implementation strategy

Start with one installable Android TV application and strict internal module boundaries. Avoid premature service splitting or a custom OS. Keep module APIs explicit so code can be extracted into separate Gradle modules only when build/test evidence justifies it.

Initial code layout target:

- `app/`: application composition, manifest, navigation and installable APK.
- `core/foundation/`: configuration, persistence, logging, identity and recovery contracts.
- `core/player/`: engine-neutral playback contracts and adapters.
- `feature/home/`: TV home and remote-first navigation.
- `feature/live-tv/`: playlist, channel and EPG domain/UI.
- `feature/media-center/`: local library and playback history.
- `test-fixtures/`: deterministic local media/manifests and test data.
- `docs/acceptance/`: device inventory, procedures, raw-result index and release evidence.

No source code/build system currently exists on `main` as of the initial repository tree inspection (2026-11-22). This is an observed repository fact, not a claim that no work exists elsewhere.

## 5. P0 execution gates

### Gate 0 — Establish reproducible engineering baseline
- [ ] Add Android build system, pinned toolchain/dependencies and CI build.
- [ ] Add app skeleton, package/application ID, versioning and artifact naming.
- [ ] Add static checks and a minimal automated test suite.
- [ ] Produce first debug APK and record SHA-256.
- [ ] Confirm the APK installs and launches on an emulator or test device before R700 changes.

### Gate 1 — R700 device baseline and recovery
- [ ] Record exact model suffix, build fingerprint, firmware, ABI, SoC/codec inventory where exposed, RAM/storage and network state.
- [ ] Record current launcher/default HOME resolution and relevant permissions.
- [ ] Verify ADB connectivity and collect read-only diagnostics.
- [ ] Establish and verify user-data/config backup and OEM launcher return path.
- [ ] No package disable/uninstall or system-setting mutation before recovery is verified.

### Gate 2 — TV Foundation
- [ ] Install APK without root or system privilege.
- [ ] Validate D-pad, OK, Back, Home, focus visibility/restoration and app launch.
- [ ] Verify app crash/relaunch, reboot and OEM launcher fallback.
- [ ] Do not require setting the app as default launcher to use its features.

### Gate 3 — Super Player
- [ ] Define stable Open/Play/Pause/Stop/Seek/Position/State/Error/Statistics contract.
- [ ] Build deterministic local fixtures and contract tests.
- [ ] Compare VLC as the first real-device reference baseline with the in-app engine candidate(s); do not declare an engine winner before repeatable measurements.
- [ ] Capture first frame, rebuffer, decoder, audio/video, memory, crash and lifecycle evidence where measurable.
- [ ] Separate source/network/auth/format/decoder/player/device failures.

### Gate 4 — Live TV
- [ ] Import local M3U first; add remote URL import only with safe URI handling and bounded network work.
- [ ] Preserve channel IDs, user ordering, favorites and last-known-good catalog on failed refresh.
- [ ] Import XMLTV, map IDs and handle timezone/DST.
- [ ] Hand playback to Super Player and expose truthful source/playback states.
- [ ] Add bounded retry only for transient failures; no silent unauthorized source substitution.

### Gate 5 — Media Center
- [ ] Scan user-selected local folders/media through supported Android storage APIs.
- [ ] Generate stable media IDs; do not use file path as permanent identity.
- [ ] Browse by folders/type and support title search.
- [ ] Persist favorites, playback history and resume position transactionally.
- [ ] Treat artwork/metadata as optional providers with provenance and cache limits.

### Gate 6 — Real device acceptance and release
- [ ] Install, upgrade, uninstall and recover to OEM entry path.
- [ ] Test remote navigation, local playback, network playback and live TV.
- [ ] Inject network loss, stream stall, malformed playlist, stale EPG and low-storage conditions.
- [ ] Run 30-minute screening, 2-hour stability and at least 8-hour soak; record exact conditions.
- [ ] Verify config backup/restore and app rollback independently.
- [ ] Publish APK, SHA-256, version notes, compatibility limits, logs and acceptance result.

## 6. Release acceptance rules

A release gate is PASS only when the linked test has:
- exact device model/build and app version;
- repeatable setup and steps;
- expected and observed results;
- raw logs or recordings where appropriate;
- explicit PASS/FAIL/BLOCKED/NOT RUN status;
- known limitations and rollback result.

A source document, successful compilation, emulator screenshot or isolated happy-path demo is not device acceptance. A target is never reported as a measurement.

## 7. Decision register

| Decision | Current rule | Evidence / reopen condition |
|---|---|---|
| Platform | Existing R700 Android 12, user-space only | Reopen only with an explicit separately approved OS program |
| Product form | One installable app initially, internal module boundaries | Split Gradle modules when build/test evidence justifies it |
| Player | Engine-neutral contract; VLC is first real-device reference baseline | Choose in-app default only after same-device fixture comparison |
| Storage | Local-first; no NAS dependency | Add remote providers only after V1.0 |
| Security | Least privilege, verified sources/artifacts, no secrets in logs, reversible changes | Reopen for threat-model or acceptance failure |
| Long stability | 8-hour soak target | Threshold may be revised only from measured baseline and documented decision |

## 8. Current status snapshot — 2026-11-22

| Module | Planning | Source implementation | Build/deploy | R700 test | Acceptance |
|---|---|---|---|---|---|
| M1 Engineering Foundation | Partial | Not started | Not run | Not run | Not accepted |
| M2 TV Foundation | Partial | Not started | Not run | Not run | Not accepted |
| M3 Super Player | Partial | Not started | Not run | Not run | Not accepted |
| M4 Live TV | Partial | Not started | Not run | Not run | Not accepted |
| M5 Media Center | Partial | Not started | Not run | Not run | Not accepted |
| M6 Real Device Acceptance | Partial | Test tooling not started | Not run | Not run | Not accepted |

This is a status snapshot from the inspected repository and available evidence. It is not a percentage-complete estimate.

## 9. Immediate next actions

1. Create the minimal Android project and CI build, then prove a reproducible APK artifact.
2. Obtain the exact R700 read-only device inventory and verified recovery baseline.
3. Implement a minimal remote-first home that can be installed and launched before adding media features.
4. Add the playback contract and deterministic local playback test.
5. Integrate Live TV and Media Center only through the shared playback/foundation contracts.

## 10. Change control

Scope changes must state user value, dependency impact, security/privacy impact, storage/migration impact, test impact and release impact. Keep V1.0 frozen to the six modules above unless a fact error, real test conflict, security issue or acceptance blocker requires reopening it.
