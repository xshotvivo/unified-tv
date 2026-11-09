# UTOS System Architecture — Draft 0.4

**Status:** Proposed; not implemented or validated.
**Primary constraint:** R700 first phase runs on its existing Android 12 firmware unless a separately approved system-image program is created.

## 1. Layered, capability-oriented model

1. **Experience:** home, navigation, focus engine, visual system, accessibility and ambient mode.
2. **Domain modules:** Media Center, Online Video Aggregation, Live TV, Sports Live, Cloud Drive, Retro Game Library and App Library.
3. **Cross-device & Intelligence:** device discovery, casting/handoff, session synchronization, voice hooks and the independent AI Agent. The agent invokes only registered capabilities under policy and consent controls.
4. **Device Capability:** display, audio, HDMI/CEC, tuner, network, Bluetooth, USB, game controllers and power capabilities exposed through adapters.
5. **Runtime & Resources:** process lifecycle, rendering budgets, media-session coordination, emulator/player isolation, memory/performance telemetry and host-supported scheduling hooks.
6. **Trust & Reliability:** permissions, privacy boundaries, secure configuration, update verification, diagnostics, backup, recovery and rollback.
7. **Platform Adapter / HAL Boundary:** Android public APIs first; OEM/system APIs only when documented and authorized. A true OS port additionally requires framework, system services, HAL, kernel and vendor integration.

## 2. Independent domain modules

### UTOS Media Center
Owns local/private movie and series discovery, normalized metadata, user library and playback handoff. It does not own the player engine, content rights or source hosting. See [Media Center](../modules/media-center.md).

### Online Video Aggregation
Optional provider adapter for self-hosted catalog/front-end projects such as LunaTV/MoonTV. It remains isolated from the local media server and Live TV domains. It does not provide content rights, bundle source lists or bypass authentication/DRM. See [Online Video Aggregation](../modules/online-video-aggregation.md).

### UTOS Live TV
Owns general live-channel cataloguing, M3U/XMLTV ingestion, EPG mapping, source provenance, live playback orchestration, health diagnostics, bounded retry and controlled failover. It is a player/source-management module, not a broadcaster or rights provider. See [Live TV](../modules/live-tv.md).

### Sports Live Center
Owns sports-event discovery, event metadata, schedules, authorized source mapping and sports-specific entry UX. It may reuse Live TV playback and diagnostics contracts, but must not merge sports event identity, schedules or rights state into the general channel catalog. See [Sports Live Center](../modules/sports-live-center.md).

### Cloud Drive & Resource Sharing
Owns provider-neutral remote-file access, mount/share policy, cache limits, permissions and source provenance. Remote mounts are not backups. Keep OpenList, rclone and other backends replaceable. See [Cloud Drive & Resource Sharing](../modules/cloud-drive-resource-sharing.md).

### NAS & Home Media Server
Owns storage platform adapters, media-server adapters, metadata persistence, access control and verified backup/restore. Kodi/VLC and other TV applications are clients, not NAS operating systems. See [NAS & Home Media Server](../modules/nas-home-media-server.md).

### Retro Game Library
Owns legal game cataloguing, emulator/runtime selection, controller mapping, save-state lifecycle and per-platform compatibility. No copyrighted ROM/BIOS distribution is included. See [Retro Game Library](../modules/retro-game-library.md).

### Docker Operations
Owns Compose definitions, image provenance, version pinning, secrets, health checks, monitoring, backup and rollback. DPanel is an optional management UI, not the source of truth or a core runtime dependency. See [Docker Operations](../modules/docker-operations.md).

### Home Network
Owns network capability inventory, LAN discovery, DNS, multicast/IPTV, VLAN, VPN and policy-routing adapters. OpenWrt is a router OS, OpenClash a management integration and Mihomo a proxy core; these are distinct layers. See [Home Network](../modules/home-network.md).

### Cross-device Services
Owns device registry, local discovery, remote-control pairing, casting/handoff and session synchronization through protocol adapters. AirPlay, Google Cast and DLNA are not assumed to be interchangeable. See [Cross-device Services](../modules/cross-device-services.md).

### UTOS AI Agent
Owns intent understanding, bounded planning, tool orchestration, consent, user-controlled memory and audit/recovery. It calls domain modules through typed service contracts and does not receive unrestricted system privileges. See [AI Agent](../modules/ai-agent.md).

### App Library
Remains a separate domain module for installed-app discovery, categories, favorites and launch handoff. It must not own or duplicate Live TV, Media Center or game catalogs.

## 3. Shared service boundaries

- **Playback System:** shared engine-neutral session, capability, track, subtitle, timing, error and metrics contracts; content modules do not embed engine-specific logic.
- **Source Diagnostics:** distinguishes configured, reachable, media-valid, first-frame, sustained and authorized states; credentials and raw authorization material are never returned to the AI Agent.
- **Metadata Provider:** stores provider provenance and local NFO/user corrections; routine scans must not overwrite curated metadata.
- **Storage Provider / Backup Provider:** reports capacity, integrity, backup age, restore evidence and RPO/RTO separately.
- **Device Discovery / CrossDeviceSession:** explicit protocol, sender/receiver role, identity, authentication and supported media capability.
- **Network Capability Provider:** reports DNS, route, multicast, VPN/proxy and connectivity state without exposing private keys or subscription credentials.
- **Container Operations:** allowlisted lifecycle actions with least privilege, audit log, image digest, health and rollback evidence.
- **Configuration Store:** versioned settings with schema migration and export/restore.
- **Update Manager:** verifies origin, signature and integrity; supports staged deployment and rollback where the host permits.
- **AgentToolGateway:** typed allowlist of service operations, policy checks, consent gates and auditable results.

## 4. Dependency direction

Experience → domain modules → capability interfaces → platform adapters. AI Agent → registered service contracts through the policy gateway. Lower layers must not depend on UI modules. Vendor-specific code must not leak into shared domain models. Optional features must degrade gracefully when a capability is absent.

## 5. Quality attributes

Responsiveness (input-to-feedback, focus transition, frame pacing, cold/warm launch); reliability (crash-free sessions, recovery success, update failure rate); resource discipline (RAM, CPU/GPU, storage, background wakeups, thermal impact); accessibility; security/privacy; data integrity; portability. Numeric targets remain TBD until R700 and service-host baselines are measured.

## 6. R700 boundary

Treat OEM firmware as the trusted host. Preserve OEM paths for panel/display processing, HDR, audio codecs/passthrough, HDMI input and CEC, DRM/HDCP, tuner, power management and firmware OTA. Root, bootloader unlock, package removal, system partition changes and firmware replacement are out of initial scope.

## 7. Deployment maturity

- **L0 Research:** documents, mockups and capability inventory.
- **L1 User-space prototype:** ordinary app/launcher using supported APIs.
- **L2 Privileged integration:** only with legitimate OEM/system privileges and device-specific validation.
- **L3 Custom system image:** separate program requiring source/BSP, signing, HAL/driver work, security maintenance and recovery infrastructure.

Never describe L1/L2 as an independent OS.
