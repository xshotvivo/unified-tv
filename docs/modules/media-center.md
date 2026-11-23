# UTOS Media Center

**Status:** Proposed module specification; not implemented or validated.  
**Initial validation target:** Toshiba REGZA 75R700SF on its existing Android 12 firmware.

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

## 4. TVOS resource-library subsystem

TVOS is the working name for the Media Center's self-owned metadata/catalog/resource-library subsystem. It is not a separate NAS operating system, parallel media database or independent player.

Responsibilities:
- stable movie/series/season/episode identity;
- title, alias, language, year, region and content classification;
- artwork, synopsis, people, subtitles and local NFO/user corrections;
- legal Provider registration and source provenance;
- search aggregation, duplicate merging and source health;
- favorites, watch history and cross-device synchronization through shared services.

Metadata availability, a catalog entry and an actual playable authorized source are three separate states. A poster or synopsis never proves that the video is present or licensed.

## 5. UTV Movie Lite experience

UTV Movie Lite is the lightweight Android TV client consuming TVOS and Media Center contracts. It is not a second media server or player engine.

Product requirements:
- TV-first, low-clutter home screen and poster wall;
- global search, filters, details, seasons and episode ordering;
- clear source provenance and user-selected playback route;
- favorites, history and resume position;
- remote-first focus navigation, Back behavior and accessibility;
- subtitle/audio selection and honest playback error recovery;
- no ads, forced recommendation feed, social stream or bundled unauthorized sources.

It reuses the shared Super Player and Device Bridge. The Movie Lite client must not embed provider-specific scraping or create a separate history/identity store.

## 6. Online Video Aggregation integration decision

LibreTV and LunaTV/MoonTV-style services may be evaluated as optional isolated provider/UI adapters, not embedded as mandatory core dependencies. Keep deployment, update cycle, license obligations and content-source configuration isolated.

Before integration:
1. Verify the upstream repository, current license and notices at the time of adoption.
2. Review the exact deployment model and dependency chain.
3. Confirm configured catalogs/sources are lawful and authorized for the user's region and use.
4. Test remote focus, Back, full-screen, subtitles, audio, seeking, episode continuity and failure recovery on the target device.
5. Record measured compatibility separately from desktop/browser assumptions.

UTOS must not bundle, proxy or promote unauthorized copyrighted streams, bypass DRM, authentication or access controls, or represent third-party content availability as guaranteed.

## 7. R700 validation plan

P0 — Research: inspect upstream code, license, dependencies, security posture and source configuration.  
P1 — Isolated trial: deploy independently on R700 only after approval; record exact model, firmware, network and player/browser version.  
P2 — Remote usability: test directional focus, select, Back, playback controls, subtitles and focus restoration.  
P3 — Playback quality: test representative codecs, containers, resolutions, frame rates, audio formats and network conditions; record unsupported cases.  
P4 — UTOS integration: connect through ContentProvider and PlaybackHandoff only after P0–P3 gates pass.

## 8. Quality gates and evidence

Record cold start, search latency, time-to-first-frame, seek response, playback interruption, memory use, crash/recovery and remote-navigation defects. Numeric acceptance thresholds remain TBD until a baseline and repeatable test method exist.

Use evidence labels: A = authoritative source verified; B = measured on target hardware; C = evidence-based inference; D = design target; U = unknown/not tested. Every result records date, device, firmware, software version, test conditions and limitations.

## 9. Explicit non-goals

- No assumption that any provider supplies or licenses film/TV content.
- No DRM, authentication or geographic restriction bypass.
- No dependency on one provider or player.
- No claim of native Android TV support until verified on the target firmware.
- No cloud account or telemetry requirement for basic local library functions.

## 10. Online Video Aggregation boundary

LunaTV/MoonTV-style services are optional external provider/front-end adapters, not local media servers or content providers. They remain isolated from the local NAS library, Live TV channel catalog and shared playback engine.

- LunaTV upstream states CC BY-NC-SA; its GitHub license metadata was not identified as a standard SPDX license. Do not reuse or redistribute without reviewing the exact license and all assets/dependencies.
- Stardm0/MoonTV declares MIT and states it self-maintains from LunaTV 2.0.1. Its fork status and upstream provenance mean the MIT declaration alone does not settle every inherited component's rights.
- No built-in sources, content rights or region authorization are assumed. The user must configure lawful, authorized sources.
- Prefer private LAN/VPN deployment, explicit authentication, persistent data backup and sanitized diagnostics. Do not bypass DRM, paid access or authentication.

**Status:** Study/Hold. No service deployment, content-source configuration or R700 compatibility test is claimed.


## Weekly client ecosystem update — 2026-11-23

- Marco Movie Android TV 0.6.2 was published 2026-11-23. Upstream release notes list fixes for ExoPlayer release-state NullPointerException and libmpv SIGABRT, Media3 fallback for MPV initialization failures/DRM-encrypted streams, subscription refresh and multi-source grouping, plus network-drive/configuration-center work. The release notes explicitly do not claim physical TV installation or long-session playback testing. Study only; no R700 compatibility claim.
- Jellyfin Android TV 0.20.0 was published 2026-11-10; minimum Android 6/API 23 and built against the Jellyfin 10.11 SDK. Playback, subtitle and external-player changes are worth a controlled trial, but Jellyfin Server 12.0 compatibility and R700 ABI/codec behavior remain unverified.
- TVBox Desktop 0.1.0 is a Windows Electron client under MIT; last visible repository commit is 2026-09-27. No verifiable code signature was found. Built-in feed/channel claims are not independent evidence or proof of content authorization.
- CatVodTVOfficial/TVBoxOSC could not be read at the canonical path; forks and aggregators must not be treated as the official upstream.
- TVOS remains a UTV-owned metadata/resource catalogue, Movie Lite remains the lightweight discovery UI, Provider remains the lawful source adapter and Super Player remains the playback owner. No new code or deployment was performed.


## Weekly client ecosystem update — 2026-11-23

- Marco Movie Android TV 0.6.2 was published 2026-11-23. Upstream release notes list fixes for ExoPlayer release-state NullPointerException and libmpv SIGABRT, Media3 fallback for MPV initialization failures/DRM-encrypted streams, subscription refresh and multi-source grouping, plus network-drive/configuration-center work. The release notes explicitly do not claim physical TV installation or long-session playback testing. Study only; no R700 compatibility claim.
- Jellyfin Android TV 0.20.0 was published 2026-11-10; minimum Android 6/API 23 and built against the Jellyfin 10.11 SDK. Playback, subtitle and external-player changes are worth a controlled trial, but Jellyfin Server 12.0 compatibility and R700 ABI/codec behavior remain unverified.
- TVBox Desktop 0.1.0 is a Windows Electron client under MIT; last visible repository commit is 2026-09-27. No verifiable code signature was found. Built-in feed/channel claims are not independent evidence or proof of content authorization.
- CatVodTVOfficial/TVBoxOSC could not be read at the canonical path; forks and aggregators must not be treated as the official upstream.
- TVOS remains a UTV-owned metadata/resource catalogue, Movie Lite remains the lightweight discovery UI, Provider remains the lawful source adapter and Super Player remains the playback owner. No new code or deployment was performed.
