# Docker Operations

**Status:** Operations architecture proposed; Windows 11 Docker state and NAS host state are unknown. No container was installed or changed in this review.

## 1. Purpose

Provide repeatable service lifecycle management across a future NAS, Windows 11 development host and disposable test environments. Compose files and versioned configuration remain the source of truth; a web panel is optional.

## 2. Candidate and boundary

- DPanel v1.11.2 (2026-10-29) is an optional management UI, not a UTOS core dependency.
- DPanel's custom LICENSE permits non-commercial use but restricts redistribution of derivative/modified/third-party versions without permission. Do not embed or redistribute it as a UTOS component.
- Its release notes describe configuration rollback for selected failed Compose deployments; they explicitly do not restore container runtime state or undo completed directory synchronization. The in-memory backup can expire after one hour or be lost on panel restart.
- The DPanel quick-start model uses Docker daemon access. Any web panel with docker.sock access is a host-root-equivalent control surface.

## 3. Required controls

- Pin image tags and immutable digests; never use latest as the production identity.
- Verify source repository, license, maintainer, release, signature, SBOM/provenance and CVEs before trial.
- Use non-root containers, read-only root filesystem, minimal capabilities, resource limits and explicit health checks where supported.
- Prefer rootless Docker when compatible; document exceptions.
- Use Compose secrets or a credential vault; do not store passwords/tokens in Git or plaintext environment variables.
- Avoid privileged mode, host networking, broad writable host mounts and Docker socket mounts unless a documented threat model justifies them.
- Keep admin panels LAN/VPN-only with strong authentication and audit logs.
- Persist application data in named volumes or explicit host paths; backup data and configuration independently.
- Test restore, image rollback, configuration migration and runtime recovery separately.

## 4. Operational lifecycle

Research → isolated trial → security review → backup → staged update → health verification → rollback decision → release record. A successful container start is not acceptance.

## 5. P0 tests

1. Determine Docker Desktop/WSL2 state on Windows 11 without modifying production services.
2. Test one harmless disposable Compose stack and verify volume persistence.
3. Verify image digest, SBOM/provenance, health check and resource ceilings.
4. Exercise backup/restore and update/rollback in a disposable VM.
5. Review DPanel EULA and Docker-socket access before any trial.

No Docker host, NAS, router or R700 configuration was modified.
