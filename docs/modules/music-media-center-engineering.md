# UTV Music Media Center — Engineering Charter

**Project:** UTV Music Media Center  
**Snapshot:** 2026-11-27  
**State:** Independent engineering workstream initialized; architecture and code foundation in progress.  
**Code branch:** `feat/music-media-center-foundation`  
**Existing research baseline:** `research/2026-11-16-utv-cloud-music`  
**Existing product specification:** [Music Center](music-center.md)

## 1. Mission

Build a maintainable, modular music media system for Android TV, Android phone, Windows and later macOS/Linux. The product combines music discovery, local/private libraries, playback orchestration, lyrics, immersive presentation, Hi-Fi diagnostics, household karaoke and cross-device control.

This is an engineering program, not a claim that a working product already exists. Research, design, implementation, build, automated tests, device tests and production acceptance are separate maturity states.

## 2. Product naming and ownership

- System: **UTV Music Media Center（音乐媒体中心）**
- Flagship experience: **Music Theater（音乐媒体放映厅）**
- Independent business module: **Karaoke Center（家庭卡拉 OK 中心）**
- Theater entry into karaoke: **Karaoke Stage**

Placement: `OriginOSTV → UTV → Media & Experience Center → Music Media Center`.

## 3. Non-negotiable architecture boundaries

1. Music Media Center owns music-domain identity, catalog, library, playlists, favorites, history, lyrics, discovery and provider configuration.
2. The shared UTV Media Library owns reusable media identity/artwork/metadata primitives where appropriate; music-specific semantics remain in this domain.
3. The shared UTV Playback System / Super Player owns playback engine lifecycle, decoding, rendering, output route, common playback session, metrics and diagnostics. Music Center must not create a parallel playback engine or competing session state machine.
4. Provider adapters resolve authorized sources and return normalized source references. Provider-specific code and credentials do not leak into domain models or logs.
5. Device Bridge owns discovery, pairing, revocation and transport. Music Center supplies music commands and session projections through typed contracts.
6. AI operates only through explicit, typed, permission-checked service APIs. It cannot run arbitrary provider scripts or bypass rights/authentication.
7. Karaoke is a separate business domain sharing the music catalog, playback capabilities and audio infrastructure. It owns song request, karaoke session, vocal/instrumental association, microphone workflow and singing records.
8. Music Theater is a presentation experience over shared music and playback state, not a second playback engine.

## 4. End-to-end product flows

### A. Library-to-playback
Source discovery → provider adapter → resource resolution → canonical catalog → search/library → selected source and rights check → shared Playback System → audio output → session events → UI and device state.

### B. Music Theater
Current shared playback session → artwork/metadata/lyrics resolution → visual theme → synchronized lyrics/visualization → remote-first immersive UI. Rendering must degrade gracefully under TV memory/GPU limits.

### C. Household karaoke
Device pairing → song search/request → queue → Karaoke Session → authorized accompaniment/original-vocal asset selection → lyrics timeline → microphone capture → input processing/mixing → output → session/history. Vocal separation and scoring are optional later capabilities, not prerequisites for basic karaoke.

### D. Cross-device control
Device discovery → authenticated pairing → role/permission check → command with idempotency/session version → shared playback service → event/state snapshot → reconnect and conflict resolution.

### E. AI control
Natural-language intent → typed plan → policy/permission check → domain API call → observed result → explanation/audit. No unrestricted execution.

## 5. Initial engineering structure

```text
music-center/
  core-domain/             # canonical music-domain types and invariants
  provider-api/            # versioned source adapter contracts
  catalog/                 # indexing, identity resolution and persistence
  playback-integration/    # adapter to shared UTV Playback System
  lyrics/                  # lyrics formats, timeline and correction
  theater/                  # Music Theater presentation
  karaoke/                  # Karaoke Center workflows
  device-integration/       # Device Bridge integration
  ai-api/                   # typed, policy-gated operations
  diagnostics/              # domain diagnostics and event mapping
  test-fixtures/             # deterministic local test corpus
```

The initial code slice is deliberately limited to the canonical domain model. Package layout and language boundaries remain revisable until the existing UTV build/runtime and shared playback implementation are inspected.

## 6. Delivery sequence

| Priority | Milestone | Completion evidence |
|---|---|---|
| P0 | Existing code/spec audit and dependency map | Reviewed paths, reuse map, open gaps |
| P0 | Canonical music resource model | Source code, schema/invariant tests |
| P0 | Provider and resolver contracts | Contract tests and fake provider |
| P0 | Catalog persistence and local scan | Integration tests, duplicate/version fixtures |
| P0 | Shared playback integration | No duplicate engine/session; playback contract tests |
| P0 | End-to-end local playback | Build + automated test + repeatable playback evidence |
| P1 | Lyrics, artwork and metadata correction | Parser/timeline tests and UI evidence |
| P1 | Music Theater | TV remote/focus, rendering and resource tests |
| P1 | Phone pairing and control | Authentication, reconnect and state-conflict tests |
| P1 | NAS/self-hosted adapters | Protocol, rights, credentials and failure tests |
| P2 | Hi-Fi diagnostics | End-to-end output-path evidence; no unsupported bit-perfect claims |
| P2 | Basic Karaoke Center | Queue, accompaniment, lyrics and microphone tests |
| P2 | Advanced karaoke | Latency, mixer, vocal separation and scoring measurements |
| P3 | AI assistant and self-healing | Permission, audit, cancellation and recovery tests |
| P3 | Cross-platform hardening and release | Platform matrix, signed builds, migration and rollback |

## 7. Quality and acceptance gates

- **Build:** clean, reproducible build from a documented toolchain.
- **Domain correctness:** stable identity across sources; same title does not imply same recording/version.
- **Data integrity:** metadata corrections, playlists and history survive rescans, restart, migration and restore.
- **Playback:** shared playback contracts only; no stale or conflicting session state.
- **Stability:** long-session, repeated seek/track-change, app lifecycle, network loss and recovery tests.
- **TV UX:** D-pad focus graph, Back behavior, readable typography and graceful resource degradation.
- **Audio:** source format, decoder output, OS mixer/SRC/DSP, output route and measurement limits recorded.
- **Karaoke:** microphone latency and mixing are measured; Bluetooth latency is not assumed acceptable.
- **Security/rights:** provider permission scopes, secret handling, content rights and plugin trust review.
- **Release:** requirements linked to executable tests and evidence; known issues and rollback documented.

Numeric performance thresholds are **TBD** until reproducible baselines exist. Never convert design targets into measured claims.

## 8. Work discipline

Each iteration records:
- objective and scope;
- changed paths and commit;
- decisions and alternatives;
- exact build/test commands;
- PASS / FAIL / BLOCKED / NOT RUN status;
- logs, fixture hashes and device/firmware details where applicable;
- risks, unresolved questions and next gate.

Prefer small, reviewable commits. Keep adapters replaceable. Do not modify the repository's default branch directly for this workstream. Do not install or change anything on Toshiba R700 without an explicit device-test step and a recovery plan.

## 9. Current baseline

- Existing research and product specification are present in the research branch.
- Existing Playback System specification requires an engine-neutral shared contract and Media3-first Android implementation policy, with secondary engines conditional on measured gaps.
- Initial core-domain implementation now exists: canonical resource identity, source/availability/rights separation, technical metadata and asset references.
- Local Kotlin CLI smoke tests passed in this session using kotlinc-jvm 1.9.0 on JRE 21.0.12.1. This is not a Gradle/CI build and not a device test.
- No full application build, R700 installation, audio-output measurement or device acceptance is claimed.
- Next code slice: versioned provider/resolver contracts and fake-provider contract tests, after auditing shared playback code.
