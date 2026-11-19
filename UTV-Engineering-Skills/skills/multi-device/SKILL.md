---
name: utv-multi-device
description: Use for phone, Android TV and Windows interoperability, device discovery, pairing, remote control, casting, playback handoff and session synchronization.
---
# UTV Multi-device Engineering

## Workflow
1. Define supported sender/receiver pairs and exact platform versions.
2. Define device identity, enrollment, trust, pairing, expiry and revocation.
3. Define protocol adapters independently (DLNA/UPnP, Cast, AirPlay or local HTTP); do not assume feature parity.
4. Specify session model, playback position semantics, conflict resolution, idempotency and offline behavior.
5. Define network assumptions: same LAN, VLAN, Wi-Fi isolation, multicast, VPN and sleep/wake.
6. Test disconnects, duplicate commands, stale sessions, clock drift, app restart and device revocation.
7. Record real-device compatibility per pair.

## Guardrails
Do not claim cross-platform support from compiling on one platform. Discovery does not imply authorization.
