# Roadmap

Planning baseline only; not a promise of delivery dates.

## Phase 0 — Foundation
- [x] Create repository and establish evidence rules.
- [x] Define initial architecture and research taxonomy.
- [ ] Confirm governance, license and contribution policy.
- [ ] Record exact R700 model suffix, firmware/build, SoC, RAM/storage and developer access.

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

## Phase 3 — Modular services
- [ ] Capability registry and platform adapter interfaces.
- [ ] Configuration, backup/restore and diagnostics.
- [ ] Update channel, signed artifacts, staged rollout and rollback design.
- [ ] Optional content providers and player integrations with explicit source/legal checks.

## Phase 4 — Portability
- [ ] Capability matrix for Android TV/AOSP, Tizen, webOS, Roku and tvOS.
- [ ] Identify shared elements (design system, metadata, state models, tests) versus platform-specific work.
- [ ] Validate each target on real hardware or vendor-supported emulator before claiming support.

## Release gates
A milestone is complete only when requirements, test evidence, known limitations, security review, recovery plan and release notes are recorded.
