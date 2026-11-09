# Retro Game Library

**Status:** Separate module scope established for research; no emulator or game package deployed.
**Reference:** User-provided TMXK article 142, https://tmxk.pp.ua/d/142-ceng-zhan-man-ni-tong-nian-zhou-mo-de-you-xi-zhe-li-quan-du-you-mian-xia-zai-ling-yan-gao-da-kai-jiu-neng-wan. The article is a discovery lead, not independent verification of every game or license.

## 1. Purpose

Create a remote-first library for legally obtained retro games, with artwork, platform/category metadata, search, controller mapping, emulator/runtime selection, save states and per-game settings.

## 2. Candidate platform families

Arcade, DOS, NES/FC, SNES/SFC, Mega Drive/MD, GBA, NDS, PlayStation, Flash and Java are research categories, not confirmed R700 capabilities.

## 3. Architecture

- GameCatalogProvider: title, platform, region, year, publisher and artwork.
- GameAssetProvider: local/user-provided game files and optional metadata.
- EmulatorRuntimeAdapter: per-platform runtime/core with exact version and license.
- ControllerProfile: D-pad, buttons, analog axes, hotkeys and per-game overrides.
- SaveStateStore: game saves, emulator save states, backup and restore.
- LaunchHandoff: suspend/resume and return-to-library behavior.

## 4. Legal and security boundaries

- Do not bundle, scrape, distribute or link unauthorized copyrighted ROMs, BIOS files or commercial game images.
- Use only user-owned files where lawful, public-domain works or homebrew with clear redistribution permission.
- Review each emulator/core license and all bundled dependencies before redistribution.
- Treat downloaded game files and metadata as untrusted input; sandbox runtimes and restrict filesystem access.
- Do not bypass platform DRM or console authentication.

## 5. R700 acceptance

R700 browser, Android app, GPU/API, controller and storage compatibility are U. P0 tests must identify exact emulator build, ABI, graphics backend, controller, frame pacing, audio latency, save-state integrity, memory and thermal behavior. No performance claim is made.

No ROM collection, emulator runtime or controller mapping has been tested in this project.
