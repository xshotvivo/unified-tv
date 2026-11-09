# NAS Ecosystem Research Register

**Last baseline:** 2026-11-09
**Status:** Initial shortlist; not a complete comparative audit.
**Rule:** Recheck official documentation, current releases, licensing and security before adoption.

## 1. FygoOS / fnOS
- International entry: https://fygonas.com/
- Help center: https://help.fygonas.com/
- Research differences between international FygoOS and domestic fnOS: supported hardware, feature matrix, account/cloud dependency, application availability, license tiers, update policy, telemetry, data export and long-term support.
- Verify published storage/filesystem, Docker, VM, mobile backup, remote access and Fygo TV capabilities against current official documentation.
- Commercial editions and trial-to-free downgrade behavior may affect app availability. Recheck current terms and exact edition before relying on paid features.
- Evaluate Fygo TV separately: server requirements, client availability, direct play/transcoding, metadata, remote access and client platform compatibility.
- **Status: Study.** No installation, security audit, hardware compatibility or performance test is claimed.

## 2. TrueNAS
- Official site: https://www.truenas.com/
- Documentation: https://www.truenas.com/docs/
- Research current edition/release status, supported hardware, ZFS pool design, snapshots, replication, SMB/NFS, apps/containers/virtualization, updates and recovery.
- Do not infer all ZFS features or configurations suit low-memory or USB-boot consumer hardware.
- **Status: Study** as a storage integrity and operations reference.

## 3. OpenMediaVault
- Official site: https://www.openmediavault.org/
- Documentation: https://docs.openmediavault.org/
- Research Debian base, supported architectures, filesystem/RAID, plugin lifecycle, Docker/Compose, permissions, updates and recovery.
- **Status: Study** as a modular, self-managed NAS reference.

## 4. Media servers and clients

| Project | Official source | Research focus |
|---|---|---|
| Jellyfin | https://jellyfin.org/ | Open media server, clients, direct play/transcoding, metadata, plugins, license/security |
| Emby | https://emby.media/ | Server/client matrix, premium feature boundaries, remote access and licensing |
| Plex | https://www.plex.tv/ | Account/cloud dependencies, remote access, client availability, pricing and privacy |
| Fygo TV | https://fygonas.com/ | NAS-integrated server/client workflow; exact platform and format support |
| Kodi | https://kodi.tv/ | Local/network library playback, metadata and Android TV capabilities |

A media server, library manager, scraper and player are different roles. Do not treat a Kodi client or poster scraper as a NAS replacement.

## 5. Metadata and library management
- tinyMediaManager: https://www.tinymediamanager.org/
- TMDB: https://www.themoviedb.org/
- TVDB: https://thetvdb.com/
- Research NFO portability, provider terms/rate limits, title matching, multilingual metadata, season/special ordering, artwork caching, subtitle/audio tags and offline operation.
- Preserve user corrections and local metadata; prevent routine scans from overwriting curated data.

## 6. Container and service ecosystem
Research Docker Engine/Compose, image provenance, version pinning, health checks, resource limits, secrets, volume permissions, backup/restore and rollback. Candidate home services may include Jellyfin, Immich, Nextcloud and monitoring tools, but each requires independent license, security and resource review. Do not install a stack merely because an app store lists it.

## 7. Comparison dimensions
Every candidate review must cover:
1. Hardware/CPU architecture and minimum resources.
2. Installation, boot, recovery and supported upgrade path.
3. Filesystem, RAID, snapshots, scrubbing and disk health.
4. Backup, replication, restore verification and ransomware resilience.
5. SMB/NFS permissions, user isolation and identity integration.
6. Docker/VM capability, privilege model and image provenance.
7. Media server, metadata and client ecosystem.
8. R700 Android TV client support and remote-control UX.
9. Remote access, VPN, relay/cloud/account dependency and attack surface.
10. License, pricing, telemetry, support lifecycle and migration/export.
11. Community maintenance, release cadence, security advisories and regressions.
12. Reproducible performance and recovery evidence.

## 8. Adoption gates
- **Study:** official source and architecture understood.
- **Trial:** license/security review passed; disposable hardware or virtual environment only.
- **Adopt:** restore drill, upgrade/rollback, access-control and workload tests passed.
- **Hold:** material unknowns or unresolved security/data-integrity risk.
- **Reject:** incompatible license, unacceptable risk, unsupported hardware or failed acceptance criteria.

No candidate is currently at Adopt. No benchmark, data-integrity or R700 compatibility result is claimed.

## 9. Evidence record template
For each test record: date/time, operator, product/edition/version, hardware/firmware, disk layout, network, configuration, test data, exact steps, expected and observed result, logs/screenshots, repeat count, failures, recovery result, RPO/RTO, evidence grade and unresolved risks.

## 10. Initial source notes
FygoOS/Fygo TV official product/help material is a starting point; vendor-published capabilities are not independent validation. TrueNAS and OpenMediaVault official entry points are identified; detailed version-specific comparison remains open. Jellyfin, Emby, Plex, Kodi and metadata services are candidates only; exact current release/client/license status must be rechecked before adoption.


## 11. Version and product verification — 2026-11-09

### FygoOS / Fygo TV
- Official license page: https://fygonas.com/pricing. It lists x86/ARM and ZFS/ext4/Btrfs, a free tier limited to 3 users and 4 storage devices, and paid tiers for expanded limits. Prices and terms can change; recheck before purchase.
- Fygo TV install guide: https://help.fygonas.com/articles/v1/media/media_install. Fygo TV is installed on FygoOS from the NAS App Center; it has its own account and folder-scope model, can access remote mounts, and can prefer local NFO/images.
- Remote access guide: https://help.fygonas.com/articles/v1/access/how-access. FygoConnect requires a Fygo account; relay traffic is speed-limited; P2P direct access is conditional on IPv6 availability.
- Account distinction: https://help.fygonas.com/articles/v1/contact/account-diff. Local accounts are used on LAN; cloud services require a Fygo account.
- Public source code: https://help.fygonas.com/articles/v1/contact/public-source-code. The page lists Linux kernel and Samba source; it does not establish that the entire FygoOS distribution is open source.
- Privacy policy: https://fygonas.com/privacy-policy. It describes account and technical data for online services; it is not sufficient to infer all offline OS telemetry behavior.
- **Decision:** Study only. Fygo TV is a NAS-side server application in the checked docs, not a confirmed Android TV client. R700 direct-play, transcode, HDR/audio/subtitle and remote-control compatibility remain U.

### TrueNAS
- Software status: https://www.truenas.com/docs/softwarestatus/
- Current stable checked: 25.10.8 (2026-11-05). Release notes describe a fix for dataset passphrases appearing in internal job arguments, NVMe-over-TCP I/O fixes and a kernel update addressing more than 1,600 upstream CVEs.
- 27-RC.1 (2026-10-06) is an early release. Do not use it for production data.
- **Decision:** Study 25.10.8 as the current stable reference; keep 27-RC.1 out of production evaluation.

### OpenMediaVault
- Official changelog: https://github.com/openmediavault/openmediavault/blob/master/deb/openmediavault/debian/changelog
- Core package 8.6.1-1 is dated 2026-10-30. Keep the core OS and plugins as separate version records.
- OpenMediaVault Compose plugin changelog: https://github.com/OpenMediaVault-Plugin-Developers/openmediavault-compose/blob/main/debian/changelog — version 8.2.5 dated 2026-10-25.
- **Decision:** Study; no install or hardware compatibility is claimed.

### Media server
- Jellyfin latest checked release: 12.4, published 2026-10-26: https://github.com/jellyfin/jellyfin/releases/tag/v12.4
- Jellyfin 12.4 is a minor release within 12.x; the major migration boundary is entering 12.0 from an older major line. The 12.4 release note still says to take a full backup before upgrading. Clone the database/library, back up configuration and metadata, review plugins and test every client before any production upgrade.
- Swiftfin's current README is linked to Jellyfin 12.0 and tvOS 26+; validate server 12.4 compatibility instead of inferring it.
- Plex and Emby exact current stable release numbers were not verified in this pass; keep version fields U rather than guessing.

### Cloud-drive service candidates
- OpenList v4.2.9 (2026-10-25), AGPL-3.0: https://github.com/OpenListTeam/OpenList/releases/tag/v4.2.9
- AList v3.66.0 (2026-10-21), AGPL-3.0: https://github.com/AlistGo/alist
- rclone v1.76.0 (2026-11-06), MIT: https://rclone.org/changelog/
- rclone 1.76.0 includes security fixes for multiple CVEs, safer HTTPS downgrade/redirect behavior and local symlink/path protections. Use a disposable environment and pin the exact version/digest before trial.

No NAS, disk, Docker host, cloud account or R700 device was changed or tested.
