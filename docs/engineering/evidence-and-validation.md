# Evidence, Testing & Validation Rules

## Evidence labels
- **A — Verified source:** authoritative primary source checked directly; cite URL, version and access date.
- **B — Measured:** actual test on named hardware/firmware; preserve conditions, raw data and repeatability.
- **C — Inference:** reasoned conclusion based on A/B; state assumptions and confidence.
- **D — Target:** desired outcome or proposed threshold; not a result.
- **U — Unknown:** not checked or evidence insufficient.

Never upgrade C/D/U to A/B through wording.

## Test record minimum
Device model and suffix; firmware/build; region; connected peripherals; app/build; procedure; environment; sample count; raw observations; summary statistic; anomalies; pass/fail rule; evidence links; tester; date.

## Test layers
1. Static: schema, lint, dependency and license checks.
2. Unit: domain logic, navigation graph, configuration migration and capability negotiation.
3. Integration: adapter contracts, player handoff, settings and update flows.
4. Device: real remote, display modes, audio, HDMI/CEC, sleep/resume and network behavior.
5. Reliability: long-duration soak, crash/restart, storage pressure, interrupted update and rollback.
6. Security: permission review, dependency/SBOM scan, signature verification and threat-model review.
7. UX: task completion, navigation steps, focus visibility, legibility and accessibility.

## Safety gates
- No destructive system changes before a verified recovery path exists.
- No DRM/HDCP circumvention, credential extraction or unauthorized stream access.
- No firmware flashing without exact model/region compatibility and vendor-supported recovery evidence.
- No multi-brand support claim based only on emulator or source inspection.

## Freeze rule
Freeze interfaces and scope only after end-to-end review. Reopen for factual error, acceptance contradiction, security issue or inability to execute. Unresolved assumptions remain explicit P0/P1 validation items.
