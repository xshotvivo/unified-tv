# Apple TV / tvOS Ecosystem Research

**Status:** Research baseline; not an implementation or endorsement list  
**Last reviewed:** 2026-11-07  
**Scope:** Apple TV applications and tvOS platform behavior, compared with Android TV / Google TV and UTOS opportunities  
**Evidence:** A = official platform/developer listing or source checked; C = comparative inference; U = availability, behavior, or device result not yet verified

## 1. Purpose and platform boundary

Apple tvOS is a reference platform for focused television interaction, native media playback, app continuity, AirPlay, and ambient display experiences. It is not an Android launcher ecosystem.

- tvOS applications are distributed through the Apple TV App Store or developer testing/distribution paths supported by Apple.
- tvOS does not provide a public third-party home-launcher replacement model equivalent to Android HOME/launcher apps. UTOS must not describe CleanYourTV, Aerial TV, or FLauncher as tvOS applications.
- Apple TV home-screen icon arrangement, app installation, AirPlay, and system screen-saver settings are not equivalent to replacing the system shell.
- App availability, service catalog, subscriptions, language, and feature parity vary by Apple TV model, tvOS version, storefront region, and provider policy. Check the target storefront at installation time.

This research distinguishes **native App Store apps**, **official platform features**, **developer/beta builds**, **self-built/sideloaded projects**, and **Android-only tools**.

## 2. Five-category tvOS application map

### 2.1 Official and mainstream streaming

Candidate list (availability is storefront- and subscription-dependent):
- Apple TV app / Apple TV subscription: native Apple service entry point and ecosystem integration.
- Netflix, Disney+, Prime Video, Max (formerly HBO Max in some markets): mainstream subscription video services; check regional catalog and current app listing.
- YouTube: mainstream video client. YouTube Music does **not** provide a dedicated living-room device app according to Google's support documentation; music playback on TV-connected devices is through YouTube app / supported casting paths, not a separate tvOS YouTube Music client.
- Apple Music: native music service; lossless/spatial-audio claims must be qualified by source, device, content, output path and current tvOS/audio configuration. Do not imply every Apple TV + speaker chain delivers every format.

**UTOS lessons:** clear app entry points, account/service boundaries, resume state, consistent focus navigation, accessible metadata and predictable playback handoff. Do not hardwire commercial services or assume their APIs are open.

### 2.2 Local and private media libraries

| Candidate | tvOS status / role | What to learn | Caveat |
|---|---|---|---|
| Infuse | Native Apple TV app; broad file/container and network-library support | Library indexing, metadata, subtitle/audio selection, playback UX, direct-play/transcode decision presentation | Supported formats and HDR/audio behavior must be tested against exact Apple TV model and media sample |
| Zuno | App Store listing explicitly includes Apple TV; Plex, Jellyfin and Emby client with custom player | Native multi-server library UX, sync, profiles, playback controls, Live TV concepts | Newer product; feature/decoder claims are vendor claims until independently tested |
| Plex | Official Apple TV client | Server/client separation, metadata, continue watching, remote access UX | Server transcoding, codecs, subscriptions and account requirements vary |
| Emby | Official Apple TV client | Personal library browsing, music/photo integration, server-centric model | Feature parity and premium requirements vary |
| Jellyfin | No official tvOS client is listed on the official Jellyfin downloads page reviewed here; third-party clients exist | Open server APIs, user-controlled library, direct-play/transcode compatibility | Do not label third-party clients as official Jellyfin apps |
| Swiftfin / xfinn / other Jellyfin clients | Third-party Apple-platform clients; verify current tvOS availability and maintenance individually | Native tvOS client patterns, server compatibility, remote-first playback | Status, capabilities, licensing and release activity differ by project |
| IIVA | App Store listing includes Apple TV; supports SMB/WebDAV and selected cloud/media sources | Lightweight network file access and media browsing | Listing's visible version history is old; maintenance and compatibility are U |
| Filebar (not “Fileball”) | App Store listing currently shows Apple TV compatibility, but the developer FAQ contains older statements that Apple TV support was planned | Remote file management, network protocols, media library integration | Name and support claims conflict across sources; verify current install and actual feature behavior before recommendation |
| Leplayer | Public GitHub tvOS project, with source/build instructions; not treated as confirmed App Store distribution | M3U/IPTV, cloud drives, WebDAV/FTP, metadata, dual AVPlayer/VLC architecture | Self-build/developer signing path; audit current source, dependencies, licenses, credentials and release state before use |

**UTOS lessons:** separate media server from client, protocol adapters from playback engines, metadata from files, and source ingestion from playback validation. Keep engine-neutral interfaces. A feature matrix must identify direct play, server transcode, container, video/audio codec, subtitles, HDR, DRM and device support independently.

### 2.3 Utilities and developer tools

- TestFlight: Apple-supported beta distribution/testing for tvOS apps. A tester still needs an invitation/public link and a compatible tvOS build; each uploaded build is available for up to 90 days.
- Speedtest by Ookla: Apple TV App Store listing supports download/upload/ping and additionally advertises jitter and packet-loss measurement. The listing also indicates advertising and an in-app ad-removal purchase; do not claim all tvOS apps or utilities are ad-free.
- Other network diagnostics (for example iPerf clients): candidate tools only; distinguish internet throughput from LAN throughput and do not confuse either with actual streaming quality.

**UTOS lessons:** diagnostics should expose timestamped, repeatable measurements and explain their limits. A speed test is not a substitute for first-frame, rebuffer, packet-loss-under-load, decoder, or sustained-playback measurements.

### 2.4 Casting, browser and cross-device handoff

- AirPlay is a built-in Apple ecosystem capability for supported senders and receivers. Treat it as a system capability, not a separately installed tvOS browser.
- Apple TV does not ship with a general-purpose Safari-style browser. Third-party browser availability is constrained by tvOS public APIs and App Store rules.
- AirWeb is an iPhone/iPad app that uses AirPlay mirroring to display browsing on Apple TV; it is not a native Apple TV browser.
- tvOS Browser is a community project whose own documentation says it uses private Apple APIs and is installed by building with Xcode rather than through the App Store. Treat it as a research/sideload experiment, not a supported consumer installation path.
- “飞浏览器” and other Chinese browser/casting names require exact App Store listing, developer identity, current tvOS compatibility, and DLNA/AirPlay directionality checks. Do not infer Android-to-Apple-TV DLNA reception from a generic “casting” label.

**UTOS lessons:** model sender/receiver roles, protocols (AirPlay, Google Cast, DLNA/UPnP, Miracast), codec handoff, network discovery, permissions and DRM restrictions explicitly. Do not promise cross-protocol interoperability without a tested path.

### 2.5 Ambient display and visual customization

- Wobbly TV: App Store listing identifies an Apple TV-only generative ambient-art app and requires tvOS 26 or later.
- Dawnboard: App Store listing identifies iPhone + Apple TV support, with configurable layouts, themes, photos, calendar/countdowns, weather and music; requires current-generation OS support as stated by its listing.
- Apple system screen savers / aerial-style imagery: native platform capability; learn idle-state transitions, power/resource budgets and graceful exit to content.
- These apps do not replace the tvOS launcher or system shell.

**UTOS lessons:** ambient mode should be opt-in, privacy-aware, low-power, memory-bounded, interruptible by remote/input/HDMI events, and must never compete with active playback.

## 3. Android TV / Google TV comparison set

These are Android-platform tools and must remain in the Android column:

| Project / app | Platform role | UTOS research value | Boundary |
|---|---|---|---|
| CleanYourTV (Gathin23 upstream) | Android/Google TV ADB-assisted debloat and Apple TV-style launcher workflow | Reversible setup, backup/restore, model-specific package lists, launcher UX | Requires debugging/ADB and device-specific review; do not run on R700 without explicit user approval and verified recovery plan |
| Aerial TV | Android TV ambient/screen-saver project (verify exact upstream and current maintenance before adoption) | Ambient scene design and screensaver behavior | Not a tvOS app |
| FLauncher | Android TV launcher | Categories, app-grid navigation, focus behavior | Not a tvOS app; verify maintained fork/version rather than assuming one canonical active project |
| Android TV / Google TV launchers generally | Android HOME surface | Replaceable home experience and deep integration where OS permits | OEM restrictions and firmware behavior vary |
| Kodi / VLC / Just Player / Nova / mpv-android | Android media applications | Playback UX and format/library compatibility | App product ≠ playback engine; inspect exact engine/build before attribution |

**Important:** CleanYourTV upstream describes Android/Google TV operation and explicitly excludes non-Android TV platforms such as Apple tvOS. A launcher replacement is not equivalent to an independent operating system.

## 4. Cross-platform capability comparison

| Capability | tvOS | Android / Google TV | UTOS direction |
|---|---|---|---|
| Home shell / launcher replacement | No public third-party launcher replacement model | Possible on eligible devices, OEM-dependent | Android first; keep shell isolated from domains |
| App distribution | App Store, TestFlight, developer paths | Play Store, OEM store, sideloading subject to policy | Signed, staged, auditable distribution |
| Private media libraries | Infuse, Plex, Emby, Zuno and third-party clients | Kodi, Plex, Emby, Jellyfin, VLC and others | Provider adapters + common library model |
| Playback engine | AVPlayer and app-specific engines | Media3/ExoPlayer, VLC/libmpv or app-specific | Engine-neutral contract; Media3 first on Android |
| Live TV / IPTV | Third-party apps/projects; source rights remain user's responsibility | Broad app/player ecosystem | Dedicated Live TV module with source provenance and health checks |
| Casting | AirPlay ecosystem | Google Cast and varying DLNA support | Explicit protocol adapters; no assumed parity |
| Ambient mode | System screen saver and dedicated apps | Screensavers, launchers and ambient apps | Resource-governed ambient service |
| System modification | Highly constrained | More flexible, still OEM/security bounded | No root or destructive system changes in initial R700 phase |

## 5. Shortlist for deeper study

Priority is based on architectural learning value, not a claim that every product is best-in-class.

1. **Infuse + Zuno + Plex/Emby:** compare library UX, server integration, direct-play behavior, subtitles, metadata, and playback recovery.
2. **Jellyfin client landscape (Swiftfin, xfinn and other current clients):** compare open-server integration and third-party client maintenance; verify exact tvOS build availability.
3. **Leplayer:** study the source architecture for IPTV + cloud + media-library convergence; perform license, dependency, credential and security review before any code reuse.
4. **TestFlight + Speedtest:** reference beta distribution and on-device diagnostics; note ad/privacy behavior.
5. **AirPlay + AirWeb + tvOS Browser:** compare native handoff, mirroring and unsupported/private-API browser paths.
6. **Wobbly TV + Dawnboard + native screen saver:** compare ambient content, customization, resource use and recovery.
7. **CleanYourTV + Aerial TV + FLauncher:** Android-only comparison group for launcher, debloat reversibility and ambient behavior.

## 6. Research and adoption rules

For each candidate, record:
- Official product/repository URL, developer/maintainer, platform, minimum OS, storefront region, last checked date.
- Distribution path: App Store / TestFlight / official APK or package / source build / sideload.
- Version/release date, active maintenance, issue status and supported device range.
- Feature evidence, privacy disclosures, network protocols, codec/container support and known limitations.
- License and dependency inventory (for source projects), security advisories, secret handling and update mechanism.
- UTOS mapping: Study / Isolated Trial / Adopt after review / Hold / Reject.
- Evidence grade A/B/C/D/U. Storefront listing or vendor feature claims are A only for the fact that the vendor publishes the claim, not independent proof of performance or compatibility.

Do not infer app availability from another region's App Store. Recheck the user's storefront before installation advice. Do not sideload private-API apps as a default recommendation. Do not reuse code until the exact license and dependency licenses are reviewed.

## 7. Current source register

Checked 2026-11-07:
- Apple TV App Store installation and app availability guidance: https://support.apple.com/en-us/102348
- Apple media service regional availability: https://support.apple.com/en-asia/118205
- Infuse supported platforms and formats: https://support.firecore.com/hc/en-us/articles/360045051934-What-is-Infuse
- Zuno Apple App Store listing: https://apps.apple.com/us/app/zuno-media-player/id6759619793
- Wobbly TV Apple App Store listing: https://apps.apple.com/us/app/wobbly-tv/id6788838278
- Dawnboard Apple App Store listing: https://apps.apple.com/us/app/dawnboard/id6758313682
- IIVA Apple TV App Store listing: https://apps.apple.com/us/app/iiva/id1614231457?platform=tv
- Filebar Apple App Store listing: https://apps.apple.com/us/app/filebar-media-player/id1558391784
- Filebar developer FAQ (contains older conflicting Apple TV support statement): https://fileball.app/faq/
- Leplayer source project: https://github.com/leisurehuang/Leplayer
- TestFlight official instructions: https://beta.itunes.apple.com/
- Speedtest Apple TV App Store listing: https://apps.apple.com/us/app/speedtest-by-ookla/id300704847?platform=tv
- Google YouTube Music TV-device limitation: https://support.google.com/youtubemusic/answer/6308244
- Emby Apple TV client: https://emby.media/emby-for-appletv.html
- Jellyfin official client directory: https://jellyfin.org/downloads.html
- CleanYourTV upstream: https://github.com/Gathin23/cleanyourtv
- CleanYourTV official project site: https://cleanyour.tv/
- tvOS Browser (private API / Xcode install path): https://tvos-browser.com/
- AirWeb App Store listing: https://apps.apple.com/us/app/airweb-tv-web-browser/id770893993

## 8. Open questions / validation backlog

- [ ] Confirm the target Apple TV generation, tvOS version and App Store storefront before producing a user-facing install list.
- [ ] Confirm current availability of Netflix, Disney+, Prime Video, Max, YouTube, Apple Music and Apple TV app in the target storefront; do not assume universal catalog parity.
- [ ] Verify Filebar's current Apple TV build and reconcile its App Store compatibility with older FAQ wording.
- [ ] Verify IIVA current release cadence and real-world SMB/WebDAV/cloud playback behavior.
- [ ] Verify Leplayer current buildability, dependencies, license compatibility, secret placeholders and whether any official distribution channel exists.
- [ ] Verify current tvOS Jellyfin client options and distinguish official from third-party.
- [ ] Verify “飞浏览器” exact product identity, current tvOS listing, casting direction and protocol support.
- [ ] Test actual Apple TV direct-play/transcode, codec, HDR, audio passthrough, subtitles, network recovery and long-duration stability before performance comparisons.
- [ ] Map each Apple-side learning to a UTOS requirement, ADR or validation case; do not add apps as core dependencies merely because they are popular.

**Non-claim:** This document is a researched comparison baseline. No Apple TV device was tested, no application was installed, and no UTOS capability was implemented by writing this document.
