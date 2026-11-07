# Reliability Engineering Baseline

**Status:** Proposed engineering baseline; not implemented or device-validated.
**Applies to:** All UTOS modules, adapters, background workers and update paths.
**Primary objective:** Reduce preventable failures, limit blast radius, detect faults accurately and recover safely. Reliability improvements must be demonstrated by repeatable measurements; no percentage reduction is claimed before a baseline exists.

## 1. Reliability contract

Every feature that can fail must define:
- its owner, dependencies and supported operating states;
- expected inputs, outputs and typed failure modes;
- a bounded execution deadline and resource budget;
- retry eligibility, retry limit and cancellation behavior;
- degraded-mode behavior and user-visible explanation;
- recovery path, data-integrity guarantees and audit evidence;
- tests and an explicit acceptance rule.

A feature is not complete merely because its happy path works.

## 2. Failure classification

Use stable machine-readable categories. Preserve the original cause; do not collapse unrelated failures into a generic "playback failed" or "network error".

| Class | Examples | Default response |
|---|---|---|
| INPUT_INVALID | malformed playlist, invalid config, unsupported URI | reject safely; identify item and reason |
| NETWORK_TRANSIENT | timeout, temporary DNS/connectivity loss | bounded retry with backoff |
| NETWORK_PERMANENT | unreachable host, repeated connection refusal | stop retrying; mark unavailable with expiry/recheck policy |
| AUTH_OR_POLICY | expired credential, forbidden, DRM/access restriction | do not bypass; request authorized remediation |
| FORMAT_UNSUPPORTED | unsupported container, codec, profile or track | report capability gap; suggest compatible rendition |
| RESOURCE_EXHAUSTED | memory, storage, decoder or thermal budget exhausted | cancel/degrade safely; release resources |
| DEPENDENCY_FAILURE | player, parser, provider or platform service crash | isolate, restart only when safe, preserve state |
| DATA_INTEGRITY | corrupt cache, failed migration, partial write | transactional rollback or last-known-good restore |
| SECURITY_BLOCK | invalid signature, unsafe redirect, policy violation | fail closed; do not auto-retry around the control |
| UNKNOWN | insufficient evidence | preserve diagnostics; do not invent a root cause |

Every incident record should include timestamp, module/version, device/build when relevant, operation, category, sanitized error, correlation ID, retry count, recovery action and final outcome. Never log passwords, tokens, raw authorization headers or sensitive user content.

## 3. Severity and response priority

- **P0 — Critical:** data loss/corruption, security boundary failure, unrecoverable boot/device failure, or broad system unavailability. Stop rollout and disable the implicated path where safe.
- **P1 — High:** core viewing/navigation unavailable, repeated crash loop, or failure affecting a major user journey. Prioritize mitigation and regression coverage.
- **P2 — Medium:** a feature is degraded but a safe workaround exists. Track and fix without destabilizing unrelated modules.
- **P3 — Low:** cosmetic, low-impact or rare issue with no data/security impact. Batch into planned maintenance.

Severity is based on user impact, scope, recurrence and recoverability—not on how easy the bug is to fix. Security and data-integrity issues are escalated regardless of frequency.

## 4. Resilience controls

### Deadlines and cancellation
- Every network, parser, player and background operation must have a finite deadline or bounded work budget.
- Propagate cancellation when the user leaves, changes channel or supersedes a request.
- Never let a hung optional provider block the home screen or unrelated modules.

### Retries and backoff
- Retry only failures classified as transient and operations known to be safe to repeat.
- Use bounded exponential backoff with jitter, a maximum attempt count and an overall deadline.
- Do not retry invalid input, unsupported formats, authorization failures, security blocks or permanent failures without a changed condition.
- Avoid retry multiplication across layers. One layer owns the retry budget for a given operation.

### Circuit breaking and isolation
- Temporarily stop calling a dependency after a defined pattern of repeated failures.
- Permit controlled probes to determine recovery; do not create rapid retry storms.
- Isolate optional providers, parsers and AI services so their failure cannot take down core navigation or playback.
- Apply concurrency, response-size, memory and storage limits to untrusted inputs.

### Fallback and degraded operation
- Fallback must be preconfigured, eligible, compatible and authorized.
- Preserve last-known-good configuration/catalog where integrity checks pass.
- Never silently switch to a lower-trust source or bypass DRM, credentials or user policy.
- If no safe fallback exists, show a precise failure state and retain diagnostics.

### State and data integrity
- Use transactional writes for catalogs, settings and migrations.
- Version persisted schemas and define forward/backward compatibility.
- Make recovery idempotent; repeating recovery must not duplicate or corrupt data.
- Keep backup/restore and rollback procedures separate from ordinary retry logic.

## 5. Live TV specific safeguards

- Separate source reachability, media validity, first-frame success and sustained playback.
- Distinguish source/network/auth/format/decoder/player/lifecycle failures before changing engine or source.
- Do not treat HTTP 200, manifest retrieval or a successful probe as proof of sustained playback.
- Only fail over to a previously validated, authorized candidate; verify first frame and a sustained sample after switching.
- Keep channel identity and user ordering stable when source candidates change.
- Record first-frame time, rebuffer events, decoder identity, dropped frames and session duration only when actually measurable.

## 6. Update and configuration safeguards

- Validate configuration before activation.
- Write new state atomically; retain a known-good version until health checks pass.
- Verify package/artifact origin, signature and integrity before installation.
- Use staged rollout and rollback only where the host platform supports them.
- Never change OEM firmware, system packages, bootloader or system partitions as an automatic recovery action.
- Require explicit user confirmation for persistent or destructive changes.

## 7. Reliability measurement

Do not invent SLOs or claim a failure-rate reduction before collecting a baseline.

For each release candidate, record at minimum:
- operation/session count and observation window;
- success rate and failure rate with numerator and denominator;
- crash/ANR count and affected sessions;
- recovery attempts, recovery success and time to recover;
- startup/first-frame latency and p50/p95/p99 where sample size supports them;
- resource use (RAM/CPU/storage/temperature/power) where measurable;
- test device, exact model/firmware, app build, network and fixture set.

Separate lab fixtures from real-world sessions. Report sample size and confidence limitations. Compare releases under equivalent conditions. A metric without a reproducible method is not an acceptance metric.

## 8. Required test gates

1. **Unit:** failure classification, retry policy, circuit-breaker state, migration and rollback logic.
2. **Integration:** dependency timeout, malformed response, cancellation, duplicate request and adapter failure.
3. **Fault injection:** DNS loss, slow network, server reset, partial download, corrupt cache, low storage and process death.
4. **Recovery:** restart, last-known-good restore, interrupted update and repeated recovery idempotency.
5. **Soak:** long-duration playback, repeated channel changes, memory pressure and thermal observation.
6. **Security:** unsafe redirects, private-network target restrictions, oversized/decompression-bomb inputs, secret redaction and signature failure.
7. **Device:** exact R700 model/firmware, real remote, sleep/resume, HDMI/CEC, audio, HDR, DRM and OTA preservation.

A test is not passed if the test did not run. Mark unavailable hardware tests as **U — Unknown**, not pass.

## 9. Release gates and stop conditions

Do not promote a module or release when:
- a P0 remains open;
- a P1 has no mitigation, owner or regression test;
- data migration/rollback is unsafe;
- a security or license review is incomplete for included dependencies;
- core acceptance tests are missing or were not executed;
- device-specific claims lack device-specific evidence.

Freeze a baseline only after architecture, interfaces, test coverage, failure handling, security review and recovery have been reviewed together. Reopen for factual error, acceptance contradiction, security issue or inability to execute.

## 10. Evidence and ownership

For each requirement, record: owner, evidence class (A/B/C/D/U), source or test record, last verified date, applicability, known limitations and next action. Proposed controls remain **D** until implemented; implementation without a successful test remains unverified; R700 behavior remains **U** until tested on the exact device and firmware.

## 11. Initial P0 backlog

1. Confirm exact R700 model suffix, firmware/build, SoC, RAM/storage and safe developer access.
2. Establish repository license, contribution policy and dependency/license inventory before reusing upstream code.
3. Add CI baseline: Markdown/link checks, formatting, secret scanning and dependency/security review; expand to build/test checks when executable code exists.
4. Define canonical incident schema and module-level failure taxonomy.
5. Define retry/deadline/fallback ownership for Live TV and playback; prevent retry multiplication.
6. Define backup/restore and rollback behavior before persistent settings or update implementation.
7. Create a traceable test matrix linking every release gate to a test, result and evidence record.

**Non-goal:** promising zero failures. The engineering goal is to reduce failure probability, bound impact, improve detection and make recovery predictable—with measured evidence.
