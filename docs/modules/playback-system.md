# UTOS Playback System

**Status:** Proposed engineering baseline; not implemented or validated on Toshiba R700.
**Research snapshot:** 2026-11-07.
**First target:** Android TV / Toshiba R700, Android 12; exact firmware, SoC, RAM and decoder capabilities remain to be recorded.

## 1. Mission

Deliver a dependable, fast, remote-first playback experience for UTOS Live TV and Media Center. Treat playback as a complete system: source interpretation, engine selection, decoder compatibility, session lifecycle, user controls, diagnostics and recovery.

The goal is not to collect the largest number of engines. The goal is to play supported content reliably, explain failures accurately and recover without hiding the cause.

## 2. Product and engine taxonomy

### End-user applications (UX references)
- Android TV: VLC for Android, Just (Video) Player, Nova Video Player, Kodi, MX Player and other maintained candidates.
- Android phone/tablet: same products where form-factor behavior differs.
- PC: PotPlayer, mpv, VLC, MPC-HC/MPC-BE and IINA as desktop interaction/quality references.
- iOS: VLC and other media-player products as cross-platform UX references.

These are products to study or test. Their internal engines, licensing and feature support must be verified individually; do not assume an application uses a particular engine because it supports a format.

### Embeddable engines / SDKs
- Media3 / ExoPlayer: Android playback library and first implementation.
- LibVLC: embeddable VLC engine, conditional secondary candidate.
- libmpv: embeddable mpv API, research candidate.
- IJKPlayer: legacy FFmpeg/MediaCodec-based project, held pending maintenance/security review.
- FFmpeg: multimedia framework/library collection; not a complete end-user player by itself.

## 3. Architecture

`Live UI → Playback Orchestrator → Engine Selector → PlaybackEngine adapter → Native/Android decoder`

Shared services:
- Track and subtitle catalog
- Session lifecycle and surface management
- Playback analytics and diagnostics
- Error classification
- Retry/failover policy
- Device capability registry
- Local test fixture and regression corpus

Engine-specific implementations must remain behind a stable, versioned `PlaybackEngine` contract. UI and domain logic may not depend on ExoPlayer, VLC or mpv concrete classes.

## 4. Engine policy

### Media3 — primary
Use Media3 first for Android-native playback, HLS/DASH, supported progressive streams and DRM flows that the platform and content license permit. Its support is conditional on container, sample codec, Android API level, DRM scheme and device decoder. A protocol being supported does not imply every stream encoded within it is supported.

### LibVLC — conditional fallback
Evaluate only when repeatable tests show a meaningful Media3 compatibility gap. Potential strengths include broad format/protocol support and network filesystem access. Costs include native binaries, package size, memory/CPU overhead, build and security maintenance, and license obligations. LibVLC's Android engine license and the VLC application license are not the same; review exact notices and linking model.

### libmpv — research
Study rendering, subtitle, audio-track and advanced playback control capabilities. Do not add it to the first release until a measured requirement cannot be met by Media3/LibVLC and an Android TV integration trial proves the benefit.

### IJKPlayer — hold
Do not use as the new product's primary engine. Reconsider only after checking upstream commits, release practices, FFmpeg revision, CVE patchability, Android toolchain compatibility, ABI support and license. Historical adoption is not evidence of current suitability.

### FFmpeg — component only
Do not describe FFmpeg as a complete player. Use individual libraries/extensions only for a defined gap, with codec patent, distribution, security and license review.

## 5. Deterministic engine selection

1. Validate source policy and access requirements.
2. Identify protocol/container/manifest and declared media formats.
3. Check device capability and DRM requirements.
4. Select Media3 as default when compatible.
5. If Media3 fails with an engine-specific, reproducible unsupported-format/decoder issue, evaluate LibVLC if enabled and compatible.
6. If failure is network, expired URL, server, authentication/DRM or authorization-related, do not switch engines as a supposed repair.
7. Verify first frame/audio and a sustained sample after any switch.
8. Record selected engine, reason, versions, metrics and result.

Selection is deterministic and auditable. AI may explain evidence or suggest a policy change, but may not invent media URLs, bypass access controls or silently change credentials.

## 6. Product experience requirements

- Channel-up/down and direct channel selection with low-latency zapping.
- Predictable D-pad, OK, Back and media-key behavior.
- Minimal, low-occlusion playback HUD.
- Audio track and subtitle selection, language labels and supported delay controls.
- Aspect ratio, scaling and display options only when the engine/device exposes them.
- Honest live-edge/timeshift affordances; do not expose seeking when the stream does not support it.
- Clear buffering, reconnecting, source failure, decoder failure and unsupported-format states.
- Session resume and surface lifecycle without leaks or stale video frames.
- No forced advertising, autoplay recommendation feed, mandatory account or telemetry by default.
- Advanced diagnostics are optional and do not obstruct normal viewing.

## 7. Test corpus and acceptance

Create local synthetic fixtures before relying on public IPTV URLs. Include HLS TS, HLS fMP4/CMAF, DASH clear test vectors, progressive MP4/MKV, MPEG-TS, subtitles, multiple audio tracks, malformed manifests, truncated segments, unsupported codecs, slow network, DNS failure and server interruption.

On identical R700 firmware/network/fixtures, compare:
- first-frame time (median and p95 only with adequate sample count);
- channel-zap time;
- rebuffer count and total duration;
- dropped frames where exposed;
- audio/video synchronization;
- decoder and codec path;
- subtitle and track-selection behavior;
- RAM, CPU, thermal impact and APK size;
- crashes, ANRs, lifecycle leaks and 2-hour stability.

Record device, firmware, build, engine version, fixture hash, network, test order, repetitions and raw logs. Do not freeze performance thresholds until a baseline is measured. No engine is currently proven best on R700.

## 8. Security, licensing and maintenance

- Pin exact dependency versions and source commits.
- Review direct and transitive licenses, FFmpeg build configuration and codec components.
- Preserve notices and meet applicable source-distribution/linking obligations.
- Generate SBOM; scan native dependencies and define vulnerability response ownership.
- Bound parsers, network requests, redirects, memory, concurrency and decompression.
- Never treat public source availability as permission to redistribute code or media.
- Have legal review for commercial distribution; this specification is not legal advice.

## 9. Research references

- Android Media3 release/docs: https://developer.android.com/media/media3
- Media3 supported devices: https://developer.android.com/media/media3/exoplayer/supported-devices
- Media3 HLS: https://developer.android.com/media/media3/exoplayer/hls
- Media3 DRM: https://developer.android.com/media/media3/exoplayer/drm
- VLC Android / LibVLC: https://github.com/videolan/vlc-android
- mpv / libmpv: https://github.com/mpv-player/mpv
- mpv Android app: https://github.com/mpv-android/mpv-android
- IJKPlayer: https://github.com/bilibili/ijkplayer
- Just (Video) Player: https://github.com/moneytoo/Player
- Kodi: https://github.com/xbmc/xbmc
- Nova Video Player: https://github.com/nova-video-player/aos-AVP

References are research entry points. Re-check current upstream state, exact licenses and dependencies before adoption.

## 10. Evidence and maturity

- A: official documentation/repository facts checked on the snapshot date.
- B: repeated R700 measurements; none available yet.
- C: engineering inference, explicitly labeled.
- D: product targets and design decisions.
- U: unknown / pending verification.

This document is a design baseline only. No UTOS player build or R700 test result is claimed.
