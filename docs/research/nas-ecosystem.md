# NAS Ecosystem Research Register

**Last baseline:** 2026-11-07
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
