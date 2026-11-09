# UTOS Live TV — GitHub Ecosystem Research

**Snapshot date:** 2026-11-07
**Purpose:** identify reusable ideas, architectural patterns, test references and upstream projects for UTOS Live TV.
**Scope note:** GitHub is too large and dynamic to enumerate exhaustively. This is a curated discovery baseline from repository searches and public project pages, not a claim that every relevant repository has been found. The recurring research process should expand, re-check and prune this catalog.

## 1. Evaluation rules

For every candidate, record: canonical repository, last checked date, commit/release freshness, maintenance activity, platform/device evidence, architecture, features, dependency chain, license, security posture, known limitations and UTOS applicability.

Do not rank by stars alone. Separate:
- **Study:** learn concepts or UX; no code copied.
- **Trial:** build/run isolated candidate for comparison.
- **Adopt:** approved dependency after license, security, compatibility and maintenance review.
- **Reject/hold:** unsuitable, inactive, incompatible or license unclear.

A README claim is upstream-reported, not independent validation. Device support is not established for R700 unless tested on that exact device/firmware.

## 2. Android TV IPTV / Live TV products

### 2.1 OwnTV — https://github.com/ahXN00/OwnTV
- Snapshot evidence: public repository page retrieved 2026-11-07; upstream page reported Kotlin, Jetpack Compose for TV, Media3/ExoPlayer, libmpv, Room, M3U, Xtream, Stalker, XMLTV, catch-up and profiles.
- Architectural ideas: two playback engines behind a PlaybackEngine interface; preview-to-fullscreen promotion; streaming playlist parsing; bounded paging for large catalogs; rolling XMLTV window; shared core/player-core separation.
- UTOS value: high-value study and isolated trial candidate for player abstraction, large-list handling, EPG and remote-first interaction.
- License: upstream page identifies GPL-3.0. Any code reuse/distribution must satisfy GPLv3 obligations and be reviewed against UTOS's eventual licensing model.
- Limit: upstream feature claims are not R700 test results. Verify exact commit, build toolchain, ABI and device behavior before trial.

### 2.2 StreamVault IPTV — https://github.com/Davidona/StreamVault-IPTV
- Snapshot evidence: public repository page retrieved 2026-11-07; upstream describes Android TV-first Kotlin/Compose/Room/Hilt/Media3; M3U, Xtream, Stalker and Jellyfin providers; EPG, recording, plugins, multi-view and TV integrations.
- Architectural ideas: separation into app/data/domain/player; plugin API boundaries; local backups; rich guide overlay; explicit provider abstraction.
- UTOS value: study for feature taxonomy, data/domain boundaries and integration surface.
- License warning: upstream page states Source-Available License (Non-Commercial) as of April 2026, despite earlier project history. Do not copy code or bundle it into a commercial/distributable UTOS product unless a compatible grant is obtained and reviewed.
- Limit: very broad scope; avoid copying feature breadth before Live TV core reliability is proven.

### 2.3 Lumora — https://github.com/LumoraPlayer/Lumora
- Snapshot evidence: public repository page retrieved 2026-11-07; upstream describes Kotlin, Android Views/XML, Media3, Room, WorkManager, M3U/Xtream/Stalker/Jellyfin/Plex and TV-first functionality.
- Architectural ideas: lower-overhead Views-based UI for constrained TV hardware; WorkManager sync; EPG timeline; channel family grouping; PiP preview and recording architecture.
- UTOS value: study as a performance/compatibility alternative to Compose-heavy UI and for live guide workflows.
- License: not approved in this baseline; inspect the repository's exact LICENSE and dependencies before any reuse.
- Limit: upstream's tested device list does not establish R700 compatibility.

### 2.4 OpenIPTV — https://github.com/Hunter-Clipper/OpenIPTV
- Snapshot evidence: public repository page retrieved 2026-11-07; upstream describes Flutter UI with native Android Media3 player, M3U/XMLTV/Xtream parsing, TV D-pad handling and local encrypted database.
- Architectural ideas: Flutter-to-native playback bridge, TV-specific text input, clear user-facing errors, opt-in crash reporting and explicit no-content-provider policy.
- UTOS value: study for privacy, error UX, source-only player positioning and cross-platform boundary design.
- License: repository page reports a source-available non-commercial license; not approved for code reuse.
- Limit: Flutter is not the default UTOS choice; evaluate only if cross-platform requirements justify its bridge and input complexity.

### 2.5 MyTV Android — https://github.com/mytv-android/mytv-android
- User-reported issue: many source URLs fail to load and app reliability is unsatisfactory.
- Prior research found a multi-engine/component approach and release notes repeatedly addressing player initialization, component loading, reconnects and stream compatibility.
- UTOS value: failure taxonomy and regression-case reference; do not use as architectural foundation without deeper code and license review.
- Limit: user experience is explicitly unsatisfactory; no assumption that MyTV defects are all player defects. Bad/expired sources, network restrictions, codec mismatch and application faults must be separated.

### 2.6 Strix — https://github.com/isnow-git/strix
- Earlier discovery snapshot describes a Kotlin/Compose for TV, Media3-oriented lightweight IPTV client.
- UTOS value: candidate for low-memory and minimal zapping UX study.
- Status: early-stage status and license must be rechecked from current upstream files before trial or reuse.

### 2.7 StreamBox — https://github.com/newjan/StreamBox
- Earlier discovery snapshot describes Kotlin, Compose, Media3, M3U/XMLTV and D-pad navigation.
- UTOS value: parser/UI patterns and comparison candidate.
- Status: current maintenance, device evidence and license not yet verified in this research pass.

## 3. Channel metadata and EPG ecosystem

### 3.1 iptv-org/iptv — https://github.com/iptv-org/iptv
### 3.2 iptv-org/epg — https://github.com/iptv-org/epg
### 3.3 iptv-org/database — https://github.com/iptv-org/database
### 3.4 iptv-org/awesome-iptv — https://github.com/iptv-org/awesome-iptv

Repository discovery confirmed these canonical repositories exist and are publicly searchable on 2026-11-07. Their purpose and current policy, channel availability, country coverage, EPG completeness, license and source authorization must be inspected in each upstream README, data schema, contribution guide and license before use.

UTOS use:
- study normalized channel metadata and identifiers;
- compare XMLTV mappings and channel naming conventions;
- build import adapters and validation fixtures;
- do not assume public playlist inclusion grants retransmission rights;
- do not bundle entries as "official" unless independently verified.

## 4. Technology references to evaluate

- AndroidX Media3 / ExoPlayer: primary Android playback candidate; review current official docs, supported formats, analytics and error APIs.
- libmpv / FFmpeg: optional compatibility engine; review Android integration, codec/ABI footprint, security updates and licenses.
- Jetpack Compose for TV: remote-first UI and focus behavior; benchmark against Android Views on R700.
- Room + Paging: indexed local channel/EPG data and bounded large-list rendering.
- Kotlin Coroutines / Flow: cancellation and state propagation.
- WorkManager: scheduled background refresh only when Android/OEM policy allows it.
- OkHttp: bounded network requests, TLS correctness, redirect policy and redaction.
- XMLTV: guide interchange format; timezone, encoding, gzip and channel-ID edge cases require fixtures.

No dependency versions are frozen by this document. Use current stable versions at implementation start and capture dependency tree, SBOM and licenses.

## 5. Cross-project lessons

1. One player engine is not necessarily sufficient; multiple engines add size, maintenance, decoder variability and lifecycle complexity. Measure the trade-off.
2. Preview-to-fullscreen promotion can reduce apparent zapping latency if the same active session/surface is retained safely.
3. Large playlists require streaming parsing, chunked transactions, indexes and bounded paging.
4. EPG needs channel-ID mapping, rolling retention, timezone correctness and stale-data signaling.
5. Source health requires real playback evidence; HTTP 200 is not equivalent to first frame or sustained playback.
6. Recovery must be bounded, cancellable and observable to avoid retry storms and source oscillation.
7. Error UX should explain the next useful action without leaking raw stack traces or secrets.
8. Broad VOD, DVR, multi-view, plugins and sports integrations should not distract from P0 live playback reliability.

## 6. Research backlog

- Inspect exact LICENSE and dependency licenses for every shortlisted repository.
- Fetch and review current README, source tree, releases, open/closed issues and security policy for OwnTV, Lumora, StreamVault, OpenIPTV, Strix and StreamBox.
- Compare Media3-only versus Media3 + libmpv builds on R700.
- Benchmark Compose for TV versus Views for focus latency, frame pacing, RAM and APK size.
- Review M3U/XMLTV parsers for malformed input, giant feeds, gzip, encoding and timezone edge cases.
- Evaluate official/authorized source catalog workflows separately from player implementation.
- Build a repeatable source-health test harness with synthetic local HLS and MPEG-TS fixtures.
- Maintain a license and dependency inventory before any code reuse.

## 7. Source and evidence log

- GitHub public repository search and repository pages checked: 2026-11-07.
- OwnTV page: https://github.com/ahXN00/OwnTV
- StreamVault page: https://github.com/Davidona/StreamVault-IPTV
- Lumora page: https://github.com/LumoraPlayer/Lumora
- OpenIPTV page: https://github.com/Hunter-Clipper/OpenIPTV
- MyTV page: https://github.com/mytv-android/mytv-android
- Strix page: https://github.com/isnow-git/strix
- StreamBox page: https://github.com/newjan/StreamBox
- iptv-org projects: https://github.com/iptv-org/iptv ; https://github.com/iptv-org/epg ; https://github.com/iptv-org/database ; https://github.com/iptv-org/awesome-iptv

**Evidence discipline:** repository existence and visible upstream descriptions are verified discovery facts; feature descriptions remain upstream claims until code review/build/test; R700 compatibility remains unknown; legal authorization of any stream remains separately unverified.


## 8. High-priority source aggregation reference: Guovin IPTV-API

Canonical repository: https://github.com/Guovin/iptv-api
Official documentation: https://github.com/Guovin/iptv-api/blob/master/README.md and https://github.com/Guovin/iptv-api/blob/master/docs/config.md
Checked: 2026-11-07.

The current upstream README documents:
- multiple local and subscribed source aggregation;
- channel aliases and matching;
- availability validation and optional automatic disabling;
- latency, throughput, resolution and frame-rate measurement;
- resolution/speed filters and sorting;
- multiple output candidates per channel;
- EPG aggregation and logos;
- M3U, TXT and HTTP API output;
- scheduled/interval updates across GUI, CLI and Docker modes;
- logs, statistics, cached results and optional stream screenshots.

### UTOS assessment
- **Study priority: High.** The project provides a concrete reference for the source-ingestion/update pipeline and configurable result generation.
- **Trial priority: High, isolated.** Use only user-authorized inputs and compare its test outputs with actual playback results on the target player/device.
- **Adoption: Pending.** Before dependency or code reuse, inspect the exact LICENSE, dependency licenses, security posture, API stability, runtime footprint and maintenance status. Do not infer license permissions from a public repository.
- **Architectural boundary:** keep aggregation and scheduled source testing in a worker/service layer; keep Android TV playback and user interaction in UTOS Live TV. The R700 should not need to run full-scale network scans.
- **Critical gap to close:** URL reachability, sampled transfer speed, manifest metadata or a screenshot do not prove sustained successful playback. UTOS must add target-engine first-frame and sustained-playback tests, event-based incident diagnosis, controlled failover and post-recovery verification.
- **AI addition:** AI should explain structured evidence, propose likely causes and invoke only allowlisted reversible actions. It must not invent URLs, bypass access controls or claim recovery without verified playback.

Evidence boundary: these are features described by upstream documentation; independent code review, license review and R700 testing remain pending.


## 9. Playback application and engine ecosystem

**Checked:** 2026-11-07. This is a targeted reference list, not a complete market-share ranking. Claims about popularity or market share are intentionally omitted unless supported by reliable measurement.

### 9.1 Official engine references

- **AndroidX Media3 / ExoPlayer** — primary UTOS candidate. Official Android documentation lists HLS/DASH, DRM and device/API requirements. Container support does not guarantee every contained codec is supported; device decoder capability remains decisive.
- **LibVLC for Android** — conditional secondary engine. Official VLC Android repository describes broad formats, protocols, network filesystems, HDR and audio passthrough capabilities. The repository distinguishes the VLC Android application GPL license from the LibVLC Android engine LGPL license; review exact distribution/linking obligations.
- **libmpv / mpv-android** — research candidate for advanced rendering, libass subtitles and playback controls. The mpv-android repository states its app is not itself an importable AAR; integrating libmpv requires native build and application-side work.
- **IJKPlayer** — legacy reference only. The official Bilibili repository releases page currently shows no packaged releases. This alone does not prove abandonment, but it is insufficient evidence of a maintained, security-supported production dependency. Inspect recent commits, FFmpeg revision and patchability before reconsidering.
- **FFmpeg** — component/framework, not an end-user player UI. Build configuration affects codec support, binary size, patent exposure and license obligations.

### 9.2 End-user applications to study

| Product | Platform / role | UTOS research value |
|---|---|---|
| VLC | Android TV, Android, desktop | Broad network playback, SMB/NAS, track controls, fallback UX |
| Just (Video) Player | Android TV / Android | Minimal player UX, Media3 behavior, remote controls |
| Nova Video Player | Android TV / Android | Library, metadata, network shares and TV navigation |
| Kodi | Android TV and other platforms | Media-center information architecture, add-on boundaries, library and playback settings |
| mpv-android | Android / Android TV | Advanced subtitle/rendering controls and engine behavior |
| MX Player | Android | Gesture and track-control patterns; verify current app policy, ads and TV behavior |
| PotPlayer / MPC-HC / MPC-BE | Windows | Advanced playback controls, subtitles and diagnostics as desktop references |
| IINA | macOS | Minimal UI and mpv-backed interaction as a desktop reference |

These are research targets, not an endorsement, code-reuse approval or assertion that each product uses a particular engine version.

### 9.3 Snapshot findings and cautions

- Media3 official release documentation showed stable 1.11.1 on the 2026-11-07 snapshot; pin the current stable version at implementation time rather than freezing this research snapshot.
- Media3 official docs list HLS MPEG-TS and fMP4/CMAF support, while the codecs inside those containers must also be supported.
- VLC Android's repository describes LibVLC as an embeddable engine and states the Android application and LibVLC engine have different license terms.
- mpv's official documentation recommends libmpv as its embedding API; mpv-android notes its own app is not a drop-in AAR.
- The IJKPlayer GitHub releases page currently reports no releases. Treat project maintenance and vulnerability response as unresolved pending commit-level review.
- Just (Video) Player is a useful Media3-based product reference, but its advertised feature list is upstream information and must be checked against the exact source/release and R700 hardware.

### 9.4 UTOS decision

**Primary:** Media3.
**Conditional secondary:** LibVLC, only for measured Media3 gaps.
**Research-only:** libmpv.
**Hold:** IJKPlayer.
**Not a standalone player choice:** FFmpeg.

Do not ship multiple engines merely to maximize a feature checklist. Each engine increases native binary size, ABI coverage, memory use, test matrix, security patch burden and license review. The R700 comparison must use the same local test fixtures, firmware, network and measurement procedure.

### 9.5 Official source links

- Media3 releases: https://developer.android.com/jetpack/androidx/releases/media3
- Supported devices: https://developer.android.com/media/media3/exoplayer/supported-devices
- HLS: https://developer.android.com/media/media3/exoplayer/hls
- DRM: https://developer.android.com/media/media3/exoplayer/drm
- VLC Android / LibVLC: https://github.com/videolan/vlc-android
- IJKPlayer releases: https://github.com/bilibili/ijkplayer/releases
- mpv libmpv embedding: https://github.com/mpv-player/mpv/blob/master/DOCS/man/libmpv.rst
- mpv-android: https://github.com/mpv-android/mpv-android
- Just (Video) Player: https://github.com/moneytoo/Player

Evidence boundary: official docs establish documented feature/support statements, not R700 performance. All product-specific compatibility, UX and stability conclusions remain unmeasured until local fixture and hardware tests.


## 10. Next-generation video codec decoder research (2026-11-07)

### Official status checks

- **AV2:** AOMedia's official AV2 specification site lists v1.0.0 as current, dated 28 May 2026. AOMedia announced the release in June 2026. The AV2 standard is therefore no longer merely awaiting finalization.
- **dav2d:** VideoLAN announced its AV2 decoder on 31 May 2026. The project page describes it as early and explicitly says it should not be used in production. It has ongoing work for correctness, conformance, SIMD, threading and platform support. It is not a mature UTOS dependency.
- **VVC / H.266:** Fraunhofer's VVdeC is a software decoder implementation supporting Main10. Fraunhofer documents SIMD and multithreading and says its implementation can enable live decoding up to 4Kp60 in its tested context. Do not generalize this to R700 ARM SoCs.
- **Android VVC:** Android 17 introduces VVC media type / MediaCodec / Codec2 and MP4 extractor support. AOSP explicitly does not provide a VVC software decoder; a vendor must supply the decoder. This is not evidence of VVC support on Android 12 / Toshiba R700.
- **AV1 / Media3:** AndroidX Media3 defaults to platform decoders but also offers optional software decoder extensions, including AV1, which must be separately built and included. Therefore the claim that Media3 can only use hardware decoding is incorrect.
- **AV1 software:** VideoLAN's dav1d is a cross-platform AV1 decoder for Android and other platforms, focused on speed and correctness. Actual real-time resolution/FPS depends on the target CPU and thermal envelope.

### Claims that must not be treated as established facts

Do not copy generic claims such as "AV2 is 5x AV1 complexity", "AV2 saves exactly 30%", "AV1 hardware starts at Snapdragon 8 Gen 2", "VVC is always HEVC x2", "VLC is the only all-in-one software decoder SDK", or "all VLC builds include dav1d/dav2d/VVC". These need exact, reproducible, profile- and workload-specific sources. Codec compression gains and decoder complexity vary by content, encoder settings, quality metric, bit depth and implementation.

### Decoder / engine relationship

Media3, IJKPlayer, LibVLC and libmpv are playback frameworks or integration layers, not synonymous with codec implementations. A build may use platform MediaCodec, FFmpeg decoders, dav1d, VVdeC or other libraries only if they are actually integrated, enabled and selected. Inspect exact source revision and build flags. Do not assume IJK or LibVLC automatically ships the newest decoder.

### Optimization areas

1. **Algorithm hot paths:** profile entropy decode, transforms, prediction, interpolation, in-loop filters and film grain before tuning.
2. **Parallelism:** schedule only codec-defined independent regions; AV1 tile parallelism is useful, but frame/CTU/CU dependencies in other codecs must be preserved.
3. **SIMD:** prefer maintained NEON/AVX paths from upstream decoders before custom assembly; require bit-exact/conformance tests.
4. **Memory/bandwidth:** measure reference-frame footprint, cache misses, copies, allocations, stride and memory bandwidth.
5. **Hardware pipeline:** use the vendor hardware decoder where present; record the actual MediaCodec/Codec2 component and secure status.
6. **Thermal-aware degradation:** do not disable normative bitstream tools such as ALF or CDEF to make an invalid/incomplete decoder. Prefer a lower-resolution/frame-rate rendition, disable optional post-processing, or clearly report unsupported playback.

### Candidate projects

- AV1: https://code.videolan.org/videolan/dav1d
- VVC: https://github.com/fraunhoferhhi/vvdec
- AV2 specification: https://av2.aomedia.org/
- AV2 reference software: https://github.com/AOMediaCodec/avm
- AV2 early decoder: https://code.videolan.org/videolan/dav2d
- Android Media3 supported formats: https://developer.android.com/media/media3/exoplayer/supported-formats
- Android VVC platform support: https://source.android.com/docs/core/media/vvc

All project adoption requires commit-level maintenance, security, license and Android ABI review. Public code is not a guarantee of product readiness.


## 11. 2026-11-09 weekly source and operations refresh

### Guovin IPTV-API
- Official repository: https://github.com/Guovin/iptv-api
- Latest checked release: 3.1.0, published 2026-10-12: https://github.com/Guovin/iptv-api/releases/tag/3.1.0
- License: AGPL-3.0. Python 3.14 is required. The release improves the Windows/macOS PySide6 desktop app, configuration/output-directory migration, large-channel handling, EPG/live sampling resource limits, Docker proxy handling and dependency security.
- The current config checked in this review has subscription EPG auto-discovery disabled and automatic source disabling disabled. Preserve these safe defaults until source trust and recovery tests pass.
- Upstream Compose sample persists config/output paths but uses a mutable latest tag. Trial must pin release tag and digest. Windows desktop support is upstream-documented; local Windows 11 + Docker Desktop/WSL2 compatibility remains U.

### IPTV Checker
- Official repository: https://github.com/zhimin-dev/iptv-checker
- Release tag v5.0.2 was published 2026-09-25; the release body is empty. The checked CHANGELOG still starts with 5.0.1, so the exact 5.0.2 delta is U.
- License: MIT. README documents Docker, Compose and CLI, HTTP plus FFmpeg forced checks, scheduled tasks, EPG, M3U/TXT and IPv4/IPv6 exports.
- Prior changelog entries include SSRF and path-traversal hardening; this is not a substitute for a current security review.
- Status: Study / isolated Trial candidate; no deployment or R700 test.

### Docker Hub-only candidates
The following are separate image identities; a corresponding public source repository, explicit license or verifiable SBOM/provenance was not established in this review. Docker Hub tags and architecture are not proof of stream rights or functional compatibility.

| Image | Visible tag / freshness | Visible architecture | UTOS status |
|---|---|---|---|
| https://hub.docker.com/r/instituteiptv/smart-v1 | 0.1.2; last pushed about 6 months earlier | amd64, arm/v7, arm64 | Hold pending source/license/provenance |
| https://hub.docker.com/r/instituteiptv/iptv-4gtv | 1.1.3; last pushed 2 days earlier | amd64, arm64 | Hold; service-specific credentials and access rights required |
| https://hub.docker.com/r/instituteiptv/4gtv-v1 | Overview updated about 8 days earlier; README shows 2.4.5 | Not established in checked page | Hold; credentials and source/license unknown |
| https://hub.docker.com/r/instituteiptv/bee-v1 | 0.1.2; last pushed about 5 months earlier | amd64, arm/v7, arm64 | Study only; stale |
| https://hub.docker.com/r/instituteiptv/bee-v2 | 0.0.1; last pushed about 10 months earlier | amd64, arm/v7, arm64 | Hold; older image |
| https://hub.docker.com/r/instituteiptv/iptv-4gtv-system | 0.2.5; updated about 1 month earlier | amd64, arm/v7, arm64 | Separate project; do not merge with iptv-4gtv or 4gtv-v1 |

Do not run these images until source provenance, license, image digest, SBOM/CVE posture, persistent paths, authentication defaults and recovery behavior are reviewed. Do not use any proxy option to bypass DRM, paid access or geographic authorization restrictions.

### Operational candidate: DPanel
- Official repository and release: https://github.com/donknap/dpanel/releases/tag/v1.11.2
- v1.11.2 was published 2026-10-29. The custom LICENSE allows non-commercial use but restricts redistribution of derivative/modified/third-party versions without permission.
- Release notes state that Compose rollback restores YAML, .env and task settings only; it does not restore container runtime state or undo already-completed directory synchronization. Its in-memory backup may expire after one hour or be lost on panel restart.
- Docker socket access is a high-privilege control plane. Keep DPanel on Hold until license acceptance and a host-root-equivalent threat model are complete.

### Source links
- Guovin release: https://github.com/Guovin/iptv-api/releases/tag/3.1.0
- IPTV Checker release/changelog: https://github.com/zhimin-dev/iptv-checker/releases/tag/v5.0.2 and https://github.com/zhimin-dev/iptv-checker/blob/main/CHANGELOG.md
- DPanel license: https://github.com/donknap/dpanel/blob/master/LICENSE
- Docker daemon security: https://docs.docker.com/engine/security/
- Docker image attestations: https://docs.docker.com/build/metadata/attestations/
- Docker Compose secrets: https://docs.docker.com/compose/how-tos/use-secrets/
