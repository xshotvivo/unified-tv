# UTV Engineering Skills

A governed, task-routed skill library for UNIFIED TV (UTOS). It is an engineering asset, not a claim that any module is implemented or device-validated.

## Goals
- Select the smallest sufficient workflow for each task.
- Preserve evidence labels: verified fact, measurement, inference, design target, unknown.
- Make changes reviewable, reversible, testable and traceable.
- Separate project-startup rigor from routine maintenance.

## Use in Codex
1. Pull the parent repository and open its root in Codex.
2. Read the root AGENTS.md and this library's AGENTS.md.
3. The repository now includes 12 Codex-discoverable skills under the root `.agents/skills/` directory. The detailed library copies under this folder are the governance/reference source; keep the two sets synchronized when editing skills.
4. Pull the latest repository in your local Codex workspace. Invoke a skill by its `name` or use ROUTING.md. Skills do not grant permissions or prove tests passed.

## Modes
| Mode | Trigger | Required depth |
|---|---|---|
| Startup | New module/repo, architecture boundary change | Requirements, architecture, threat/risk, test plan |
| Routine | Small bug, isolated refactor, UI adjustment | Minimal diff, focused tests, concise review |
| Major change | Cross-module, schema, player, security, migration | Impact analysis, independent review, regression, rollback |
| Release | Candidate build or milestone | Build, test, device evidence, limitations, recovery |

Full Superpowers-style planning/TDD is not the default for every task.

## Catalog
project-architect; requirements; engineering; context-memory; device-adb; player-engineering; iptv-engineering; security; multi-device; observability; ui-engineering; release-qa.

## Evidence
- A: authoritative source or directly inspected artifact.
- B: reproduced measurement with device/build/conditions/method.
- C: reasoned inference with assumptions.
- D: design target, not measured.
- Unknown: not established.

Never upgrade C/D to A/B without new evidence. Record what could falsify an inference.

## Safety and change policy
- Do not modify business code when authoring skills.
- Read before overwrite; prefer additive changes.
- Never commit secrets, API keys, private playlist URLs or personal data.
- No root, firmware flash, factory reset, system partition writes or irreversible device operation without explicit informed approval and tested recovery.
- Build success is not device acceptance; command success is not user-visible acceptance.
