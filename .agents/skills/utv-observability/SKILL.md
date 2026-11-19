---
name: utv-observability
description: Use for logs, metrics, traces, incident taxonomy, root-cause analysis, automated diagnosis, recovery and self-healing design.
---
# UTV Observability and Recovery

## Workflow
1. Define user-visible symptom and incident severity.
2. Collect timestamped, correlated, privacy-scrubbed logs and metrics.
3. Build a timeline and distinguish observation from hypothesis.
4. Classify failure domain: source, DNS, network, auth, parser, storage, player, decoder, UI, OS or device.
5. Rank hypotheses with evidence and a falsification test.
6. Recommend the least risky deterministic action first.
7. Separate diagnosis from mutation. Automated repair must use an allowlisted runbook, dry-run where possible, bounded retries, independent verification and rollback.
8. Record incident, root-cause confidence, fix and regression test.

## Guardrails
AI output is a hypothesis, not authorization. No unbounded retries, silent persistent changes or self-modification of safety thresholds.
