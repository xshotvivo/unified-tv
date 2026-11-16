# UTV / Unified TV — Consolidated Weekly Intelligence

**Checked:** 2026-11-16  
**Scope:** IPTV and Cloud IPTV Hub, source projects, proxy subscription conversion, tvOS, NAS/media servers, player reliability, Music Center, Folia/Aerial, Movie Lite and TVBox ecosystem.  
**Repository state before update:** PR #1 is merged; PR #2 remains an open draft for Super Player/reliability work. This report is added on a separate branch and is not merged to main.  
**Evidence labels:** A = official source or project record; B = repeatable measurement on named hardware; C = evidence-based inference; D = design target; U = unknown/not verified. Upstream claims are not independent measurements.

## 1. Main conclusions

1. **Cloud IPTV Hub is feasible as a publication/authentication layer, not as a universal zero-touch player configurator.** UTV clients can implement QR pairing and per-device sync. Existing players still need a remote M3U/XMLTV URL or a supported configuration import. No generic player-side pairing API has been demonstrated.
2. **Keep the IPTV Source Engine and Cloud IPTV Hub separate.** Guovin/IPTV Checker handle source collection and validation; Cloudflare handles artifacts, API/auth, caching and device delivery. Cloudflare Pages/R2 do not transcode or improve the live video stream.
3. **Correct the IPTV version baseline.** Guovin IPTV-API latest stable checked is v3.0.0 (2026-08-07), not 3.1.0. IPTV Checker latest release checked is v5.0.2 (2026-09-25), not 4.7.1. Guovin v3.0.0 requires Python 3.14 and changes configuration defaults.
4. **Community source files are fresh enough to inspect, not enough to certify playback.** best-fan CCTV/province status lists show 2026-11-15 metadata; all-status shows 2026-11-13. No individual HLS playback or sustained stream test was performed.
5. **Mihomo upstream moved to v1.19.34 on 2026-11-11.** OpenClash 0.48.002 remains associated with several open issue reports; one recent report describes a private-route/throw-route conflict. Do not apply community workarounds to the user's router without matching firmware and reproduction.
6. **SubConverter-Extended v1.11.5** was released 2026-11-13 with parser/rule-output fixes. This is not evidence that any converted node connects faster or more reliably. No controlled performance or protocol matrix test was run.
7. **tvOS client update:** Jellyfin Swiftfin 1.7.0 released 2026-11-11 and added/updated Live TV guide, player options including mpv and SwiftVLC, tvOS tab placement and several playback/UI fixes. It remains a tvOS app, not an Android TV client.
8. **Movie Lite research gained a concrete TV candidate:** Marco Movie Android TV 0.5.25 was released 2026-11-15. The official release describes a 32-bit TV/box APK, 6-way range proxy, JAR/QuickJS compatibility and DoH fallback. The release's own offline tests and signature checks are upstream claims; no R700 install or playback test was done.
9. **TVBox identity needs care.** The original CatVodTVOfficial/TVBoxOSC repository is no longer available at that URL. j4Uq/TVBoxOSC is an automated-build/aggregation fork; other forks and source/config repositories exist. hgtuj5688/tvbox-desktop is a separate Windows Electron client with an MIT license and v0.1.0 release. Do not combine them as one project or adopt bundled public source lists.
10. **Music Center becomes a first-level UTV module.** AlgerMusicPlayer's desktop code is a Provider Registry reference, but its owner states Android/iOS/TV clients are no longer maintained. Folia's provider-module interface is preview-stage and AGPL-3.0. Neither is a ready-made R700 production dependency.
11. **No new player incident can be ruled out.** No new R700/VLC/MyTV logs or controlled playback measurements were supplied in this run. The absence of a new report is not evidence of no faults. The known MyTV freeze and VLC baseline plan remain open.
12. No user device, NAS, disk, router, Apple TV or production configuration was changed. No APK was installed, no image/container was pulled or run, and no service was deployed.

## 2. IPTV source health and Cloud IPTV Hub

### 2.1 Current public source snapshot

| Source | Current repository evidence | Result and limitation |
|---|---|---|
| best-fan/iptv-sources cn_cctv_status.m3u8 | GitHub file metadata and playlist header show 2026-11-15 | File retrieval succeeded; no individual stream playback |
| best-fan/iptv-sources cn_province_status.m3u8 | GitHub file metadata/header show 2026-11-15 | File retrieval succeeded; no completeness or playback claim |
| best-fan/iptv-sources cn_all_status.m3u8 | Header shows 2026-11-13 | File retrieval succeeded; visible repeated channel labels/variants need canonical dedupe |
| hujingguang/ChinaIPTV cnTV_AutoUpdate.m3u8 | File exists in repository | Retrieved raw content was cached about two months ago; current freshness/availability not confirmed |

Official repositories:
- https://github.com/best-fan/iptv-sources
- https://github.com/hujingguang/ChinaIPTV

The user's previously confirmed China mainland IPTV V1.0 baseline remains the preferred source set. New community entries are candidates only and must not overwrite the confirmed baseline.

No channel-count audit, HTTP status sweep, latency sample, HLS segment validation or real playback was performed. Community response-time attributes are upstream metadata, not measurements from the user's network. No success rate or “low-latency” ranking is published.

### 2.2 Guovin IPTV-API

Official release page: https://github.com/Guovin/iptv-api/releases

- Latest stable release observed: v3.0.0, 2026-08-07.
- Python minimum moved to 3.14.
- New PySide6 desktop UI for Windows/macOS; old Tkinter UI is retained only for compatibility.
- Adds channel/source management, import/export, diagnostics, screenshots, RTMP monitoring/control and status API.
- New settings include performance/speed-test modes, output URL limit, service_port and public_url.
- Default behavior changed: subscription EPG/logo/header extraction is enabled; automatic source disabling is disabled.
- Release notes recommend recreating the Pipenv environment with Python 3.14 and reviewing config/config.ini.
- The generated playlist workflow shows a latest manually generated playlist dated 2026-09-28.

Impact: do not update an existing environment blindly. First pin Python 3.14, compare config defaults and Docker image/entrypoint, preserve user config and test in a disposable environment. No Windows 11/Docker install was executed in this run.

### 2.3 IPTV Checker

Official release page: https://github.com/zhimin-dev/iptv-checker/releases

- Latest release observed: v5.0.2, 2026-09-25.
- The release note itself is sparse; do not infer undocumented features from the version number.
- The repository README describes Docker and CLI usage for M3U/M3U8/TXT validation and a Web management interface.

Impact: keep as a candidate health-check worker. Before trial, inspect v5.0.2 source diff, image digest, license, dependency tree, output schema and probe semantics. A TCP/HTTP response or playlist parse is not proof of first-frame or sustained playback.

### 2.4 Cloud IPTV Hub architecture decision

Proposed topology:
- GitHub: source code, versioned schemas, CI, build/test workflow and immutable change history.
- GitHub Actions or a self-hosted worker: scheduled source collection, normalization, dedupe, EPG mapping and bounded media probes.
- Cloudflare Pages: management console/static UI.
- Cloudflare Workers: device authentication, subscription API, current-version manifest, ETag/cache control, revocation and rate limiting.
- Cloudflare R2: immutable playlist and XMLTV snapshots.
- Cloudflare D1 or another relational database: source/channel/EPG/device/audit records.
- Cloudflare KV: hot cache and current-version pointers only, not the transactional source of truth.

Official limits checked 2026-11-16:
- Workers Free: 100,000 requests/day, 10 ms CPU/request, 50 subrequests/request, 128 MB memory and 5 Cron Triggers/account. Paid Workers minimum is US$5/month.
- KV Free: 100,000 reads/day, 1,000 writes/day, 1 GB storage, 25 MiB value maximum.
- Pages Free: 20,000 files/site, 25 MiB/file, 500 builds/month.
- R2 Standard Free: 10 GB-month, 1 million Class A and 10 million Class B operations/month; Internet egress is free. Operations and storage beyond free allowance are billed.

Official docs:
- https://developers.cloudflare.com/workers/platform/limits/
- https://developers.cloudflare.com/workers/platform/pricing/
- https://developers.cloudflare.com/kv/platform/limits/
- https://developers.cloudflare.com/pages/platform/limits/
- https://developers.cloudflare.com/r2/pricing/

Inference: Cloudflare is appropriate for lightweight API/auth and artifact delivery. It is not the right place for full feed crawling, large playlist dedupe, FFmpeg probing or transcoding. Static hosting distributes stream references; it does not proxy or improve the upstream media stream.

### 2.5 Player-side truth

Kodi PVR IPTV Simple documents M3U/M3U8 and XMLTV inputs. That is a remote-source capability, not a pairing API. VLC can consume playlists/URLs but does not provide a unified cloud EPG/device identity layer. TiviMate and other players must be evaluated against documented playlist refresh/configuration behavior; no universal remote configuration API is assumed.

Therefore:
- UTV clients: QR pairing and per-device credentials are an implementation requirement.
- Third-party players: one-time manual URL setup remains the fallback.
- If a player cannot fetch a remote URL or import a supported configuration, automatic sync is not available.

## 3. IPTV related projects and source alternatives

| Project | Current disposition | Next check |
|---|---|---|
| Smart IPTV Server | Hold; source/license/image provenance not fully established | Verify repository identity, custom license, digest, SBOM and secret model |
| IPTV-4GTV / 4GTV-v1 | Hold | Verify authorized service relationship, auth model, source and deployment contract |
| Bee-v1 / Bee-v2 | Hold | Verify source, license, image freshness, sports rights and resource usage |
| DPanel 1.11.2 | Study/Hold | Review custom EULA, Docker socket privileges and rollback semantics |
| LunaTV / MoonTV | Isolated Study/Hold | Keep provider boundary and verify license/dependencies/content-source legality |
| Selene-TV 1.4.6 | Study only | Android TV API/ABI/signing/license review; no R700 test |
| TVGO / VsTV | U | Exact official repository and distribution identity not established in this pass |

Do not install or run these candidates as part of research.

## 4. Proxy subscription conversion

Official project pages:
- https://github.com/tindy2013/subconverter
- https://github.com/lonelam/subconverter-rs
- https://github.com/Aethersailor/SubConverter-Extended/releases

Current observed upstream change:
- SubConverter-Extended v1.11.5 released 2026-11-13.
- Fixes malformed SSD traffic metadata handling, SingBox catch-all rules, libcurl write callback compatibility and line-boundary/source handling.
- It improves parser/output robustness, not node RTT, throughput or availability.

The original subconverter and Rust implementation have not shown a newer verified release in this pass than the existing baseline. Their precise latest tag and protocol matrix remain to be rechecked before any adoption.

No controlled benchmark was run. VLESS Reality, Vision, XHTTP, Hysteria2, Shadowsocks and UDP/XUDP/TFO support must be verified separately at three levels: documentation, source implementation and target-client load/connection. Conversion time is not node connection quality.

Do not submit real user subscriptions to public conversion services. UUIDs, passwords, tokens and Reality parameters are credentials.

UTV mapping: reuse generic parsing, normalization, template, versioning and publication patterns only. Proxy node YAML and IPTV M3U/XMLTV remain separate data models.

## 5. Apple TV / tvOS ecosystem

### Swiftfin

Official releases: https://github.com/jellyfin/Swiftfin/releases

- Swiftfin 1.7.0 released 2026-11-11.
- Release notes include Live TV guide, tvOS tab placement, player changes including SwiftVLC and mpv, Jellyfin SDK updates, playback progress/lifecycle fixes and UI refinements.
- Swiftfin is a Jellyfin client for Apple platforms; it is not an Android TV app.
- TestFlight platform review and build expiry must be checked independently; do not infer storefront availability for the user's region.

### Aerial TV product vs Aerial TV Launcher

- https://aerialtv.app/ is a commercial multi-platform IPTV/media player product. Its official site advertises QR setup, cloud sync, EPG, sports hub, profiles and DVR; it lists USD monthly/yearly/lifetime pricing.
- https://github.com/pengchujin/aerial-tv is an open-source Android TV launcher. It released Aerial 1.98 on 2026-11-15, with device-specific fixes for standby/ADB, HDMI source switching, boot recovery and focus behavior. The maintainer states several fixes were not tested on all corresponding models.

These projects are not the same app and must not share status or compatibility claims.

### Other named candidates

IIVA, Filebar/Fileball, Leplayer, Wobbly TV, Dawnboard, AirWeb and tvOS Browser did not reach a sufficiently verified official-source record in this pass. Keep Hold/U until exact official App Store/developer/repository identity, version, storefront and distribution path are established. Do not infer regional App Store availability.

## 6. NAS and home media servers

| Project | Last verified baseline | Current assessment |
|---|---|---|
| Jellyfin Server | 12.4, 2026-10-26 | Minor release updates .NET 10.0.14; upstream explicitly recommends full backup before upgrade |
| rclone | 1.76.0, 2026-11-06 | Recent release; review changelog and provider behavior before any upgrade |
| OpenMediaVault | 8.6.1-1, 2026-10-30 from previous baseline | No newer official release confirmed in this pass |
| TrueNAS | 25.10.8 from previous verified baseline | No newer stable release confirmed in this pass; exact release should be rechecked before a test |
| OpenList | 4.2.9 from previous baseline | No newer official release verified in this pass |
| DPanel | 1.11.2 from previous baseline | No newer release verified; custom EULA and Docker privileges remain blockers |
| FygoOS / fnOS | No new verified release recorded | Keep as separate NAS OS candidate; Fygo TV is a separate media-server/client product |

Official links:
- https://github.com/jellyfin/jellyfin/releases
- https://github.com/rclone/rclone/releases
- https://github.com/openmediavault/openmediavault
- https://www.truenas.com/docs/
- https://github.com/OpenListTeam/OpenList
- https://github.com/DPanel-dev

No storage platform or hardware was selected. No NAS, disk, share, container or production service was modified. RAID is not a backup; snapshot, backup, offsite copy, integrity verification and restore rehearsal remain separate acceptance items.

## 7. Home network and proxy core

### Mihomo

Official upstream: https://github.com/MetaCubeX/mihomo/releases

- v1.19.34 released 2026-11-11.
- Release changes include dependency/runtime maintenance and fixes for connection/resource handling.
- v1.19.33 included a CVE-2026-97031 crypto/tls fix; v1.19.34 is now the newer upstream release.

### OpenClash

- 0.48.002 is the last confirmed version in the current baseline.
- Issue #5352 reports a configuration-page save/apply failure in a particular environment.
- Issue #5361 reports a TUN default stack_type compatibility issue on an older kernel.
- Issue #5395, opened 2026-11-07, describes private-network traffic falling back to WAN due to a throw route; the reporter states the observed environment and workaround.

These are issue reports, not evidence of the same fault on the user's router. Do not install, upgrade or edit routes based on a community issue without matching firmware, kernel, target and reproducible diagnosis.

## 8. Player reliability and incident review

### Known baseline

- User's known MyTV symptom remains: WebView white/spinner on one path, X5 path succeeds; long playback may freeze video and audio after 10–60 minutes or overnight.
- User's preferred first comparison baseline is VLC because it is widely available.
- No new R700 Logcat, VLC log, MyTV playback trace, media fixture or controlled test result was supplied in this run.

### This week's result

No new incident record could be added. This means “no new evidence received,” not “no new failures.” Root cause remains unknown. Possible layers include source/push, network/DNS/proxy, demux, timestamps, decoder, Surface/render, player state machine, system media service, memory, GPU or thermal state. None is confirmed.

### Required next diagnostic matrix

1. Same authorized source, same device: VLC vs MyTV vs Media3.
2. Same player, same device: known-good local HLS/file vs network IPTV source.
3. Same source, same player: R700 vs Windows/Android reference device.
4. If engine switching is available, compare Media3 and VLC on the same fixture and capture decoder/render state.
5. Record start/end time, source type (redacted), container/codec/profile/resolution/FPS, network/DNS/proxy, buffer state, audio continuity, PTS/DTS, decoder/Surface state, CPU/RAM/temperature and logs.
6. Long-session validation only after baseline is repeatable. A temporary restart is not a root-cause fix.

No production code, device configuration or source list was changed. The open draft PR #2 remains the Super Player/reliability specification path.

## 9. Music Center monthly review

See the full monthly report: [Music Ecosystem — November 2026](music-ecosystem-2026-11.md).

Key results:
- AlgerMusicPlayer 5.1.0 is desktop-oriented; its owner states mobile/TV clients are no longer maintained.
- Folia v0.7.28 and Folium 2 preview are active; the provider-module interface is preview-stage and AGPL-3.0.
- Folia Android TV is a third-party port with a declared minSdk 24, but not R700-tested.
- Finamp is a Jellyfin music client with a beta release line; TV remote compatibility remains unknown.
- No bit-perfect output claim is accepted without end-to-end measurement.

## 10. Folia / Aerial / TV experience

- Folia upstream: v0.7.28 stable on 2026-11-11; Cielo/Folium 2 preview on 2026-11-14; provider modules are preview-stage.
- Folia Android TV: README advertises v0.7.25-tv.14, minSdk 24, D-pad focus and visualizer features; source and license reviewed, no APK installed.
- Aerial Launcher: v1.98 on 2026-11-15; Android TV launcher only.
- Aerial TV product: separate commercial IPTV/media product; QR/cloud sync is a useful experience reference only.

## 11. UTV Movie Lite / TVOS / TVBox ecosystem

### Marco Movie

Official releases: https://github.com/Pilipala9919/MarcoMovie/releases

- Android TV/box 0.5.25 released 2026-11-15.
- Release describes 6-way range-proxy prefetch, JAR/QuickJS compatibility, DoH fallback and API 23 bytecode checks.
- The TV APK is described as a 32-bit TV/box build. R700 ABI/32-bit app compatibility still needs exact manifest and device verification.
- Repository license is GPL-3.0. Upstream offline test/signature claims are not our device measurements.

### TVBoxOSC

- The original CatVodTVOfficial/TVBoxOSC repository is no longer available at that URL.
- j4Uq/TVBoxOSC describes itself as an automated build/aggregation repository for related forks.
- kukuqi666/TVboxOSC is another distinct fork/resource project with its own release history.
- Do not assume a single canonical TVBoxOSC upstream or merge source/config repositories with the client code.
- Keep any bundled content/source lists out of UTOS unless their rights and provenance are explicitly verified.

### TVBox Desktop

Repository: https://github.com/hgtuj5688/tvbox-desktop

- Windows Electron/React/TypeScript client; MIT license; release v0.1.0.
- README claims 177 self-test assertions and an included public source preset. These are maintainer claims, not independently rerun in this report.
- README states the installer is not code-signed and uninstall removes the local data directory.
- Study the parser, channel aggregation, library and external-player handoff; do not adopt the included public source list or treat it as an Android TV client.

### Architecture boundary

- UTV Movie Lite is the lightweight TV discovery/browser experience.
- TVOS is the working name for the metadata/catalog/resource-library subsystem inside Media Center.
- Provider adapters resolve authorized catalogs and user-owned libraries.
- Super Player owns playback; Media Library owns normalized metadata and user corrections.
- IPTV channel data remains separate from VOD/movie/series metadata.
- Metadata presence does not prove a playable stream exists or that playback is authorized.

## 12. Priority actions

### P0 — before any deployment
1. Confirm exact R700 firmware/build, ABI, SoC, RAM, storage and ADB access.
2. Define the Cloud IPTV Hub canonical schema and threat model.
3. Preserve the user-confirmed IPTV V1.0 baseline; build a separate test fixture set.
4. Reproduce source health semantics using local/authorized feeds; separate HTTP reachability, playlist validity, first-frame and sustained playback.
5. Capture the MyTV/VLC freeze baseline with logs before changing engines or settings.
6. Review licenses and source provenance for every IPTV/VOD/music candidate.

### P1 — isolated trials
1. Run Guovin 3.0.0 and IPTV Checker 5.0.2 only in a disposable Windows/Docker environment after dependency/image pinning.
2. Prototype playlist/EPG artifact publication and token revocation in a disposable Cloudflare account only after explicit approval.
3. Inspect Folia Android TV and Marco Movie APK manifests/signatures; no installation yet.
4. Compare Jellyfin 12.4, OpenList/rclone and backup tooling in a disposable environment.
5. Review SubConverter-Extended 1.11.5 parser support and protocol tests without real subscriptions.

### P2 — later
1. Implement UTV client QR pairing and per-device configuration sync.
2. Implement TVOS catalog and Movie Lite experience on shared Media Library/Super Player contracts.
3. Implement Music Center Provider Registry, lyrics and remote-first UI.
4. Add cross-device session sync, audit and recovery after the underlying interfaces are stable.

## 13. No-change / evidence-gap statement

Sports live, retro gaming, TVGO/VsTV, several tvOS apps (IIVA, Filebar/Fileball, Leplayer, Wobbly TV, Dawnboard, AirWeb), FygoOS/Fygo TV and some Docker image candidates did not produce enough new official evidence to justify a state change this week. They remain Study/Hold/U, not Adopt. No new R700 playback data was available. No service was deployed and no user device or production configuration was touched.
