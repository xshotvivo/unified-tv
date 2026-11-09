# NAS & Home Media Server

**Status:** Architecture and research baseline; no NAS platform or hardware has been selected.
**Scope:** Home storage, backup/recovery, media server, metadata, container services and TV/mobile/desktop access.
**First TV integration target:** Toshiba R700 (Android 12; exact model, firmware and client compatibility remain unverified).

## Product goal

Provide a dependable home media and storage foundation that can serve UTOS and other household devices without coupling the TV experience to one NAS vendor. Data integrity, recoverability, privacy and long-term maintainability take priority over dashboard appearance or feature count.

## System boundaries

1. **Storage platform adapter:** NAS OS, filesystem, disks, pools, RAID and SMART/health telemetry.
2. **Data protection:** snapshots, versioning, local backup, off-site backup, restore verification and disaster recovery.
3. **Identity & access:** users, groups, least-privilege shares, credentials, MFA where supported and audit logs.
4. **Container platform:** Docker/Compose or vendor application runtime; pinned images, health checks, resource limits, secrets and update/rollback.
5. **Media ingestion:** folder conventions, file validation, duplicate detection and library scanning.
6. **Metadata & artwork:** provider adapters (e.g. TMDB/TVDB where permitted), NFO/local metadata, posters, seasons, subtitles and language/audio metadata.
7. **Media server:** Jellyfin, Emby, Plex, Fygo TV or other replaceable server adapters.
8. **Playback clients:** Android TV/R700, mobile, desktop and browser clients; client capability and codec compatibility are tested separately.
9. **Cross-device access:** LAN discovery, SMB/NFS/WebDAV or application APIs, remote access, VPN and secure sharing.
10. **Operations:** monitoring, logs, notifications, capacity planning, backup checks and documented recovery procedures.

## Initial platform research set

| Candidate | Research role | Initial posture |
|---|---|---|
| FygoOS / fnOS | Integrated NAS OS and home media ecosystem | Study; verify edition, licensing, hardware support and app lifecycle |
| TrueNAS | Storage-centric platform; ZFS and data-integrity model | Study as storage/reliability reference |
| OpenMediaVault | Debian-based modular NAS platform | Study as lightweight/self-managed reference |
| Generic Linux + Docker | Portable services independent of NAS vendor | Keep as fallback/reference architecture |

No candidate is selected. Product pages and vendor documentation establish published capabilities, not independent performance, security or recovery results.

## Media service candidates

Evaluate Jellyfin, Emby, Plex and Fygo TV as independent media-server options. Evaluate Kodi and dedicated Android TV clients as playback front ends, not as substitutes for a server. Evaluate tinyMediaManager and local NFO workflows for metadata preparation. Do not assume a server's TV app supports the R700 until package ABI, Android version, remote navigation, codecs, DRM requirements and real playback have been checked.

## Core contracts

- `StorageProvider`: enumerate volumes, capacity, health and supported operations.
- `BackupProvider`: create/list/verify/restore backup points; report RPO/RTO and integrity evidence.
- `MediaLibraryProvider`: scan, identify, match, refresh and export library metadata.
- `MediaServerProvider`: server identity, library state, users, playback handoff and health.
- `ClientCapabilityProfile`: container, codecs, profiles, HDR, audio, subtitle and network capabilities.
- `RemoteAccessProvider`: access method, authentication, exposure, certificate and revocation state.

Keep contracts vendor-neutral. Vendor-specific capabilities must be exposed as optional, explicitly detected extensions; never silently emulate unsupported operations.

## Reliability and security requirements

- RAID is availability, not backup. Require at least one independent backup and a tested restore path.
- Define 3-2-1 or an explicitly justified alternative, including an offline/immutable copy where feasible.
- Verify backups by checksum or application-supported integrity checks; periodically perform restore drills.
- Treat filesystem, RAID, snapshot and backup semantics as different layers.
- Use least privilege for shares and containers; do not expose SMB/NFS or NAS administration directly to the public internet.
- Prefer VPN or authenticated vendor-supported remote access; assess relay/cloud dependencies and account recovery.
- Pin container versions; review image provenance, signatures where available, SBOM, vulnerabilities, privileges, host mounts and network exposure.
- No privileged container, host networking, broad writable mounts or Docker socket access without a documented necessity and threat review.
- Updates require pre-update backup/snapshot, compatibility review, staged rollout and rollback procedure.
- Monitor disk SMART, pool state, temperature, capacity, failed jobs, backup age, service health and restore-test age.
- Protect metadata and credentials; do not put tokens, private URLs or personal media details in logs or public documentation.

## Validation plan

### P0 — Hardware and data safety
- Record NAS CPU architecture, RAM, disk/controller topology, network interfaces, boot media and supported OS images.
- Confirm exact supported hardware and installation/recovery path from primary vendor documentation.
- Test disk failure/replacement, degraded pool behavior, power-loss recovery and filesystem checks on disposable test data.
- Verify independent backup and full restore before storing irreplaceable media.

### P1 — Media workflow
- Import representative films, series, multi-season shows, specials, mixed-language titles and malformed filenames.
- Measure match accuracy, manual correction effort, duplicate handling, metadata persistence and rescan behavior.
- Verify posters, episode order, subtitles, audio tracks, language tags and NFO portability.
- Test server restart, client reconnect, concurrent users and library availability during scans.

### P2 — R700 playback and network
- Confirm R700 exact model/firmware, client APK ABI and Android API compatibility.
- Verify D-pad focus, remote navigation, sleep/wake, server discovery and credentials.
- Test direct play versus transcoding; record codec/profile/bit depth/HDR/audio/subtitle path.
- Measure first frame, bitrate, buffering, dropped frames, CPU/RAM/thermal load and 2-hour stability.
- Test LAN access during VPN/proxy use, DNS changes, router reboot and NAS unavailability.

### P3 — Operations and lifecycle
- Test backup schedules, restore drills, snapshot rollback, container updates, image rollback and NAS OS updates.
- Measure recovery time, data loss window, service restoration and operator steps.
- Review license, privacy, telemetry, cloud dependency, support lifecycle and migration/export options.

## Evidence policy

Use A for independently authoritative documentation or verified source code; B for reproducible hands-on tests; C for evidence-based inference; D for design goals; U for unknown/unverified. Record source URL, access date, exact product/version/edition, hardware, conditions and test artifact. Vendor claims remain published claims until independently measured. Never describe a planned test as passed.

## Initial decisions

- **D-001:** Keep NAS, storage, media server and TV client as separate replaceable layers.
- **D-002:** Data protection and restore validation are release blockers before production use.
- **D-003:** No NAS OS is selected; FygoOS/fnOS, TrueNAS and OpenMediaVault remain research candidates.
- **D-004:** R700 compatibility is U until tested on the exact retail device and firmware.
- **D-005:** Do not make public internet exposure, privileged containers or vendor account dependency mandatory.

## Change log

- 2026-11-07: Initial architecture and research baseline created. No hardware deployment or R700 test is claimed.


## 12. Current product/version findings — 2026-11-09

### Platform status
- TrueNAS 25.10.8 is the current checked stable 25.10 maintenance release (2026-11-05). TrueNAS 27-RC.1 is an early release and is excluded from production use.
- OpenMediaVault core 8.6.1-1 is dated 2026-10-30; its Compose plugin 8.2.5 is dated 2026-10-25. Track OS and plugin versions separately.
- FygoOS official materials list x86/ARM and ZFS/ext4/Btrfs. Its free tier is limited to 3 users and 4 storage devices; paid tiers expand limits. Fygo TV is installed on the NAS from its App Center, has its own folder access scope and can prefer local NFO/images. It is not a confirmed Android TV client.
- FygoConnect requires a Fygo account; relay traffic may be speed-limited; P2P direct connection depends on IPv6. The public source-code page lists Linux kernel and Samba, not a complete OS source release. Privacy-policy statements about online services do not establish all offline OS telemetry behavior.

### Media server
- Jellyfin 12.4 was published 2026-10-26 and is a minor release within 12.x. The major migration boundary is entering 12.0 from an older major line; the 12.4 release note still requires a full backup before upgrading. Use a clone, full database/config/metadata backup, plugin review, migration test and restore test before any production upgrade.
- Swiftfin is a first-party Jellyfin tvOS client, but its current README carries a Jellyfin 12.0 badge and requires tvOS 26+. Compatibility with server 12.4 and any R700 Android TV client remains U.
- Plex and Emby exact current stable version numbers were not verified in this review; no version is guessed.

### Cloud-drive boundary
OpenList, AList and rclone belong to the separate [Cloud Drive & Resource Sharing](cloud-drive-resource-sharing.md) module. A remote mount is not a backup and must not be counted as an independent 3-2-1 copy.

### Release gate
No NAS platform is selected. No hardware, disk layout, filesystem, backup target, Fygo TV client, Jellyfin migration or R700 playback path was installed or tested. Restore, upgrade/rollback, permissions and workload tests remain mandatory before Adopt.

## Change log

- 2026-11-09: Updated TrueNAS, OpenMediaVault, FygoOS/Fygo TV and Jellyfin baselines; clarified that Fygo TV is NAS-side in checked docs and no R700 client compatibility is established. No installation or test is claimed.
