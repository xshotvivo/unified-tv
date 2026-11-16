# UTV Cloud IPTV Hub

**Snapshot:** 2026-11-16  
**State:** Architecture proposal / Study. Not deployed, not device-tested, not production-accepted.  
**Boundary:** Cloud IPTV Hub is the publication, authorization and device-delivery layer for the existing UTV IPTV Source Engine. It is not a second source crawler and is not a video CDN, transcoder or broadcaster.

## 1. Product goal

One user-authorized source configuration should be collected and maintained centrally, normalized into a canonical channel catalog, checked, matched to EPG, versioned and published. A UTV-owned client should be able to pair once and then retrieve the correct playlist, guide and configuration without repeatedly copying M3U URLs.

For third-party players, the achievable baseline is usually one-time manual subscription setup followed by remote refresh. A cloud service cannot silently configure an arbitrary player. Automatic device binding requires a player-side pairing flow, supported remote-configuration API or a UTV-controlled client.

## 2. Architecture boundary

| Component | Responsibility | Proposed implementation |
|---|---|---|
| Source Engine | Authorized source collection, normalization, dedupe, health checks, channel identity, EPG mapping | Guovin/IPTV Checker as replaceable workers; UTV-owned orchestration and canonical data model |
| Scheduler / Build Worker | Heavy ingestion, playlist parsing, probing, XMLTV merge, report generation | GitHub Actions for controlled jobs or a self-hosted worker; not a latency-sensitive request handler |
| Source Database | Channel/source records, provenance, mappings, test state, device registry, release pointers | Relational store; D1 is a candidate, not selected |
| Artifact Store | Immutable M3U/TXT/JSON/XMLTV snapshots and manifests | Cloudflare R2 candidate |
| Cache | Short-lived catalog/manifest cache and current-version pointer | KV candidate; not the sole source of truth |
| API / Auth | Device registration, token validation, subscription retrieval, revocation, rate limiting | Cloudflare Workers candidate |
| Web Console | Source review, channel grouping, EPG mapping, device management and audit | Cloudflare Pages + authenticated API candidate |
| Client Adapter | Fetch, parse, refresh, display and play | UTV clients; third-party player adapters only where their documented interfaces permit |

The Source Engine remains responsible for IPTV semantics. Proxy-subscription converters such as SubConverter are not IPTV playlist/EPG converters. Only generic parsing, validation, templating, versioning and publication patterns may be reused.

## 3. End-to-end pipeline and current maturity

| Stage | Required behavior | Current evidence/state |
|---|---|---|
| 1. Collect | Pull from user-authorized feeds and configured source projects | Upstream tools exist; no UTV pipeline deployed |
| 2. Normalize | Convert M3U/TXT/API variants into one canonical record | Design target; schema not implemented |
| 3. Dedupe | Match channel identity while retaining multiple candidate URLs | Upstream projects show variants; UTV dedupe not implemented |
| 4. Health check | Separate reachable, playlist-valid, first-frame and sustained-play states | Guovin/IPTV Checker are candidates; no UTV test run |
| 5. Channel catalog | Stable channel ID, aliases, region, category, provenance and rights state | Not implemented |
| 6. EPG | Parse XMLTV, map tvg-id/tvg-name, timezone and freshness | Upstream capability exists; UTV mapping not implemented |
| 7. Build | Generate M3U/TXT/JSON/XMLTV from a versioned catalog | Not implemented |
| 8. Publish | Publish immutable artifacts and current manifest | Cloudflare design only |
| 9. Pair / fetch | Register device, issue revocable credential, fetch client-specific manifest | No UTV client implementation |
| 10. Playback | Confirm actual playback on named client/device | Not tested |
| 11. Degrade / fail over | Exclude failed candidate, select eligible backup, record cause | Design only |
| 12. Roll back | Repoint to last-known-good manifest and restore catalog version | Design only |

A playlist download or a successful HTTP status is not proof that a channel plays. A successful first frame is not proof of sustained stability. Every stage must retain its own evidence and timestamp.

## 4. Player integration truth

- **UTV-owned TV/Windows/Android clients:** implement device registration, short-lived pairing code, QR flow, per-device token, revocation, manifest refresh and configuration schema migration. This is the only path to a coherent one-time pairing experience.
- **Kodi PVR IPTV Simple:** supports M3U/M3U8 and XMLTV inputs, but the user still configures the playlist and guide locations. It is not a cloud device-pairing API.
- **VLC:** can consume a playlist/stream URL, but this does not provide EPG account sync or cross-device configuration management.
- **TiviMate and other commercial clients:** only use documented remote-playlist and EPG refresh behavior. No public remote-configuration API is assumed.
- **Aerial TV product:** its official site advertises QR-based setup and cross-device cloud sync. It is a separate commercial product, not the Aerial Android TV Launcher and not an open-source dependency.

If a player cannot fetch a remote subscription or accept a supported configuration import, automatic synchronization is unavailable for that player. Do not hide this limitation behind the phrase “Cloud IPTV.”

## 5. Cloudflare limits checked 2026-11-16

Official documentation checked:
- Workers limits: https://developers.cloudflare.com/workers/platform/limits/
- Workers pricing: https://developers.cloudflare.com/workers/platform/pricing/
- Workers KV limits: https://developers.cloudflare.com/kv/platform/limits/
- Pages limits: https://developers.cloudflare.com/pages/platform/limits/
- R2 pricing: https://developers.cloudflare.com/r2/pricing/

Current documented limits relevant to this design:
- Workers Free: 100,000 requests/day, 10 ms CPU per request, 50 subrequests per invocation, 128 MB memory and up to 5 Cron Triggers per account. Workers Paid starts at US$5/month, with higher included limits; total cost still depends on usage.
- Workers KV Free: 100,000 reads/day, 1,000 writes/day, 1 GB storage, 25 MiB maximum value and 512-byte key limit. KV is appropriate for cache/pointers, not as the transactional canonical database.
- Pages Free: up to 20,000 files per site, 25 MiB per individual asset and 500 builds/month.
- R2 Standard Free tier: 10 GB-month storage, 1 million Class A operations and 10 million Class B operations per month. Internet egress is free; storage and operation overages are still billable.

Design inference: use Pages for the console, Workers for lightweight auth/API and subscription delivery, R2 for immutable playlist/EPG snapshots, D1 or another relational store for catalog/device/audit state, and KV only for cache/current pointers. Consume one-time QR pairing codes through D1 transactions or a Durable Object, not KV. Heavy scraping, parsing, large-feed dedupe, FFmpeg probing and long health checks belong in GitHub Actions or a controlled worker. Free Worker CPU limits are too restrictive for a full ingestion and media-probing pipeline.

Cloudflare static hosting does not transcode or proxy live video by itself. It cannot improve an upstream stream's bitrate, source latency, codec or reliability. Publishing a playlist only distributes references to streams; the player still connects to the actual authorized media endpoint.

## 6. Security and privacy controls

1. Keep private source URLs, tokens, cookies, UUIDs and provider credentials out of Git, public reports and application logs.
2. Use per-device revocable credentials; store token hashes where possible and keep signing secrets in the platform secret store.
3. Prefer authorization headers where clients support them. If a player only accepts a secret URL, treat the URL as a bearer credential, redact it from logs and provide rotation/revocation.
4. Use least-privilege, read-only source access by default. Validate URL schemes, redirect chains, DNS resolution and destination ranges to prevent SSRF and private-network probing.
5. Limit request rates and feed sizes; cap redirects, decompression, XML entities, parse depth and probe duration.
6. Retain minimal logs. Separate source metadata from credentials. Define retention and user deletion/export behavior.
7. Keep published artifacts immutable by version; never overwrite the only known-good snapshot.
8. Do not expose SMB/NFS, Docker socket, admin panels or source databases publicly.
9. Do not bypass DRM, authentication, paid access or regional authorization.

## 7. China mainland IPTV source baseline

Preserve the user-confirmed China mainland IPTV baseline V1.0 as the preferred starting point. It centers CCTV, national provincial satellite channels and local stations; international/HK/Macau/Taiwan channels remain optional. Do not overwrite this baseline with unverified community feeds.

This run retrieved the following public repository files:
- best-fan/iptv-sources: https://github.com/best-fan/iptv-sources
  - cn_cctv_status.m3u8 and cn_province_status.m3u8 show 2026-11-15 metadata.
  - cn_all_status.m3u8 shows 2026-11-13 metadata.
  - The files contain multiple variants and visibly repeated channel labels. Counts and dedupe results have not been recomputed in this run.
- hujingguang/ChinaIPTV: https://github.com/hujingguang/ChinaIPTV
  - cnTV_AutoUpdate.m3u8 exists, but the retrieved raw-content cache is approximately two months old. It is not a current freshness or availability confirmation.

The response-time attributes inside a community playlist are upstream metadata, not measurements made from the user's network. No per-channel HTTP health sweep, HLS segment check or real playback test was run in this automation. No source success rate, latency ranking or CCTV/satellite completeness claim is made.

## 8. Candidate projects

| Candidate | Role | Current disposition |
|---|---|---|
| Guovin IPTV-API 3.0.0 | Source collection, quality checks, EPG, M3U/TXT outputs and service status API | Study; isolated trial after Python 3.14/Docker/dependency pinning |
| IPTV Checker 5.0.2 | Playlist validation and Docker/CLI workflow | Study; release notes are sparse, inspect source and test fixtures before trial |
| Smart IPTV Server | IPTV proxy/server candidate | Hold until source, license, image digest, SBOM and credential model are verified |
| IPTV-4GTV / 4GTV-v1 | Service-specific adapters | Hold; verify access rights, credentials and upstream provenance |
| Bee-v1 / Bee-v2 | Sports/live aggregation candidates | Hold; verify source, license, image provenance and rights before any trial |
| Kodi PVR IPTV Simple | Existing client adapter | Study; documented M3U/XMLTV input, manual configuration still required |
| TiviMate | Existing commercial Android TV client | Study; do not assume remote configuration API |
| Aerial TV product | Cross-device IPTV UX reference | Study; commercial closed-source product, not a dependency |

## 9. Acceptance gates

P0 — schema and threat model:
- Canonical channel/source/EPG/device models, provenance, rights state, secret handling and audit model.
- Feed parser limits, SSRF defense, XML safety and token redaction tests.

P1 — isolated pipeline:
- Ingest the preserved V1.0 baseline and two explicitly authorized test feeds.
- Parse, normalize, dedupe, map EPG and generate deterministic versioned artifacts.
- Verify that temporary probe failures do not delete previously valid channels.

P2 — publication:
- Deploy to a disposable Cloudflare project only after explicit approval.
- Test authentication, cache behavior, ETag/versioning, rate limits, token rotation and rollback.
- Verify Pages/Workers/R2 quotas and actual cost from measured traffic.

P3 — client pairing:
- Implement UTV client pairing and test TV/Windows/Android refresh.
- Test third-party players individually; record where manual setup remains necessary.

P4 — playback and recovery:
- Test each selected channel on the named player/device.
- Record first-frame, sustained playback, audio/video, EPG mapping, failover and rollback.
- Production acceptance requires reproducible results and no unresolved P0 security/data-integrity issue.

## 10. Decision summary

Keep the existing IPTV Source Engine as the semantic source of truth. Add Cloud IPTV Hub as a thin publication, identity and device-delivery layer. Do not build a giant Worker that scrapes, probes and proxies every stream. Do not promise universal zero-touch player setup. The first meaningful prototype is a deterministic, versioned playlist/EPG publisher plus a UTV client pairing contract, not a video proxy.
