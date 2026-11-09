# UTV Module Status Register

**Snapshot:** 2026-11-09
**Rule:** Architecture, deployment, device testing and production acceptance are four separate fields. A candidate project or written specification is not a deployment.

| Module | Architecture / planning state | Deployed | Repeatable device/service test | Production acceptance |
|---|---|---:|---:|---:|
| Experience shell / launcher | Proposed baseline | No | No | No |
| App Library | Separate domain proposed | No | No | No |
| Playback System | Engine-neutral design proposed | No | No | No |
| Live TV / IPTV | Proposed engineering baseline | No | No | No |
| Sports Live Center | Separate module scope established for research | No | No | No |
| Media Center / local library | Proposed module specification | No | No | No |
| Online Video Aggregation | Optional isolated adapter proposed | No | No | No |
| NAS / Home Media Server | Research baseline; no platform/hardware selected | No | No | No |
| Cloud Drive / Resource Sharing | Provider-neutral module proposed | No | No | No |
| Retro Game Library | Separate module scope established for research | No | No | No |
| Docker Operations | Operations architecture proposed; host state unknown | No | No | No |
| Home Network | Research baseline; router model/config unknown | No | No | No |
| Cross-device Services | Protocol-adapter architecture proposed | No | No | No |
| AI Agent | Bounded orchestration specification proposed | No | No | No |

## Candidate decision state

| Candidate | Role | Research disposition | Deployment / test state |
|---|---|---|---|
| Guovin IPTV-API 3.1.0 | Source aggregation and output worker | Study; isolated trial after Python/Docker pinning and AGPL review | Not deployed; no R700 playback validation |
| IPTV Checker 5.0.2 | Playlist health/FFmpeg validation | Study; isolated trial candidate | Not deployed; no repeatable result recorded |
| instituteiptv/smart-v1 | IPTV proxy/server candidate | Hold pending source, license, SBOM and digest review | Not deployed; untested |
| instituteiptv/iptv-4gtv 1.1.3 | 4GTV-specific service adapter | Hold pending license, credential and access-rights review | Not deployed; untested |
| instituteiptv/4gtv-v1 2.4.5 | 4GTV management/playlist candidate | Hold pending source, license and credential review | Not deployed; untested |
| instituteiptv/bee-v1 0.1.2 | Sports/live proxy candidate | Study only; image is stale and source/license are unverified | Not deployed; untested |
| instituteiptv/bee-v2 0.0.1 | Alternative sports/live candidate | Hold; image is older and source/license are unverified | Not deployed; untested |
| DPanel 1.11.2 | Optional Docker management UI | Hold pending custom-license acceptance and Docker-socket threat review | Not deployed; untested |
| LunaTV | Self-hosted VOD aggregation UI | Hold for UTOS code reuse due CC BY-NC-SA; isolated private study only after legal review | Not deployed; untested |
| Stardm0/MoonTV | Maintained MoonTV fork | Study; verify upstream provenance and full dependency/license chain | Not deployed; untested |
| Selene-TV 1.4.6 | Android TV client for MoonTV/Helios | Study; Android TV API 23, target 36, arm64/armeabi-v7a; no LICENSE file found | Not installed/tested; R700 ABI unknown |
| OpenList 4.2.9 | Cloud-drive listing/WebDAV service | Study; isolated trial candidate | Not deployed; untested |
| rclone 1.76.0 | Cloud mount/sync engine | Study; isolated trial candidate | Not deployed; untested |
| AList 3.66.0 | Alternative cloud-drive service | Hold as a parallel primary until OpenList/rclone comparison is complete | Not deployed; untested |
| FygoOS / fnOS | NAS operating system | Study; no hardware or full-OS source/license audit | Not installed; untested |
| TrueNAS 25.10.8 | Storage-centric NAS OS | Study; stable reference; test only on disposable hardware/VM | Not installed; untested |
| OpenMediaVault 8.6.1-1 | Debian-based NAS OS | Study; test only on disposable hardware/VM | Not installed; untested |
| Jellyfin 12.4 | Media server | Study; 12.x migration/client compatibility gate; 12.4 is a minor release | Not deployed or upgraded |
| OpenWrt 25.12.6 | Router OS | Study; hardware and recovery path unknown | No router changes |
| OpenClash 0.48.002 | OpenWrt management integration | Hold pending APK dependency issue reproduction/resolution | Not installed |
| Mihomo 1.19.33 | Proxy core | Study; stable release only, alpha excluded | Not installed |
| Swiftfin 1.6.1 | First-party Jellyfin tvOS client | Study; Apple TV only, not an R700 client | Not installed or tested |
| xfinn | Independent Jellyfin tvOS client | Study; tvOS 18.6+ per developer source | Not installed or tested |
| Leplayer 1.2.1 | tvOS IPTV/cloud/media player source project | Study; MIT source with third-party dependency review | Not built or tested |
| Airwave 1.2 | Plex-to-live-channel experience | Study; requires a self-hosted Airwave server connected to Plex | Not installed or tested |

## Environment facts still unknown

- Exact Toshiba R700 model suffix, firmware/build, SoC, RAM, storage, ADB and network-debugging state.
- Windows 11 Docker Desktop/WSL2 state.
- NAS hardware, disk topology, filesystem and backup target.
- Router model, flash/RAM, OpenWrt support and recovery method.
- Apple TV model, tvOS version and App Store storefront.

User-reported working IPTV URLs remain user-test evidence only. They are not revalidated by this review and must not be copied into public docs if they contain private credentials or subscription tokens.
