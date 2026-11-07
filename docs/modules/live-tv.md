# UTOS Live TV

**Status:** Proposed engineering baseline; not implemented, built, or validated on target hardware.
**Research snapshot:** 2026-11-07. Repository status and licenses must be rechecked before adoption.
**First target:** Toshiba R700, Android 12 (exact model suffix, firmware, SoC, RAM and storage still to be recorded).

## 1. Product definition

UTOS Live TV is a remote-first live television client and service layer for UTOS. It combines channel cataloguing, playlist ingestion, EPG, playback orchestration, source diagnostics, recovery, favorites, history and transparent operational status in one coherent product.

It is a player and management system, not a broadcaster, channel-rights provider, stream-hosting service or guarantee that a public URL will remain available.

**Product ambition:** a leading, dependable Android TV live-TV experience. "Best" is a design ambition, not a verified comparative claim.

## 2. Product principles

1. Watch first: enter live playback quickly; keep overlays lightweight and dismissible.
2. Remote first: every core flow works with D-pad, OK, Back and standard media keys; deterministic focus restoration.
3. Truthful status: distinguish configured, reachable, playable, stable, authorized and unknown. A successful HTTP response alone is not proof of a working channel.
4. Recovery without deception: retry and switch only among user-configured, lawful and compatible alternatives; explain when no fallback exists.
5. User control: no forced advertising, account, recommendation feed, telemetry or source replacement.
6. Local resilience: cached catalog, favorites and recent EPG remain usable when a source or network fails.
7. Modular replacement: parser, provider, player engine, EPG source and UI can evolve behind versioned interfaces.
8. Evidence before claims: separate upstream claims, code inspection, measured device results, inference, design targets and unknowns.

## 3. User experience / information architecture

### Main navigation
- **Live:** last channel, now-playing, compact channel rail and playback surface.
- **Guide:** time-grid EPG with Now / Next, date navigation, genre and channel filters.
- **Channels:** numbered channel list, categories, search, favorites, recent and custom order.
- **Sources:** configured playlists/providers, import status, refresh schedule and provenance.
- **Diagnostics:** source health, player events, network state, recent errors and exportable support report.
- **Settings:** playback engine, startup behavior, zapping, subtitles/audio, EPG mapping, privacy, backup/restore.

### Playback UI
- Full-screen video remains dominant.
- A low-occlusion HUD shows channel number/name, logo (if available), current programme, next programme, time progress and source label.
- Channel-up/down and left/right behavior is configurable and consistent.
- Guide and channel list may open as overlays; preview-to-fullscreen promotion should avoid restarting playback where the engine allows it.
- Buffering, reconnecting, source switching and error states use plain language and visible elapsed time.
- Optional detailed diagnostics are separate from the normal viewing UI.

### Truthful status vocabulary
- **Configured:** user added this source.
- **Metadata parsed:** playlist entry was parsed.
- **Endpoint reachable:** a bounded network probe succeeded.
- **Playback started:** player rendered a first frame / audio output event.
- **Stable sample:** stream remained active for the defined observation window.
- **Degraded:** measurable interruptions or repeated errors.
- **Unavailable:** repeatable failure under recorded conditions.
- **Unknown:** not tested, test inconclusive or capability not exposed.
- **Rights status:** separate field; technical playability never implies authorization.

Do not use a single green "online" badge for all of these states.

## 4. Functional scope

### 4.1 Channel catalog
- M3U/M3U8 import from local file or user-supplied URL.
- XMLTV EPG import, gzip where supported, timezone and channel-ID mapping.
- Provider adapters may be added only after protocol, security and license review.
- Normalize channel identity, display name, number, group, language, country/region, logo, EPG ID, source, stream format and provenance.
- Merge duplicate channel identities while retaining distinct stream candidates and provenance.
- Support large playlists using streaming parsing, bounded memory, paging and cancellable refresh.
- Preserve user channel numbers, favorites, hidden channels and sort order across refreshes.
- Import must be transactional: failed refresh cannot silently destroy the last known-good catalog.

### 4.2 Playback
- Primary candidate: AndroidX Media3 / ExoPlayer for Android-native HLS, DASH and supported progressive/live formats.
- Optional compatibility candidate: libVLC adapter for confirmed Media3 compatibility gaps, subject to ABI, device decoder, package size, security, maintenance and LGPL/compliance review. libmpv remains a research candidate, not a first-release dependency.
- A common PlaybackEngine contract isolates UI and domain logic from either engine.
- Engine choice and fallback policy must be evidence-based on R700; do not assume two engines always improve reliability.
- Capture player analytics: load start, manifest/playlist events, first frame, rebuffer start/end, dropped frames where available, audio/video format, decoder, playback error and release lifecycle.
- Support external audio/subtitle tracks only where the format and engine expose them.

### 4.3 Source health and failover
Use separate stages rather than a simplistic URL ping:
1. Parse and validate URI / scheme.
2. DNS and TLS / connection diagnostics where platform APIs permit.
3. Bounded HTTP request and response classification.
4. Manifest or transport-stream inspection where safe and supported.
5. Player preparation and first-frame/audio confirmation.
6. Sustained observation window with rebuffer and fatal-error tracking.
7. Optional periodic recheck with rate limits and user-configurable schedules.

Classify errors at least as: malformed URL, DNS, TLS/certificate, timeout, HTTP status, redirect/headers, geo/network restriction (only when evidence supports it), expired token, unsupported manifest/container/codec, decoder failure, server-side stream failure, DRM/authorization requirement, and unknown.

**Failover policy**
- Try the preferred candidate first.
- Retry only bounded, backoff-controlled attempts; avoid infinite loops and synchronized retry storms.
- Switch to the next user-configured candidate only when the failure class is eligible.
- Do not rotate credentials, forge headers, bypass authentication, DRM, paywalls or geographic controls.
- Keep the original candidate and failure record; report which alternative was selected.
- Add hysteresis/cooldown to avoid rapid oscillation between unstable sources.
- Never claim a source is fixed merely because another source played.

### 4.4 EPG
- XMLTV ingestion with incremental/stream parsing and rolling retention window.
- Map by explicit tvg-id first; offer reviewed/manual mapping when ambiguous.
- Display programme title, start/end, description and category only when supplied by the guide.
- Show missing or stale guide data honestly; do not fabricate programme details.
- EPG time-zone conversion and daylight-saving behavior require tests.

### 4.5 Source and channel governance
Every source record stores:
- source URI or local reference (credentials stored separately and securely);
- user-entered label and import time;
- upstream project/provider, license/terms URL and review date where known;
- provenance class: official/authorized, user-provided, public community candidate, unknown;
- region and access restrictions if documented;
- last checked time, test method, device/network context and result;
- expiration/refresh behavior if known.

No public IPTV list is automatically treated as authorized. Public availability, successful playback and legal permission are different questions. Do not package third-party channel URLs in the app without a documented rights and license review.

## 5. Real-data UI and observability

### User-facing compact view
- Channel: number, name, group and current programme.
- Playback: starting / playing / buffering / reconnecting / failed.
- Startup: measured time-to-first-frame for the current session.
- Source: configured source label and active candidate index (never expose secrets).
- Recovery: retry count and whether a fallback was actually used.

### Diagnostic detail view
- Timestamped event timeline.
- DNS/connect/TLS/HTTP/manifest/prepare/first-frame stage durations where available.
- HTTP status and redirect count when safely observable.
- Player engine/version, media MIME/container, video/audio codec, resolution, frame rate and decoder when exposed.
- Rebuffer count, total rebuffer duration, dropped frames where supported, fatal error code and sanitized message.
- Network type and measured throughput only when actually sampled; label sampling window and method.
- Source health result with confidence and test duration.
- Last successful playback time and test context.

### Data labels
- **Measured (B):** captured from the current device/session with timestamp and method.
- **Upstream fact (A):** verified from official documentation/repository and dated.
- **Inference (C):** explain the evidence and uncertainty.
- **Target (D):** intended threshold, not achieved performance.
- **Unknown (U):** unavailable or untested.

No fabricated signal strength, bitrate, resolution, channel uptime, viewer count or "source score". A bitrate estimate must state whether it is manifest metadata, sampled network throughput or measured media payload rate.

### Local-first privacy
- Diagnostics stored locally by default.
- Export requires user action and sanitizes tokens, MAC addresses, cookies, authorization headers, IPs and other secrets.
- No background collection or external telemetry by default.
- Crash reporting, if later considered, is opt-in with a documented data-flow map.

## 6. Proposed architecture

- **live-ui:** Compose for TV screens, overlays, focus graph and accessibility.
- **live-domain:** channel/program/source/playback models and use cases.
- **catalog-ingest:** streaming M3U/M3U8 and XMLTV parsers, validation and normalization.
- **source-registry:** provenance, source policy, candidate grouping and user configuration.
- **epg-service:** guide sync, channel mapping, rolling retention and queries.
- **playback-core:** engine-neutral PlaybackEngine abstraction, Media3 adapter, optional LibVLC adapter only after evidence gate, session lifecycle and metrics.
- **health-monitor:** bounded probes, playback observation, error taxonomy and health history.
- **recovery-orchestrator:** retry budget, backoff, candidate failover, cooldown and cancellation.
- **local-data:** Room database, migrations, indexes, paging and backup/restore.
- **platform-adapter:** Android TV remote, network and supported device capabilities.
- **diagnostics:** structured event timeline, metrics, sanitized report export.

Dependency direction: UI → domain contracts → implementations/adapters. Player engines and provider adapters do not own UI state. Live TV exposes typed contracts to UTOS home and AI Agent; the AI Agent cannot directly access credentials or arbitrary URLs.

### Core contracts (draft)
- ChannelRepository
- PlaylistImporter
- EpgRepository
- SourceRegistry
- PlaybackEngine
- PlaybackSession
- PlaybackAnalytics
- SourceHealthProbe
- RecoveryPolicy
- RemoteInputAdapter
- DiagnosticsExporter

All interfaces need versioning, cancellation, bounded timeouts and typed error results before implementation.

## 7. Candidate technology baseline

- Language: Kotlin.
- TV UI: Jetpack Compose for TV, with Android Views interoperability only where required.
- Playback: AndroidX Media3 as the first-release primary engine; libVLC only as an optional secondary adapter after measured gap analysis and legal/security/device validation. libmpv remains research-only until separately justified.
- Persistence: Room; DataStore for preferences; WorkManager only for permitted, bounded background refresh.
- Networking: OkHttp or platform networking APIs with explicit timeouts, redirect policy and TLS validation.
- Parsing: streaming M3U and XMLTV parser; avoid loading full large feeds into memory.
- Async/state: Kotlin coroutines and Flow.
- Tests: JVM unit tests, parser fixtures, instrumented playback tests, repeatable hardware test scripts and regression corpus.

Versions are intentionally not frozen here. Select mutually compatible stable versions at implementation start and record dependency/SBOM and license data.

## 8. Performance and reliability acceptance

Do not freeze numeric product thresholds until R700 baseline measurements exist. The following are mandatory test scenarios:
- cold start, warm start and resume;
- channel selection to first frame across representative HLS and MPEG-TS streams;
- 50 consecutive channel changes with no leaked player/session resources;
- 2-hour continuous playback on representative streams;
- temporary Wi-Fi loss, DNS failure and server timeout recovery;
- malformed, expired, redirected, unsupported and unavailable source cases;
- primary engine failure and secondary-engine handover;
- large M3U and XMLTV imports with cancellation and low-memory observation;
- EPG timezone, DST, missing IDs and stale guide;
- D-pad focus, Back behavior, accessibility and repeated rapid input;
- background/foreground, screen off/on and OEM power behavior;
- thermal and memory stability on R700.

Each test record includes device model, firmware, app build, engine version, source provenance, network, date, repeat count, raw logs and result. Report median and p95 only when sample count and method are stated.

### Proposed gates
- P0: requirements, threat model, source governance, architecture and license review.
- P1: parser and local catalog with fixture-based tests.
- P2: single-engine playback and truthful diagnostics.
- P3: EPG, channel UI, favorites and remote navigation.
- P4: source health, retry and controlled failover.
- P5: R700 hardware validation and performance tuning.
- P6: UTOS Home / Media Center / AI Agent integration and release packaging.

A phase is not complete because code compiles; it must pass its stated tests.

## 9. GitHub research and reuse policy

Research is maintained separately in [GitHub ecosystem research](../research/live-tv-ecosystem.md). Upstream projects are learning references, not automatically approved dependencies.

Before copying or linking code into the product:
1. inspect the exact upstream repository, commit and license file;
2. check transitive dependencies, notices, trademarks, assets and API terms;
3. record what is learned versus what is reused;
4. preserve attribution and required notices;
5. reject incompatible or unclear licenses until reviewed.

Do not copy code from source-available/non-commercial projects into a distributable UTOS build without permission. A repository being public does not grant a reuse license.

## 10. Explicit non-goals and safety boundaries

- No bundled unauthorized channel lists or claim of guaranteed channel access.
- No DRM, paywall, authentication or access-control bypass.
- No token harvesting, credential rotation or protected URL extraction.
- No root, bootloader unlock, system partition changes or OEM firmware replacement in the R700 first phase.
- No fabricated channel availability, signal metrics or EPG.
- No mandatory cloud account, ads or tracking.

## 11. Current maturity

This document is an engineering specification and research baseline only. No UTOS Live TV APK, executable, player engine or R700 validation is claimed to exist yet.


## 12. Automated source operations and AI-assisted recovery

This subsystem is a first-class UTOS Live TV capability. It is divided into deterministic automation and an AI reasoning layer; AI must not replace repeatable measurement or bypass access controls.

### 12.1 Source aggregation and ingestion
- Accept user-configured local M3U/TXT files, subscription URLs and documented provider APIs.
- Normalize entries into a canonical channel/source model; retain source provenance and original metadata.
- Merge by reviewed channel identity and aliases, not display-name equality alone.
- Retain multiple candidates per channel, including independent origin, protocol, headers reference, resolution, measured throughput, latency, last-tested time and failure history.
- Keep credentials and request headers in a secure store; never place secrets in exported playlists or AI prompts.
- Support user-configured refresh schedules, bounded concurrency, cancellation, backoff and last-known-good snapshots.
- Export M3U, TXT and read-only API views. Export only sources the user is entitled to use; output format does not grant distribution rights.

### 12.2 Source collection policy
- "Automatic collection" means discovering and aggregating user-authorized, official, public or explicitly supplied sources under documented access conditions.
- Every connector has a declared scope, terms/rights review, rate limit, attribution and provenance.
- Do not scrape private services, extract protected stream URLs, harvest tokens, bypass DRM, paywalls, authentication, geo-controls or access restrictions.
- Unknown provenance is marked unknown and excluded from any claim of official/authorized status.

### 12.3 Health measurement pipeline
Each candidate moves through explicit states:
1. DISCOVERED — ingested, not yet tested.
2. PARSE_VALID — URI and playlist syntax pass validation.
3. NETWORK_REACHABLE — DNS/TLS/connection and bounded HTTP checks pass.
4. MEDIA_VALID — manifest/container can be inspected and supported media metadata is available.
5. FIRST_FRAME — the target playback engine renders a frame (or audio-only output is confirmed).
6. SUSTAINED_SAMPLE — a defined playback observation window completes with measured rebuffer/error statistics.
7. DEGRADED, FAILED, BLOCKED_OR_AUTH_REQUIRED, UNSUPPORTED, EXPIRED_OR_UNAVAILABLE, or UNKNOWN — classified outcomes with evidence and timestamp.

A TCP connect, HTTP 200, manifest response or a single screenshot is not equivalent to sustained playback. A successful screenshot can help identify wrong-channel/placeholder content but cannot prove long-term stability.

### 12.4 Latency, throughput and resolution semantics
- **Connection latency:** measured request/connection timing, with method and endpoint stage.
- **Startup latency:** time from playback request to first decoded frame.
- **Throughput:** sampled transfer rate over a stated byte count and time window; not automatically equal to sustainable media bitrate.
- **Resolution / frame rate / codecs:** read from manifest or actual decoded stream where available; mark source of metadata.
- **Stability:** observed rebuffer count, rebuffer duration, fatal errors and sample duration.
- Measurements are network-, region-, time-, device- and server-dependent. Store timestamp and test context; never represent a one-off sample as a permanent source score.
- Resolution filtering is user-configurable. A high-resolution source with inadequate throughput or repeated buffering must not outrank a lower-resolution stable source by resolution alone.

### 12.5 Candidate ranking and per-channel alternatives
Rank candidates using a configurable policy with hard eligibility gates first (authorization/provenance policy, protocol compatibility, expiry and repeated fatal failure), then measured playback quality. A suggested ordering is: sustained-playback success, startup latency, rebuffer ratio, throughput headroom, resolution/frame rate, recency and stability confidence. Do not collapse all dimensions into an opaque AI score.

Keep several independent candidates per channel (default count is a product setting to be calibrated). Avoid duplicate URLs, same-host false diversity and correlated upstream origins. Preserve last-known-good candidate and allow user pin/blacklist/restore.

### 12.6 Failure detection and diagnosis
Use deterministic rules and player/network telemetry to generate a structured incident:
- observed symptom and event timeline;
- affected channel/candidate and source provenance;
- test stage and timestamp;
- error code / sanitized HTTP status / player error;
- likely cause with confidence and evidence;
- whether the issue appears source-side, network-side, device/decoder-side, EPG/catalog-side or unknown;
- safe recommended action and rollback state.

Examples include DNS failure, TLS certificate failure, timeout, HTTP 403/404/5xx, redirect/header incompatibility, expired URL, manifest parse error, unsupported codec, decoder failure, no first frame, repeated rebuffering, wrong-channel/placeholder content and stale EPG. Do not infer a geo-block or rights status without evidence.

### 12.7 AI diagnosis and controlled remediation
AI receives only sanitized structured incidents, recent measurements, source provenance and permitted action schemas. It may:
- summarize the failure in plain language;
- compare the current candidate with the channel's known alternatives;
- propose a cause and confidence with supporting evidence;
- select an already configured, policy-eligible alternative;
- request a bounded re-test or re-import of an authorized feed;
- draft a configuration change for user approval;
- produce a before/after report and explain whether playback recovery was actually confirmed.

AI must not:
- invent or generate stream URLs;
- bypass authentication, DRM, paywalls or regional access controls;
- harvest credentials, tokens or protected headers;
- alter source provenance or claim authorization;
- silently change user-pinned sources, delete candidates or overwrite the last-known-good catalog;
- claim "fixed" until the playback engine confirms first frame and the configured stability sample passes.

### 12.8 Recovery transaction
1. Detect and deduplicate an incident.
2. Run a bounded diagnostic profile.
3. Identify eligible recovery actions from a deterministic allowlist.
4. For low-risk reversible actions (retry, switch to a previously configured candidate, refresh a user-authorized feed), execute under policy and record the action.
5. For persistent changes (blacklist, source replacement, schedule changes, credentials, provider configuration), require user confirmation.
6. Verify playback by first-frame and sustained-sample checks.
7. If verification fails, roll back to last-known-good configuration and report failure.
8. Store incident, action, evidence, result and user decision in an auditable local log.

### 12.9 AI autonomy levels
- **A0 Observe:** read-only monitoring and reports.
- **A1 Diagnose:** classify and recommend; no mutation.
- **A2 Recover:** may retry or switch among existing eligible candidates and run bounded checks.
- **A3 Maintain:** may refresh approved feeds and apply reversible source-health state changes under explicit user policy; persistent changes remain confirm-gated.
- **Forbidden:** access-control bypass, credential harvesting, unapproved scraping, destructive device changes or silent rights/provenance changes.

Default initial release: A0/A1. A2 only after test evidence and user opt-in. A3 is a later opt-in capability, never the default.

### 12.10 Operations dashboard and reports
Provide:
- total configured sources and candidates, with counts by test state;
- channel coverage and channels with no eligible candidate;
- last successful playback and last check timestamps;
- source quality distributions by measured startup latency, throughput, resolution and rebuffering;
- newly failed / recovered / degraded candidates;
- incidents grouped by root-cause class;
- automatic actions attempted, succeeded, rolled back or awaiting approval;
- EPG freshness and unmapped channel count;
- update run ID, start/end time, duration and data provenance.

Every number must be computed from stored observations and show its sample window. No fabricated uptime percentage or "AI repair success" metric without defined denominators.

### 12.11 Security and abuse resistance
- Treat playlist URLs, M3U metadata, XMLTV descriptions, provider responses and AI-visible text as untrusted input.
- Defend against SSRF: allow only approved schemes; block loopback, link-local, private and metadata-service destinations unless the user explicitly configures a local source under a separate policy; re-check redirects and DNS resolution.
- Apply per-source timeouts, response-size limits, redirect limits, concurrency caps and decompression limits.
- Prevent command injection; never pass untrusted URL or playlist strings into a shell.
- Sanitize logs and exported reports; protect secrets at rest and in transit.
- AI tools are typed, narrow, auditable and least-privilege; no arbitrary shell, browser, filesystem or network access.

## 13. Integration reference: Guovin IPTV-API

Repository: https://github.com/Guovin/iptv-api

The upstream README/config documentation describes multi-source aggregation, source validation, automatic disabling, latency/speed/resolution/frame-rate measurements, resolution and speed filters, multiple per-channel output candidates, EPG aggregation, scheduled updates, and M3U/TXT/API outputs. It also documents optional screenshot checks and structured logs.

UTOS treatment:
- **Study:** high-value reference for source ingestion, channel aliases, candidate aggregation, configurable filters, output formats, update scheduling and operational logs.
- **Trial:** run isolated with user-authorized source inputs; compare results against UTOS's own playback validation on the target player/device.
- **Adopt:** do not make it a mandatory runtime dependency until its exact current license, dependencies, security posture, deployment footprint, API contract and R700 operating model are reviewed.
- Keep source aggregation/measurement independent from the Android TV playback UI. A Windows/Docker/server-side worker may prepare catalogs; the R700 client should consume a signed/versioned catalog or standard playlist/API without needing to run expensive scans itself.
- Do not assume its reachability/speed/screenshot checks prove long-term playback stability. UTOS adds device-side first-frame and sustained-playback verification.

Upstream documentation is descriptive evidence of documented features, not proof that every configured source is legal, available or reliably playable. Re-check repository and license before reuse.


## 14. Playback engine strategy and product-quality requirements

### 14.1 Product apps versus playback engines

UTOS separates two categories:

- **End-user player applications:** PotPlayer, mpv-android, VLC for Android, Just (Video) Player, Nova Video Player, Kodi, MX Player, IINA and similar products. Study their user experience, controls, subtitle/audio handling, library workflows and device compatibility. They are not interchangeable SDKs.
- **Embeddable playback engines / SDKs:** AndroidX Media3 (ExoPlayer), LibVLC, libmpv and FFmpeg-based components. These are implementation choices behind UTOS playback contracts. FFmpeg is a multimedia framework/library collection, not by itself a complete player UI or product.

PC and iOS products are useful comparative references, but the first implementation and acceptance target remain Android TV / Toshiba R700. Do not let desktop feature lists expand the first-release scope.

### 14.2 Engine decision

| Candidate | UTOS disposition | Reason / boundary |
|---|---|---|
| AndroidX Media3 / ExoPlayer | Primary, first implementation | Android-native integration, HLS/DASH and MediaDrm support, analytics and lifecycle APIs; actual codec support still depends on formats, Android version and device decoders. |
| LibVLC | Conditional secondary adapter | Broad format/protocol coverage and network filesystem features; add only for reproducible Media3 gaps that matter to users. Review LGPL obligations for LibVLC and the wider VLC application GPL terms separately. |
| libmpv | Research candidate | Strong playback controls, rendering and subtitle capabilities; native integration/ABI/build/maintenance burden needs a dedicated trial. |
| IJKPlayer | Hold / legacy reference | Do not select as a new primary engine based on historical popularity. Verify upstream maintenance, exact FFmpeg revision, security fixes, Android build compatibility and license obligations before any reconsideration. |
| FFmpeg directly | Not a standalone player choice | Consider only as a narrowly scoped extractor/decoder component when a concrete requirement justifies the integration and compliance burden. |

No engine is declared universally superior. Engine selection is per-device and per-stream evidence, not brand preference.

### 14.3 Unified playback contract

The UI and Live TV domain must depend only on a versioned engine-neutral contract:

- `PlaybackEngine`: capabilities, create session, prepare, play, pause, seek, stop, release.
- `PlaybackSession`: lifecycle, media item, surface attachment, track selection and playback state.
- `PlaybackCapabilities`: protocols, containers, codecs, HDR/audio features and known device limitations.
- `PlaybackEvent`: state changes, first frame, buffering, format/decoder changes, recoverable and fatal errors.
- `PlaybackMetrics`: time-to-first-frame, rebuffer count/duration, dropped frames where exposed, decoder, resolution and session duration.
- `TrackCatalog`: audio, subtitle and video tracks with stable IDs and language/role metadata.
- `EngineSelector`: deterministic policy based on format, DRM requirement, device capability, prior measured compatibility and user override.

All engine calls must support cancellation, bounded timeouts, explicit release and sanitized typed errors. Engine-specific types must not leak into UI/domain models.

### 14.4 Playback UX requirements

The player experience must be designed as a product, not a thin wrapper around a video surface:

1. Fast channel zapping, with measured selection-to-first-frame time and no unbounded preloading.
2. Stable D-pad behavior: channel up/down, guide, OK, Back and media keys; focus must return predictably.
3. Low-occlusion HUD with channel, programme, source, playback state and timeout behavior.
4. Audio-track and subtitle selection, subtitle delay where supported, aspect ratio and scaling controls.
5. Clear controls for live edge / timeshift only when the source and engine support them; never display fake seek capability.
6. Resume/reconnect behavior that distinguishes temporary network interruption, source failure and decoder failure.
7. Optional advanced diagnostics separated from normal viewing.
8. No forced ads, autoplay recommendations, account requirement or telemetry by default.

### 14.5 Engine-selection and fallback policy

- Start with Media3 for every supported test fixture and user-configured stream.
- Classify the failure before considering another engine: unsupported manifest/container/codec, decoder initialization, DRM/authorization, network/source failure, malformed input or unknown.
- A second engine is eligible only for an engine-specific compatibility failure. Do not switch engines to mask a dead URL, expired credential, network outage or access restriction.
- For a confirmed engine-specific gap, try LibVLC only if it supports the required feature on the target device and passes the same playback verification.
- Do not silently switch DRM sessions, credentials, request headers or protected content between engines.
- Record the engine decision and reason; allow a user override and retain a safe default.
- Avoid repeatedly switching engines during one session. Use cooldown and bounded retries.

### 14.6 Required comparison tests

Build a repeatable local fixture corpus before evaluating public streams:

- HLS MPEG-TS and fMP4/CMAF; live playlist refresh and discontinuities.
- DASH clear and DRM test vectors only where lawful test licenses are available.
- MPEG-TS over supported transport, progressive MP4/MKV and representative audio/subtitle tracks.
- H.264, HEVC, VP9 and AV1 only where the R700 hardware/software decoder reports support.
- Unsupported codec, malformed manifest, truncated segment, slow network, DNS loss, server timeout and abrupt stream termination.
- HDR / audio passthrough only where the R700 hardware, firmware, display and sink chain expose those capabilities.

For each engine, record build/version, ABI, APK size delta, RAM, CPU, thermal behavior, decoder path, first-frame time, rebuffer ratio, dropped frames, audio sync, track behavior, crash/ANR and 2-hour stability. Compare on the same R700, firmware, network, source fixture and test order. No performance winner is declared before repeated measurements.

### 14.7 Licensing and supply-chain gate

Before shipping any native engine:
- inspect the exact source commit and license files;
- enumerate bundled and transitive FFmpeg/codecs dependencies and build flags;
- retain required copyright, license and notices;
- assess dynamic/static linking and source-offer obligations with qualified review;
- generate an SBOM and vulnerability inventory;
- define update ownership and response time for security fixes.

Public source availability does not grant reuse rights. This section is a design decision, not a legal opinion or completed compliance review.

### 14.8 Current status

**Decision status:** architecture direction accepted for planning: Media3 primary; LibVLC conditional; libmpv research-only; IJKPlayer held.

**Implementation status:** not implemented. No engine has been benchmarked on Toshiba R700. No APK, compatibility result, speed advantage or reliability claim is made.


## 15. Advanced codec compatibility and performance

UTOS codec policy is now: AV1 = compatibility target; H.266/VVC = research; AV2 = experimental. Media3 is not hardware-only: Android platform decoders are the default, and optional software decoder extensions (including AV1) can be built and included. Hardware support must be queried from the actual target device.

Do not infer decoder capability from chipset generation labels. Record codec profile/level, bit depth, chroma, resolution, frame rate, HDR, DRM and actual decoder component. Prefer hardware decode; evaluate software decode only with sustained FPS, CPU, RAM, thermal and power evidence.

VVC research candidates include vendor MediaCodec/Codec2 and VVdeC. Android 17 introduces VVC platform interfaces but AOSP does not supply a VVC software decoder. AV2 v1.0.0 is published; dav2d exists but remains early-stage and is not a production default. IJK/FFmpeg/VLC do not automatically contain or wire every decoder library.

Performance work must profile first, then optimize algorithm hot paths, codec-valid parallel units, maintained SIMD implementations, memory traffic and rendering. Do not skip mandatory normative tools such as ALF/CDEF to force playback; use a lower-complexity rendition or reject the stream if real-time decode is not possible.

See `docs/modules/playback-system.md` section 11 for decoder selection, tests, metrics and evidence boundaries.
