# Cloud Drive & Resource Sharing

**Status:** Proposed module specification; not deployed or validated.
**Initial posture:** Study OpenList, rclone and AList as replaceable adapters; no cloud account or token is configured.

## 1. Purpose and boundary

Provide one controlled entry to user-authorized cloud drives, remote shares and network storage. The module owns source identity, permissions, read/write policy, mount/share lifecycle, cache policy, provenance and diagnostics.

A remote mount is a file-access path, not a backup. NAS storage, cloud provider, file index, mount engine and TV playback client remain separate components.

## 2. Candidate roles

| Candidate | Role | Current posture |
|---|---|---|
| OpenList | Web file listing/API/WebDAV service across supported storage backends | Study; AGPL-3.0; release v4.2.9 dated 2026-10-25 |
| AList | Alternative listing/API/WebDAV service | Hold as a competing primary; AGPL-3.0; v3.66.0 dated 2026-10-21 |
| rclone | Mount/sync/transfer engine | Study; MIT; v1.76.0 dated 2026-11-06 |

Do not deploy OpenList and AList as competing primaries without a migration reason. Use rclone only where local filesystem semantics or scheduled transfer are actually required.

## 3. Architecture

- CloudProviderAdapter: provider identity, supported operations, rate limits and authentication method.
- RemoteFileProvider: list, stat, read, range/seek, optional write and delete.
- MountManager: mount/unmount state, read-only default, cache limits and recovery.
- ShareManager: local share identity, expiry, revocation and audit.
- CredentialVault: token storage, rotation and least-privilege scopes.
- PlaybackHandoff: pass file metadata and a temporary access capability to the playback layer without exposing long-lived credentials.

## 4. Data and security rules

- Read-only by default. Write/delete operations require explicit module policy and user confirmation.
- Never place provider tokens, cookies or private subscription URLs in Git, Compose files, logs or screenshots.
- Prefer Docker Compose secrets or a dedicated credential vault over plaintext environment variables.
- Keep WebDAV and management endpoints LAN/VPN-only; no anonymous public listing.
- Pin image version and digest; review license, SBOM, provenance, CVEs and volume permissions.
- Bound VFS/cache size and eviction; distinguish cache from original data.
- Do not follow arbitrary redirects with authorization headers; verify HTTPS and provider host boundaries.
- Record file provenance and remote source; do not treat mounted content as locally owned or licensed.

## 5. Playback acceptance

P0 tests in a disposable environment:
1. Read-only mount and token rotation.
2. Large directory listing and pagination.
3. HTTP range, seek, resume and interrupted transfer.
4. 4K sample with representative HEVC/AV1, HDR, audio and subtitles.
5. Cold cache versus warm cache; RAM, CPU, disk cache and network use.
6. Remote provider rate limit, token expiry, network outage and mount recovery.
7. Backup/restore of service config and credential metadata without exposing secrets.

R700/Kodi/VLC compatibility remains U until exact clients and hardware are tested.

## 6. Adoption gates

Study → isolated Trial → Adopt only after license/security review, read-only and write-policy tests, restore drill, cache/resource tests and playback acceptance. No candidate is currently Adopt.
