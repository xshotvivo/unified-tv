---
name: utv-release-qa
description: Use for CI, builds, regression, device acceptance, release readiness, versioning, rollout, rollback and milestone closure.
---
# UTV Release and QA

## Release gates
1. Requirements and acceptance criteria trace to tests.
2. Source diff reviewed; no secrets, unrelated changes or unreviewed dependency drift.
3. Static analysis, unit and contract tests pass or exceptions are documented.
4. Integration and regression tests pass for affected modules.
5. Build artifact identifies commit, toolchain and configuration.
6. Device tests identify exact model, firmware, app build, setup, fixtures, duration and results.
7. Security, privacy, license and supply-chain review is complete for changed surface.
8. Backup, recovery and rollback are tested for risky changes.
9. Known limitations, release notes and evidence are recorded.

## Result states
PASS, FAIL, BLOCKED, NOT RUN. Never convert BLOCKED or NOT RUN to PASS. Build success is not device acceptance. One playback success is not sustained reliability.

## Freeze policy
Freeze only after evidence meets acceptance. Reopen for factual error, reproducible test conflict, security issue or acceptance blocker; otherwise schedule follow-up.
