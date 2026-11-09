# Cross-device Services

**Status:** Proposed capability architecture; no device pairing, casting or session synchronization has been tested.

## 1. Purpose

Connect TV, phone, tablet, desktop and home media services without making any single vendor ecosystem mandatory. Cross-device capability is an adapter layer, not a universal promise that every protocol works with every device.

## 2. Capability domains

- DeviceRegistry: user-approved devices, identity, capabilities, last-seen and revocation.
- DiscoveryAdapter: mDNS, SSDP/UPnP and platform-specific discovery with network-scope controls.
- CastingAdapter: AirPlay, Google Cast, DLNA/UPnP and any later supported protocol.
- RemoteControlAdapter: authenticated pairing, playback controls, keyboard/text entry and pointer only where supported.
- MediaHandoff: sender/receiver role, content identifier, playback position, track and capability negotiation.
- SessionSync: watch progress, favorites and playback state with conflict handling and offline queue.
- Transfer/Share: SMB, NFS, WebDAV and secure application-level file sharing.

## 3. Protocol rules

- AirPlay, Google Cast, DLNA and Miracast are different protocols with different sender/receiver roles; do not infer interoperability from a generic "cast" label.
- A TV client and a phone sender may have different codec, subtitle, DRM and audio capabilities.
- Remote access must be authenticated and revocable. Prefer LAN/VPN over public management ports.
- Do not send long-lived media credentials to discovery broadcasts or AI tools.
- Keep cloud synchronization optional; local-only operation must remain possible.

## 4. P0 tests

Test same-LAN discovery, VLAN isolation, Wi-Fi client isolation, IPv4/IPv6, sleep/wake, sender disconnect, receiver restart, playback resume, subtitle/audio selection, DRM restrictions and user revocation. Record exact devices, OS versions, protocol versions and network topology.

No protocol or device pair is currently marked Verified.
