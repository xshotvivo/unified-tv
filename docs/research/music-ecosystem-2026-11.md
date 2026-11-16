# UTOS Music Center — Monthly Ecosystem Review

**Checked:** 2026-11-16  
**Scope:** AlgerMusicPlayer, Folia upstream and ports, Android TV music clients, server-backed clients, Aerial ambient experience and audio-fidelity boundaries.  
**State:** Research only. No code downloaded/executed, no APK installed, no dependency adopted.

## 1. Executive conclusion

1. AlgerMusicPlayer remains a useful desktop architecture reference, not an Android TV/mobile foundation. Its owner explicitly closed the mobile/TV maintenance path on 2026-10-07 because the public API service used by those clients was blocked; desktop Windows/macOS/Linux remains maintained.
2. Folia is the strongest candidate for lyric-centric visual experience and a modular provider model. Upstream stable v0.7.28 was released 2026-11-11; a Cielo/Folium 2 preview appeared 2026-11-14, with source modules moving to a preview interface. AGPL-3.0 requires a separate reuse/distribution review.
3. miranda031/folia-android-tv is a non-upstream TV port based on Folia and whycfawa/folia-android. Its README advertises v0.7.25-tv.14, minSdk 24 and D-pad-oriented navigation. This is a project claim and source-level review only; no R700 install or performance measurement exists.
4. Finamp has moved to the finamp-app organization. The visible release line includes beta 1.0.1; it is a Jellyfin music client reference, not a verified Android TV remote-first app.
5. Aerial TV Launcher and the commercial Aerial TV product are different projects. The launcher is Android TV desktop/ambient software; the product is a multi-platform IPTV/media player. Do not mix their licenses, features or deployment models.
6. No candidate has demonstrated a measured bit-perfect path on the R700. File-format support, high-resolution settings and bit-perfect output remain separate evidence categories.

## 2. Candidate matrix

| Candidate | Latest evidence checked | Platforms / distribution | License / risk | UTOS disposition |
|---|---|---|---|---|
| AlgerMusicPlayer | package.json 5.1.0; recent commits through 2026-11-09; official Releases page | Desktop Windows/macOS/Linux; Web build; Android/mobile/TV no longer maintained | MIT repo license; third-party music APIs/plugins and account credentials require separate review | Study: Provider Registry, queue/history, desktop lyrics and local service patterns |
| Folia upstream | Realeco v0.7.28 on 2026-11-11; Cielo/Folium 2 preview on 2026-11-14 | Desktop/web distribution; official repository releases | AGPL-3.0; source-module API preview, plugin trust and third-party provider rights | Study: lyrics, visualizer, themes, modular source architecture |
| Folia Android TV | README advertises v0.7.25-tv.14; last repository commit observed 2026-11-04 | Android TV/Google TV/projector; APK distribution linked from repo | AGPL-3.0; no independent APK signature/ABI validation in this run | Study: D-pad/focus, 10-foot UI, lyrics stage; no R700 compatibility claim |
| Folia Android phone port | Public non-official port; AGPL-3.0 | Android APK/build from source; no formal release verified in this pass | Upstream license is not automatically sufficient for every dependency or modification | Hold for TV implementation; study only |
| Finamp | finamp-app organization; beta 1.0.1 release line and beta update history | Android/iOS client for Jellyfin music server | Open-source client; beta migration/build stability needs review | Study: Jellyfin music server integration, library sync, mobile UX |
| Aerial TV Launcher | Aerial 1.98 released 2026-11-15 | Android TV launcher/ambient layer | MIT project plus separately licensed assets/fonts | Study: ambient/desktop integration only |
| Aerial TV product | Official website checked 2026-11-16 | Apple TV, iPhone/iPad, Mac and Android; commercial product | Closed-source service, paid tiers, cloud account and provider terms | Study: QR setup, cross-device sync, EPG, profiles and handoff UX |

Official links:
- AlgerMusicPlayer: https://github.com/algerkong/AlgerMusicPlayer
- AlgerMusicPlayer Releases: https://github.com/algerkong/AlgerMusicPlayer/releases
- AlgerMusicPlayer mobile/TV maintenance decision: https://github.com/algerkong/AlgerMusicPlayer/issues/228
- Folia upstream: https://github.com/chthollyphile/folia-major
- Folia Releases: https://github.com/chthollyphile/folia-major/releases
- Folia Android TV: https://github.com/miranda031/folia-android-tv
- Folia Android phone port: https://github.com/Timingyyds/folia-android
- Finamp: https://github.com/finamp-app/finamp
- Aerial TV Launcher: https://github.com/pengchujin/aerial-tv
- Aerial TV product: https://aerialtv.app/

## 3. AlgerMusicPlayer

### Verified upstream facts

- Repository license is MIT.
- package.json identifies version 5.1.0 and Electron/Electron Builder build targets for Windows, macOS and Linux.
- The README distinguishes desktop application use from the development workflow. npm install + npm run dev starts a development build; it is not a signed production installer.
- The project describes playlists, history, favorites, lyrics, remote control, music-source parsing and a local service.
- Recent repository commits show a Provider Registry refactor on 2026-10-25 and web playback/lazy database initialization fixes on 2026-11-09.
- The owner closed the mobile/TV issue on 2026-10-07, stating that Android/iOS/tablet/car/TV clients are no longer maintained because the public API service used by those clients was blocked. Desktop clients remain maintained.

### Engineering interpretation

The Provider Registry is a useful architectural pattern: built-in providers and plugin providers participate through a shared resolution contract. The plugin system and local media proxy need threat modeling; a plugin is executable code, not merely a URL entry.

Do not build UTOS Android TV around the Alger mobile branch. Do not assume a public music API is stable or that its content can be downloaded/re-distributed. Do not claim lossless or bit-perfect output based on the app's feature list.

**Disposition:** Study desktop architecture; Hold mobile/TV adoption.

## 4. Folia upstream and Android TV port

### Upstream change

Folia's v0.7.28 release (2026-11-11) includes visual/lyric and playback refinements. A Cielo/Folium 2 preview (2026-11-14) says built-in sources are being moved into source modules and the folium:2 interface is preview-stage. The project also describes a new “scene” experience in which the playback view becomes a visual room with lyric styles and background presentation.

### License and integration risk

The upstream repository is AGPL-3.0. The Android phone port and Android TV port each publish their own AGPL-3.0 license file, but this does not replace review of upstream code, third-party dependencies, assets, modifications and distribution obligations. This report is not legal advice.

### Android TV port

The miranda031/folia-android-tv README describes:
- a native Android TV host with LEANBACK_LAUNCHER entry;
- a multi-level spatial D-pad focus engine;
- album-wall navigation and remote-accessible command palette;
- themes and lyric/visualizer presentation;
- hardware detection and render-mode selection;
- MediaSession and physical media-key integration.

The README advertises minSdk 24. Android 12 has a higher API level, but this does not establish compatibility with the Toshiba firmware, ABI, vendor WebView, GPU or media stack. Claims such as “hours without lag” are not independent measurements and are not accepted as test evidence.

**Disposition:** Study visual and navigation patterns; Trial only in a disposable Android TV environment after license, APK signature, ABI and permission review; not Adopt.

## 5. Finamp and self-hosted music

Finamp moved to the finamp-app GitHub organization in 2026. The release page includes beta 1.0.1 and its notes warn about a startup regression for users updating from 0.9.25. This demonstrates why update channel and migration state need explicit handling.

Finamp is useful as a client reference for a user-owned Jellyfin music library. Android TV D-pad navigation, background behavior and R700 compatibility are not established by the mobile release notes. Do not treat an Android phone APK as a TV-ready package.

Potential UTOS relationship:
- Jellyfin/Emby/Navidrome remain replaceable server adapters.
- Music Center owns the user-facing music catalog and playlists.
- Super Player owns actual decoding/output.
- The same library should be exportable and recoverable independently of the selected server.

## 6. Aerial: two separate projects

### Aerial TV Launcher

The Android TV launcher repository released Aerial 1.98 on 2026-11-15. Release notes include device-specific fixes for standby/ADB behavior, HDMI source switching, boot-black-screen recovery and focus behavior. The maintainer explicitly says several fixes were not tested on every corresponding device; a small set of device regressions were tested. This is not R700 validation.

Use it as a reference for launcher lifecycle, HDMI control, device adapters and ambient experience, not as a music engine.

### Aerial TV product

The separate aerialtv.app site describes a commercial multi-platform IPTV/media app with QR setup, playlist/server connectors, cloud sync, EPG, profiles, sports hub, DVR and playback handoff. The site lists USD subscription/lifetime pricing. This is a product claim, not a measured comparison or an open-source component.

Use its QR pairing and synchronized session experience as a UX reference for Cloud IPTV Hub and Device Bridge. Do not conflate its app with the open-source Android TV launcher.

## 7. Audio fidelity and copyright

No candidate's marketing or settings page proves a bit-perfect output path. UTOS must trace the path from source file through decoder, resampler, system mixer, DSP/effects and output device. Record the actual sample rate and output backend. If the OS or vendor mixer cannot be bypassed, state that limitation rather than promising bit-perfect output.

For online providers:
- document provider identity, terms, account requirements and geographic restrictions;
- store credentials in a protected vault, not in plugin manifests;
- do not execute arbitrary community scripts;
- do not bypass DRM, subscription payment or authentication;
- do not download or redistribute music unless the provider explicitly permits it.

## 8. UTOS decisions

| Decision area | Proposed direction |
|---|---|
| Module ownership | Music Center is a first-level domain module |
| Player reuse | Use shared Super Player contracts; do not create a second playback engine |
| Provider architecture | Versioned Provider Registry with trust, permission and rollback controls |
| Local library | Preserve tags and user corrections; support export and backup |
| Lyrics | LRC/TTML adapters with timing confidence and user correction |
| TV UX | D-pad-first album wall, concise queue and readable lyrics |
| Ambient mode | Separate rendering budget from audio playback and launcher lifecycle |
| Audio fidelity | Measurement-based claims only |
| Cloud sync | Opt-in, conflict-aware and exportable |
| License | Keep AGPL candidates as references until reuse/distribution review passes |

## 9. P0 verification backlog

1. Inventory R700 firmware, ABI, audio route, RAM and storage.
2. Inspect Folia Android TV manifest, APK signature, source dependencies and exact build outputs.
3. Confirm whether a TV-specific release is reproducibly built from the public source commit.
4. Test D-pad, Back, lyrics timing, background playback, MediaSession, sleep/wake and memory on disposable hardware.
5. Compare local-file playback through VLC and the candidate app using the same test files.
6. Capture output route/sample rate and determine whether system SRC/mixing occurs.
7. Validate library export/restore and provider credential redaction.

**Monthly disposition:** AlgerMusicPlayer = Study (desktop only); Folia = Study; Folia Android TV = Trial candidate after security/license checks; Finamp = Study; Aerial Launcher = Study for ambient UX; Aerial TV product = Study as commercial UX reference. No Adopt or production dependency.
