# Platform Capability & Compatibility Matrix

**Status:** Initial research map. No platform is certified by this project.

| Platform | Architecture / app model | Research focus | UTOS portability boundary |
|---|---|---|---|
| Toshiba R700 / Android 12 | Existing OEM Android TV host; exact firmware and privileges TBD | Home/default launcher, permissions, media, HDMI/CEC, DRM, update and recovery | First validation target; user-space first |
| Android TV / Google TV | Android framework and TV-specific app/system capabilities | TV app quality, media APIs, input framework, OEM extensions | Shared Android modules; OEM features need adapters |
| AOSP custom TV | AOSP framework, system services, HAL, kernel/vendor components | CDD/VSR, CTS/VTS, BSP, HAL, security patch and signing | Separate OS engineering program |
| Samsung Tizen TV | Tizen TV web/native application environment; model-year differences | SDK/API levels, media formats, certificates, device APIs, OS upgrade | Reuse concepts; rebuild integration |
| LG webOS TV | webOS TV application/runtime and device APIs | SDK, lifecycle, media, remote/input and model support | Rebuild adapter and packaging |
| Roku TV | Roku channel/app model | SceneGraph/BrightScript, playback, certification and device APIs | Reimplement runtime; share metadata/test concepts |
| Apple tvOS | Apple frameworks and focus-driven interaction model | Focus engine, SwiftUI/UIKit, AVKit, entitlements and distribution | Learn interaction principles; no API/app portability assumption |

## Status vocabulary
Unknown; Documented (official documentation only); Prototype; Verified (exact device/model/firmware test recorded); Unsupported; Not applicable.

All rows are research targets, not verified UTOS compatibility claims. Record model/year/API version and test evidence before changing status.
