# Roadmap

Planning baseline only; not a promise of delivery dates.

## Phase 0 — Foundation
- [x] Create repository and establish evidence rules.
- [x] Define initial architecture and research taxonomy.
- [ ] Confirm governance, license and contribution policy.
- [ ] Record exact R700 model suffix, firmware/build, SoC, RAM/storage and developer access.
- [ ] Define module ownership, interface versioning and compatibility policy.

## Phase 1 — R700 capability and risk baseline
- [ ] Inventory launcher/default-home behavior, package manager, permissions and ADB availability.
- [ ] Map display/HDR, audio passthrough, HDMI-CEC, tuner, DRM/HDCP, network, Bluetooth/USB and OEM update dependencies.
- [ ] Capture cold boot, resume, app launch, focus-navigation latency, memory and crash baselines.
- [ ] Define a reversible recovery procedure before system-level changes.

## Phase 2 — Experience prototype
- [ ] Remote-first home screen, deterministic focus graph and accessible typography.
- [ ] App library, favorites, recent items and configurable content rows.
- [ ] Ambient/screen saver with explicit resource and privacy controls.
- [ ] Settings and device-control surfaces limited to supported APIs.
- [ ] Measure startup, frame pacing, memory, input latency and navigation errors.

## Phase 3 — Independent domain modules
- [ ] Define and test stable contracts for Media Center, Live TV and App Library.
- [ ] Evaluate LibreTV as an optional isolated adapter after license, security and content-source review.
- [ ] Validate remote navigation, playback handoff, codecs, subtitles and recovery on R700.
- [ ] Add local library, watchlist and viewing-progress behavior with user controls.

## Phase 4 — UTOS AI Agent
- [ ] Complete threat model, data-flow map, permission matrix and tool registry.
- [ ] Build offline/read-only intent and search prototype.
- [ ] Add allowlisted app launch and low-risk controls.
- [ ] Add confirmation-gated persistent actions only after security and usability review.
- [ ] Validate cancellation, failure recovery, privacy and degraded/offline behavior.

## Phase 5 — Platform services and portability
- [ ] Capability registry and platform adapter interfaces.
- [ ] Configuration, backup/restore and diagnostics.
- [ ] Update channel, signed artifacts, staged rollout and rollback design.
- [ ] Capability matrix for Android TV/AOSP, Tizen, webOS, Roku and tvOS.
- [ ] Validate each target on real hardware or vendor-supported emulator before claiming support.

## Release gates
A milestone is complete only when requirements, test evidence, known limitations, security review, recovery plan and release notes are recorded.
