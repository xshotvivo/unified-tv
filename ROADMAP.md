# Roadmap

Planning baseline only; not a promise of delivery dates.

## Phase 0 — Foundation
- [ ] Establish reliability engineering baseline: incident taxonomy, severity, deadlines, bounded retries, circuit breaking, safe fallback, recovery and release gates.
- [ ] Add CI/static quality gates and link every release requirement to an executable test and evidence record.
- [x] Create repository and establish evidence rules.
- [x] Define initial architecture and research taxonomy.
- [x] Define UTOS Live TV product baseline and curated GitHub ecosystem research.
- [ ] Confirm governance, license and contribution policy.
- [x] Record known first target: Toshiba REGZA 75R700SF / Android 12; capture exact firmware/build, SoC, ABI, RAM/storage and developer access in P0 inventory.
- [ ] Define module ownership, interface versioning and compatibility policy.

## Phase 1 — Live TV research and architecture closure
- [ ] Re-check shortlisted GitHub projects, releases, issue activity, security posture and exact licenses.
- [ ] Inspect Media3, libmpv/FFmpeg, Compose for TV, Room, XMLTV and network stack compatibility.
- [ ] Decide whether the first implementation should use Media3 only or a dual-engine adapter; require R700 evidence.
- [ ] Define channel/source provenance, rights review and credential protection.
- [ ] Define source health state machine, error taxonomy, retry budget and failover eligibility.
- [ ] Create UI wireframes for Live, Guide, Channels, Sources and Diagnostics.

## Phase 2 — R700 capability and risk baseline
- [ ] Inventory launcher/default-home behavior, package manager, permissions and ADB availability.
- [ ] Map display/HDR, audio passthrough, HDMI-CEC, tuner, DRM/HDCP, network, Bluetooth/USB and OEM update dependencies.
- [ ] Capture cold boot, resume, app launch, focus-navigation latency, memory and crash baselines.
- [ ] Define a reversible recovery procedure before system-level changes.

## Phase 3 — UTOS Live TV core
- [ ] Implement M3U/M3U8 parser with malformed-input and large-feed tests.
- [ ] Implement transactional channel catalog, favorites, groups and stable user ordering.
- [ ] Implement XMLTV parser, channel mapping, timezone handling and rolling EPG retention.
- [ ] Implement one playback engine behind a stable PlaybackEngine contract.
- [ ] Capture truthful first-frame, buffering, error and decoder analytics.
- [ ] Build synthetic local HLS/MPEG-TS test fixtures and source-health harness.

## Phase 4 — Live TV experience and reliability
- [ ] Implement remote-first channel list, EPG guide, player HUD and focus restoration.
- [ ] Add bounded retries, backoff, source diagnostics and controlled fallback.
- [ ] Validate large playlist performance, 50 channel changes and 2-hour playback.
- [ ] Test network loss, malformed/expired sources, codec errors and player lifecycle.
- [ ] Evaluate optional secondary engine only against measured compatibility gaps.
- [ ] Run complete R700 hardware validation before declaring support.

## Phase 5 — Experience prototype
- [ ] Remote-first home screen, deterministic focus graph and accessible typography.
- [ ] App library, favorites, recent items and configurable content rows.
- [ ] Ambient/screen saver with explicit resource and privacy controls.
- [ ] Settings and device-control surfaces limited to supported APIs.
- [ ] Measure startup, frame pacing, memory, input latency and navigation errors.

## Phase 6 — Independent domain integration and AI
- [ ] Define and test stable contracts for Media Center, Live TV and App Library.
- [ ] Evaluate LibreTV as an optional isolated adapter after license, security and content-source review.
- [ ] Complete AI Agent threat model, data-flow map, permission matrix and tool registry.
- [ ] Integrate AI only through typed, sanitized Live TV service contracts.
- [ ] Validate cancellation, failure recovery, privacy and degraded/offline behavior.

## Phase 7 — Platform services and portability
- [ ] Maintain cross-platform capability matrix and compare tvOS-native apps, Android TV launchers/players, and UTOS equivalents; use docs/research/apple-tv-tvos-ecosystem.md as the research baseline.
- [ ] Revalidate Apple TV app availability by storefront, OS version and distribution path; distinguish App Store, TestFlight, self-built/private-API and Android-only tools.
- [ ] Map useful tvOS interaction/media/ambient patterns into UTOS requirements and tests without assuming platform/API parity or copying proprietary implementations.
- [ ] Capability registry and platform adapter interfaces.
- [ ] Configuration, backup/restore and diagnostics.
- [ ] Update channel, signed artifacts, staged rollout and rollback design.
- [ ] Capability matrix for Android TV/AOSP, Tizen, webOS, Roku and tvOS.
- [ ] Validate each target on real hardware or vendor-supported emulator before claiming support.

## Phase 8 — NAS, home media and cross-device services
- [ ] Record candidate NAS hardware, CPU architecture, RAM, disk/controller topology, network and recovery path.
- [ ] Compare FygoOS/fnOS, TrueNAS and OpenMediaVault against the NAS research register; verify current edition, licensing, supported hardware and lifecycle.
- [ ] Select a disposable test environment; do not install on production storage before recovery and backup procedures are validated.
- [ ] Define storage, backup, snapshot, restore, identity/access and monitoring contracts.
- [ ] Evaluate Docker/Compose security, image provenance, least privilege, resource limits, secrets and rollback.
- [ ] Compare Jellyfin, Emby, Plex and Fygo TV as replaceable media-server adapters.
- [ ] Define metadata and artwork pipeline with local NFO/user-correction persistence and duplicate protection.
- [ ] Validate mobile, desktop and Android TV clients; verify exact Toshiba R700 ABI, firmware, D-pad navigation and codec behavior.
- [ ] Test LAN discovery, SMB/NFS permissions, VPN/proxy coexistence, remote access security and offline behavior.
- [ ] Complete disk-failure, power-loss, backup-restore, OS-update and container-rollback drills; record RPO/RTO before adoption.
- [ ] Keep vendor-specific NAS and media services optional; UTOS contracts must support migration and replacement.

## Phase 9 — Cloud Drive & Resource Sharing
- [ ] Select one primary listing/WebDAV service (OpenList or AList) after license and security review; keep rclone as an optional mount/sync engine.
- [ ] Define provider adapters, token vault, read-only default, cache quotas, source provenance and revocation.
- [ ] Test range/seek, resume, rate limits, cache eviction, token expiry and config restore in a disposable environment.
- [ ] Do not treat remote mounts or cache as backups; test independent backup/restore.

## Phase 10 — Sports Live Center
- [ ] Keep sports event, team, competition and schedule models separate from general IPTV channel catalog.
- [ ] Evaluate Bee-v1/v2, Smart IPTV Server and service-specific adapters only after source/license/SBOM/digest review.
- [ ] Verify event timezone, event-to-channel mapping, rights and user-authorized source provenance.
- [ ] Reuse the shared playback and diagnostics contracts; measure first frame and sustained playback on named hardware.

## Phase 11 — Retro Game Library
- [ ] Define legal game asset intake, metadata, emulator/runtime adapter and controller profile contracts.
- [ ] Review emulator/core licenses, BIOS requirements, supported formats and save-state portability.
- [ ] Test D-pad/controller mapping, frame pacing, audio latency, suspend/resume and save-state backup on disposable hardware.
- [ ] Do not bundle or redistribute copyrighted ROMs, BIOS or commercial game images without explicit rights.

## Phase 12 — Docker Operations & Home Network
- [ ] Inventory Windows 11 Docker Desktop/WSL2, NAS host and router model before any trial.
- [ ] Define Compose-as-source-of-truth, immutable image digests, SBOM/provenance, secrets, least privilege, health checks and resource limits.
- [ ] Evaluate DPanel only after custom EULA review and Docker-socket threat assessment.
- [ ] Validate OpenWrt/OpenClash/Mihomo compatibility on a disposable router/VM; preserve config backup and recovery path.
- [ ] Complete container data restore, image rollback and network recovery drills before production use.

## Phase 13 — Cross-device Services
- [ ] Define device identity, pairing, revocation, discovery and session synchronization contracts.
- [ ] Build protocol-specific adapters for AirPlay, Google Cast, DLNA/UPnP and local file services; do not assume protocol parity.
- [ ] Test same-LAN, VLAN, Wi-Fi isolation, sleep/wake, sender disconnect and receiver recovery.
- [ ] Validate every sender/receiver pair and media capability on real hardware before claiming support.

## Phase 14 — Cloud IPTV Hub
- [ ] Keep Cloud IPTV Hub separate from IPTV Source Engine: source semantics and health stay in the source engine; Hub owns publication, identity, auth and delivery.
- [ ] Define canonical source/channel/EPG/device schemas, provenance, rights state and token lifecycle.
- [ ] Build a deterministic pipeline: collect → normalize → dedupe → health state → EPG map → versioned M3U/TXT/JSON/XMLTV.
- [ ] Compare GitHub Actions/self-hosted ingestion against Cloudflare Workers; keep heavy scraping/probing outside latency-sensitive Workers.
- [ ] Prototype Pages + Workers + R2 + D1/KV only in a disposable account after explicit approval.
- [ ] Implement per-device QR pairing and revocation in UTV clients; preserve one-time manual URL setup for third-party players without a pairing API.
- [ ] Test ETag/cache, rate limits, token redaction, last-known-good rollback and real client refresh.
- [ ] Do not claim zero-touch player setup, video proxying or playback quality improvement without end-to-end evidence.

## Phase 15 — UTOS Music Center
- [ ] Establish Music Center as a first-level domain module reusing the shared Playback System.
- [ ] Define library, artist/album/track metadata, playlists, queue, lyrics, history, favorites and Provider Registry contracts.
- [ ] Review AlgerMusicPlayer, Folia, Folia Android TV and Finamp with license, platform, maintenance and provider-security checks.
- [ ] Implement local library and metadata correction persistence before online providers.
- [ ] Add lyrics timing and visualizer only after playback/session stability and resource budgets are measured.
- [ ] Instrument sample rate, resampling, system mixing, DSP and output route; no bit-perfect claim without measurement.
- [ ] Validate Android TV remote focus, MediaSession, sleep/wake, background audio, export/restore and long-session reliability.

## Phase 16 — TVOS Resource Library & UTV Movie Lite
- [ ] Treat TVOS as the Media Center catalog/metadata subsystem, not a second media database or player.
- [ ] Treat UTV Movie Lite as the lightweight TV discovery/browser client on shared Media Library and Super Player contracts.
- [ ] Define legal Provider adapters, media identity, aliases, language, year, season/episode, artwork, subtitles and provenance.
- [ ] Compare Marco Movie, Kodi, Jellyfin Android TV, Emby/Plex and TVBox-derived clients by remote UX, license, security and actual release lineage.
- [ ] Verify APK ABI, Android API, signature, dependencies and source before any installation.
- [ ] Test search, details, episode ordering, source switch, subtitles, history, resume, playback recovery and remote navigation on R700.
- [ ] Do not bundle unauthorized sources or infer playback rights from metadata availability.

## Release gates
A milestone is complete only when requirements, test evidence, known limitations, security review, recovery plan and release notes are recorded. A specification or successful build is not proof of reliable playback.

## Phase 3A — Source intelligence and AI-assisted operations
- [ ] Evaluate Guovin IPTV-API as an isolated source aggregation/measurement worker; review exact license and dependencies before adoption.
- [ ] Define canonical source/channel schema, provenance and per-channel multi-candidate retention.
- [ ] Implement authorized multi-source ingestion and channel alias normalization.
- [ ] Implement bounded latency/throughput sampling and resolution/frame-rate metadata extraction.
- [ ] Implement source state machine: discovered → reachable → media-valid → first-frame → sustained-sample, plus typed failure states.
- [ ] Implement M3U/TXT/API exports and transactional scheduled refresh with last-known-good rollback.
- [ ] Implement EPG merge, freshness, timezone mapping and unmapped-channel diagnostics.
- [ ] Build incident model and deterministic root-cause rules before adding AI.
- [ ] Add AI A0/A1 diagnosis/reporting; keep all mutations disabled by default.
- [ ] Add opt-in A2 retry/failover among preconfigured eligible sources, with bounded retries and post-action playback verification.
- [ ] Add user-confirmed persistent changes, audit log, rollback and sanitized reports.
- [ ] Threat-model playlist ingestion, SSRF, redirects, decompression bombs, credential handling and prompt injection from untrusted feed metadata.

## Phase 3B — Playback system and quality
- [ ] Add engine-neutral PlaybackEngine, PlaybackSession, PlaybackCapabilities, PlaybackEvent and PlaybackMetrics contracts.
- [ ] Implement Media3 as the only first-pass engine and build a local HLS/DASH/progressive regression fixture corpus.
- [ ] Design remote-first player HUD, channel zapping, track selection, subtitle controls and honest live-edge behavior.
- [ ] Capture first-frame, rebuffer, decoder, track, lifecycle, memory and thermal metrics.
- [ ] Classify failures before retry/fallback; do not use engine switching to mask source/network/auth failures.
- [ ] Review LibVLC license/dependency/build footprint and run an isolated adapter trial only if Media3 gaps are reproducible.
- [ ] Keep libmpv research-only and IJKPlayer on hold pending maintenance/security review.
- [ ] Compare engines on identical R700 firmware, fixtures, network and repeatable test procedure before any performance claim.

## Phase 3C — Codec compatibility and decode performance
- [ ] Capture Toshiba R700 MediaCodec/Codec2 decoder inventory and supported profiles/levels.
- [ ] Build a local codec fixture matrix for H.264, HEVC, VP9 and AV1; include profile, bit depth, chroma, resolution, frame rate and HDR metadata.
- [ ] Verify Media3 platform decoder selection and optional AV1 software extension separately.
- [ ] Measure sustained FPS, dropped frames, first frame, A/V sync, CPU, RAM, temperature and power.
- [ ] Research VVdeC only as a VVC software candidate; do not assume 4K real-time on R700.
- [ ] Track AV2 v1.0.0, AVM and dav2d conformance/performance; keep AV2 out of production defaults until hardware/software maturity is proven.
- [ ] Do not disable mandatory codec tools to force playback; use compatible renditions or report unsupported.
- [ ] Reassess codec support, decoder libraries, security and license state during weekly ecosystem reviews.

## Weekly ecosystem baseline — 2026-11-16
- [x] Correct Guovin IPTV-API to v3.0.0 and IPTV Checker to v5.0.2 using official release pages.
- [x] Add Cloud IPTV Hub architecture and Cloudflare limits/one-time pairing constraints.
- [x] Preserve the user-confirmed China mainland IPTV V1.0 source baseline; record new source-file freshness without claiming playback.
- [x] Add Music Center as a first-level module and complete the November music ecosystem review.
- [x] Add TVOS/Movie Lite boundaries and review Marco Movie, TVBox-derived clients and Aerial/Folia distinctions.
- [x] Update Mihomo upstream to v1.19.34 and record OpenClash issue evidence.
- [ ] P0 remains: exact R700 capability inventory, authorized playback fixtures, repeatable player logs, disposable service trials and restore tests.
