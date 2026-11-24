# Adaptive Runtime Mechanism Control

Status: Proposed engineering specification (not implemented)

## 1. Purpose

OriginOSTV agents must be able to change not only a task plan, but also the mechanism used to execute that plan when runtime evidence shows the current mechanism is ineffective.

A mechanism is a reusable way of working: a parser, transport, scheduler, retry policy, cache policy, playback engine, workflow, tool chain, or agent topology. Changing a parameter is not necessarily changing a mechanism.

## 2. Core control loop

Observe -> Diagnose -> Select control level -> Apply bounded change -> Verify -> Keep or rollback -> Learn.

The controller must distinguish:
- **Parameter adaptation**: timeout, concurrency, backoff, buffer size.
- **Strategy switching**: alternate source, parser, resolver, player, route, or scheduling policy.
- **Mechanism replacement**: replace a failing subsystem or workflow with a different implementation.
- **Architecture evolution**: change component boundaries, contracts, or topology. This is a planned engineering change, not an unrestricted live mutation.

## 3. Runtime evidence and diagnosis

Every adaptation decision records:
- objective and acceptance predicate;
- observed signals and time window;
- failure stage and classified cause;
- candidate mechanisms and compatibility checks;
- expected benefit and regression risks;
- selected action, policy/approval basis, and rollback action;
- before/after measurements and verification outcome.

Do not infer root cause from a single timeout or transient failure. Use bounded retries, independent signals, and confidence thresholds. If evidence is insufficient, collect more evidence or request human review.

## 4. Mechanism selection

Use the least disruptive effective control first:
1. Correct invalid input or configuration.
2. Adjust a bounded parameter.
3. Switch strategy or provider.
4. Switch implementation behind a stable interface.
5. Reconfigure component topology through a versioned migration.

Candidate mechanisms must pass capability, platform, security, resource, and compatibility checks before activation.

## 5. Safe runtime change protocol

1. Snapshot current configuration and mechanism version.
2. Prepare the candidate in isolation.
3. Run contract and health checks.
4. Canary on a limited workload or one resource.
5. Compare success rate, latency, error rate, resource use, and user-visible quality against the baseline.
6. Promote only when the acceptance predicate passes.
7. Otherwise roll back automatically and preserve diagnostics.

Never hot-swap a component while it owns non-transferable state unless a tested handoff protocol exists.

## 6. Stability controls

- Circuit breaker: stop repeating a mechanism after a defined failure threshold.
- Retry budget: retries are bounded and use backoff/jitter.
- Change budget: cap the number and blast radius of adaptations in a time window.
- Hysteresis: require sustained evidence before switching back, preventing oscillation.
- Cooldown: prevent rapid repeated mechanism changes.
- Idempotency: make recovery safe to repeat.
- Dependency guard: do not replace a shared dependency in a way that breaks unrelated modules.
- Audit log: retain decision, action, actor, version, and outcome.

## 7. Authority and safety

Autonomy is tiered:
- **Tier 0**: observe and recommend.
- **Tier 1**: reversible, low-risk parameter changes within configured limits.
- **Tier 2**: reversible strategy/implementation switches with canary and rollback.
- **Tier 3**: code deployment, firmware/system changes, data deletion, security-policy changes, purchases, or actions with broad impact require explicit approval.

The agent must never bypass authentication, platform security, licensing, or user consent to achieve a task.

## 8. Completion predicate

A task is complete only when:
- the user-visible objective is met;
- the acceptance predicate passes on the target environment;
- no unresolved critical regression remains;
- the final mechanism/configuration is recorded;
- rollback information and test evidence are retained.

A successful command or green unit test alone is not proof of end-to-end completion.

## 9. UMI example

For a media source that repeatedly fails:
- classify whether failure is input/schema, DNS/TLS, HTTP, provider protocol, parsing, stream resolution, decoder, or rendering;
- stop retrying the same failing stage without new evidence;
- switch parser/transport/resolver/player only when the relevant contract is supported;
- canary against a known-good source and the failing source;
- verify actual playback and stability;
- roll back if the candidate increases failure rate or causes regressions.

## 10. Initial implementation sequence

1. Define typed failure stages and structured diagnostic events.
2. Add mechanism registry with capability declarations and version identifiers.
3. Implement policy engine with bounded retry/change budgets and circuit breakers.
4. Add snapshot, canary, health-check, and rollback interfaces.
5. Add deterministic tests for switch, rollback, oscillation, and unsafe-change rejection.
6. Integrate one low-risk UMI path first; expand only after measured acceptance.

This document defines a target mechanism. It does not claim that runtime self-reconfiguration is already implemented.
