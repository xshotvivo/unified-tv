---
name: utv-ui-engineering
description: Use for Android TV / large-screen UI, remote-first navigation, D-pad focus, typography, accessibility, home screen, player HUD and visual consistency.
---
# UTV TV UI Engineering

## Workflow
1. Define screen purpose, primary action, remote-control path and content hierarchy.
2. Specify D-pad focus graph, initial focus, visible focus, wrap/edge behavior, back behavior and restoration.
3. Design for 10-foot viewing: readable type, contrast, safe areas, restrained density and clear states.
4. Define loading, empty, offline, error, retry and partial-data states.
5. Reuse design tokens and components; avoid one-off visual patterns.
6. Test on target resolution, overscan assumptions, actual remote and accessibility settings.
7. Measure navigation steps, focus errors, frame stability and input latency where possible.

## Guardrails
A screenshot is not interaction validation. Do not optimize only for mouse/touch. Preserve viewing-first and user-control principles.
