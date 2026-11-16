# UTV Module Status Register

**Snapshot:** 2026-11-16  
**Rule:** Architecture proposal, architecture approval, deployment, repeatable device/service test and production acceptance are distinct states. A candidate project or written specification is not a deployment.

| Module | Architecture / planning state | Deployed | Repeatable device/service test | Production acceptance |
|---|---|---:|---:|---:|
| Experience shell / launcher | Proposed baseline | No | No | No |
| App Library | Separate domain proposed | No | No | No |
| Playback System / Super Player | Engine-neutral design proposed; Super Player draft PR #2 open | No | No | No |
| Live TV / IPTV Source Engine | Proposed engineering baseline; user-confirmed China mainland IPTV V1.0 baseline preserved | No | User source history exists; this weekly run did not re-test | No |
| Cloud IPTV Hub | Architecture proposal added 2026-11-16; distribution/device identity separated from source engine | No | No | No |
| Sports Live Center | Separate module scope established for research | No | No | No |
| Media Center / TVOS catalog | Proposed module specification; TVOS is a catalog subsystem, not a second database | No | No | No |
| UTV Movie Lite | Lightweight Android TV discovery/browser product proposal | No | No | No |
| Online Video Aggregation | Optional isolated provider adapter proposed | No | No | No |
| NAS / Home Media Server | Research baseline; no platform/hardware selected | No | No | No |
| Cloud Drive / Resource Sharing | Provider-neutral module proposed | No | No | No |
| Retro Game Library | Separate module scope established for research | No | No | No |
| Docker Operations | Operations architecture proposed; host state unknown | No | No | No |
| Home Network | Research baseline; router model/config unknown | No | No | No |
| Cross-device Services | Protocol-adapter architecture proposed | No | No | No |
| UTOS Music Center | First-level module proposal added 2026-11-16; reuses shared Playback System | No | No | No |
| AI Agent | Bounded orchestration specification proposed | No | No | No |

## Candidate decision state

| Candidate | Role | Research disposition | Deployment / test state |
|---|---|---|---|
| Guovin IPTV-API 3.0.0 | Source aggregation, measurement, EPG and output worker | Study; isolate after Python 3.14/Docker/dependency pinning and AGPL review | Not deployed; no R700 playback validation |
| IPTV Checker 5.0.2 | Playlist health/FFmpeg validation | Study; inspect source diff, image digest, SBOM and probe semantics before trial | Not deployed; no repeatable result recorded |
| best-fan/iptv-sources | China mainland IPTV source candidate | Preserve alongside user-confirmed V1.0; new entries remain unverified | Public playlist files fetched; no channel playback test |
| hujingguang/ChinaIPTV | China IPTV source candidate | Hold pending current freshness and URL revalidation | Current source freshness not confirmed |
| instituteiptv/smart-v1 | IPTV proxy/server candidate | Hold pending source, license, SBOM and digest review | Not deployed; untested |
| instituteiptv/iptv-4gtv | 4GTV-specific service adapter | Hold pending license, credential and access-rights review | Not deployed; untested |
| instituteiptv/4gtv-v1 | 4GTV management/playlist candidate | Hold pending source, license and credential review | Not deployed; untested |
| instituteiptv/bee-v1 / bee-v2 | Sports/live proxy candidates | Hold; verify source, license, image freshness and sports rights | Not deployed; untested |
| DPanel 1.11.2 | Optional Docker management UI | Hold pending custom-license acceptance and Docker-socket threat review | Not deployed; untested |
| LunaTV | Self-hosted VOD aggregation UI | Hold for code reuse due CC BY-NC-SA; isolated private study only after license/content review | Not deployed; untested |
| Stardm0/MoonTV | Maintained MoonTV fork | Study; verify upstream provenance and full dependency/license chain | Not deployed; untested |
| Selene-TV 1.4.6 | Android TV client for MoonTV/Helios | Study; API 23 claim, target 36, arm64/armeabi-v7a; no LICENSE file found in previous review | Not installed/tested; R700 ABI unknown |
| OpenList 4.2.9 | Cloud-drive listing/WebDAV service | Study; isolated trial candidate | Not deployed; untested |
| rclone 1.76.0 | Cloud mount/sync engine | Study; review v1.76.0 changelog and provider compatibility before trial | Not deployed; untested |
| AList 3.66.0 | Alternative cloud-drive service | Hold as a parallel primary until OpenList/rclone comparison is complete | Not deployed; untested |
| FygoOS / fnOS | NAS operating system | Study; no hardware or full-OS source/license audit | Not installed; untested |
| TrueNAS 25.10.8 | Storage-centric NAS OS | Study; last verified stable baseline; test only on disposable hardware/VM | Not installed; untested |
| OpenMediaVault 8.6.1-1 | Debian-based NAS OS | Study; last verified baseline; test only on disposable hardware/VM | Not installed; untested |
| Jellyfin 12.4 | Media server | Study; full backup and client compatibility gate before upgrade | Not deployed or upgraded |
| OpenWrt 25.12.6 | Router OS | Study; hardware and recovery path unknown | No router changes |
| OpenClash 0.48.002 | OpenWrt management integration | Hold pending matching-kernel/route issue review and reproduction | Not installed |
| Mihomo 1.19.34 | Proxy core | Study; official upstream release 2026-11-11 | Not installed |
| SubConverter-Extended 1.11.5 | Proxy subscription conversion | Study; review parser/protocol compatibility; no performance benchmark | Not deployed; no real subscription imported |
| Swiftfin 1.7.0 | Jellyfin tvOS client | Study; Apple TV only, not an R700 client | Not installed or tested |
| xfinn | Independent Jellyfin tvOS client | Study; verify current developer/system requirements | Not installed or tested |
| Leplayer / IIVA / Filebar/Fileball / Wobbly TV / Dawnboard / AirWeb | tvOS media/browser/ambient candidates | Hold/U until exact official identity, distribution and current version are confirmed | Not installed or tested |
| Aerial TV product | Commercial multi-platform IPTV/media player | Study as QR pairing/cloud-sync UX reference; not open source | Not installed or tested |
| Aerial TV Launcher 1.98 | Android TV launcher/ambient layer | Study; separate from aerialtv.app product | Not installed or tested |
| AlgerMusicPlayer 5.1.0 | Desktop music client and Provider Registry reference | Study for desktop architecture; mobile/TV clients no longer maintained per owner | Not installed or tested |
| Folia upstream 0.7.28 / Folium 2 preview | Lyrics, visualizer and provider-module reference | Study; AGPL-3.0; provider interface preview-stage | Not built or installed |
| miranda031/folia-android-tv | Folia Android TV port | Trial candidate only after license, signature, ABI and permission review; minSdk 24 claim | Not installed; no R700 test |
| Navidrome 0.64.2 | Self-hosted music server | Study; GPL-3.0; v0.64.1 security fixes and v0.64.2 slow-storage scan fixes; backup DB before migration | Not deployed or tested |
| Feishin 1.17.0 | Desktop client for Navidrome/Jellyfin/Subsonic | Study; GPL-3.0; no Android TV client established | Not installed or tested |
| Finamp 1.0.2 | Jellyfin music client | Study; MPL-2.0; Android TV D-pad support not established | Not installed or tested |
| Marco Movie Android TV 0.5.25 | Android TV movie client | Study; GPL-3.0; release describes 32-bit TV/box APK | Not installed; R700 ABI/codec behavior unknown |
| TVBoxOSC | Android TV client/fork ecosystem | Hold for adoption; original CatVodTVOfficial URL unavailable; distinguish j4Uq build aggregator from other forks | No APK installed or source imported |
| hgtuj5688/tvbox-desktop 0.1.0 | Windows Electron movie/TV client | Study for parser, catalog and UI patterns; MIT; do not adopt bundled public sources | Not installed or independently tested |

## Environment facts still unknown

- Toshiba REGZA 75R700SF is the first target and is reported to run Android 12; user-provided software version is V2.0.260415 / R700SF-HISI-V03. Exact Android build fingerprint, SoC, ABI, RAM/storage, MediaCodec inventory and ADB state remain to be captured.
- Windows 11 Docker Desktop/WSL2 state.
- NAS hardware, disk topology, filesystem and backup target.
- Router model, flash/RAM, OpenWrt support and recovery method.
- Apple TV model, tvOS version and App Store storefront.

User-confirmed IPTV V1.0 source history remains user evidence, not current health evidence. Do not publish private source URLs, subscription tokens, cookies or credentials. No candidate above is deployed or production-accepted unless a later test record explicitly changes its state.
