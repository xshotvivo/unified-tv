# UTOS Super Player

**Status:** Proposed independent product module; architecture baseline only. No production build or device validation is claimed.
**Snapshot:** 2026-11-10
**First target:** Toshiba R700 / Android TV 12. User supplied LAN address: `192.168.2.75`; this is an unverified, potentially changing private-network address. No port, service, static lease or reachability is assumed.
**Parent:** UNIFIED TV / UTOS
**Related modules:** Live TV, Playback System, AI Agent, Reliability Engineering.

## 1. Mission

Build an Android TV-first live-stream player whose defining advantage is not the number of codecs or settings, but dependable playback, explainable failures, safe recovery and measurable improvement over time.

The player must:
- start supported streams reliably and switch channels predictably;
- detect the exact stage at which a session fails;
- preserve structured, privacy-safe evidence for diagnosis;
- propose actionable fixes with confidence and evidence;
- execute only bounded, reversible, policy-approved recovery actions;
- verify whether recovery actually worked;
- convert every confirmed incident into regression tests and future quality signals.

This is a player, not a source provider, playlist marketplace, IPTV relay, DRM bypass, or promise that third-party streams remain online.

## 2. Product boundaries

| Module | Owns | Must not absorb |
|---|---|---|
| UTOS Live TV | Sources, channel identity, playlist ingestion, EPG, favorites, source provenance | Decoder internals and engine lifecycle |
| Super Player | Playback session, engine adapters, surfaces, tracks, buffering, rendering, player diagnostics and recovery | Playlist ownership, EPG authority, AI policy |
| UTOS AI Agent | Evidence interpretation, explanation, remediation proposal and approved tool orchestration | Unrestricted shell, root, silent config mutation |
| Reliability Engineering | Incident taxonomy, test corpus, SLO methodology, release gates | Product-specific playback implementation |

Stable versioned interfaces connect these modules. The player remains usable if the AI service is offline.

## 3. Product principles

1. Playback first; AI is optional and must never block video startup or channel changes.
2. Evidence before diagnosis; no fabricated root cause or success claim.
3. Local-first diagnostics; cloud analysis is opt-in and receives only redacted, minimal data.
4. Recovery must be bounded, cancellable, explainable and verifiable.
5. Preserve the original source and user settings; never silently rewrite credentials or playlist URLs.
6. Engine-neutral domain layer; no UI or business logic may depend on a concrete engine SDK.
7. No forced ads, tracking, account or recommendation feed.
8. Security and licensing are release blockers, not paperwork after implementation.

## 4. Architecture

`TV UI → Playback Session Coordinator → Playback Orchestrator → Engine Selector → Engine Adapter → Android decoder / renderer`

Cross-cutting services:
- Source Resolver (consumes a URI and sanitized request metadata from Live TV)
- Device Capability Registry
- Network Probe (strictly bounded and policy-controlled)
- Playback Telemetry Collector
- Error Classifier
- Recovery Policy Engine
- Incident Journal (local, append-only, retention-bounded)
- AI Diagnostic Gateway (optional, permission-scoped)
- Regression Fixture Runner

### 4.1 PlaybackEngine contract

Every engine adapter implements equivalent operations:
- initialize / release;
- prepare(media request);
- play / pause / stop;
- attach / detach surface;
- seek only when the stream supports it;
- select audio / subtitle / video track where exposed;
- expose normalized playback state and supported analytics;
- return typed errors with original engine code preserved;
- support cancellation and lifecycle-safe teardown.

Engine-specific classes must never leak into UI state or persisted domain records.

### 4.2 Engine strategy

**Primary candidate: AndroidX Media3 / ExoPlayer.** Start with native Android integration and supported HLS, DASH and progressive formats. Actual codec/container/DRM/device support must be checked, not inferred from a protocol label.

**Conditional compatibility candidate: LibVLC.** Trial only for reproducible gaps where it materially improves supported-stream playback on the target device. Review native footprint, ABI, security, maintenance and LGPL obligations.

**Research candidate: libmpv.** Evaluate only if measurable playback requirements remain unmet and Android TV integration is demonstrated.

**Do not select IJKPlayer as the default foundation.** Reconsider only after upstream maintenance, FFmpeg revision, CVE patchability, toolchain, ABI and license review.

Multiple engines are not automatically better. More engines increase APK size, memory, lifecycle complexity and test combinations. The default release should ship the smallest engine set that passes the test corpus.

## 5. Failure detection pipeline

Each session emits a correlated, timestamped event sequence:

1. REQUEST_ACCEPTED
2. URI_VALIDATED
3. DNS_RESOLVED (when observable)
4. CONNECTION_ESTABLISHED
5. TLS_NEGOTIATED (HTTPS)
6. MANIFEST_OR_CONTAINER_READ
7. ENGINE_PREPARED
8. FIRST_VIDEO_FRAME / FIRST_AUDIO_OUTPUT
9. PLAYBACK_STABLE_WINDOW
10. SESSION_INTERRUPTED / SESSION_ENDED

Each event records monotonic duration, session correlation ID, engine/build version and a sanitized result. Unsupported observability is marked unavailable, never fabricated.

### 5.1 Error taxonomy

| Code | Meaning | Default action |
|---|---|---|
| INPUT_INVALID | Invalid URI, playlist item or request | Stop; identify input field |
| DNS_FAILURE | Name resolution failed | Bounded retry only if transient |
| TLS_FAILURE | Certificate or handshake failure | Fail closed; no certificate bypass |
| CONNECT_TIMEOUT | Connection deadline exceeded | Bounded retry with backoff |
| HTTP_FAILURE | HTTP status / redirect / server response | Classify status; do not assume source fault |
| AUTH_EXPIRED | Credential/token rejected or expired | Ask user/provider for authorized renewal |
| MANIFEST_INVALID | HLS/DASH or container parse failure | Capture sanitized parser detail |
| FORMAT_UNSUPPORTED | Codec/container/profile unsupported | Evaluate compatible engine or rendition |
| DECODER_FAILURE | Hardware/software decoder failure | Release session; controlled compatible fallback |
| BUFFERING_STALL | No progress beyond defined threshold | Inspect throughput and buffer telemetry |
| SOURCE_INTERRUPTED | Server or stream ended unexpectedly | Eligible bounded reconnect / alternate route |
| AUDIO_VIDEO_FAILURE | Track, renderer or sync failure | Isolate track/renderer evidence |
| RESOURCE_PRESSURE | Memory, CPU, thermal or decoder exhaustion | Release resources; degrade optional features |
| LIFECYCLE_FAILURE | Surface, background, sleep/resume or process issue | Reconcile session state safely |
| PLAYER_INTERNAL | Engine crash or internal exception | Isolate adapter and retain crash evidence |
| UNKNOWN | Insufficient evidence | Preserve trace; no speculative repair |

Keep the original engine exception, normalized category and confidence as separate fields.

## 6. AI Diagnostic Agent

### 6.1 Responsibilities
- summarize the incident in plain language;
- reconstruct the event timeline;
- rank likely causes with evidence and confidence;
- distinguish source, network, server, authentication, format, decoder, player and device failures;
- identify missing evidence and recommend the next safe diagnostic probe;
- generate remediation steps and explain risks;
- propose a code-level bug report with minimal reproduction and regression test;
- compare incidents across releases without confusing correlation with causation.

### 6.2 Closed-loop workflow

`Detect → Capture → Classify → Diagnose → Propose → Authorize → Execute → Verify → Learn`

- **Detect/Capture:** deterministic local instrumentation; independent of AI availability.
- **Classify:** rules first; AI may assist but cannot overwrite raw facts.
- **Diagnose:** rank hypotheses, list supporting and contradicting evidence.
- **Propose:** create a structured plan with preconditions, risk, rollback and expected result.
- **Authorize:** automatic only for explicitly allowlisted, low-risk, reversible actions; otherwise ask for user approval.
- **Execute:** invoke typed tools, not arbitrary shell or unrestricted code.
- **Verify:** confirm first frame/audio and a sustained playback window; a successful retry command is not success.
- **Learn:** add sanitized incident signature and regression fixture only after review.

### 6.3 Safe automated actions
Potentially eligible after policy implementation:
- retry a transient connection with a finite budget;
- restart the current playback session;
- test a user-configured alternate route;
- switch engine for a confirmed engine/format compatibility failure;
- clear a narrowly scoped disposable playback cache after integrity checks.

Never automate:
- disabling TLS/certificate validation;
- bypassing DRM, paywalls, authentication, geo-controls or access policies;
- scanning arbitrary LAN hosts or ports;
- installing unverified APKs or native libraries;
- changing router/DNS/firewall settings;
- deleting playlists, credentials or user data;
- root, ADB privilege escalation, firmware/system partition changes;
- uploading raw logs, IPs, tokens or URLs containing secrets to a cloud model.

### 6.4 Diagnostic report schema

Each report includes:
- incident_id, session_id, timestamp, app_version, engine_name/version;
- device model, Android version and user-approved diagnostic context;
- source identifier as a local opaque ID, never a credential-bearing URL;
- event timeline and stage durations;
- normalized error, original error code, sanitized message;
- codec/container/manifest metadata when observable;
- network type and probe outcome, without collecting payload contents;
- retry/failover actions and exact verification outcome;
- ranked hypotheses, confidence, evidence links and unknowns;
- remediation proposal, risk class, approval and rollback plan.

## 7. Logging, privacy and observability

- Structured JSON event model with monotonic and wall-clock timestamps.
- Local ring buffer with configurable retention and size limits.
- Exportable support bundle with preview and explicit user action.
- Redact authorization headers, cookies, query tokens, passwords and private content.
- Never log full URLs when query parameters may contain secrets; use opaque source IDs and sanitized host metadata.
- Separate crash traces from ordinary playback logs.
- Log levels: ERROR, WARN, INFO, DEBUG, TRACE; verbose trace is opt-in and time-bounded.
- AI analysis defaults to on-device/local rules where feasible; cloud transfer is separately opt-in.
- Provide delete/export controls and a documented retention policy.

## 8. Source and network safety

- Accept only explicitly configured/user-supplied sources and approved provider adapters.
- Enforce request deadlines, response-size limits, redirect limits and decompression limits.
- Validate redirects and block unsafe scheme transitions.
- Guard against SSRF: do not let playlist/script metadata trigger arbitrary private-network requests. User-configured local sources require an explicit allowlist and clear scope.
- Never scan the user's LAN. The supplied address `192.168.2.75` is only a user-provided candidate test-device address; verify it manually before use, and do not infer a port or service.
- Do not bypass authentication, DRM, geo-controls or network policy.

## 9. UX requirements

- Instant, low-occlusion player HUD; detailed diagnostics never cover the video by default.
- Clear states: Connecting, Preparing, Playing, Buffering, Reconnecting, Switching engine, Failed.
- Error message includes what failed, what was checked, what can be done and whether recovery was verified.
- Show retry count and elapsed time; avoid endless spinners.
- Remote-first operation: D-pad, OK, Back, channel up/down and media keys.
- Restore focus and playback state after diagnostics panel dismissal.
- AI diagnosis is a separate panel and never interrupts normal playback unless user requests it.

## 10. Performance and reliability measurements

Establish a baseline before setting numeric targets. Report numerator, denominator, sample size, test conditions and confidence limitations.

Required metrics:
- play-request success, first-frame success and sustained-session success;
- time-to-first-frame and channel-zap latency (p50/p95/p99 when sample size supports it);
- rebuffer events per session and total rebuffer duration;
- unexpected session termination, crash and ANR rates;
- recovery success rate and time-to-recovery;
- false-positive and false-negative error classification;
- diagnostic usefulness: confirmed root cause / diagnosed incidents;
- resource cost: RAM, CPU, temperature, power and APK size;
- AI latency, token/network cost (if cloud), unsafe-action block rate and user correction rate.

No numeric superiority claim until identical fixtures, devices, network and repeated trials support it.

## 11. Test and validation plan

**P0 — Architecture and safety**
- Interface contract, threat model, privacy map, engine/license/SBOM review.
- Define incident schema, log redaction and LAN request policy.

**P1 — Deterministic playback core**
- Media3 prototype; local synthetic HLS TS/fMP4, DASH, MP4, MPEG-TS and MKV fixtures.
- Valid/invalid manifests, unsupported codec, malformed URI, truncated segments.
- Unit/integration tests for cancellation, surface lifecycle, retries and state transitions.

**P2 — Failure injection**
- DNS loss, timeout, TLS failure, HTTP 403/404/5xx, redirect loop, slow throughput, server reset, expired test token, corrupt manifest, decoder error, low memory and process death.
- Verify correct classification and no infinite retry.

**P3 — AI diagnostic loop**
- Replay sanitized incident traces; compare AI hypotheses with ground truth.
- Test hallucination resistance, confidence calibration, privacy redaction and policy refusal.
- Human approval for persistent or high-risk actions.

**P4 — R700 device validation**
- Record exact model suffix, firmware, SoC, RAM, storage, network and installed build.
- Test remote navigation, channel zapping, sleep/resume, HDMI/audio, HDR where supported, long playback, memory pressure and thermal behavior.
- The supplied LAN address is not proof that the R700 is reachable or that ADB is enabled.

**P5 — Release readiness**
- No unresolved P0; P1 has mitigation, owner and regression test.
- License/security review complete; SBOM and dependency pins present.
- Crash/ANR, recovery and playback metrics reported from repeatable runs.
- Rollback and support-bundle workflows tested.

## 12. Iteration and learning governance

Every incident becomes a record with:
- reproducible conditions and raw sanitized evidence;
- cause status: confirmed / probable / unknown;
- user impact and severity P0-P3;
- workaround and permanent fix status;
- regression test ID;
- affected versions and first fixed version;
- confidence and reviewer.

Every release review compares the same test corpus and reports regressions, improvements and newly discovered gaps. AI may draft summaries and test suggestions; maintainers approve changes to rules, thresholds and code.

Do not train or update models from unreviewed logs. Do not turn one user's failure into a universal rule without independent evidence.

## 13. Initial success criteria

The first milestone is not "beat every player". It is:
1. deterministic playback with a reproducible local test corpus;
2. accurate stage-level logs and privacy-safe export;
3. correct classification of injected failure cases;
4. bounded recovery with verified outcomes;
5. AI reports that cite actual events and abstain when evidence is insufficient;
6. R700 compatibility measured rather than assumed.

## 14. Evidence status

- **A:** official upstream docs/repository facts checked on the stated date.
- **B:** repeated measurements on the exact target device; none claimed yet.
- **C:** engineering inference, labeled as such.
- **D:** design goals and acceptance criteria.
- **U:** unknown / not tested.

This document is a design baseline, not a claim that the product has been implemented, built, benchmarked or validated.
