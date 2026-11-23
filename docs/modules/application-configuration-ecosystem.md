# OriginOSTV Application & Configuration Ecosystem (OACE)

**Status:** Architecture baseline; implementation and device validation not yet claimed.
**Parent:** UNIFIED TV / OriginOSTV.
**Reference:** Apple TV / tvOS onboarding and settings experience, Android TV/AOSP capabilities, open application ecosystems.
**Principle:** Learn from Apple TV's clarity and integration, but do not reproduce a closed vendor ecosystem.

## 1. Mission

Provide one coherent, trustworthy and reversible way to discover, install, configure, operate, diagnose and maintain applications and device settings across OriginOSTV-supported devices.

Success is measured by task completion, compatibility, safety, recoverability and sustained reliability—not by application count or visual resemblance.

## 2. Scope and boundaries

OACE is a cross-cutting foundation, not a second launcher, app store backend, media player or security product. It integrates with UTV modules and Guardian through versioned contracts.

Initial validation target remains the Toshiba REGZA 75R700SF / Android 12, subject to actual retail firmware capabilities. Windows and mobile are companion clients, not assumed to share TV APIs.

OACE must not assume root, privileged system APIs, silent installation, unrestricted package management, vendor-private APIs or universal DRM/codec support. Platform capabilities are discovered and recorded per device.

## 3. Logical subsystems

1. **First-run Setup** — device capability inventory, locale/time, network, display/audio discovery, account, privacy and baseline validation.
2. **Application Catalog & Lifecycle** — discovery, provenance, compatibility, install/update/uninstall, version history and rollback where platform-supported.
3. **Unified Settings** — network, DNS, display, audio, language, accessibility, captions/subtitles, storage and power; adapters isolate platform differences.
4. **Identity & Device Hub** — device identity, user/family profiles, pairing, trust and cross-device coordination.
5. **Media Experience Profiles** — player selection, playback preferences, subtitle/audio defaults, library connections and UTV handoff.
6. **Trust & Privacy Integration** — application provenance, signature/hash records, permission inventory and Guardian policy decisions.
7. **Diagnostics & Recovery** — health checks, event timeline, backup/restore, safe repair plans and verified rollback.
8. **AI Configuration Assistant** — intent interpretation, explanations and proposed plans; privileged or destructive actions require explicit authorization.

## 4. Shared platform services

- Device Capability Registry: model, firmware, OS/API level, hardware features and verified capability evidence.
- Application Identity Registry: package/bundle ID, developer, source URL, signing identity, version and artifact hash.
- Configuration Registry: typed schema, current/desired values, source, timestamp, owner, sensitivity and schema version.
- Policy & Permission Gateway: least privilege, user consent and platform capability checks.
- Change Transaction Manager: preflight, snapshot, apply, verify, rollback and audit.
- Event & Sync Bus: versioned events, idempotency, conflict policy and offline behavior.
- Evidence Ledger: distinguish official facts, measured results, inference, design targets and unknowns.

## 5. Core data contracts

Every setting must declare:
- stable key and schema version;
- supported platform/capability predicate;
- type, allowed range/enum and default provenance;
- sensitivity and required permission;
- read/write adapter and verification method;
- backup/restore behavior;
- rollback strategy and failure mode.

Every application record must declare:
- canonical application ID and platform package ID;
- developer identity and official source;
- version, artifact digest and signing metadata when available;
- supported OS/device range;
- permissions, network behavior and known limitations;
- license and distribution constraints;
- verification date and evidence grade.

Unknown fields remain explicitly unknown; no fabricated provenance or compatibility claims.

## 6. Change safety model

All configuration changes follow:

**Discover → Explain → Preflight → Snapshot → Authorize → Apply → Verify → Commit**

On failure:

**Stop → Preserve diagnostics → Roll back when safe → Verify restored state → Report**

Non-reversible or unverified operations are blocked from unattended execution. AI is an orchestration interface, never the security boundary.

## 7. Cross-device model

TV, Windows and mobile clients share logical identities and user-approved profiles through authenticated, encrypted, versioned interfaces. Device-specific settings remain device-scoped unless explicitly portable. Sync must support offline operation, conflict visibility, revocation and audit.

## 8. Delivery phases

### P0 — Safe foundation
- Capability and application identity schemas.
- Configuration schema and evidence model.
- Transaction/rollback contract.
- First-run setup specification.
- Platform capability matrix and threat model.
- Android TV/R700 feasibility spike before any privileged implementation.

### P1 — Functional vertical slice
- Read-only device inventory.
- Read-only application inventory and provenance records.
- A small allowlisted set of reversible settings.
- Snapshot/apply/verify/rollback integration tests.
- Guardian policy integration.

### P2 — Product experience
- Application catalog and lifecycle where platform APIs permit.
- Guided setup UI and settings UX.
- Media profiles and UTV integration.
- Cross-device companion clients.

### P3 — Intelligence and hardening
- AI-assisted recommendations and approved execution.
- Automated regression, fault injection and long-duration tests.
- Accessibility, localization, upgrade and recovery validation.

## 9. Non-goals

- Copying Apple proprietary UI assets, services, APIs or closed-store restrictions.
- Circumventing DRM, subscription controls, regional restrictions or application licensing.
- Rooting or replacing the TV firmware as an assumed prerequisite.
- Automatically changing DNS, disabling packages or altering system services without explicit approval and recovery evidence.

## 10. Release gates

No capability is called implemented until its code exists. No capability is called validated until repeatable tests exist. No capability is called production-ready until device-specific acceptance, security review, recovery testing and upgrade regression pass.

See the OACE acceptance matrix and ADR-0002.
