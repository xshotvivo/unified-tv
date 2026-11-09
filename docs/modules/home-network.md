# Home Network

**Status:** Research baseline; router model, topology and configuration are unknown. No router changes were performed.

## 1. Purpose

Provide reliable LAN connectivity for live TV, NAS, media streaming, cloud access, discovery and cross-device handoff. The network module reports capabilities and health; it does not silently reconfigure the household router.

## 2. Layer separation

- **OpenWrt:** router operating system and package/network stack.
- **OpenClash:** OpenWrt management integration for proxy policy and core lifecycle.
- **Mihomo:** proxy core/runtime.
- **VPN/overlay:** separate authenticated connectivity adapter.
- **UTOS Network Capability Provider:** read-only DNS, route, multicast, latency and connectivity status where APIs permit.

These components are not interchangeable and should not be bundled as one opaque dependency.

## 3. Current checked release baseline

- OpenWrt 25.12.6 stable, released 2026-10-18; includes security fixes for remotely reachable default services.
- OpenClash v0.48.002, released 2026-10-12.
- Mihomo v1.19.33 stable, released 2026-10-21. The Prerelease-Alpha update dated 2026-11-06 is not a stable adoption target.
- OpenClash issue #5367 reports an APK-based dnsmasq-full dependency installation problem on OpenWrt 25.12.6 that can interrupt DNS during package replacement. This is an issue report, not a reproduced result; keep the combination on Hold until tested in a disposable router/VM.
- OpenWrt 25.12.6 release notes also list target-specific upgrade caveats and known issues (including some Ethernet targets and WAN-interface renaming). A release number alone is not enough to authorize an upgrade; the exact router target and recovery procedure must be checked first.

## 4. Network requirements

- Wired LAN preferred for NAS, server and TV where available.
- Record DNS resolver, DHCP, IPv4/IPv6, VLAN/subnet, firewall, mDNS/SSDP and IGMP/multicast behavior.
- IPTV multicast and unicast are separate test paths.
- Define split-DNS and policy-routing behavior; prevent VPN/proxy rules from breaking local discovery or LAN streaming.
- Keep credentials, subscription URLs and private keys out of public configs.
- Do not expose router administration, SMB/NFS or Docker management to the public internet.
- Require config backup and a physical/recovery path before any router update.

## 5. P0 inventory and validation

Record router model, SoC, RAM/flash, Wi-Fi driver, switch/VLAN support, hardware offload, OpenWrt target, bootloader/recovery and current config backup. Then test DNS, multicast, local discovery, VPN/proxy coexistence, throughput under load, router restart and rollback in a controlled environment.

No router model or production compatibility is currently verified.
