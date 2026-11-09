# UTOS Media Center

**Status:** Proposed module specification; not implemented or validated.
**Initial validation target:** Toshiba R700 on its existing Android 12 firmware.

## 1. Purpose and boundary

UTOS Media Center is the unified media-discovery and playback-entry module. It brings movie/series discovery, live-TV discovery handoff, metadata, favorites and viewing continuity into one consistent experience.

It is an independent service module, not the system launcher, player engine, content host or source of media rights. The home screen consumes its stable interfaces; it must remain possible to replace any provider or player without rewriting the experience layer.

## 2. Functional areas

- Catalog and discovery: normalize titles, artwork, synopsis, people, seasons and episode metadata from configured providers.
- Provider adapters: isolate each external catalog/service behind a provider contract; report availability and provenance.
- Search and filters: unified search, categories, language, year and user-defined filters.
- Personal library: favorites, watchlist and locally stored viewing progress, subject to user control.
- Playback handoff: launch an installed compatible player or supported web playback surface through an explicit handoff contract.
- Remote-first interaction: deterministic focus order, directional navigation, Back behavior, focus restoration and accessible text.
- Live-TV integration: expose channel/program metadata and hand off to the separately governed Live TV module.

## 3. Architecture and contracts

The module sits beside Live TV and App Library as a domain service. It depends on shared platform capabilities but does not own device-wide settings.

Proposed contracts:
- MediaCatalogProvider: search and retrieve normalized media metadata.
- MediaSourceProvider: describe a source, its provenance, supported playback modes and availability; never imply authorization merely because a source responds.
- MediaLibraryStore: store favorites, watchlist and progress with export/delete controls.
- PlaybackHandoff: pass a user-selected item and compatible playback intent to an approved player.
- RemoteNavigationAdapter: map remote events to accessible focus and navigation behavior.

Provider failures must not block the home screen. Unknown source status must be shown as unknown, not available or verified.

## 4. LibreTV integration decision

LibreTV may be evaluated as an optional provider/UI adapter, not embedded as a mandatory core dependency. Keep its deployment, update cycle, license obligations and content-source configuration isolated.

Before integration:
1. Verify the upstream repository, current license and notices at the time of adoption.
2. Review the exact deployment model and dependency chain.
3. Confirm configured catalogs/sources are lawful and authorized for the user's region and use.
4. Test remote focus, Back, full-screen, subtitles, audio, seeking, episode continuity and failure recovery on the target device.
5. Record measured compatibility separately from desktop/browser assumptions.

UTOS must not bundle, proxy or promote unauthorized copyrighted streams, bypass DRM, authentication or access controls, or represent third-party content availability as guaranteed.

## 5. R700 validation plan

P0 — Research: inspect upstream code, license, dependencies, security posture and source configuration.
P1 — Isolated trial: deploy independently on R700; record exact model, firmware, network and player/browser version.
P2 — Remote usability: test directional focus, select, Back, playback controls, subtitles and focus restoration.
P3 — Playback quality: test representative codecs, containers, resolutions, frame rates, audio formats and network conditions; record unsupported cases.
P4 — UTOS integration: connect through ContentProvider and PlaybackHandoff only after P0–P3 gates pass.

## 6. Quality gates and evidence

Record cold start, search latency, time-to-first-frame, seek response, playback interruption, memory use, crash/recovery and remote-navigation defects. Numeric acceptance thresholds remain TBD until a baseline and repeatable test method exist.

Use evidence labels: A = authoritative source verified; B = measured on target hardware; C = evidence-based inference; D = design target; U = unknown/not tested. Every result records date, device, firmware, software version, test conditions and limitations.

## 7. Explicit non-goals

- No assumption that LibreTV itself supplies or licenses film/TV content.
- No DRM, authentication or geographic restriction bypass.
- No dependency on one provider or player.
- No claim of native Android TV support until verified on the target firmware.
- No cloud account or telemetry requirement for basic local library functions.


## 8. Online Video Aggregation boundary

LunaTV/MoonTV-style services are optional external provider/front-end adapters, not local media servers or content providers. They must remain isolated from the local NAS library, Live TV channel catalog and shared playback engine.

- LunaTV upstream states CC BY-NC-SA; its GitHub license metadata was not identified as a standard SPDX license. Do not reuse or redistribute without reviewing the exact license and all assets/dependencies.
- Stardm0/MoonTV declares MIT and states it self-maintains from LunaTV 2.0.1. Its fork status and upstream provenance mean the MIT declaration alone does not settle every inherited component's rights.
- No built-in sources, content rights or region authorization are assumed. The user must configure lawful, authorized sources.
- Prefer private LAN/VPN deployment, explicit authentication, persistent data backup and sanitized diagnostics. Do not bypass DRM, paid access or authentication.

**Status:** Study/Hold. No service deployment, content-source configuration or R700 compatibility test is claimed.
