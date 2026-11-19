# Skill routing matrix

| Task | Primary | Add only if needed |
|---|---|---|
| New module / architecture change | project-architect | requirements, security, release-qa |
| Requirement / acceptance definition | requirements | project-architect, release-qa |
| Small implementation / bug fix | engineering | domain skill, release-qa |
| Project continuity / ADR | context-memory | requirements |
| R700 discovery / ADB | device-adb | security, release-qa |
| Playback / decoder / buffering | player-engineering | observability, device-adb, release-qa |
| M3U / XMLTV / source health | iptv-engineering | security, observability |
| Credentials / permissions | security | project-architect |
| Pairing / handoff / sync | multi-device | security, observability |
| Logs / incident / recovery | observability | domain skill, release-qa |
| TV layout / D-pad / focus | ui-engineering | requirements, release-qa |
| Build / regression / release | release-qa | relevant domain skill |

## Startup vs routine
Use full startup workflow only for a new project/module, material architecture change, security boundary change, data migration or cross-module contract change. Routine work uses existing rules, code inspection, minimal diff and focused tests. Do not invoke every skill by default.
