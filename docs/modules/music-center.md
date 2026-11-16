# UTOS Music Center

**Snapshot:** 2026-11-16  
**State:** Research baseline and architecture proposal. Not implemented, installed or device-tested.  
**Placement:** Independent first-level module within UTV/UTOS. It shares playback, metadata, identity and cross-device contracts but does not duplicate the Super Player or Media Library.

## 1. Purpose

Provide one music experience across TV, Windows PC and Android phone, with future macOS/Linux and self-hosted/NAS adapters. The module covers local music, authorized streaming services, private music servers, playlists, lyrics, history, metadata and synchronized playback state.

The TV experience is designed for remote control and large-screen album/artist browsing. Desktop and phone clients may expose richer editing and library-management tools. Basic local playback should remain usable without a cloud account.

## 2. Responsibility boundaries

- **Music Center:** music catalog, artists/albums/tracks, local library scan, tags, playlists, favorites, queue, listening history, lyrics, discovery and provider configuration.
- **Media Library:** shared file identity, artwork and metadata persistence where movie/series/music entities can share generic media primitives.
- **Super Player:** actual audio/video engine, output route, buffering, decoder lifecycle, playback state machine and diagnostics.
- **Provider Registry:** connectors to authorized music services, local folders and self-hosted servers. Provider code is isolated and permission-scoped.
- **Device Bridge:** device discovery, remote control, handoff and cross-device session sync.
- **AI Assistant:** optional natural-language search, playlist organization and diagnostics through explicit APIs; no unrestricted source-script execution.

Music and video should share playback contracts, not be forced into one UI model or one metadata schema.

## 3. Core submodules

1. Library scanner: local files, removable media and approved network shares; incremental scan, duplicate detection and cancellation.
2. Metadata: stable track/album/artist IDs, tags, multi-disc ordering, artwork, release date, language, composer and user corrections.
3. Playback queue: queue persistence, shuffle/repeat, gapless capability, replay gain/normalization policy and history.
4. Lyrics: LRC/TTML parsing, time alignment, translation/romanization, word-level timing and user correction.
5. Provider registry: built-in providers and external adapters behind a versioned interface; per-provider permissions, credentials and health.
6. Remote control: Android MediaSession, Windows media keys, TV remote, phone companion and handoff.
7. Visual experience: album wall, full-screen lyrics, audio visualizer and ambient display with explicit resource budgets.
8. Sync: user-controlled playlists, favorites, progress and device session synchronization with conflict handling.
9. Diagnostics: playback engine, sample rate, output route, underruns, focus/remote events, memory and failure reason.

## 4. Audio fidelity evidence model

Do not label a player “Hi-Res” or “bit-perfect” merely because it can open FLAC/ALAC/WAV or displays a high sample-rate setting.

For each playback path, record:
- Source file codec, sample rate, bit depth, channel count and decoded format.
- Decoder output format and any resampling stage.
- Android AudioTrack/AAudio or desktop output backend and its configured format.
- Whether system mixing, SRC, DSP/EQ, volume normalization, spatial audio or vendor sound effects are active.
- Output device and connection type.
- Any available loopback or external measurement and its limitations.

File-format support, configured output rate and bit-perfect output are different claims. A measurement is required before a bit-perfect statement is allowed. On TV speakers or OEM audio paths, the OS/vendor pipeline may remain outside app control.

## 5. Plugin and source safety

- Unknown source scripts are not auto-run.
- Provider adapters use a documented manifest, version, permission list and integrity hash.
- Credentials are stored outside plugin-readable configuration and never written to logs.
- Network access is scoped; local-network access requires explicit permission.
- Plugins are disabled by default until reviewed; updates can be pinned and rolled back.
- Do not bypass DRM, account authentication, subscription payment or copyright restrictions.
- Public API availability is not assumed to grant rights to redistribute audio or download content.

## 6. Cross-platform targets

| Platform | Primary UX | Playback/metadata evidence needed |
|---|---|---|
| Android phone | touch, headset, Bluetooth, offline | MediaSession, AudioTrack route, focus, SRC and background lifecycle |
| Android TV | D-pad, album wall, large lyrics, physical media keys | Leanback/TV intent, focus graph, MediaSession, wake/sleep and memory |
| Windows 11 | keyboard, media keys, local library, desktop lyrics | packaged desktop build, output backend, update/signing and local data recovery |
| macOS/Linux | optional desktop client | official build, output backend, signing/package and server compatibility |
| NAS/self-hosted | library and streaming server | user authentication, transcoding policy, remote access, backup and metadata export |

No cross-platform feature is considered supported until its platform-specific build and interaction path are tested.

## 7. November 2026 candidate disposition

- AlgerMusicPlayer: useful desktop architecture reference for Provider Registry and local service integration; desktop only. The project owner stated on 2026-10-07 that Android/iOS/tablet/car/TV clients are no longer maintained because their public API service was blocked. Do not use it as the Android TV or mobile foundation.
- Folia upstream: the folia-major release page lists Realeco v0.7.28 (2026-11-11), Cielo Canary / Folium 2 preview (2026-11-14) and Limo nightly (2026-11-15). It is a strong lyrics, visualizer and provider-modularity reference. AGPL-3.0; the Folium 2 source-module API is preview-stage. Do not copy code without a license review.
- Folia Android TV fork: TV-specific remote/focus/album-wall reference. Its README claims minSdk 24 and provides an APK link, but the hardware-adaptive and smoothness claims are upstream claims, not R700 test evidence. AGPL-3.0.
- Finamp: Jellyfin-backed mobile music client. Its maintainers moved to the finamp-app organization; the current release line includes beta builds. Android TV D-pad and R700 support remain unverified.
- Aerial TV Launcher: Android TV launcher/ambient reference only; it is not a music engine.
- Aerial TV product: commercial multi-platform media/IPTV application with QR setup and cloud sync; UX reference only, not open source.

Detailed monthly report: ../research/music-ecosystem-2026-11.md

## 8. R700 / Android TV 12 validation plan

P0 — inventory exact Toshiba REGZA 75R700SF firmware/build, ABI, SoC, memory, storage and available audio output routes.  
P1 — inspect source, manifest, signing, license, dependencies, permissions and API requirements.  
P2 — build or obtain a verified package only after approval; do not install during research-only runs.  
P3 — test install/start, D-pad focus, Back, queue, lyrics, media keys, background playback, sleep/wake, network loss and crash recovery.  
P4 — measure memory, CPU, frame pacing, audio underruns, output route and long-session stability.  
P5 — test restore/export of playlists, metadata corrections and user library.

No R700 installation or playback test is claimed in this snapshot.

## 9. Acceptance criteria

- Music metadata and user corrections survive rescans and provider changes.
- Queue/history/favorites survive app restart and backup restore.
- Provider failure cannot corrupt local library or playback state.
- Remote navigation has no focus traps and is usable without touch.
- Lyrics timing and visual effects remain within a measured resource budget.
- Audio fidelity statements are supported by the actual end-to-end output path.
- Every provider has documented rights, terms, credential handling and failure behavior.
- The player engine can be replaced without rewriting Music Center UI or metadata.
