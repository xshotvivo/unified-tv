# UTOS APK Analysis Center

**Status:** Proposed module and security design; no analyzer implementation or malware verdict capability is claimed.
**Snapshot:** 2026-11-10
**Parent:** UNIFIED TV / UTOS
**Related:** Super Player, AI Agent, Reliability Engineering

## 1. Mission

Provide a safe workflow to submit Android APK files for inspection, understand their technical structure and assess whether they are suitable as research candidates or dependencies for UTOS.

Two modes:
1. **Local static inspection:** analyze APK metadata and contents without installing or executing the app.
2. **Optional ChatGPT-assisted review:** submit selected artifacts or extracted, sanitized findings to ChatGPT for explanation, code/config review and report drafting, subject to explicit user consent and active ChatGPT upload/tool limits.

An APK upload is not permission to install, execute, redistribute, publish, or upload it to a third-party scanning service.

## 2. What can be inspected

### Package and build metadata
- Application ID, package name, version name/code, minSdk/targetSdk and compile SDK where available.
- APK signing certificate information, signature scheme presence and certificate fingerprints.
- Split APK relationships where supplied files permit identification.
- ABI support, native library inventory, resources and assets.
- Debuggable/test-only flags and build metadata where exposed.

### Manifest and permissions
- Exported activities, services, receivers and providers; intent filters and deep links.
- Permissions requested and declared, with protection levels where determinable.
- Cleartext traffic configuration, network security config references, backup/debug flags.
- Foreground services, boot receivers, accessibility, overlay, VPN, package visibility and device-admin declarations.
- SDK compatibility and potentially risky permission/component combinations.

### Code and dependencies
- DEX inventory and package/class references where practical.
- Embedded third-party SDK and library fingerprints.
- Native shared libraries by ABI; identify FFmpeg, VLC, mpv, Media3-related and other multimedia components only when evidence supports identification.
- Strings/configuration relevant to player behavior: user-agent, HTTP stack, headers, timeouts, retry logic, cache settings, protocols and codec references.
- Network endpoints/domains extracted from static resources or code, with uncertainty labels. A string is not proof of an active connection.
- Potential hard-coded secrets as high-risk findings, but never echo secrets in reports.

### Player-focused inspection
- Identify likely playback engine/version from metadata, DEX references, native libraries and dependency metadata.
- Look for Media3/ExoPlayer, LibVLC, mpv, FFmpeg, IJKPlayer or custom native playback components.
- Inspect references to HLS, DASH, MPEG-TS, RTSP, RTMP and HTTP-FLV; references do not prove full protocol support.
- Search for buffer/load-control configuration, live offset, retry/backoff, HTTP data source, user-agent, connection timeout and cache configuration.
- Identify decoder selection/fallback and playback telemetry hooks where visible.
- Record obfuscation, packing, dynamic loading or encrypted assets that limit inspection.

## 3. What static inspection cannot prove

Static analysis alone cannot prove:
- that the app is malware-free or safe to install;
- that an endpoint is currently active, authorized or reliable;
- actual live playback smoothness, buffering rate, first-frame time or decoder throughput;
- runtime behavior hidden behind native code, reflection, dynamic loading, encrypted assets or server-delivered configuration;
- that a library fingerprint corresponds to a specific unmodified upstream version;
- that a permission is actually exercised at runtime.

Label findings as observed, inferred, not found, or not inspectable. Absence of a static indicator is not proof of absence at runtime.

## 4. Safe processing pipeline

1. **Receive:** accept APK only through explicit user-selected upload. Record filename, size and SHA-256.
2. **Quarantine:** store as inert binary; never auto-install or execute.
3. **Validate:** verify file type by content, archive integrity, size limits and ZIP entry/path safety. Reject malformed or resource-exhaustion archives.
4. **Extract safely:** enumerate and extract only required entries in a sandbox with path traversal, decompression bomb, symlink and file-count protections.
5. **Inspect:** parse manifest, signing metadata, DEX/native inventory, resources and strings using pinned, maintained tools.
6. **Correlate:** compare dependencies and player signatures against known upstream artifacts; preserve confidence and source links.
7. **Report:** provide summary, detailed findings, evidence locations, severity, confidence, limitations and recommended next tests.
8. **Optional ChatGPT review:** ask before transmitting the APK or extracted content outside the local analysis boundary. Prefer a sanitized report or selected files over the entire APK where sufficient.
9. **Retain/delete:** apply a short, explicit retention policy; allow deletion of the uploaded APK and generated artifacts.

## 5. Security and privacy controls

- APK is untrusted input. Never execute its code during static analysis.
- Use a sandboxed, non-privileged worker with CPU, memory, wall-time, file-count and expanded-size limits.
- Keep analysis offline by default. Reputation/dependency lookup is a separate explicit network action with domain allowlists.
- Do not install APKs on the user's TV or phone automatically.
- Do not upload APKs, code, extracted strings or certificates to ChatGPT or external scanners without explicit consent.
- Redact secrets, tokens, personal identifiers and private URLs.
- Never claim virus-free, safe, official, unmodified or vulnerability-free based only on static scanning.
- Preserve original hash and tool versions for reproducibility.
- Do not bypass licensing, DRM, authentication or access controls.

## 6. ChatGPT-assisted analysis

ChatGPT may:
- explain manifest entries and permissions;
- summarize likely architecture and player-engine clues;
- review extracted source/configuration snippets;
- compare dependency fingerprints with official upstream releases;
- explain suspicious patterns and prioritize manual review;
- generate a structured technical report and runtime test plan.

ChatGPT must not:
- execute or install the APK;
- claim full dynamic malware analysis;
- certify the APK as safe;
- upload it to other services without explicit approval;
- treat obfuscated or inaccessible code as benign.

When the ChatGPT client accepts the APK attachment, the user may attach it directly to the conversation. If the client does not accept APK binaries or the file exceeds upload limits, use a local analyzer to produce a report or selected text artifacts such as manifest, dependency inventory, native-library list and hashes. Do not rename an APK to another extension to bypass upload restrictions.

## 7. Risk model and report format

Each finding includes:
- finding ID and title;
- severity: Critical / High / Medium / Low / Informational;
- confidence: High / Medium / Low;
- evidence type: observed / inferred / not found / not inspectable;
- file path and byte/class/resource location where available;
- why it matters and false-positive considerations;
- recommended validation or mitigation.

Report sections:
1. APK identity and SHA-256.
2. Package/build/signature overview.
3. Permissions and exported components.
4. Network and security configuration.
5. Player engine and multimedia dependencies.
6. Playback-related configuration clues.
7. Native libraries and ABI coverage.
8. Suspicious or risky patterns.
9. Inspection limitations and uninspected areas.
10. Recommended runtime tests and next steps.

## 8. Integration with Super Player research

APK analysis is supporting evidence, not a substitute for runtime testing.

For a candidate IPTV player APK:
- identify likely playback engine and multimedia library versions;
- inspect visible buffering, cache, HTTP and retry configuration clues;
- compare manifest/network configuration;
- extract a dependency inventory;
- then run a separately authorized device test using the same stream URL and controlled network conditions.

Do not infer that an app plays smoothly merely because it bundles Media3, VLC or mpv. Playback performance is empirical for the app/source/device/network combination.

## 9. Delivery phases

- **P0:** file handling, threat model, consent, retention and report schema.
- **P1:** safe metadata/manifest/signature/ABI inventory.
- **P2:** DEX/native dependency and multimedia engine fingerprinting.
- **P3:** player-specific configuration and static risk heuristics.
- **P4:** optional ChatGPT report handoff with explicit consent and redaction.
- **P5:** optional controlled runtime test harness on a dedicated test device; no automatic installation on personal devices.

## 10. Evidence status

- **A:** official Android APK/manifest/signing documentation and upstream tool documentation, to be attached during implementation.
- **C:** proposed inspection heuristics and architecture.
- **D:** product requirements and delivery phases.
- **B:** no APK has been analyzed or runtime-tested as part of this document.

This is a proposed design. No APK scanner, sandbox, ChatGPT upload integration or malware detection engine is implemented by this document.
