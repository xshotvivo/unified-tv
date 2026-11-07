# UTOS System Architecture — Draft 0.1

**Status:** Proposed; not implemented or validated.  
**Primary constraint:** R700 first phase runs on its existing Android 12 firmware unless a separately approved system-image program is created.

## 1. Layered, capability-oriented model
1. **Experience:** home, navigation, focus engine, visual system, accessibility and ambient mode.
2. **Content & Apps:** app catalog, favorites, recent items, live-TV metadata, media-provider integrations and search.
3. **Interaction & Intelligence:** remote/input routing, voice hooks where available, contextual actions and optional AI. AI cannot autonomously change device settings outside explicit user policy and consent.
4. **Device Capability:** display, audio, HDMI/CEC, tuner, network, Bluetooth, USB and power capabilities exposed through adapters.
5. **Runtime & Resources:** process lifecycle, rendering budgets, media-session coordination, memory/performance telemetry and host-supported scheduling hooks.
6. **Trust & Reliability:** permissions, privacy boundaries, secure configuration, update verification, diagnostics, recovery and rollback.
7. **Platform Adapter / HAL Boundary:** Android public APIs first; OEM/system APIs only when documented and authorized. A true OS port additionally requires framework, system services, HAL, kernel and vendor integration.

## 2. Dependency direction
Experience → domain services → capability interfaces → platform adapters. Lower layers must not depend on UI modules. Vendor-specific code must not leak into shared domain models. Optional features must degrade gracefully when a capability is absent.

## 3. Core contracts
- CapabilityProvider: reports supported, unsupported, restricted or unknown capabilities, with source/version.
- DeviceControl: exposes only controls permitted by the platform and user authorization.
- ContentProvider: supplies normalized metadata; provider failures cannot block the home screen.
- PlaybackHandoff: delegates playback to a compatible player/app; never bypasses DRM or access controls.
- ConfigurationStore: versioned settings with schema migration and export/restore.
- UpdateManager: verifies origin, signature and integrity; supports staged deployment and rollback where host permits.
- Telemetry: local-first, minimized and opt-in diagnostics; no covert tracking.

## 4. Quality attributes
Responsiveness (input-to-feedback, focus transition, frame pacing, cold/warm launch); reliability (crash-free sessions, recovery success, update failure rate); resource discipline (RAM, CPU/GPU, storage, background wakeups, thermal impact); accessibility; security/privacy; portability. Numeric targets remain TBD until R700 baselines are measured.

## 5. R700 boundary
Treat OEM firmware as the trusted host. Preserve OEM paths for panel/display processing, HDR, audio codecs/passthrough, HDMI input and CEC, DRM/HDCP, tuner, power management and firmware OTA. Root, bootloader unlock, package removal, system partition changes and firmware replacement are out of initial scope.

## 6. Deployment maturity
- L0 Research: documents, mockups and capability inventory.
- L1 User-space prototype: ordinary app/launcher using supported APIs.
- L2 Privileged integration: only with legitimate OEM/system privileges and device-specific validation.
- L3 Custom system image: separate program requiring source/BSP, signing, HAL/driver work, security maintenance and recovery infrastructure.

Never describe L1/L2 as an independent OS.
