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
- Secondary candidate: libmpv/FFmpeg adapter for formats or stream behavior not handled by the primary engine, subject to ABI, device decoder, package size, security and license review.
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
- **playback-core:** PlaybackEngine abstraction, Media3 adapter, optional libmpv adapter, session lifecycle.
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
- Playback: AndroidX Media3 first; libmpv as optional secondary adapter after device and license validation.
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
