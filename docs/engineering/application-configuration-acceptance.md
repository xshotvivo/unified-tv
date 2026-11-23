# OACE Acceptance & Evidence Matrix

**Status:** Test design baseline; no tests are claimed executed.

## Evidence labels

- **A — Official:** vendor/platform documentation or official release metadata.
- **B — Measured:** repeatable test on a named device/firmware/build.
- **C — Inference:** reasoned conclusion supported by evidence.
- **D — Target:** desired behavior or threshold not yet measured.
- **Unknown:** evidence missing; must not be represented as verified.

## P0 acceptance matrix

| ID | Area | Acceptance condition | Evidence required | Gate |
|---|---|---|---|---|
| OACE-001 | Device capability | Model, firmware, OS/API and available capabilities are recorded without assuming unsupported APIs | Device inventory + source/evidence references | Blocker |
| OACE-002 | App identity | Each catalog entry has stable app/package ID, developer/source and version; missing provenance is explicit | Schema validation + sample records | Blocker |
| OACE-003 | Configuration schema | Each setting defines type, range, platform predicate, permission and verification | Schema tests | Blocker |
| OACE-004 | Snapshot | Before a supported mutation, previous state is captured and readable | Automated test + artifact | Blocker |
| OACE-005 | Authorization | Unauthorized or unsupported mutations are rejected | Negative tests | Blocker |
| OACE-006 | Apply/verify | Successful changes are read back and compared with intended state | Integration test | Blocker |
| OACE-007 | Rollback | Injected failure restores prior state or reports that safe restoration is impossible | Fault-injection test | Blocker |
| OACE-008 | Audit | Actor, target, before/after, reason, timestamp and result are recorded without leaking secrets | Audit test + privacy review | Blocker |
| OACE-009 | Offline/conflict | Interrupted sync does not silently overwrite device-scoped settings | State-machine tests | P1 |
| OACE-010 | Upgrade | Schema migration preserves values and supports recovery | Migration tests | P1 |

## Required vertical slice

1. Read device capability inventory.
2. Read installed application inventory.
3. Select one low-risk, reversible setting supported by the target device.
4. Capture its original value.
5. Require explicit user authorization.
6. Apply the setting through a platform adapter.
7. Read back and verify.
8. Restore the original value.
9. Verify restoration and retain an audit record.

Do not choose DNS, package disabling, factory reset, account changes or privileged settings as the first mutation unless platform support and recovery are independently demonstrated.

## Device test record

Every result must include:
- device model and hardware revision where known;
- firmware/build fingerprint and Android API level;
- app/build and adapter version;
- exact preconditions and test steps;
- expected and observed result;
- logs/artifacts and timestamps;
- pass/fail/blocked status;
- evidence grade and reviewer.

## Release policy

Architecture approval, code completion, CI pass, device test and production acceptance are separate states. A document or mocked demo cannot satisfy a device-validation gate.
