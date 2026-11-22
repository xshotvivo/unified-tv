# UMI End-to-End Acceptance Matrix

**Status:** Test design plus initial Kotlin/JVM adapter unit tests. Twelve Kotlin/JVM core unit tests passed in GitHub Actions; no end-to-end, network, embedded-player or R700 hardware tests have been executed.
**Applies to:** First UMI vertical slice on Android TV / Toshiba R700.
**Evidence:** Every test must link to a run, app commit/build, fixture hash and device record.

## Test environment record

Required before reporting device results:
- Exact TV model suffix and hardware revision.
- Firmware/build fingerprint, Android version, security patch and ABI.
- App commit, version, build variant and dependency versions.
- Player engine/version and decoder inventory where available.
- Network topology, DNS/proxy/VPN state and measured link conditions.
- Fixture manifest, hashes and authorization/license provenance.
- Test date, operator, command sequence and sanitized raw logs.

## Functional matrix

| ID | Scenario | Expected result | Gate |
|---|---|---|---|
| SRC-001 | Add valid M3U source | Source stored; provenance and revision visible | G2 |
| SRC-002 | Add valid TXT source | Supported lines parsed; unsupported lines reported | G2 |
| SRC-003 | Add supported TVBox JSON fixture | Named schema adapter parses expected resources | G2 |
| SRC-004 | Malformed/unknown schema | Typed error; no partial catalog replacement | G2 |
| SRC-005 | Oversized feed | Bounded rejection or streaming parse; no ANR/OOM | G2 |
| SRC-006 | Interrupted refresh | Last-known-good snapshot remains usable | G2 |
| RES-001 | Same resource from two sources | One logical result; two provenance-preserving candidates | G3 |
| RES-002 | Ambiguous title collision | No silent destructive merge | G3 |
| RES-003 | Candidate expires | Candidate marked stale/failed with evidence | G3 |
| URL-001 | Resolve HTTP(S)/HLS candidate | Typed request; URI policy enforced | G3 |
| URL-002 | Redirect to loopback/private target | Blocked unless trusted local-source policy explicitly allows it | G3 |
| PLAY-001 | Play known-good local HLS | First frame and session events captured | G4 |
| PLAY-002 | Unsupported codec fixture | Accurate unsupported/decoder failure; no false network retry | G4 |
| PLAY-003 | Compare embedded engine and VLC app | Same fixture/device/network; separate logs and measurements | G4 |
| REC-001 | DNS/connection interruption | Correct transient network classification | G5 |
| REC-002 | HTTP 404 / invalid manifest | Correct permanent/input classification; bounded retry | G5 |
| REC-003 | Active candidate fails; configured alternate works | Switch only if eligible; verify first frame and sustained sample | G5 |
| REC-004 | All candidates fail | Stop safely; preserve state and explain failure | G5 |
| DATA-001 | Duplicate source import | Idempotent result; no duplicate logical resources | G6 |
| DATA-002 | Kill app during refresh | Last-known-good catalog intact after restart | G6 |
| DATA-003 | Restart and replay | Expected source/resource/candidate state restored | G6 |
| SEC-001 | Secret-bearing URL/header in fixture | Secrets redacted from logs and diagnostics | G5/G6 |
| SEC-002 | Malicious metadata/redirect/oversized response | Rejected within resource bounds | G2/G3 |
| SOAK-001 | 30-minute controlled playback | No fatal error; interruptions/resource metrics recorded | G6 |
| SOAK-002 | 2-hour playback and repeated channel changes | Repeatable stability record; no unsupported claims | Release gate |
| SOAK-003 | Overnight run | Required before making overnight-stability claims | Release gate |

## Required fault injection

1. DNS resolution failure and recovery.
2. TCP timeout, server reset and slow response.
3. HTTP 404, 403, 429 and 5xx responses.
4. Redirect loop and redirect to prohibited private/link-local address.
5. Malformed JSON, M3U and HLS manifest.
6. Truncated segment and missing segment.
7. App process death during import and playback.
8. Network loss and restoration during playback.
9. Invalid/expired credential simulation; no bypass.
10. Storage pressure and cache corruption.
11. Plugin timeout/crash only after a plugin runtime is introduced.

## Reporting rules

- Mark each test PASS, FAIL, BLOCKED or NOT RUN.
- A test that did not execute is never PASS.
- Distinguish host tests, emulator tests, external VLC reference tests and R700 hardware tests.
- Include numerator/denominator, repetitions and raw measurement method for rates/latencies.
- Do not claim reliability percentages from a single run or tiny sample.
- Every failure gets a typed category, sanitized evidence, owner and next action.
- Re-run the full regression set after fixing a P0/P1 or changing a shared contract.

## Exit criteria

G0–G6 are complete only when all mandatory cases are executed and pass, no unresolved P0/P1 remains, data integrity and secret redaction are proven, and a clean-install-to-playback-to-recovery-to-restart run is reproducible on the named R700 firmware. Device tests remain U — Unknown until the exact hardware is available and tested.
