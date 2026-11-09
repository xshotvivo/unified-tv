# Sports Live Center

**Status:** Separate module scope established for research; not implemented, deployed or tested.
**Boundary:** Authorized sports-event metadata and live-playback entry only. No broadcaster, subscription provider or rights ownership is implied.

## 1. Purpose

Provide a sports-first experience for events, competitions, teams, start times, live status, channel/source mapping, favorites and replay handoff. Keep sports schedules and event identity separate from the general IPTV channel catalog.

## 2. Shared services and boundaries

- Reuse Playback System, Source Diagnostics, network health and authorized Live TV handoff.
- Own SportsEvent, Competition, Team, Schedule, EventState and event-to-source mapping.
- Preserve timezone and event-start semantics; distinguish scheduled, live, delayed, finished and unknown.
- Do not infer rights from a working URL or source label.
- No DRM, authentication, paid-access or geographic-restriction bypass.

## 3. Candidate research

| Candidate | Role | Current posture |
|---|---|---|
| instituteiptv/bee-v1 0.1.2 | User-identified sports/live proxy candidate | Study only; Docker Hub image last pushed about 5 months before 2026-11-09; public source/license/SBOM not established |
| instituteiptv/bee-v2 0.0.1 | Alternative candidate | Hold; image last pushed about 10 months before 2026-11-09 |
| Smart IPTV Server / smart-v1 | Possible source relay/failover service | Hold pending source, license, provenance and authorization review |
| IPTV-4GTV / 4GTV-v1 | Service-specific candidates | Keep separate from general sports module; credentials and rights must be verified |

Image availability and multi-architecture tags are not proof of security, stream rights, reliability or R700 compatibility.

## 4. P0 acceptance

- Verify source ownership, authorization, license and service terms.
- Verify event schedule, timezone, team identity and event-to-channel mapping.
- Test M3U/EPG import and authorized playback separately.
- Test start-time transitions, reconnect, source failover and stale schedule handling.
- Measure first frame, buffering, sustained playback and resource use on named hardware.
- Keep credentials in secrets; no public admin endpoints.

No sports candidate is deployed, device-tested or production-accepted.
