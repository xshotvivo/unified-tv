---
name: utv-security
description: Use for threat modeling, authentication, authorization, device trust, API keys, secret storage, network exposure, supply-chain review and security-sensitive changes.
---
# UTV Security Engineering

## Workflow
1. Identify assets, actors, trust boundaries, entry points and abuse cases.
2. Classify data and secrets; minimize collection and retention.
3. Define least-privilege permissions, identity lifecycle, pairing, revocation and audit.
4. Store secrets in OS/user secret stores or approved vaults; never hardcode, log or commit them.
5. Validate untrusted input, URL schemes, redirects, file paths, archives and serialized data.
6. Review dependencies, license, provenance, maintenance and known vulnerabilities.
7. Define secure defaults, rate limits, timeouts, bounded retries and safe failure.
8. Test denial, expiry, revocation, replay, device loss and recovery.

## Guardrails
Do not weaken TLS/authentication to make a demo work. Do not expose admin endpoints without an explicit threat model. Redact credentials in evidence.
