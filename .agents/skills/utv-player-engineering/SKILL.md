---
name: utv-player-engineering
description: Use for playback architecture, VLC/Media3/LibVLC evaluation, codec compatibility, buffering, first-frame, A/V sync, subtitles, long-run freezes and diagnostics.
---
# UTV Playback Engineering

## Principles
Start with one engine behind a stable PlaybackEngine contract; add another only to close a reproducible compatibility gap. Separate source/network/auth failures from demux, decoder, renderer, audio and lifecycle failures. Do not switch engines or disable hardware decoding as a generic workaround.

## Workflow
1. Capture source type, container, codecs/profile/level, resolution, frame rate, bit depth, tracks and DRM status; redact private URLs.
2. Capture device MediaCodec inventory, firmware and app/player versions.
3. Reproduce with a local known-good fixture and same network where possible.
4. Measure first-frame time, rebuffer count/duration, dropped frames, A/V sync, decoder errors, memory, CPU, thermal and sustained duration.
5. Classify failure stage; use bounded retries only for transient classes.
6. Compare candidate engines on identical fixtures, firmware, settings and network.
7. Test lifecycle: background/foreground, sleep/wake, channel zapping, network loss, 2-hour and overnight runs as applicable.
8. Record limitations and rollback.

## Guardrails
VLC baseline is a comparison point, not root-cause diagnosis. Never claim 4K/HDR/codec support from a spec sheet alone; validate on target hardware.
