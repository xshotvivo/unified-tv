# Online Video Aggregation

**Status:** Optional isolated provider module proposed; no service deployed and no content sources configured.

## 1. Purpose and boundary

Study self-hosted video catalog/front-end projects such as LunaTV and MoonTV as replaceable discovery and playback-entry adapters. They are not NAS operating systems, general media servers, rights providers or guaranteed content sources.

Keep this module separate from:
- local/private media libraries and Jellyfin/Emby/Plex;
- general IPTV channel catalogs;
- Sports Live event catalogs;
- the shared playback engine.

## 2. Candidate findings (checked 2026-11-09)

- **MoonTechLab/LunaTV:** https://github.com/MoonTechLab/LunaTV. The repository description states CC BY-NC-SA; GitHub metadata did not identify a standard SPDX license. Last push observed 2026-09-10. Do not reuse or redistribute without reviewing the exact license and all bundled assets/dependencies.
- **Stardm0/MoonTV:** https://github.com/Stardm0/MoonTV. The repository declares MIT and says it self-maintains from LunaTV 2.0.1; last push observed 2026-11-05. Its README warns of Cloudflare Pages suspension risk. The fork's license declaration does not automatically settle rights for all inherited upstream code/assets.

No built-in content source, third-party source list or stream authorization is assumed. Sources must be configured by the user and must be lawful and authorized for the user's region and use.

## 3. Integration contract

- ProviderAdapter: catalog search, metadata, source provenance and capability reporting.
- PlaybackHandoff: user-selected item, expiry-bounded access capability and target-player requirements.
- ConfigStore: isolated provider URL, credentials and source settings.
- Diagnostics: sanitized provider and playback errors; no tokens or cookies in logs.

The module must be removable without changing Media Center, Live TV or the home shell.

## 4. Security and validation

- Prefer a private LAN/VPN deployment; no public unauthenticated admin interface.
- Review container image provenance, dependencies, persistent storage, registration policy, session security and update mechanism.
- Keep Redis/Kvrocks or other persistence choices explicit; verify restart/restore behavior.
- Test remote focus, Back, full-screen, subtitles, audio, seek, episode continuity, first frame and 2-hour playback on exact hardware.
- Do not bypass DRM, authentication, paid access or geographic restrictions.

LunaTV/MoonTV remain Study/Hold candidates. No R700 compatibility or performance result is claimed.


## 5. Android TV client candidate: Selene-TV

- Project: https://github.com/MoonTechLab/Selene-TV
- Role: Android TV (Leanback) client for MoonTV / Helios, optimized for D-pad navigation; it is not the VOD backend or a content provider.
- Latest checked release: v1.4.6, 2026-06-22. The release note says its MPV live buffer was deepened and automatic reconnect enabled for longer 4K live sessions; this is a maintainer claim, not an independent performance result.
- README claims minimum Android 6.0 / API 23, targetSdk 36, arm64-v8a and armeabi-v7a APKs, ExoPlayer (Media3) + mpv, embedded phone QR remote control and in-app update.
- **License:** GitHub repository metadata did not identify a license and the checked LICENSE path returned 404. Do not reuse or redistribute code until the author provides an explicit license.
- **Security review:** the README advertises compatibility with cleartext HTTP and self-signed TLS, and an embedded Ktor HTTP server for phone remote control. Verify certificate validation, server binding/authentication, update signature/integrity and network exposure before any trial.
- Android 12 is above the stated minimum API 23, but that only clears the published minimum-version threshold. R700 ABI, OEM firmware, decoder/DRM, remote navigation, update path and sustained playback remain U.

LunaTV/MoonTV README also mentions OrionTV as an Android TV client path. Keep OrionTV and Selene-TV as distinct client candidates; do not infer identical backend/API compatibility.
