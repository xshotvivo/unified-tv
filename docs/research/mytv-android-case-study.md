# MyTV Android — Competitive Case Study

**Checked:** 2026-11-10
**Candidate:** https://github.com/mytv-android/mytv-android
**Role in UTOS:** Study / regression-case reference only. Not an adopted code dependency.
**Evidence boundary:** Public repository, release notes, documentation and surfaced issue/activity records. No source-code deep audit, APK instrumentation or R700 reproduction has been completed.

## 1. What is verifiable

- The project describes itself as an Android / Android TV IPTV player and identifies Media3, IJKPlayer and VLC among its playback engines.
- Its public usage documentation describes M3U, TXT, Xtream Codes, Stalker Portal, EPG, JavaScript sources, multi-view and other advanced capabilities.
- The release page surfaced V3.0.0.233 as a preview released 2026-10-27. Its notes mention playback parameter tuning, reconnect improvements, engine component delivery, and fixes for source indexing, component activation and other playback issues.
- Public issue/activity records surfaced reports involving downloaded playback components failing to activate, 4K playback crashes, reconnect failure and private-network source playback.
- The repository README states that public source availability/participation is constrained and directs interested developers to request access. Confirm the exact current repository state before any code-reuse decision.

Sources:
- Repository: https://github.com/mytv-android/mytv-android
- Releases: https://github.com/mytv-android/mytv-android/releases
- Issues: https://github.com/mytv-android/mytv-android/issues
- User documentation: https://mytv-android.github.io/mytv-doc/
- Activity/issues snapshot: https://ghe.derekchia.com/repo/mytv-android/mytv-android

## 2. What must not be claimed

- The project is not proven abandoned or near closure. A preview release was surfaced on 2026-10-27.
- A reported bug is not proof that every user/device has the same failure.
- A failed stream does not prove a player defect; the source may be expired, restricted, malformed, overloaded or incompatible.
- A large feature list or multiple engines does not prove higher reliability.
- No R700 reproduction or comparative benchmark is available in this case study.

## 3. Useful ideas to study

- Multi-engine abstraction and explicit engine component lifecycle.
- Aggregated source candidates and alternate-route behavior.
- Playlist formats, EPG mapping, channel metadata and remote configuration.
- Playback diagnostics, reconnect controls and device capability display.
- Regression scenarios implied by real issue reports.

## 4. Failure cases to add to UTOS regression corpus

| Case | Upstream-reported symptom | UTOS test design |
|---|---|---|
| Component activation | Downloaded engine cannot be enabled | Simulate missing/corrupt component, ABI mismatch, signature failure and activation race |
| 4K stream crash | Some 4K live sources crash app | Test codec/profile/level, decoder capacity, memory pressure, thermal and malformed segments |
| Reconnect failure | Reconnect does not restore playback | Inject server reset, network loss and live-edge movement; assert bounded retry and first-frame verification |
| Private-network source | Some LAN sources fail | Test explicit user-allowlisted local endpoint, routing, DNS, HTTP and device isolation without LAN scanning |
| Source index corruption | Cloud/config push can disturb source index | Transactional import, stable source IDs and rollback after interrupted sync |
| Engine lifecycle race | Engine initialization/component loading failures | Repeated create/prepare/release, concurrent download and playback, process recreation |
| HLS header propagation | Dynamic playlists may require request headers | Ensure permitted headers propagate to manifests and segments; redact them from logs |
| 4K/codec mismatch | Stream may exceed device decode support | Separate format unsupported from network/server/player failures |

These are test hypotheses derived from surfaced upstream reports and release notes, not independently reproduced root causes.

## 5. UTOS differentiation

1. Stage-based observability: distinguish DNS, TLS, HTTP, manifest, prepare, decoder, first frame and sustained playback.
2. Error taxonomy with original engine code preserved.
3. AI diagnosis grounded in trace evidence, with confidence and explicit unknowns.
4. Bounded remediation plans, user approval for consequential actions, and verification after recovery.
5. Local-first, secret-redacted incident journal.
6. Every confirmed incident adds a deterministic regression fixture.
7. Benchmark by equivalent hardware/network/fixtures, not stars or subjective impressions.

## 6. Reuse and licensing

Do not copy code, assets or bundled dependencies based solely on the project page's license label. The repository references upstream projects and third-party code; inspect the exact license files, notices, dependency tree and distribution obligations for every component before reuse. Prefer learning public behavior and independently implementing compatible interfaces unless a code-reuse review explicitly approves otherwise.

## 7. Research follow-up

- Re-check repository access model and exact LICENSE files before any source-level audit.
- Inspect relevant release diffs and issue threads when accessible.
- Reproduce the reported failure classes on a controlled Android device and synthetic fixtures.
- Record source, date, exact version, device/firmware, network and raw sanitized logs.
- Promote a hypothesis to confirmed root cause only after repeatable reproduction and a regression test.
