# UNIFIED TV (UTOS)

A long-term research and engineering project for a coherent, modular, reliable smart-TV experience.

**Status:** Architecture discovery / foundation. Module specifications and research baselines only; no production module is declared deployed.  
**First validation target:** Toshiba REGZA 75R700SF, Android 12. User-provided software version: V2.0.260415 / R700SF-HISI-V03. Exact Android build fingerprint, SoC, ABI and developer-access state remain to be captured.  
**Evidence rule:** Never present a design target or inference as a measured fact.

## Vision

Study the strongest ideas across Apple tvOS, Google TV, Android TV/AOSP, Samsung Tizen, LG webOS, Roku and selected open-source projects. Re-design useful capabilities around one coherent architecture rather than combining launchers or copying proprietary implementations.

## Product principles

1. Viewing-first: fast access to live TV, apps and media with minimal interruption.
2. User control: no forced advertising or recommendation feed in the UTOS experience.
3. Remote-first: predictable focus, visible focus state, short navigation paths and accessibility.
4. Modular: independently replaceable components behind stable contracts.
5. Preserve reliability: do not break OEM display, HDR, audio, HDMI/CEC, DRM/HDCP, tuner or OTA paths.
6. Evidence-led: separate verified facts, measurements, inference, design goals and unknowns.
7. Portable: isolate platform differences behind adapters; never assume vendor API parity.
8. Data-safe: backup, restore, least privilege and rollback are release blockers for storage and operations modules.
9. Rights-aware: media metadata, source availability and legal playback authorization are separate states.

## Independent product and infrastructure modules

- [Module Status Register](docs/modules/module-status-register.md): architecture, deployment, device-test and production-acceptance state kept separate.
- [System Architecture](docs/architecture/system-architecture.md): shared layer and domain boundaries.
- [Architecture Addendum — 2026-11-16](docs/architecture/architecture-addendum-2026-11-16.md): Cloud IPTV Hub, Music Center and TVOS/Movie Lite boundaries.
- [NAS & Home Media Server](docs/modules/nas-home-media-server.md): vendor-neutral storage, backup/recovery, media servers, metadata, TV clients and cross-device access.
- [NAS Ecosystem Research](docs/research/nas-ecosystem.md): FygoOS/fnOS, TrueNAS, OpenMediaVault, Jellyfin, Emby, Plex and metadata tools.
- [UTOS Media Center](docs/modules/media-center.md): local/private media discovery, metadata, personal library and playback handoff; TVOS and Movie Lite remain within this domain.
- [Online Video Aggregation](docs/modules/online-video-aggregation.md): isolated provider adapters; no bundled or unauthorized content.
- [UTOS Live TV](docs/modules/live-tv.md): M3U/XMLTV ingestion, channel catalog, EPG, source health, diagnostics and controlled failover.
- [Live TV Ecosystem Research](docs/research/live-tv-ecosystem.md): Guovin IPTV-API, IPTV Checker and separately evaluated proxy/server candidates.
- [Cloud IPTV Hub](docs/modules/cloud-iptv-hub.md): versioned playlist/EPG publication, device pairing, subscription authentication and cross-client delivery.
- [Sports Live Center](docs/modules/sports-live-center.md): separate sports-event catalog and authorized live-playback entry.
- [Cloud Drive & Resource Sharing](docs/modules/cloud-drive-resource-sharing.md): OpenList, rclone and compatible remote-storage adapters.
- [Retro Game Library](docs/modules/retro-game-library.md): legal game catalog, emulator/runtime adapters, controller and save-state requirements.
- [Docker Operations](docs/modules/docker-operations.md): Compose lifecycle, image supply chain, secrets, monitoring, backup and recovery.
- [Home Network](docs/modules/home-network.md): OpenWrt, OpenClash, Mihomo, LAN, DNS, multicast and secure remote access.
- [Cross-device Services](docs/modules/cross-device-services.md): discovery, casting, handoff, session sync and remote control.
- [UTOS Music Center](docs/modules/music-center.md): local music, authorized providers, playlists, lyrics, audio fidelity and cross-device listening.
- [UTV Music Media Center Engineering](docs/modules/music-media-center-engineering.md): independent engineering charter, end-to-end workflows, implementation milestones and acceptance gates.
- [Music Center Code Workspace](music-center/README.md): independent implementation workspace and current code status.
- [Music Ecosystem — November 2026](docs/research/music-ecosystem-2026-11.md): monthly open-source music project review.
- [Apple TV / tvOS Ecosystem](docs/research/apple-tv-tvos-ecosystem.md): five-category Apple TV app map, Android TV comparison and UTOS learning backlog.
- [UTOS AI Agent](docs/modules/ai-agent.md): natural-language intent, bounded task planning and controlled orchestration of approved UTOS services.
- [Playback System](docs/modules/playback-system.md): engine-neutral playback contracts and measurable quality gates.
- [Reliability Engineering](docs/engineering/reliability-engineering.md): incident classification, resilience controls, recovery, measurement and release gates.

These are proposed specifications and research candidates, not implemented or device-validated features. See the [Roadmap](ROADMAP.md), the [2026-11-09 review](docs/research/weekly-intelligence-2026-11-09.md) and the [2026-11-16 consolidated weekly review](docs/research/weekly-intelligence-2026-11-16.md).

## Scope boundary

The first R700 phase is an Android 12 experience and integration layer, subject to permissions and APIs actually available on the retail device. A launcher is not a standalone operating system. A true independent TV OS requires control of the system image, framework, system services, HAL, drivers, signing/update chain, security model and app ecosystem. No such capability is assumed here.

## Architecture and governance

- [Knowledge & Research Framework](docs/research/knowledge-framework.md)
- [Evidence & Validation](docs/engineering/evidence-and-validation.md)
- [Platform Matrix](docs/compatibility/platform-matrix.md)
- Important decisions belong in docs/decisions/ as Architecture Decision Records. Every research item needs a source, access date, applicability, license boundary and confidence. Every device claim needs model, firmware, test conditions and repeatability.
- Research status, architecture approval, deployment, repeatable testing and production acceptance are distinct states.
- Do not remove or disable system packages/services without a reversible test and recovery path.
- Keep user-confirmed IPTV China mainland V1.0 baseline separate from newly discovered community sources; never overwrite it without explicit validation.
- Cloud IPTV Hub must not promise zero-touch setup for players that expose no supported pairing/configuration interface.
- Music Center must reuse the shared Playback System; it must not create a parallel playback engine.

## License

No license has been selected. Until one is added, do not assume this repository grants permission to reuse its contents.


## Latest research snapshot — 2026-11-23

- [UTV weekly intelligence report](docs/research/weekly-intelligence-2026-11-23.md): IPTV source health, Cloud IPTV Hub, Cloudflare limits, proxy conversion, NAS, tvOS, Movie Lite/TVOS, Music Center, player reliability and P0 validation.
- [Network Configuration Processing & Subscription Conversion](docs/modules/network-configuration-processing.md): proxy subscription parsing, Mihomo compatibility, reproducible benchmarks, credential handling and separation from IPTV semantics.

Research status remains Study unless an explicit test record promotes a candidate. No device, NAS, router, Cloudflare service or production configuration was changed in this review.
