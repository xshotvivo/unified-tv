# UMI Core

This is the first platform-neutral Kotlin core for UTV Universal Media Integration.

## Current scope

- Canonical source/resource/candidate/diagnostic data contracts.
- Defensive M3U/TXT ingestion with bounded input size and HTTP(S)-only stream locators.
- A named TVBox JSON adapter for the top-level sites catalog shape.
- Unit tests for expected and rejected input.

## Explicit non-goals in this first slice

- No arbitrary JavaScript, JAR, DEX or Python execution.
- No provider API search/detail/play execution.
- No network fetching, credential handling or URL dereferencing.
- No embedded player or Android UI yet.
- No claim of full TVBox compatibility.

The adapters parse source descriptions only. Network access, redirect policy, SSRF defenses, authentication, catalog persistence, resource search, playback resolution and player integration are subsequent gates defined in the UMI architecture and acceptance matrix.

Run tests from the repository root with:

    gradle --no-daemon clean test
