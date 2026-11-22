# UTV Universal Media Integration (UMI)

**Status:** Proposed architecture; not implemented, built or device-validated.
**First target:** Toshiba REGZA 75R700SF, Android 12; exact firmware/build and hardware inventory remain to be captured.
**Delivery rule:** Continue through implementation, automated tests, real-device playback, fault recovery and repeatable acceptance. This document is not evidence of completion.

## 1. Mission and ownership

UMI is the shared integration foundation that ingests heterogeneous media sources, interprets source formats and transports, normalizes results into stable resource identities, and hands playable candidates to the shared Playback System.

UMI is not a universal decoder, content provider, DRM bypass, web-video scraper or promise that every third-party plugin runs on every platform.

Existing domain ownership remains:
- **UTV Live TV:** channel catalog, M3U/TXT/XMLTV, EPG, channel provenance and live-source health.
- **UTV Media Center:** movie/series/music library identity, metadata and user curation.
- **Cloud Drive & Resource Sharing:** remote-file providers, authentication and file access.
- **Playback System:** engine-neutral sessions, decoder capability, playback events and metrics.
- **UMI:** shared source/provider contracts, format and protocol adapters, plugin capability boundaries, resource-resolution orchestration and cross-domain provenance.

Domain modules must not depend on a concrete plugin runtime or concrete player implementation.

## 2. First end-to-end product slice

The first delivery is deliberately narrow but real:
1. Add a user-provided M3U/TXT source and one documented, controlled TVBox JSON schema.
2. Parse and validate input without destroying the last-known-good snapshot.
3. Normalize channels/resources and playable candidates into the shared resource model.
4. Search, select a candidate and resolve its HTTP(S)/HLS playback descriptor.
5. Start playback on R700 through the first embedded engine selected by the existing Playback System decision.
6. Capture first-frame, buffering, fatal error and session lifecycle events.
7. Inject a controlled failure, diagnose its class, retry or switch only to a preconfigured eligible candidate, and verify playback again.
8. Restart the app and prove catalog/configuration integrity and repeatability.

**VLC reference requirement:** The installed VLC Android application is an external comparison baseline on the same R700, same authorized fixture and network. It helps separate source/device compatibility from UTV integration defects. It is not automatically the embedded UTV engine. Media3 remains the first embedded-engine candidate in the current Playback System specification; embedded LibVLC is evaluated only if a reproducible compatibility gap justifies it.

The first vertical slice does not require arbitrary third-party JavaScript/JAR/Python execution. It requires a clean provider interface and a safe, deterministic built-in adapter. Plugin runtime work is a later gate.

## 3. Keep six dimensions separate

A source is not a protocol, a plugin is not a decoder, and a media URL is not proof of playability.

| Dimension | Examples | UMI responsibility |
|---|---|---|
| Source family | TVBox JSON, M3U/TXT, local file, NAS, WebDAV, AList/OpenList | Origin, provenance, update and auth model |
| Provider schema | TVBox site schema, playlist syntax, provider API response | Versioned source adapters |
| Plugin runtime | Built-in Kotlin, JavaScript, JVM/JAR, Python | Capability-limited runtime host |
| Transport | HTTP(S), HLS, DASH, RTSP, WebDAV, SMB | Typed locator and capability |
| Container/codec | MPEG-TS, fMP4, MP4, H.264, HEVC, AV1 | Playback System/device decision |
| Access policy | Credentials, DRM, regional/service restrictions | Explicit authorization state; never bypass |

Compatibility is recorded as a tuple of platform, source adapter, schema version, runtime, transport, container/codec, access policy and app version. Parsing a protocol does not imply decoding its media.

## 4. Layered architecture

### L1 — Source registry and provenance
Stores source identity, user label, source family, adapter ID/version, origin, provenance, rights-review status, opaque auth reference, refresh policy, last-known-good revision and timestamps.

### L2 — Source adapters
Each adapter owns one bounded input contract: M3U/TXT, a named TVBox JSON variant, local library index or remote-file provider. Adapters return typed records and diagnostics; they do not open players or mutate UI state.

### L3 — Plugin capability host
Later extension point for third-party rules. Every plugin declares ID, version, publisher/origin, digest/signature status, runtime, requested capabilities, platform support, network policy, timeout and memory/output limits. Default-deny capabilities. No direct filesystem, arbitrary process, shell, private-network access or credential access. A plugin cannot silently add a source or persist a new URL without user policy approval.

### L4 — Protocol and locator adapters
Normalize transport descriptors and safe request metadata. HTTP(S)/HLS is first slice. DASH, RTSP, WebDAV, SMB and provider-specific transports are separate capabilities and are not assumed playable on every device.

### L5 — Canonical resource model
Stable IDs separate logical identity from source-specific records and playable candidates. Metadata keeps provenance and confidence. User edits and curated metadata take precedence over refreshed upstream data.

### L6 — Resolution and scheduling
Selects among preconfigured candidates using explicit policy, source health, device capability, access state and user preference. One owner controls the retry budget per operation. No hidden source rotation, authentication bypass or downgrade to an untrusted source.

### L7 — Playback and observability
Hands a typed playback request to the engine-neutral Playback System. Receives lifecycle/metrics events correlated with source, candidate, adapter and app versions.

### L8 — UX and diagnostics
Presents unified search/resource views and truthful source/playback state. Normal viewing must not depend on optional providers or diagnostics UI.

## 5. Canonical contracts (initial draft)

Implement versioned Kotlin interfaces/data classes first; add cross-platform wire schemas when a second runtime exists.

- SourceDescriptor: sourceId, family, label, adapterId/version, origin, provenance, authRef, refreshPolicy, enabled.
- SourceSnapshot: sourceId, revision, fetchedAt, contentHash, itemCount, validationState, lastKnownGood, diagnostics.
- ResourceIdentity: stable resourceId, resourceType, normalizedTitle, year/episode identity, identityNamespace.
- SourceResource: sourceId, sourceItemId, resourceId, display metadata, source metadata, provenance, observedAt.
- PlaybackCandidate: candidateId, resourceId, sourceId, locatorRef, protocol, declaredMime/container, requestProfileRef, authRef, healthState, lastValidatedAt.
- PlaybackRequest: resourceId, candidateId, typed locator, permitted request profile, start position, selected tracks, correlationId.
- AdapterResult: value or typed error, warnings, source revision, duration and trace ID.
- SourceHealth: configured, parsed, reachable, mediaValid, firstFrame, sustained, degraded, unavailable, unknown. Rights/access state is separate.
- IntegrationDiagnostic: stage, category, timestamp, safe context, retryability, correlationId and evidence references.

Secrets, cookies, authorization headers and private playlist URLs must never appear in public logs, analytics, fixtures or source control.

## 6. Stable identity and deduplication

- Source IDs identify configured source instances, not URLs copied into logs.
- Source item IDs remain namespaced by source and adapter.
- Logical resource IDs are independent of source order, display title and current URL.
- Exact source IDs and explicit provider IDs outrank fuzzy title matching.
- Fuzzy matching may propose a merge but must not silently merge ambiguous records.
- A resource can retain multiple candidates with distinct provenance, protocol, quality, access state and validation history.
- Favorites, ordering, watch progress and metadata corrections attach to logical resource IDs, not ephemeral stream URLs.
- Failed refreshes preserve the last-known-good snapshot; successful refreshes validate and commit atomically.

## 7. Failure and recovery

At minimum distinguish invalid source input, fetch timeout, auth/policy denial, parse failure, unsupported schema, plugin crash/timeout, unsupported protocol, DNS/TLS failure, HTTP status failure, invalid manifest, unsupported media format, decoder failure, player lifecycle failure, resource exhaustion, security block and unknown.

Recovery policy:
1. Preserve the original cause and correlated diagnostics.
2. Retry only transient, safe-to-repeat operations with one bounded retry owner.
3. Do not retry malformed input, unsupported schemas, auth/DRM denials, security blocks or unsupported codecs as network failures.
4. Switch candidates only if user-configured, authorized, prevalidated and device-compatible.
5. Verify first frame and a sustained sample after switching.
6. Preserve user state and last-known-good snapshot.
7. If recovery fails, stop safely and report the actual stage; never claim automatic repair.

## 8. Security and trust boundaries

External source content, redirects, playlist lines, JSON, plugin metadata, logos, EPG text and provider responses are untrusted.

Required controls:
- Strict URI scheme allowlist per adapter; reject file/content/javascript/data and unknown schemes unless a narrowly scoped adapter explicitly owns them.
- SSRF defense: block loopback, link-local, private and metadata-service destinations for remote imports by default. Trusted LAN/NAS access must be explicit and scoped. Revalidate redirects and resolved addresses.
- Bounded connect/read/overall timeouts, redirect count, response/decompressed bytes, parser depth, item count, concurrency, memory and CPU.
- TLS validation remains enabled; no automatic certificate bypass.
- Credentials stored in platform secure storage, referenced by opaque IDs, redacted in logs and revocable by source.
- Plugin execution isolated from the host process where feasible; isolation guarantees must be measured per platform. A JavaScript interpreter alone is not a security sandbox.
- No arbitrary code execution in the first vertical slice.
- Source/plugin updates are transactional, origin-verified and rollback-capable.
- License, dependency and redistribution review before adopting upstream code or binaries.

## 9. Platform strategy

First implementation: Android TV/Kotlin, targeting the R700 Android 12 host, subject to exact SDK/ABI/firmware inventory. Keep source contracts platform-neutral; put network, secure storage and playback behind platform adapters.

Later targets: Windows desktop, Android mobile and other TV platforms. Share schemas and behavior contracts where practical, but use native implementations where capabilities differ. Do not promise identical plugin runtimes or codec support.

The first repository delivery must establish an actual app/build/test workspace. Documentation-only completion is not accepted.

## 10. Implementation sequence and gates

| Gate | Deliverable | Pass condition |
|---|---|---|
| G0 Environment | Exact R700 inventory, Windows build environment, repo license and CI baseline | Reproducible toolchain and safe ADB/recovery path recorded |
| G1 Contracts | Source/resource/candidate/error models and version policy | Contract tests pass; no domain-to-engine dependency |
| G2 Ingestion | M3U/TXT + one controlled TVBox JSON adapter | Valid, malformed, large and interrupted inputs tested; snapshot rollback proven |
| G3 Resolution | Search, candidate retention, HTTP(S)/HLS locator resolution | Correct provenance and safe URL policy |
| G4 Playback | Embedded engine adapter and external VLC comparison | Same authorized fixture produces reproducible R700 results |
| G5 Recovery | Injected network/source/player failures | Correct failure class; bounded recovery; no secret leak or state loss |
| G6 Repeatability | Restart, refresh, offline, duplicate import and regression suite | Repeatable end-to-end run with preserved user state |
| G7 Expansion | Additional schemas, runtimes, transports and devices | Each capability passes compatibility and security tests |

G0–G6 are required before claiming the first vertical slice complete. G7 is ongoing capability expansion.

## 11. Required test corpus

Use locally generated or explicitly licensed fixtures, never private subscription URLs:
- Valid/malformed M3U/TXT, UTF-8/BOM, alternate line endings, duplicates, missing fields, huge feeds and interrupted downloads.
- TVBox JSON fixtures for the exact supported schema, missing keys, wrong types, unknown fields, oversized nested data and unsupported schema versions.
- Local HTTP/HLS fixtures: valid master/media playlist, TS and fMP4 segments, missing segment, 404, 5xx, redirect loop, expired URL simulation, slow response, truncated manifest and network disconnect.
- Media fixtures with known hashes, codecs, profiles, tracks and license provenance.
- State tests: duplicate import, refresh rollback, process death during refresh, app restart, cache corruption and migration.
- Security tests: private-address redirects, DNS-rebinding simulation, oversized/decompression bomb, secret redaction, invalid TLS and malicious metadata.

Each fixture needs a manifest containing origin/license, hash, expected parser result, expected error class and device assumptions.

## 12. First release acceptance

Accept the first vertical slice only when:
- A clean install can add documented test sources without editing internal files.
- Sources parse and normalize to stable records with traceable provenance.
- Search returns the expected logical resource and candidate list.
- The selected candidate resolves to a valid playback request.
- R700 renders a first frame and sustains the agreed test window with measured metrics.
- A controlled failure is classified correctly and recovery is bounded and verified.
- Refresh interruption and app process death do not destroy the last-known-good catalog or user state.
- Restart and rerun produce the same expected outcome.
- Logs/reports contain no credentials or private source URLs.
- Automated tests pass in CI and the exact device/build/test record is attached.
- Known limitations and license/security review are documented.

A successful build, HTTP 200, manifest download, parser unit test or external VLC playback alone is not acceptance.

## 13. Evidence and status

A = authoritative inspected source; B = reproducible measurement with exact device/build/fixture/network/method; C = engineering inference; D = design target; U = unknown/not tested.

Current UMI status: **D — architecture and contracts are a design baseline; A — Kotlin/JVM core source adapters, provenance-preserving catalog contract and CI workflow exist; B — eleven core unit tests passed in GitHub Actions on commit a8e9151; U — Android app integration, network resolution, embedded playback, fault recovery and R700 acceptance remain unimplemented or untested.** The CI result is recorded in docs/engineering/umi-ci-baseline-2026-11-23.md.
