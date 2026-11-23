# ADR-0002: OriginOSTV Application & Configuration Ecosystem

- **Status:** Accepted as an architectural direction; implementation remains pending.
- **Date:** 2026-11-23
- **Context:** Apple TV demonstrates a cohesive first-run, settings and application experience. OriginOSTV needs an open, multi-device equivalent that integrates with UTV and Guardian without duplicating their responsibilities.
- **Decision:** Establish OACE as a cross-cutting, modular application and configuration foundation with eight logical subsystems: first-run setup, application lifecycle, unified settings, identity/device hub, media profiles, trust/privacy integration, diagnostics/recovery and AI configuration assistant.
- **Architecture:** Platform adapters isolate Android TV, Windows and mobile capabilities. Shared typed contracts cover device capabilities, application identity, settings, permissions, transactions and events.
- **Safety:** Configuration changes use discover/explain/preflight/snapshot/authorize/apply/verify/commit. Failed operations stop safely and restore prior state when verified rollback is available. AI does not bypass policy or consent.
- **Integration:** UTV owns media-specific products and playback; Guardian owns security enforcement and trust policy; OACE provides common application/configuration workflows and calls their versioned interfaces.
- **Initial platform:** Toshiba REGZA 75R700SF / Android 12 is a validation target, not proof that required APIs are available.
- **Consequences:** More upfront work is required for capability discovery, schemas, audit and rollback. This is intentional; convenience must not conceal destructive or unsupported operations.
- **Rejected alternatives:** A launcher-only implementation; a copied Apple TV app list; an assumed universal app store; direct use of root/vendor-private APIs as baseline.
- **Revisit triggers:** Measured platform constraints, a security review finding, repeated acceptance failure, or a demonstrated simpler architecture that preserves safety and portability.
