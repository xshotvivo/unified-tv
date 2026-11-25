# AIC Safety Gateway foundation

Status: code-level foundation in review. This is not a proof of runtime safety.

## Authority boundary

- AI and learning components may propose actions only.
- Every positive runtime mutation must pass the public `AicAuditedSafetyGateway.decide` entry point. The pure `AicSafetyGateway` evaluator is module-internal.
- The gateway is a deterministic pure evaluator: all state is supplied as input; it performs no I/O and calls no model.
- L3 is structurally denied write authority. A valid L3 request can only produce `ALERT_ONLY`; invalid or unsafe requests produce `DENY`.
- Rollback is not a positive action and does not pass through the gateway. It must still append a rollback audit event.

## Decision inputs

The evaluator receives the action request, permission tier, risk, mechanism registration status, capability-matrix row, remaining action budget, emergency-stop state, and cooldown state.

Outputs are exactly: `ALLOW`, `REQUIRE_USER_CONFIRMATION`, `ALERT_ONLY`, or `DENY`.

## Policy data

The bundled `aic/safety-policy.json` is data, not model output. The runtime exposes no policy mutation API. Production packaging and repository permissions must keep this file outside AI/learning write scopes; the current JVM module alone cannot prove OS-level immutability.

## Audit

The audited entry point appends every gateway decision before returning it to an executor; the rollback entry point appends every rollback outcome to `AicHashChainAuditLog`. The hash chain detects accidental/tamper-evident changes within the retained in-process sequence. This reference implementation is not durable across process death and is not cryptographically authenticated; durable storage, signing/key custody, access control, and crash consistency remain open requirements.

## Explicit limitations

- No device integration, write executor, durable audit backend, signature verification, or emergency-stop latency measurement is implemented here.
- The 1-second emergency-stop target is a design target and remains unmeasured.
- Passing unit tests would establish only code-level behavior, not end-to-end or real-device safety.
