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


## 11. Advanced codec roadmap: AV1, VVC and AV2

### 11.1 Policy and evidence boundary

Codec support is a layered capability, not a player-brand feature. The complete path is container/demuxer → bitstream parser → decoder (vendor MediaCodec/Codec2 or software library) → color conversion/rendering → audio output. A codec label alone cannot establish compatibility.

**UTOS priority:**
- **AV1:** compatibility target for the first playback generation. Prefer vendor hardware decode where the device exposes a conforming decoder. Evaluate Media3's optional AV1 software decoder extension only as a controlled fallback on sufficiently powerful devices; it is not equivalent in power or thermal cost to hardware decode.
- **H.266/VVC:** forward-compatibility research. Use a vendor hardware decoder when the platform exposes it. VVdeC is a software decoder candidate for controlled trials, not an assumption of real-time 4K on every ARM TV.
- **AV2:** experimental / future-compatibility track. AOMedia published AV2 v1.0.0 on 28 May 2026. VideoLAN's dav2d is an early software decoder; do not ship it as a production default or promise real-time playback until conformance, performance, thermal and integration tests pass.
- **Legacy formats:** preserve robust H.264/HEVC/VP9/AAC/AC-3 and common container support before spending resources on next-generation codec support.

Do not claim fixed codec complexity multipliers, bitrate savings or universal SoC cutoffs as engineering facts without citing the exact test conditions, profile, bit depth, resolution, frame rate, encoder settings and quality metric. Compression efficiency is content- and configuration-dependent.

### 11.2 Decoder selection ladder

1. Parse container, codec, profile, level, bit depth, chroma subsampling, HDR metadata, frame rate and DRM requirements.
2. Query actual device decoder capabilities through Android platform APIs; record decoder name, hardware/software classification, supported profiles, size/rate limits and secure-decoder constraints.
3. Prefer a compatible vendor hardware decoder.
4. If hardware initialization fails, try another compatible platform decoder only when Media3 reports one; enable and test decoder fallback deliberately.
5. For AV1 only, optionally evaluate the Media3 AV1 software extension (dav1d-based) on capable devices. Gate by measured CPU headroom, sustained frame rate, memory, temperature and power.
6. Evaluate LibVLC / libmpv only for confirmed engine-specific gaps; do not assume adding a player engine automatically supplies an optimized decoder.
7. For VVC, evaluate vendor decoder first; VVdeC is a research software path subject to ARM build, performance, memory, licensing and thermal qualification.
8. For AV2, keep dav2d/AVM in lab builds and offline test tools. Do not enable automatic fallback on R700 or other low-power TV devices before device-specific proof.
9. If no safe decoder meets real-time requirements, fail clearly and suggest a compatible rendition/codec. Do not enter an endless software-decoder retry loop.

### 11.3 Performance engineering

Optimization work is ordered by evidence, not by speculative hand-tuning:

- **Algorithm / tool path:** profile hot functions first; optimize inverse transforms, interpolation, intra/inter prediction, entropy decode, in-loop filters and film-grain synthesis only where traces show material cost.
- **Parallel scheduling:** use codec-defined independent work units (e.g. AV1 tiles where legal); preserve frame/CTU/CU dependencies. Bound worker count to available big/little cores and thermal budget.
- **SIMD:** use maintained upstream NEON/AVX implementations (e.g. dav1d for AV1) before writing custom assembly. Validate bit-exactness and compare per-architecture.
- **Memory / bandwidth:** measure reference-frame footprint, cache misses, allocation churn, copy count, stride/alignment and memory bandwidth. Reuse buffers only with explicit ownership/lifetime guarantees.
- **Hardware pipeline:** prefer vendor MediaCodec/Codec2 hardware decode; inspect actual decoder selection and output path. App code cannot optimize a proprietary silicon pipeline it does not control.
- **Quality-preserving degradation:** do not arbitrarily disable normative decoder tools such as ALF or CDEF. A conforming decoder must implement required bitstream semantics. Degrade by choosing a lower-resolution/lower-frame-rate/lower-complexity source rendition, reducing optional post-processing, or declining unsupported media—not by silently skipping mandatory bitstream operations.

### 11.4 AV1-specific notes

- dav1d is a mature, cross-platform AV1 software decoder with ARM NEON and x86 SIMD optimizations; it is a candidate for controlled software decode, not a guarantee that 4K AV1 will run smoothly on a TV SoC.
- AV1 film grain synthesis is part of correct reconstruction when signaled. Do not silently disable it as a generic performance shortcut; measure its cost and preserve conformance/visual fidelity.
- Media3's AV1 software decoder extension is optional and must be explicitly built/included. Default Media3 decoding remains platform-dependent.

### 11.5 VVC / AV2 research candidates

- **VVdeC:** Fraunhofer's software VVC decoder supports Main10 and documents multithreading/SIMD work. Fraunhofer describes live software decoding up to 4Kp60 in its implementation context; this must not be generalized to R700 or arbitrary ARM hardware.
- **dav2d:** VideoLAN's AV2 decoder has early support and architecture optimizations, but its own project page warns against production use. Treat as experimental until stable API, conformance, security response, Android integration and target-device performance are established.
- **AVM:** AOMedia's reference software is the authoritative AV2 reference implementation for conformance/experimentation, not a consumer real-time playback engine.

### 11.6 Test matrix and acceptance

For each codec/profile, record:
- container and transport; codec profile/level; bit depth; chroma format; resolution; frame rate; HDR metadata; audio codec; DRM state;
- actual decoder component and hardware/software classification;
- cold start / warm start first-frame time; sustained decoded FPS; dropped frames; A/V sync;
- CPU per core, RAM peak, memory bandwidth where measurable, GPU/compositor load, temperature and power;
- 10/30/60-minute stability and thermal steady-state, plus seek/track-change/reconnect behavior;
- exact device model, SoC, firmware, Android API, engine version, decoder library commit and fixture hash.

A test passes only if playback sustains the source frame rate without unacceptable drops, audio stays synchronized, thermal behavior remains within device limits, and no crash/ANR/leak occurs. Thresholds must be established from the R700 baseline; no universal numbers are assumed.

### 11.7 Corrections to common claims

- Media3 is not hardware-only: it defaults to Android platform decoders and also has optional software decoder extensions, including AV1.
- AV1 hardware availability cannot be inferred from a generic chipset-generation slogan; query the actual device's decoder capabilities.
- AV2 v1.0.0 is published, but a published standard does not imply consumer SoC hardware support or mature playback deployment.
- Android 17 adds VVC platform interfaces, but AOSP does not include a VVC software decoder; vendor decoder availability remains device-specific.
- IJKPlayer / FFmpeg / VLC do not automatically guarantee dav1d, VVdeC or dav2d integration. Verify exact build flags, versions and decoder wiring.
- Never claim “VLC is the only one-stop SDK” or that every VLC build includes every software decoder. Verify the exact LibVLC build and enabled codecs.

### 11.8 Current status

**Design:** AV1 compatibility target; VVC research; AV2 experimental.
**Implementation:** no AV1/VVC/AV2 decoder integration has been completed for UTOS.
**Device evidence:** no R700 decoder capability dump or codec benchmark is available. All R700 performance claims remain U.
