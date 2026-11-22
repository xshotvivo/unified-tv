# ADR-0001: Establish a shared Universal Media Integration foundation

- **Status:** Proposed for implementation; not validated by a working vertical slice.
- **Date:** 2026-11-23
- **Decision owner:** UTV project owner.
- **Scope:** Shared source, plugin, protocol and resource integration contracts across UTV domains.

## Context

UTV must ingest resources from heterogeneous source families (TVBox JSON, M3U/TXT, local/NAS/cloud files and provider APIs), interpret different schemas and transports, preserve provenance, and hand candidates to playback without coupling domain modules to specific plugins or engines.

Existing Live TV, Media Center, Cloud Drive and Playback System specifications already define domain ownership. A new foundation must connect them without duplicating responsibilities.

## Decision

Establish **UTV Universal Media Integration (UMI)** as a shared architecture and implementation workstream.

1. Separate source family, provider schema, plugin runtime, transport protocol, media container/codec and DRM/access policy.
2. Use versioned source adapters and canonical resource/candidate contracts.
3. Preserve provenance and multiple candidates; deduplicate logical identity without discarding source-specific records.
4. Keep plugins capability-limited and deny-by-default. No arbitrary third-party code execution in the first vertical slice.
5. Keep Playback System engine-neutral. Use Media3 as the first embedded Android engine candidate per the current playback specification; use the installed VLC Android app as an external comparison baseline on the same device and fixtures. Evaluate embedded LibVLC only for reproducible compatibility gaps.
6. Use transactional refresh and last-known-good snapshots.
7. Make failures typed, observable, bounded and recoverable. Fail over only among configured, authorized and compatible candidates.
8. Implement and validate on Android TV/R700 first, then port shared contracts through native platform adapters.

## Alternatives considered

### A. Each domain implements its own source/plugin/protocol stack
Rejected: duplicates parsing, security, diagnostics and compatibility behavior; creates inconsistent identities and recovery.

### B. Adopt one existing TVBox application/framework as the UTV core
Rejected for now: platform, license, runtime, maintenance and product-boundary constraints are not proven; creates avoidable coupling and lock-in.

### C. Universal plugin runtime before a working product slice
Rejected: large security and compatibility surface before core ingestion-to-playback behavior is proven.

### D. One universal player/decoder
Rejected: transport, source schema, plugin runtime and decoder are different concerns; no single engine guarantees every codec, DRM path or platform.

## Consequences

### Positive
- One reusable contract surface across Live TV, Media Center and cloud/resource adapters.
- New sources can be added without rewriting search, resource identity or player UI.
- Failure provenance and source health can be measured consistently.
- Runtime expansion can be gated by platform and security evidence.

### Costs and risks
- Requires disciplined schema versioning and contract tests.
- Canonical identity/deduplication is difficult for ambiguous media.
- Plugin sandboxing differs by OS and cannot be assumed from an interpreter.
- Source formats and remote services can change without notice.
- Full cross-platform parity is not guaranteed.

## Revisit conditions

Reopen this ADR if implementation shows unacceptable latency/complexity, a supported standard offers a demonstrably safer/simpler contract, or device tests contradict assumptions. Do not reopen solely for speculative feature expansion.
