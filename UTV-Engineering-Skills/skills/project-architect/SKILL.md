---
name: utv-project-architect
description: Use when starting a UTV module or repository, changing architecture boundaries, introducing cross-module contracts, or making a material platform decision. Not for routine isolated fixes.
---
# UTV Project Architect

## Trigger
New module/repository, architecture change, platform decision, cross-module interface or architectural migration.

## Workflow
1. Read parent README, ROADMAP, relevant module specs, ADRs and root AGENTS.md.
2. State goal, users, target platforms, constraints, non-goals and unknowns.
3. Map dependencies and trust boundaries; separate control plane, data plane and UI where useful.
4. Define ownership, stable interfaces, versioning, data lifecycle and failure semantics.
5. Compare viable approaches for material decisions; state cost, compatibility, lock-in and exit path.
6. Threat-model inputs, credentials, permissions, update path and recovery.
7. Define unit, contract, integration, performance, device and failure-injection tests.
8. Produce a project brief and ADR proposal. Do not silently approve decisions requiring owner sign-off.

## Outputs
Architecture brief, module boundary map, interface/data contracts, ADR, risk register, acceptance and rollback plan.

## Guardrails
A launcher is not an operating system. Do not assume OEM APIs or hardware capabilities. Avoid speculative microservices and unnecessary abstraction. Label unverified claims.
