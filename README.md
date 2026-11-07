# UNIFIED TV (UTOS)

A long-term research and engineering project for a coherent, modular, reliable smart-TV experience.

**Status:** Architecture discovery / foundation
**First validation target:** Toshiba R700, Android 12 (exact model and firmware TBD)
**Evidence rule:** Never present a design target or inference as a measured fact.

## Vision
Study the strongest ideas across Apple tvOS, Google TV, Android TV/AOSP, Samsung Tizen, LG webOS, Roku and selected open-source projects. Re-design useful capabilities around one coherent architecture rather than combining launchers or copying proprietary implementations.

## Product principles
1. Viewing-first: fast access to live TV, apps and media with minimal interruption.
2. User control: no forced advertising or recommendation feed in the UTOS experience.
3. Remote-first: predictable focus, visible focus state, short navigation paths and accessibility.
4. Modular: independently replaceable components behind stable contracts.
5. Preserve reliability: do not break OEM display, HDR, audio, HDMI/CEC, DRM/HDCP, tuner or OTA paths.
6. Evidence-led: separate verified facts, measurements, inference, design goals and unknowns.
7. Portable: isolate platform differences behind adapters; never assume vendor API parity.

## Independent product modules
- **[UTOS Media Center](docs/modules/media-center.md):** unified movie/series discovery, metadata, user library and playback handoff. Optional providers such as LibreTV remain isolated and replaceable.
- **[UTOS AI Agent](docs/modules/ai-agent.md):** natural-language intent, bounded task planning and controlled orchestration of approved UTOS services. No unrestricted system privileges.
- **Live TV / App Library:** separate domain modules; shared through explicit service contracts.

These are proposed specifications, not implemented or device-validated features. See [System Architecture](docs/architecture/system-architecture.md) and [Roadmap](ROADMAP.md).

## Scope boundary
The first R700 phase is an Android 12 experience and integration layer, subject to permissions and APIs actually available on the retail device. A launcher is not a standalone operating system. A true independent TV OS requires control of the system image, framework, system services, HAL, drivers, signing/update chain, security model and app ecosystem. No such capability is assumed here.

## Architecture and governance
- [Knowledge & Research Framework](docs/research/knowledge-framework.md)
- [Evidence & Validation](docs/engineering/evidence-and-validation.md)
- [Platform Matrix](docs/compatibility/platform-matrix.md)
- Important decisions belong in docs/decisions/ as Architecture Decision Records. Every research item needs a source, access date, applicability, license boundary and confidence. Every device claim needs model, firmware, test conditions and repeatability. Do not remove or disable system packages/services without a reversible test and recovery path.

## License
No license has been selected. Until one is added, do not assume this repository grants permission to reuse its contents.
