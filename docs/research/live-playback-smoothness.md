# UTOS Super Player — Live Playback Smoothness Research

**Status:** Research baseline and proposed validation plan; not an implementation or benchmark report.  
**Snapshot:** 2026-11-10  
**Parent:** UNIFIED TV / UTOS  
**Related:** `docs/modules/super-player.md`, `docs/research/mytv-android-case-study.md`

## 1. Executive decision

Live playback smoothness is a P0 product requirement. A television player that cannot sustain watchable playback has no meaningful value, regardless of EPG quality, channel count or interface polish.

**Decision:** establish a playback-quality baseline and root-cause classification before selecting or tuning engines. Use AndroidX Media3 / ExoPlayer as the first implementation candidate, keep LibVLC as a controlled compatibility comparator, and evaluate libmpv only if repeatable test evidence shows an unmet requirement. Do not ship multiple engines merely because more engines sound reassuring.

EPG is metadata. It must not be on the critical path for media preparation, buffering, decoding, rendering, channel switching or recovery.

## 2. What “lag” actually means

Do not use one generic “卡顿” label. Separate at least these observable symptoms:

| Symptom | Observable signature | First suspects |
|---|---|---|
| Rebuffer / freeze | Playback position stops; player enters buffering or stalls while loading | Throughput, packet loss, server, manifest/segment delivery, buffer policy |
| Frame judder / dropped frames | Playback position advances but frames are late/dropped | Decoder throughput, renderer, frame-rate mismatch, thermal/CPU/GPU pressure |
| Audio/video desync | Audio and video clocks diverge or one track stalls | Timestamp discontinuity, malformed stream, track/renderer issue |
| Slow first frame | Long request-to-first-frame duration | DNS/connect/TLS, redirects, playlist/manifest, keyframe distance, startup buffer |
| Slow channel zap | Long time from channel selection to stable first frame | Teardown, DNS/connection reuse, manifest/segment wait, keyframe cadence, over-buffering |
| Repeated reconnect | Session cycles through connecting/buffering/retry | Unstable source/server, retry policy, expired authorization, broken playlist |
| Periodic stutter | Repeatable stalls at intervals | Segment cadence, playlist refresh, token expiry, server throttling, memory/thermal pressure |

A black screen, a spinning indicator and visible frame judder are not interchangeable evidence.

## 3. Root-cause tree

### A. Source and stream packaging
- Stream origin overloaded, geographically distant, rate-limited or unstable.
- Variable bitrate spikes exceed available sustained throughput.
- HLS playlist/segment cadence, discontinuities, timestamps or target duration are problematic.
- MPEG-TS may lack access-unit delimiters (AUDs) or IDR keyframes expected by a demuxer.
- Codec profile, level, bit depth, chroma format, resolution or frame rate exceeds device decoder capability.
- Audio codec or transport/container combination is unsupported or malformed.

### B. Network and HTTP behavior
- DNS delay/failure, TLS handshake delay, redirects, connection resets, packet loss or jitter.
- HTTP headers, user-agent, referer, cookies, range requests or authorization differ between clients.
- CDN route, IPv4/IPv6 path, DNS resolver or ISP peering differs.
- Retry/backoff and timeout behavior is too aggressive, too passive or unbounded.
- Wi-Fi signal quality, interference, congestion or Ethernet link quality is poor.

### C. Player and buffering policy
- Buffer thresholds are inappropriate for the stream and network conditions.
- Startup buffer is too small for jitter, or excessively large for channel zapping.
- Live-edge offset / target live offset is unstable.
- HLS chunk/segment loading, playlist refresh or timestamp handling is incorrect.
- Retry policy repeats a permanent failure or abandons a transient one.
- Player is accessed from the wrong thread; UI/EPG work blocks critical playback work.
- Channel switch teardown leaves stale player, surface, decoder or network resources.

### D. Decoder, renderer and device
- Hardware decoder cannot sustain the stream's codec/profile/resolution/frame rate.
- Device falls back to software decoding and saturates CPU.
- Surface/render pipeline misses frame deadlines.
- Thermal throttling, memory pressure, competing apps or exhausted codec instances.
- Android firmware/vendor codec bugs or HDMI/display refresh mismatch.

## 4. Critical evidence from upstream documentation

1. Android Media3 troubleshooting explicitly identifies insufficient device decode throughput as a cause of stuttering, including streams whose bitrate or resolution exceed device capability. It also documents a specific MPEG-TS case: missing AUDs or IDR keyframes can leave ExoPlayer apparently stuck buffering; extractor flags exist as compatibility options, with computational or visual-corruption trade-offs.  
   - https://developer.android.com/media/media3/exoplayer/troubleshooting  
   - https://exoplayer.dev/issues/cleartext-not-permitted (MPEG-TS troubleshooting section)

2. Media3 exposes `LoadControl` to control when and how much media is buffered. Its defaults are not universal proof of optimal IPTV behavior; tuning must be tested against startup latency, rebuffer rate and live-edge distance together.  
   - https://developer.android.google.cn/reference/androidx/media3/exoplayer/ExoPlayer  
   - https://github.com/androidx/media/blob/release/libraries/exoplayer/src/main/java/androidx/media3/exoplayer/DefaultLoadControl.java

3. Media3 player state distinguishes `STATE_BUFFERING` from `STATE_READY`; telemetry should record state transitions and whether playback position/frame presentation advances, rather than infer buffering from UI appearance alone.  
   - https://developer.android.google.cn/media/media3/exoplayer/listening-to-player-events

4. VideoLAN documents a network caching setting for VLC for Android and warns that larger caching can reduce buffering on slow networks but increases waiting time. This supports testing cache policy as a trade-off, not simply maximizing cache.  
   - https://docs.videolan.me/vlc-user/android/3.5/en/more/settings/extra_advanced.html

5. VLC for Android advertises network-stream support including HLS and DASH; LibVLC is therefore a credible comparison engine, but protocol support does not guarantee every provider stream is valid or smooth on a specific device.  
   - https://images.videolan.org/vlc/download-android.html  
   - https://github.com/videolan/vlc-android

## 5. First principle: isolate source, player and device

Use a controlled matrix. The exact same URL must be tested across players, and each player must be tested against the same device/network/time window as far as possible.

| Test | Source | Player | Network/device | Purpose |
|---|---|---|---|---|
| T0 | Local known-good HLS/TS fixture | Media3 | Target device | Establish decoder/renderer baseline without internet source |
| T1 | Same live URL | Media3 | Same device/network | Baseline live behavior |
| T2 | Same live URL | LibVLC | Same device/network | Compare engine behavior |
| T3 | Same live URL | Existing player | Same device/network | Compare observed product behavior |
| T4 | Same source, lower rendition if available | Each engine | Same device/network | Separate decode/bitrate ceiling from transport |
| T5 | Different known-good source, same protocol/codec | Each engine | Same device/network | Separate provider-specific faults |
| T6 | Same source at another time / wired vs Wi-Fi | Same engine | Controlled change | Detect time/network dependence |

Do not compare different M3U entries and call the result an engine comparison. A playlist can point to different servers, codecs, bitrates and routes even when the channel names look identical.

## 6. Required telemetry

Every playback session should capture privacy-safe, correlated events:

- Request accepted, URI validation, DNS, connect, TLS, first response byte, manifest/container parsed.
- First audio output and first video frame; time-to-first-frame.
- Player state transitions, playback position progress and live offset where observable.
- Buffer duration/occupancy, bytes loaded, load duration, throughput estimate and retry count.
- Segment/manifest request status and duration; redact credentials and query tokens.
- Codec, profile, level, resolution, frame rate, bitrate and selected audio/video tracks when exposed.
- Decoder name/type, initialization failures, dropped frames, video frame processing offsets.
- CPU, memory, thermal status and network type where the OS exposes them.
- Channel selection to prior-session release, new prepare, first frame and stable playback.
- Error code, original engine exception, recovery action and verified recovery outcome.

Never store full credential-bearing URLs, authorization headers, cookies or raw stream payloads in routine logs.

## 7. Metrics and acceptance gates

No performance numbers are claimed yet. First collect a baseline with repeatable samples on the exact target TV and controlled sources.

| Metric | Definition | Reporting |
|---|---|---|
| First-frame latency | Select/play request to first rendered video frame | p50 / p95, sample count |
| Zap latency | Channel selection to first frame and to stable playback | p50 / p95 |
| Rebuffer ratio | Time spent rebuffering / total watch time | Per session and aggregate |
| Rebuffer frequency | Rebuffer events per viewing hour | Per source/protocol |
| Freeze duration | Total visible playback stalls | Per session |
| Dropped-frame ratio | Dropped frames / frames presented or processed, where supported | Per device/codec |
| Sustained session success | Sessions completing the defined watch window without fatal interruption | Numerator / denominator |
| Recovery success | Recoveries that return to stable playback within bounded attempts | Numerator / denominator |
| Live latency | Distance behind measured live edge, when measurable | Median / p95 |
| Resource cost | CPU, RAM, thermal and power indicators | By engine and stream class |

**Provisional test design (D, not measured):**
- Run at least 10 repeats per cell for initial screening; increase sample count for release claims.
- Use identical source URLs, device, app build, network path and test window for A/B engine comparisons.
- Include short zap tests and long-duration viewing tests; a player that starts quickly but stalls later fails.
- Record both median and tail behavior. Do not hide outliers.
- Keep source health separate from engine quality; classify a test as inconclusive if the origin itself is unstable.

Numeric SLOs are intentionally not frozen until the target device and representative source corpus are measured.

## 8. Playback policy experiments

Run controlled experiments, one variable at a time:

1. **Buffer policy:** compare default Media3 LoadControl with bounded alternatives. Measure first-frame, rebuffer ratio and live-edge distance. Never assume “more buffer = better player”.
2. **Live offset:** test supported HLS live configurations and target offsets. Avoid aggressive catch-up that creates visible speed changes or repeated stalls.
3. **HTTP stack:** compare connection reuse, timeouts, redirect handling and required provider headers without spoofing or bypassing access controls.
4. **Retry/backoff:** finite retries for transient failures; do not endlessly retry 401/403, invalid manifests or unsupported formats.
5. **Extractor compatibility:** only enable MPEG-TS compatibility flags when the stream evidence shows the relevant missing markers/keyframes; benchmark CPU and startup impact.
6. **Decoder path:** compare hardware and software only where supported and safe; capture actual decoder identity and dropped-frame telemetry.
7. **Surface/lifecycle:** test rapid channel changes, back/foreground, sleep/resume and repeated player release/recreate.
8. **Engine fallback:** switch to LibVLC only for classified compatibility cases, not as an indiscriminate second attempt for every network failure.

## 9. Architecture requirements

- EPG, channel metadata, artwork and UI refresh are outside the playback critical path.
- Playback has a dedicated session coordinator and bounded background I/O.
- Engine adapters expose normalized state, metrics and original error details.
- Engine selection is based on a reproducible compatibility matrix, not brand preference.
- Recovery is finite, cancellable and observable.
- A successful reconnect command is not considered recovery until first frame and a stable playback window are verified.
- AI diagnosis is optional; it must never delay startup, channel switching or playback recovery.
- The player must explain whether the failure is likely source, network, format, decoder, renderer, resource pressure or unknown.

## 10. Priority plan

### P0 — Establish truth
- Obtain one exact live URL that is known to stutter and one that is known to play smoothly.
- Compare the exact same URL in the smooth EPG player and the lagging player.
- Record player names/versions, device model/firmware, network path, protocol, codec, resolution and bitrate.
- Capture logs and player state transitions; determine whether the symptom is rebuffering or dropped frames.
- Build a repeatable test sheet and baseline.

### P1 — Media3 baseline
- Implement a minimal Android TV playback spike using current stable Media3.
- Add playback state, first-frame, buffer, load and decoder telemetry.
- Test known-good HLS, MPEG-TS and failure fixtures.
- Compare default LoadControl before tuning.

### P2 — Compatibility and recovery
- Reproduce real stream failures and classify them.
- Add bounded retries, reconnect and verified fallback.
- Evaluate LibVLC only on cases Media3 reproducibly fails.

### P3 — R700 validation
- Confirm exact hardware/firmware and decoder capabilities.
- Run identical fixtures on the Toshiba R700.
- Test long sessions, rapid zapping, Wi-Fi/Ethernet, sleep/resume and thermal pressure.
- Do not assume the previously supplied private IP is reachable or that ADB is enabled.

## 11. Current unknowns and blockers

- Which exact player is smooth and which player stutters?
- Is the exact same stream URL used in both players?
- Is the symptom buffering/freezing, frame judder, audio break-up or slow channel switching?
- What is the source protocol and codec?
- What are the target device model, Android/firmware version and network path?
- Are logs/diagnostic counters available from either player?

Until these are answered, the root cause remains **U (unknown)**. Network, source, engine, decoder and buffering are hypotheses, not findings.

## 12. Evidence and source register

- **A — Official documentation:** Android Media3 troubleshooting, ExoPlayer API/LoadControl, Player events, VideoLAN VLC Android documentation (links in §4).
- **C — Engineering inference:** the root-cause tree and proposed experiment ordering.
- **D — Design target:** P0–P3 plan, metrics and architecture requirements.
- **B — Measured:** none yet. No target-device benchmark or user stream has been independently tested in this research snapshot.

This research document is a decision and validation baseline. It does not claim a player has been implemented, built, tuned or proven faster.
