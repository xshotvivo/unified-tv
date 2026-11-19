---
name: utv-engineering
description: Use for implementing or modifying code, fixing bugs, refactoring, adding tests and reviewing diffs. Default for routine coding tasks.
---
# UTV Engineering

## Routine workflow
1. Read project rules and inspect git status.
2. Reproduce or localize the issue before changing code.
3. Identify the smallest responsible component and existing patterns.
4. Make a minimal, focused diff. Avoid unrelated cleanup.
5. Add/update tests that fail before the fix when practical.
6. Run targeted tests, lint/type checks and relevant build.
7. Inspect the final diff and summarize actual commands/results.

## Major-change workflow
For cross-module, security, schema, player-engine or migration changes, add impact analysis, contract tests, independent review, regression and rollback planning.

## Guardrails
Do not run a full ceremony for a one-line fix. Do not claim tests passed if not run. Do not weaken tests or thresholds merely to make a build green. Justify every new dependency.
