# Architecture Addendum — 2026-11-16

**Status:** Proposed design clarification; not implemented or production-approved.

## 1. Cloud IPTV Hub

Add Cloud IPTV Hub as a distribution and device-identity service beside the existing IPTV Source Engine.

The Source Engine owns IPTV semantics:
- source ingestion and provenance;
- channel normalization and stable identity;
- deduplication while retaining multiple stream candidates;
- availability/first-frame/sustained health state;
- EPG parsing and channel mapping;
- rights and credential state.

Cloud IPTV Hub owns:
- immutable versioned playlist/EPG publication;
- device pairing, token issuance, revocation and policy;
- manifest/version API, cache and update notification;
- audit and rollback pointer.

Do not combine source crawling, stream proxying, video transcoding and device identity in one Worker. Heavy ingestion/probing runs in a controlled worker or GitHub Actions. Cloudflare Pages/Workers/R2 are distribution components only.

## 2. Music Center

Add Music Center as a first-level domain module. It owns music catalog, playlists, lyrics, history, favorites, provider configuration and listening discovery. It uses the shared Playback System and Media Library primitives but does not create another player engine.

Audio fidelity is a playback-path property. The source file, decoder, resampler, system mixer, DSP, output route and device must be recorded before a lossless/bit-perfect claim is accepted.

## 3. TVOS and UTV Movie Lite

TVOS is the working name for the Media Center's self-owned metadata/catalog/resource-library subsystem. It is not a second media database or a second player.

UTV Movie Lite is the lightweight Android TV experience consuming:
- TVOS/Media Library metadata;
- authorized Provider adapters;
- the shared Super Player;
- shared favorites, watch history and Device Bridge.

IPTV live-channel identity and VOD movie/series identity remain separate schemas. A poster, title or metadata result does not prove that content is available or authorized for playback.

## 4. Candidate learning boundaries

- Aerial TV product QR/cloud sync is a UX reference, not an open-source dependency.
- Aerial TV Launcher is Android TV desktop/ambient software, not a tvOS app.
- Swiftfin is a tvOS Jellyfin client, not an Android TV client.
- Folia is a lyric/visualizer/provider architecture reference with AGPL-3.0 obligations.
- AlgerMusicPlayer is a desktop Provider Registry reference; mobile/TV clients are no longer maintained according to the owner.
- Marco Movie and TVBox projects are candidate clients, not replacements for the UTOS domain contracts.

## 5. Status invariants

A research candidate is not an approved architecture dependency. A release asset is not an R700 compatibility test. A successful build is not a playback acceptance. A playlist fetch is not a channel playback test. A backup job is not a successful restore. A cloud subscription URL is not device pairing unless the client implements the pairing contract.
