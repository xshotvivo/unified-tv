# Roadmap

Planning baseline only; not a promise of delivery dates.

## Phase 0 — Foundation
- [x] Create repository and establish evidence rules.
- [x] Define initial architecture and research taxonomy.
- [x] Define UTOS Live TV product baseline and curated GitHub ecosystem research.
- [ ] Confirm governance, license and contribution policy.
- [ ] Record exact R700 model suffix, firmware/build, SoC, RAM/storage and developer access.
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
- [ ] Capability registry and platform adapter interfaces.
- [ ] Configuration, backup/restore and diagnostics.
- [ ] Update channel, signed artifacts, staged rollout and rollback design.
- [ ] Capability matrix for Android TV/AOSP, Tizen, webOS, Roku and tvOS.
- [ ] Validate each target on real hardware or vendor-supported emulator before claiming support.

## Release gates
A milestone is complete only when requirements, test evidence, known limitations, security review, recovery plan and release notes are recorded. A specification or successful build is not proof of reliable playback.
