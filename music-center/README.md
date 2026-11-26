# UTV Music Media Center — Code Workspace

This directory is the independent implementation workspace for the UTV Music Media Center.

## Current implementation status

- [x] Engineering branch created: `feat/music-media-center-foundation`.
- [x] Product scope and ownership boundaries documented.
- [ ] Existing shared Playback System implementation audited.
- [x] Canonical music resource model implemented; local Kotlin CLI smoke tests PASS.
- [ ] Provider/resolver contracts implemented.
- [ ] Local catalog and playback vertical slice.
- [ ] Android TV and mobile integration.
- [ ] Music Theater and Karaoke Center.

Do not interpret a checked planning item as proof of runtime implementation unless the source and test evidence are linked.

## Architecture constraints

- Reuse the shared UTV Playback System / Super Player. Do not introduce a second player engine or parallel playback-session authority.
- Keep music-domain identity separate from provider IDs and file paths.
- Keep authorization, availability and technical media capability as separate facts.
- Do not store provider passwords, cookies, access tokens or signed URLs in logs, fixtures or committed configuration.
- Do not bundle or redistribute copyrighted music, lyrics, karaoke tracks or source lists without rights.
- Keep provider-specific behavior behind versioned adapters.
- Keep TV-specific UI separate from domain and audio integration code.

## Planned package boundaries

```text
core-domain/
provider-api/
catalog/
playback-integration/
lyrics/
theater/
karaoke/
device-integration/
ai-api/
diagnostics/
test-fixtures/
```

The first implementation slice will be kept small and independently testable. Platform-specific app modules will be attached only after the existing UTV build and shared playback contracts have been inspected.
