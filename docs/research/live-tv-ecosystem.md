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
