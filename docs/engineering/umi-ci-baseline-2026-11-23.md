# UMI CI Baseline — 2026-11-23

**Scope:** Kotlin/JVM core module only.
**Device coverage:** None. This is hosted CI evidence, not R700 evidence.

## Successful run

- Workflow: UMI Core
- Run: https://github.com/xshotvivo/unified-tv/actions/runs/45051140556
- Tested commit: 1198e31e66cb4ce60972d9c496fe975724b62fc4
- Environment: GitHub-hosted Ubuntu 26.04 runner, Temurin JDK 17.0.21, Gradle 8.14.3.
- Command: gradle --no-daemon clean test
- Result: **PASS — BUILD SUCCESSFUL**
- Unit tests: 7 passed (4 M3U/TXT adapter tests, 3 TVBox JSON adapter tests).

## Failure found and corrected

The first CI run failed one M3U metadata test because the attribute parser's whitespace regular expression was over-escaped. The failure was reproduced in CI, corrected in the parser, and the subsequent run passed all seven tests.

Initial failing run: https://github.com/xshotvivo/unified-tv/actions/runs/45051043428

This is evidence that CI is exercising the tests and catching defects. It does not establish that the parsers cover every M3U/TVBox dialect or that any stream can be fetched or played.

## Latest core regression run

- Workflow run: https://github.com/xshotvivo/unified-tv/actions/runs/45051615757
- Tested commit: 72d4e8b295fad4aeb43790e599fd25e449bb7500
- Result: **PASS — BUILD SUCCESSFUL** (Gradle 8.14.3, JDK 17).
- Unit tests: **12 passed** — 5 M3U/TXT, 3 TVBox JSON and 4 catalog aggregation/replacement tests.
- New coverage: same logical channel from two sources retains both candidates; source snapshot replacement removes stale candidates without deleting another source; invalid mixed-source snapshot is rejected without mutating prior state; search is case-insensitive; a shared TVG ID maps to a stable cross-source resource identity while each source retains its own candidate.

## What this proves

- Gradle project configuration resolves on the hosted runner.
- Kotlin core and test sources compile.
- Current M3U/TXT and named TVBox JSON adapter tests pass.
- The automated workflow is wired to the feature branch.

## What this does not prove

- Android app builds or installs.
- Any real source endpoint is reachable or authorized.
- TVBox spider/JAR/JavaScript execution works.
- HTTP/HLS network resolution, redirect/SSRF policy or credential storage works.
- Resource persistence, global search, cross-source deduplication or recovery is implemented.
- Media3/LibVLC/VLC playback works in UTV.
- R700 playback, 2-hour soak or overnight stability.

## Next gate

Build the Android TV host app and integrate the core contracts. Then add transactional source persistence, a controlled local HTTP/HLS fixture server, candidate resolution and Playback System integration. The Windows 11/ADB environment and exact R700 firmware record are still required for device acceptance.
