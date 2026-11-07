# Knowledge & Research Framework

The project must learn systematically, not collect links indiscriminately.

## 1. Research domains
1. Product experience: home, navigation, focus, search, personalization and accessibility.
2. Platform architecture: application model, runtime, system services, permissions and lifecycle.
3. Media pipeline: codecs, containers, DRM, HDR, audio formats, passthrough, subtitles and live-TV integration.
4. Device integration: HDMI/CEC, tuner, Bluetooth, USB, network, power and remote controls.
5. Performance: boot/resume, input latency, frame pacing, memory, thermal behavior and storage.
6. Reliability: updates, rollback, crash recovery, compatibility and maintenance.
7. Security/privacy: threat model, signing, sandboxing, permissions, supply chain and data minimization.
8. Developer ecosystem: SDKs, APIs, certification, app distribution and licensing.
9. AI/automation: local/cloud boundaries, consent, latency, safety and deterministic fallback.
10. Open-source landscape: maintenance health, license, architecture, issue velocity and security posture.

## 2. Source hierarchy
A — Primary evidence: official platform documentation, standards, source code, release notes, device manuals and vendor statements.  
B — Reproducible test: measurements on a named device/firmware with method, raw result and repeat count.  
C — Engineering inference: reasoned interpretation tied to A/B evidence and explicit assumptions.  
D — Design target: intended behavior or threshold not yet validated.  
U — Unknown / pending verification.

An official source does not prove a feature is available on R700. Device applicability must be assessed separately.

## 3. Research record template
For each finding record: ID; title; domain; date accessed; claim; primary source URL and exact section/version; evidence grade; platform/version applicability; license and reuse boundary; implementation options/dependencies; risks and open questions; UTOS impact (adopt/adapt/observe/reject); validation method; owner; status.

## 4. Competitor comparison
Compare capabilities, not brand reputation. Evaluate user outcome, interaction quality, technical mechanism, API availability, reliability/performance cost, privacy/advertising implications, portability, lock-in, evidence quality and implementation effort.

Do not copy proprietary code, artwork, branding, assets or distinctive UI wholesale. Learn principles and implement an original system.

## 5. Continuous learning loop
Discover → source-check → extract capability → compare alternatives → assess applicability → prototype → measure → decide → document → revisit after platform/version changes.

P0 blocks architecture, device safety or feasibility. P1 materially affects experience or portability. P2 is optimization or optional enhancement.

## 6. Decision hygiene
Use Architecture Decision Records for decisions affecting interfaces, dependencies, security, data models or platform strategy. Record context, options, decision, consequences, evidence, reversibility and revisit trigger.
