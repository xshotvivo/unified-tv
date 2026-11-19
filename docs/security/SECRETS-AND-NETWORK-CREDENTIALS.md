# UTV Secrets & Network Credential Security Baseline

Status: **Design baseline (D)**. This document is not evidence that encryption has been implemented or tested on a device.

## 1. Immediate credential incident handling

A subscription URL containing an access token and VLESS connection identifiers were previously pasted into a chat. Treat those values as **exposed**.

Required actions:
1. Revoke or rotate the subscription token at its issuing service.
2. Rotate the VLESS UUID / client credential where the provider supports it. If it cannot be rotated independently, replace the subscription credential or provider.
3. Do not copy the exposed values into GitHub issues, commits, screenshots, logs, test fixtures, crash reports, or this repository.
4. Do not assume that encrypting a copy now makes the old credential safe. Rotation/revocation is the security boundary.
5. Until rotation is complete, mark the old credential set as `EXPOSED_PENDING_ROTATION`; do not use it for new deployments.

## 2. Secret classification

| Data | Classification | Repository / sync policy |
|---|---|---|
| Subscription URL, access token, VLESS UUID, passwords, private keys | Secret | Never commit; never ordinary-sync |
| Full generated proxy configuration containing credentials | Secret | Encrypt at rest; exclude from logs/backups unless separately protected |
| Node label, protocol family, provider label, last-checked time | Metadata | May sync after privacy review |
| Latency, packet loss, jitter, throughput, exit-IP test result | Operational metadata | May sync with user consent; avoid unnecessary precise location retention |
| Redacted endpoint fingerprint | Restricted metadata | Use for deduplication only if it cannot reconstruct credentials |

Never put a real subscription URL or credential in examples. Use obvious placeholders such as `<REDACTED_SUBSCRIPTION_URL>`.

## 3. Platform storage architecture

Use a platform-specific secret-store adapter behind one UTV interface. Do not invent one universal plaintext config file.

### Android / Android TV
- Generate a per-installation AES-256-GCM data-encryption key (DEK) and protect the wrapping key with Android Keystore.
- Prefer a hardware-backed TEE / StrongBox key when supported; query and record the actual security level. Do not claim hardware backing without runtime evidence.
- Use a fresh random 96-bit nonce for each AES-GCM encryption operation; never reuse a nonce with the same key.
- Bind ciphertext to context with authenticated additional data (AAD), such as schema version, profile ID and secret type.
- Store ciphertext and non-secret metadata in app-private storage. Never store the raw key beside the ciphertext.
- Do not use SharedPreferences or external/shared storage for plaintext secrets.
- Exclude secret material and key-dependent data from Android backup / device-transfer paths unless an explicitly designed, user-authorized encrypted migration exists.
- If hardware-backed storage is unavailable, use the Android Keystore software-backed implementation as a clearly reported lower assurance fallback; never silently claim StrongBox.

### Windows
- Use Windows-protected credential storage (for example, DPAPI / Credential Manager) through a dedicated adapter.
- Keep secret files outside the Git working tree and restrict filesystem ACLs to the current user.
- Never place credentials in command-line arguments, shell history, process listings, or diagnostic output.

### Cross-device sync
- Sync metadata by default, not credentials.
- Provision each device independently, or use an explicitly authenticated end-to-end encrypted transfer with per-device keys and revocation.
- Never sync a device's Keystore/DPAPI master key or assume ciphertext encrypted for one device can be opened by another.
- Device removal must revoke its grants; a lost device must not retain access to newly rotated credentials.

## 4. Cryptographic and lifecycle requirements

- Use an established cryptographic library / platform API; do not implement cryptographic primitives.
- Use authenticated encryption (AES-256-GCM is the baseline) and cryptographically secure random nonces.
- Separate key-encryption keys from data-encryption keys; support versioned key IDs and controlled re-encryption.
- Never hard-code keys, salts, tokens, UUIDs, or passwords.
- Never log plaintext secrets. Redact query strings, URL userinfo, authorization headers, UUIDs, and generated client configurations.
- Provide explicit add, reveal (re-authentication gated where feasible), rotate, revoke, delete, and export flows. Export must be opt-in and visibly warn about plaintext risk.
- Secret deletion must remove active records and queued copies; explain that secure erasure from flash media and old backups cannot be guaranteed.
- Keep an audit trail of metadata-only actions: actor/device, operation, timestamp, secret record ID, result. Never log secret values.

## 5. Network Intelligence pipeline

1. Import subscription through a protected input path.
2. Parse in memory; immediately separate secret fields from non-secret node metadata.
3. Validate schema and supported protocol without exposing raw values.
4. Deduplicate using a keyed HMAC fingerprint or a non-reversible, versioned fingerprint; do not use raw UUIDs or tokens as database keys.
5. Run endpoint checks separately from full tunnel checks. An entry IP ping is not proof that the proxy route works or that its exit region matches a label.
6. Measure latency, packet loss, jitter and throughput under documented conditions; attach timestamp, device, network and test version.
7. Verify exit IP / region through an independent endpoint and label it as measured only when evidence exists.
8. Score candidates with transparent weights and confidence; keep untested values as Unknown.
9. Generate client configurations only in memory or directly into the encrypted vault. Redact before diagnostics.
10. Apply to a device only after explicit user approval; keep a one-step disable / rollback path.
11. Re-test periodically and mark stale measurements. Automatic switching must never break LAN, NAS or local playback paths.

## 6. Acceptance gates

| Gate | Required evidence | State |
|---|---|---|
| G0: credential rotation | Issuer confirms old token / UUID revoked or replaced | BLOCKED until provider action |
| G1: no-secret repository | Secret scan of full tracked tree and history; no live credential values | NOT RUN |
| G2: storage design | Threat model, key lifecycle, backup and recovery design reviewed | DESIGN ONLY |
| G3: Android implementation | Keystore security level observed on target device; encrypt/decrypt/tamper tests pass | NOT RUN |
| G4: Windows implementation | DPAPI / Credential Manager round-trip, ACL and log-redaction tests pass | NOT RUN |
| G5: sync and revocation | Cross-device provisioning, lost-device revocation and rotation tests pass | NOT RUN |
| G6: network quality | Repeatable end-to-end tests with independent exit verification | NOT RUN |
| G7: rollback | Proxy disabled / removed without disrupting LAN, NAS or local playback | NOT RUN |

No gate is PASS until its evidence is attached. A written design is not an implementation, a successful parse is not a network test, and an endpoint ping is not an end-to-end proxy test.

## 7. Immediate next actions

1. Rotate the exposed subscription token and VLESS credential at the provider.
2. Add the replacement only through a local protected input flow; do not paste it into public issues or commits.
3. Implement the vault adapter and redaction tests before enabling automatic subscription refresh or node switching.
4. Connect the Windows execution device and target TV before claiming local encryption, ADB backup, deployment or acceptance.

## References

- Android Keystore system: https://developer.android.com/privacy-and-security/keystore
- Android security tips: https://developer.android.com/privacy-and-security/security-tips
- OWASP Key Management Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Key_Management_Cheat_Sheet.html
- OWASP Secrets Management Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html
- OWASP Android Keystore knowledge base: https://mas.owasp.org/MASTG-KNOW-0043/
