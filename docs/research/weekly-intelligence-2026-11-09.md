# UTV Weekly Ecosystem Intelligence — 2026-11-09

**Review date:** 2026-11-09  
**Scope:** IPTV, online video, sports live, NAS/media servers, cloud drives, Docker operations, home network, Apple TV/tvOS apps and cross-device experience.  
**Execution boundary:** Research and documentation only. No installation, upgrade, deployment, router/NAS/disk/R700 operation, account login, sideload or configuration mutation was performed.

## 1. Executive findings

1. **IPTV source pipeline:** Guovin IPTV-API 3.1.0 (2026-10-12) is the strongest current source-aggregation reference. It requires Python 3.14, offers Windows/macOS desktop GUI and Docker/Compose, and includes dependency security updates. Keep it as an isolated worker, not a mandatory UTOS runtime dependency. License: AGPL-3.0.
2. **Independent validation:** IPTV Checker 5.0.2 (2026-09-25) declares MIT and supports Docker/Compose, CLI, HTTP plus FFmpeg forced checks, EPG and M3U/TXT exports. Its 5.0.2 release body is empty and the checked CHANGELOG still begins at 5.0.1, so the exact delta remains U.
3. **Image-only IPTV candidates:** smart-v1, iptv-4gtv, 4gtv-v1, bee-v1 and bee-v2 are visible on Docker Hub, but a corresponding public source repository, explicit license and verifiable SBOM/provenance were not established in this review. Multi-architecture tags are not security or functional validation.
4. **Online video:** LunaTV and MoonTV remain optional VOD aggregation front ends, not media servers or content providers. LunaTV states CC BY-NC-SA; the Stardm0/MoonTV fork declares MIT but derives from LunaTV 2.0.1, so upstream provenance and redistribution rights need legal review.
5. **Cloud storage:** OpenList 4.2.9 (2026-10-25), AList 3.66.0 (2026-10-21) and rclone 1.76.0 (2026-11-06) are active candidates. rclone 1.76.0 includes security fixes for several CVEs and safer redirect/symlink handling. Use one primary file-listing service plus an optional mount/sync engine.
6. **NAS platforms:** TrueNAS 25.10.8 is the current stable 25.10 maintenance release (2026-11-05); TrueNAS 27-RC.1 is an early release. OpenMediaVault 8.6.1-1 was published 2026-10-30; its Compose plugin 8.2.5 was published 2026-10-25.
7. **FygoOS/Fygo TV:** Official docs distinguish NAS-side Fygo TV from an Android TV client. Docs show a NAS app with independent user/folder permissions, NFO/local artwork preference and remote-mount support. No R700 client compatibility, codec/HDR path or direct-play result was verified. FygoOS licensing, cloud-account and privacy dependencies need a separate decision.
8. **Media server migration:** Jellyfin 12.4 was published 2026-10-26 and is a minor release within the 12.x line. The major migration boundary is entering 12.0 from an older major line; the 12.4 release note still explicitly requires a full backup before upgrading. Server, plugin, database and TV-client compatibility must be tested on a clone. Swiftfin's current README still labels Jellyfin 12.0 and tvOS 26+, so do not assume it has been validated against 12.4.
9. **Docker management:** DPanel 1.11.2 (2026-10-29) adds Compose configuration rollback, but its release notes limit rollback to configuration files/settings, not container runtime state. Its custom EULA allows non-commercial use but restricts redistribution/derivative redistribution. Docker socket access remains a host-root-equivalent control surface.
10. **Home network:** OpenWrt 25.12.6 (2026-10-18), OpenClash 0.48.002 (2026-10-12) and Mihomo 1.19.33 (2026-10-21) are the current checked stable references. A reported OpenClash/APK dependency issue on OpenWrt 25.12.6 remains unverified; no router changes are recommended.
11. **Apple TV corrections:** Swiftfin is a first-party Jellyfin project client, not merely a third-party client. xfinn is an independent MIT client with tvOS 18.6+ requirements. Airwave, Dawnboard and Wobbly TV add study candidates; AirWeb remains an iPhone/iPad AirPlay-mirroring app, not a native tvOS browser.
12. **Module separation:** Sports Live, Retro Game Library, Cloud Drive, Docker Operations, Home Network and Cross-device Services remain independent domains with shared playback/storage/network contracts.

## 2. IPTV and live-source candidate register

| Candidate | Current checked state | License/source evidence | UTOS decision |
|---|---|---|---|
| Guovin/iptv-api | Release 3.1.0, 2026-10-12; Python 3.14; Docker Compose and Windows/macOS GUI | AGPL-3.0; official repo and release available | Study; isolated trial after version/digest pinning |
| zhimin-dev/iptv-checker | Tag v5.0.2, 2026-09-25; Docker/Compose and CLI; HTTP + FFmpeg checks, EPG, M3U/TXT | MIT; v5.0.2 release body empty, CHANGELOG top entry 5.0.1 | Study; isolated trial |
| instituteiptv/smart-v1 | 0.1.2; last pushed about 6 months earlier; amd64, arm/v7, arm64 | Docker Hub image page; source/license/SBOM not established | Hold pending supply-chain review |
| instituteiptv/iptv-4gtv | 1.1.3; last pushed 2 days earlier; amd64 and arm64 | Docker Hub image page; source/license/SBOM not established | Hold; service-specific credentials and access rights required |
| instituteiptv/4gtv-v1 | Overview updated about 8 days earlier; README shows 2.4.5 | Source/license/architecture/SBOM not established | Hold; credentials and source/license unknown |
| instituteiptv/bee-v1 | 0.1.2; last pushed about 5 months earlier; amd64, arm/v7, arm64 | Docker Hub image page; source/license/SBOM not established | Study only; stale |
| instituteiptv/bee-v2 | 0.0.1; last pushed about 10 months earlier; amd64, arm/v7, arm64 | Docker Hub image page; source/license/SBOM not established | Hold; older than Bee-v1 |
| instituteiptv/iptv-4gtv-system | Separate image 0.2.5; updated about 1 month earlier | Docker Hub image page; not the same project as iptv-4gtv or 4gtv-v1 | Keep as a distinct candidate |

### Guovin 3.1.0 configuration implications

- Python 3.14 is required; older Pipenv environments should not be assumed compatible.
- Docker Compose persists config/output paths. The upstream sample uses a mutable latest tag; UTOS trials should pin a release tag and digest.
- The checked config has subscription EPG auto-discovery off and automatic source disabling off. Preserve these safe defaults until source trust and recovery are tested.
- v3.1.0 fixes a Docker empty-proxy-variable issue, keeps media probing direct, bounds EPG/live sampling resource use and updates aiohttp/urllib3-related dependencies.
- Windows desktop support is an upstream claim. The user's Windows 11 + Docker Desktop/WSL2 state and end-to-end compatibility remain unknown.

### Source pipeline design

Guovin is an ingestion/aggregation worker; IPTV Checker is a validation worker; Smart/Bee/4GTV images are candidate service/proxy adapters. UTOS should own one canonical channel/source schema, provenance and authorization state. HTTP 200, a reachable manifest or a single screenshot is not proof of sustained playback. No proxy may bypass DRM, paid access or geographic authorization restrictions.

## 3. Online video and media library

- LunaTV upstream: https://github.com/MoonTechLab/LunaTV — repository last pushed 2026-09-10; description states CC BY-NC-SA. GitHub metadata did not identify a standard SPDX license, so inspect the LICENSE and all included assets/dependencies before reuse.
- MoonTV fork: https://github.com/Stardm0/MoonTV — MIT declared; last pushed 2026-11-05; README says it self-maintains from LunaTV 2.0.1 and warns that Cloudflare deployment may risk Pages suspension. A fork's MIT declaration does not by itself settle rights of inherited upstream code/assets.
- Keep both outside the core media server. They are optional provider/front-end candidates; sources, credentials and content rights remain external and user-authorized.
- **Selene-TV:** https://github.com/MoonTechLab/Selene-TV is a separate Android TV (Leanback) client for MoonTV/Helios, latest checked release v1.4.6 (2026-06-22). Its README claims Android 6/API 23 minimum, targetSdk 36, arm64-v8a and armeabi-v7a APKs, D-pad-only navigation, ExoPlayer Media3 + mpv, QR phone remote and in-app update. Android 12 exceeds the stated minimum API, but R700 ABI/firmware/decoder/DRM and remote behavior are untested. No LICENSE file or SPDX license was found; do not reuse code until clarified. Cleartext/self-signed TLS support, embedded Ktor remote-control server and app updater need security review.
- Selene-TV v1.4.6 release notes claim deeper MPV live buffering and automatic reconnect for longer 4K live sessions; this is a maintainer statement, not measured performance.
- Jellyfin 12.4 is the latest checked server release and a minor update within 12.x. The major migration boundary is moving from an older major line into 12.0; the 12.4 release note still requires a full backup before upgrading. Validate plugins and clients on a clone.
- Kodi and VLC remain playback clients, not NAS operating systems or media servers. R700 package ABI, remote focus, direct play, transcoding, HDR/audio/subtitle path and sustained playback remain U.

## 4. Cloud drive and resource sharing

| Candidate | Checked release | License | Role |
|---|---|---|---|
| OpenList | v4.2.9, 2026-10-25; active repository commits on 2026-11-09 | AGPL-3.0 | Cloud-storage listing/API/WebDAV service |
| AList | v3.66.0, 2026-10-21 | AGPL-3.0 | Alternative cloud-storage listing/WebDAV service |
| rclone | v1.76.0, 2026-11-06 | MIT | Mount, sync and transfer engine |

rclone 1.76.0 adds backends and an index command, disables Multipath TCP listeners by default and includes multiple dependency/security fixes, HTTPS downgrade protections, symlink/path protections and redirect-auth fixes. Test it in a disposable environment; do not mount production shares or overwrite existing remote data during evaluation.

Architecture recommendation: choose one primary listing/share service; use rclone only where filesystem mount/sync semantics are required. Keep tokens in secrets, use read-only access by default, set bounded VFS cache and verify seek/range, resume, metadata, subtitle and 4K behavior. A remote mount is not a backup.

## 5. NAS and home media

- **FygoOS:** official materials list x86/ARM and ZFS/ext4/Btrfs; free plan limits are 3 users and 4 storage devices, with paid tiers for expanded limits. Fygo TV is installed on the NAS from its App Center; folder access is separately granted to the app; remote-mounted/cloud-drive folders can be selected; local NFO/images can be preferred. FygoConnect requires a Fygo account and may use a traffic-limited relay; local LAN accounts are separate. Public source-code documentation lists Linux kernel and Samba source, not a complete OS source release. Privacy policy for online services describes account and technical data; it does not establish all offline OS telemetry behavior.
- **TrueNAS:** 25.10.8 maintenance release on 2026-11-05 fixes dataset passphrase exposure in internal job arguments, NVMe-over-TCP I/O issues and includes a kernel update addressing more than 1,600 upstream CVEs. 27-RC.1 is an early release; do not use for production data.
- **OpenMediaVault:** 8.6.1-1 stable package dated 2026-10-30. OpenMediaVault Compose plugin 8.2.5 dated 2026-10-25. Use the OS and plugin version as separate records.
- No NAS hardware, disk layout, filesystem, backup target or recovery test exists in this project. RAID is not backup; no candidate can be Adopt before restore, upgrade/rollback, permission and workload tests pass.

## 6. Docker and operations

- DPanel v1.11.2 was published 2026-10-29. Its Compose editor can restore prior YAML, .env and task settings after some failed deployments, but the release notes explicitly say this does not restore container runtime state or undo completed directory synchronization; the in-memory backup can expire after one hour or disappear when the panel restarts.
- DPanel's LICENSE is a custom EULA: non-commercial use is allowed, but redistribution of derivative/modified/third-party versions is restricted without permission. Do not embed or redistribute it as a UTOS component.
- Docker official security docs warn that Docker daemon control is highly privileged; a web UI with docker.sock access must be treated as host-root-equivalent. Rootless mode, Compose secrets, pinned image digests, SBOM/provenance, health checks and tested backup/restore are preferred where supported.
- Do not expose Docker management, SMB/NFS, NAS administration or raw Docker daemon endpoints to the public internet.

## 7. Home network

- OpenWrt 25.12.6 stable was released 2026-10-18 and includes security fixes for remotely reachable default network services. Its release notes also list target-specific upgrade caveats and known issues (including some Ethernet targets and WAN-interface renaming); exact router target and recovery method must be checked before any upgrade.
- OpenClash v0.48.002 was released 2026-10-12. Issue #5367 reports a possible DNS break during APK-based dnsmasq-full dependency installation on OpenWrt 25.12.6. This is an issue report, not a reproduced result; keep OpenClash on Hold until verified in a disposable environment.
- Mihomo v1.19.33 stable was released 2026-10-21 and includes CVE-2026-97031 and other fixes. A Prerelease-Alpha branch update dated 2026-11-06 is not a stable release and is excluded from adoption.
- Router model, SoC, flash/RAM, Wi-Fi drivers, VLAN/multicast/IGMP, hardware offload and recovery path are unknown. No router configuration was changed.

## 8. Apple TV / tvOS ecosystem updates

- **Swiftfin:** first-party Jellyfin project client under jellyfin/Swiftfin; App Store and TestFlight paths are documented by upstream README. Current README requires tvOS 26+ and carries a Jellyfin 12.0 compatibility badge; release 1.6.1 is dated 2026-08-23. Verify against server 12.4 before claiming compatibility.
- **xfinn:** independent client, source repository Glob0x17/xfinn, MIT; developer site states tvOS 18.6+ and Jellyfin 12 recommended. No xfinn server in between is a developer claim, not independent privacy validation.
- **Leplayer:** README says v1.2.1, tvOS 14+, Xcode 15+, CocoaPods, MIT; no formal GitHub Release was found. It combines IPTV, cloud/WebDAV/FTP and AVPlayer/VLC; OAuth credentials are placeholders in source and third-party notices/dependency licenses need review.
- **Airwave:** Apple TV app version 1.2 observed 2026-11-09; listing price US$2.99. Requires a self-hosted Airwave server connected to Plex. The server is described as source-available, not confirmed OSI open source. The server's advertised cross-device clients are not R700 validation.
- **Dawnboard:** App Store version 1.0.46 observed 2026-11-09; free iPhone + Apple TV dashboard configured over local network.
- **Wobbly TV:** Apple TV ambient-art app requiring tvOS 26+; listing shows Wobbly TV+ at US$9.99. It is an app, not a replacement for the system screen saver.
- **AirWeb:** iPhone/iPad app using AirPlay mirroring to display browsing; not a native tvOS browser.
- **Speedtest by Ookla:** Apple TV listing includes advertising and an in-app ad-removal purchase; do not describe it as ad-free.
- **Filebar/Fileball:** the Filebar App Store listing shows Apple TV compatibility; Fileball website/FAQ contains older conflicting Apple TV statements. Use the current listing only as evidence of distribution, not feature quality.
- **IIVA:** older App Store record shows version 1.0.3 from 2023; Hold pending maintenance and compatibility evidence.
- App Store storefront availability was not checked for the user's account/region. TestFlight builds expire after up to 90 days. tvOS Browser projects using private APIs remain sideload-only research, not a supported install path.

## 9. P0 validation queue

1. Confirm exact R700 model/firmware, ADB state and playback client versions; no device changes.
2. Confirm Windows 11 Docker Desktop/WSL2 state before any local trial.
3. In a disposable VM only, pin and test Guovin 3.1.0 and IPTV Checker 5.0.2; verify M3U/TXT/EPG outputs, persistent data, scheduled execution, failure behavior and actual playback on an authorized source.
4. Obtain source/license/SBOM/provenance and image digests for Smart/Bee/4GTV before running any image.
5. Trial OpenList or AList (not both as primaries) plus rclone in a disposable environment; test read-only access, range/seek, cache bounds, token rotation and restore.
6. Clone a Jellyfin 12.x test library and validate 12.4 maintenance update, plugins and R700/Kodi/VLC clients before considering any server upgrade. If migrating from an older major line into 12.0, treat that separately as a major migration.
7. Test OpenClash/APK dependency behavior only on a disposable router/VM; do not touch the household router.
8. Choose a disposable NAS VM/hardware and complete restore/upgrade/rollback drills before selecting a platform.
9. Verify Apple TV storefront, model and OS requirements before any installation; do not sideload private-API projects by default.

## 10. Evidence and decision boundary

- **A:** official source/release/license confirms the published fact; it does not prove performance or compatibility.
- **B:** reproducible test on named hardware/software. No new B-grade tests were run in this review.
- **C:** engineering inference from A/B evidence.
- **D:** design goal.
- **U:** unknown/unverified.

No module is marked deployed, device-tested or production-accepted by this review.


## 11. Primary source index

All web pages below were checked on 2026-11-09 unless a specific publication/release date is shown. A source confirms the publisher's statement, not independent performance or compatibility.

### IPTV and operations
- Guovin repository and release 3.1.0 (2026-10-12): https://github.com/Guovin/iptv-api and https://github.com/Guovin/iptv-api/releases/tag/3.1.0
- Guovin license: https://github.com/Guovin/iptv-api/blob/master/LICENSE
- IPTV Checker repository, v5.0.2 release (2026-09-25) and changelog: https://github.com/zhimin-dev/iptv-checker, https://github.com/zhimin-dev/iptv-checker/releases/tag/v5.0.2, https://github.com/zhimin-dev/iptv-checker/blob/main/CHANGELOG.md
- Smart IPTV Server image: https://hub.docker.com/r/instituteiptv/smart-v1
- IPTV-4GTV image: https://hub.docker.com/r/instituteiptv/iptv-4gtv
- 4GTV-v1 image: https://hub.docker.com/r/instituteiptv/4gtv-v1
- Bee-v1 / Bee-v2 images: https://hub.docker.com/r/instituteiptv/bee-v1 and https://hub.docker.com/r/instituteiptv/bee-v2
- Separate IPTV-4GTV-System image: https://hub.docker.com/r/instituteiptv/iptv-4gtv-system
- DPanel v1.11.2 and custom license: https://github.com/donknap/dpanel/releases/tag/v1.11.2 and https://github.com/donknap/dpanel/blob/master/LICENSE
- Docker daemon security, rootless mode, Compose secrets and build attestations: https://docs.docker.com/engine/security/ ; https://docs.docker.com/engine/security/rootless/ ; https://docs.docker.com/compose/how-tos/use-secrets/ ; https://docs.docker.com/build/metadata/attestations/

### Online video and cloud drives
- LunaTV and MoonTV fork: https://github.com/MoonTechLab/LunaTV and https://github.com/Stardm0/MoonTV
- OpenList v4.2.9: https://github.com/OpenListTeam/OpenList/releases/tag/v4.2.9
- AList: https://github.com/AlistGo/alist
- rclone v1.76.0 changelog (2026-11-06): https://rclone.org/changelog/

### NAS and media servers
- FygoOS pricing, Fygo TV setup, account distinction, remote access, public source code and privacy: https://fygonas.com/pricing ; https://help.fygonas.com/articles/v1/media/media_install ; https://help.fygonas.com/articles/v1/contact/account-diff ; https://help.fygonas.com/articles/v1/access/how-access ; https://help.fygonas.com/articles/v1/contact/public-source-code ; https://fygonas.com/privacy-policy
- TrueNAS software status and release notes: https://www.truenas.com/docs/softwarestatus/
- OpenMediaVault core and Compose plugin changelogs: https://github.com/openmediavault/openmediavault/blob/master/deb/openmediavault/debian/changelog and https://github.com/OpenMediaVault-Plugin-Developers/openmediavault-compose/blob/main/debian/changelog
- Jellyfin 12.4 release (2026-10-26): https://github.com/jellyfin/jellyfin/releases/tag/v12.4

### Home network
- OpenWrt 25.12.6: https://github.com/openwrt/openwrt/releases/tag/v25.12.6
- OpenClash v0.48.002 and issue #5367: https://github.com/vernesong/OpenClash/releases/tag/v0.48.002 and https://github.com/vernesong/OpenClash/issues/5367
- Mihomo v1.19.33: https://github.com/MetaCubeX/mihomo/releases/tag/v1.19.33

### Apple TV / tvOS
- Swiftfin and xfinn: https://github.com/jellyfin/Swiftfin and https://github.com/Glob0x17/xfinn
- Leplayer source and license: https://github.com/leisurehuang/Leplayer
- Airwave, Dawnboard and Wobbly TV listings: https://apps.apple.com/us/app/airwave-live-tv/id6801608850 ; https://apps.apple.com/gb/app/dawnboard/id6758313682 ; https://apps.apple.com/us/app/wobbly-tv/id6788838278
- Infuse and Zuno listings: https://apps.apple.com/us/app/infuse-video-player/id1136220934 and https://apps.apple.com/us/app/zuno-media-player/id6759619793
- Filebar, IIVA, AirWeb and Speedtest listings: https://apps.apple.com/app/id1558391784 ; https://apps.apple.com/us/app/iiva/id1614231457?platform=tv ; https://apps.apple.com/us/app/airweb-tv-web-browser/id770893993 ; https://apps.apple.com/us/app/speedtest-by-ookla/id300704847
- Apple TestFlight and beta-build guidance: https://developer.apple.com/testflight/ and https://developer.apple.com/help/app-store-connect/test-a-beta-version/testflight-overview/
- User-provided Retro Game discovery reference: https://tmxk.pp.ua/d/142-ceng-zhan-man-ni-tong-nian-zhou-mo-de-you-xi-zhe-li-quan-du-you-mian-xia-zai-ling-yan-gao-da-kai-jiu-neng-wan


### Additional Android TV client source
- Selene-TV repository and release v1.4.6: https://github.com/MoonTechLab/Selene-TV and https://github.com/MoonTechLab/Selene-TV/releases/tag/v1.4.6
- LunaTV's Android TV client note (OrionTV) is in the upstream README: https://github.com/MoonTechLab/LunaTV
