# UTOS AI Agent

**Status:** Proposed module specification; not implemented or validated.
**Initial validation target:** R700 user-space prototype, restricted to capabilities exposed by approved UTOS services.

## 1. Purpose and boundary

UTOS AI Agent is an independent intent-understanding, planning and orchestration layer. It helps users discover content and operate supported TV functions through natural language, while the ordinary launcher, remote navigation, Live TV and playback continue to work if the agent is unavailable.

The agent is not the operating system, a privileged administrator or an unrestricted automation engine. It may invoke only registered capabilities through explicit policy checks.

## 2. Core components

1. Interaction: optional voice and text entry, turn state and user-visible task status.
2. Intent and context: resolve the request, identify ambiguity and gather only necessary context.
3. Planner: produce a bounded sequence of capability calls with preconditions and expected outcomes.
4. Tool gateway: expose an allowlisted registry of typed UTOS service operations.
5. Policy and consent: classify risk, check permissions and require confirmation for consequential actions.
6. Memory: user-controlled preferences and task context with inspect, edit, disable and delete functions.
7. Audit and recovery: record action, authorization, result and failure; support cancellation and compensating actions where safe.

## 3. Initial capability domains

- Media Center: search titles, compare metadata, open details and hand off a user-selected item.
- Live TV: search channels and program metadata; switch channels only through an authorized interface.
- App Library: find and launch installed applications.
- Device controls: adjust only controls exposed by the platform adapter and permitted by the user.
- Diagnostics: run read-only network, storage and performance checks where APIs permit.
- Help: explain settings and provide guided instructions when direct control is unavailable.

## 4. Permission model

Classify operations before execution:

- R0 — Read-only: search, explain, inspect permitted status. May run without an extra confirmation.
- R1 — Reversible low-impact: launch an app, change a temporary playback option or adjust volume within the active session. Follow user policy; provide visible feedback.
- R2 — Persistent or consequential: change persistent device settings, modify home layout, add/remove favorites or change provider configuration. Require explicit confirmation before execution.
- R3 — Destructive, security-sensitive or privileged: install/uninstall apps, clear application data, reset device, alter network/security settings, use ADB/root, change system partitions or firmware. Not allowed in the initial release. Any future support requires a separate security review and explicit authorization design.

Never infer consent from a vague request. Show the intended action and target before an R2 confirmation. Do not claim an action succeeded until the underlying service confirms it.

## 5. Execution lifecycle

Request → intent resolution → plan preview when needed → policy check → consent gate → capability invocation → result verification → user-facing summary → audit record.

Every tool call must have a typed schema, bounded arguments, timeout, error handling and least-privilege scope. The agent must not fabricate tool results. If a capability is unsupported or unknown, say so and offer a manual path.

## 6. Memory and privacy

- Local-first for preferences and device context where feasible.
- Cloud inference is optional and must disclose what context is sent.
- Store only information needed for an enabled feature.
- Provide user-visible inspect, edit, disable and delete controls.
- Do not store credentials, secrets or sensitive personal information as agent memory.
- No covert listening, background recording or advertising profile.
- Basic TV operation must not depend on memory or cloud availability.

## 7. Reliability and degraded operation

- Agent crash or network loss must not affect launcher, remote input, Live TV or playback.
- Use bounded retries, cancellation and idempotency keys for operations where supported.
- On uncertain execution outcome, verify state before retrying.
- Provide a visible stop/cancel control for multi-step tasks.
- Prefer reversible operations; where rollback is impossible, disclose this before confirmation.
- Maintain a deterministic manual route to every core TV function.

## 8. R700 staged validation

P0 — Threat model, permission matrix, data-flow map and tool schemas.
P1 — Offline/read-only prototype for search and help.
P2 — Allowlisted app launch and non-persistent playback controls.
P3 — Confirmation-gated persistent actions, only where supported by public APIs.
P4 — Reliability, privacy and usability evaluation on exact R700 model/firmware.

No root, ADB privilege escalation, package removal, system partition changes or firmware operations are in scope.

## 9. Evaluation and evidence

Measure intent success, task completion, unnecessary clarification, incorrect action rate, policy-block rate, cancellation latency, recovery success, response latency, memory footprint and network use. Set numeric thresholds only after defining representative tasks and collecting a baseline.

Use evidence labels: A = authoritative source verified; B = measured on target hardware; C = evidence-based inference; D = design target; U = unknown/not tested. Record model/version, date, conditions and failure cases.
