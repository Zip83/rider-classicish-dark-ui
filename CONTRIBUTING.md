# Contributing

Thanks for helping keep Rider Classic-ish Dark UI maintainable. This project is
small on purpose: it nudges Rider 2026.3 EAP closer to a compact Rider Dark /
classic-style UI without installing the full Classic UI plugin.

## Development Rules

- Follow `AGENTS.md`; it is the repository policy for people and AI agents.
- Keep every class, method, script, workflow, and document focused on one clear
  responsibility.
- Use braces for every control-flow statement body, including single-line
  `if`, `else`, loop, and `catch` bodies.
- Keep runtime tweaks best-effort. A missing internal Rider setting should skip
  that tweak, not break IDE startup.
- Apply the initial runtime profile once. Explicit Settings changes must apply
  immediately and must preserve later manual IDE settings changes.
- Do not change keymaps.
- Do not add a dependency on the Classic UI plugin.
- Keep JetBrains'
  [Extended Tool Windows UI](https://plugins.jetbrains.com/plugin/34198-extended-tool-windows-ui)
  as an optional recommended companion plugin unless the project direction
  changes.

## Build

The canonical build is:

```powershell
gradle buildPlugin
```

The installable ZIP is created in:

```text
build/distributions
```

If Gradle is not available locally, GitHub Actions builds the same ZIP on push.
For local iteration against an installed Rider EAP, this fallback script can be
used:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\build-local.ps1
```

## Manual Test

1. Build or download the plugin ZIP.
2. Optionally install JetBrains' Extended Tool Windows UI plugin in Rider for
   the closest classic-style tool window layout.
3. In Rider, open `Settings | Plugins`.
4. Use the gear menu and choose `Install Plugin from Disk...`.
5. Select the plugin ZIP and restart Rider.
6. Confirm the toolbar, icons, tabs, editor colors, project tree density, and
   tool window layout still match the intended classic-ish profile.
7. Disable and re-enable individual plugin settings and confirm `Apply` restores
   or reapplies them without overwriting a newer manual Rider change.
8. With Extended Tool Windows UI disabled, confirm the recommendation dialog is
   visible once and the companion status reads `Disabled`.

## Releases

Do not publish a GitHub Release until the target Rider EAP has been manually
checked. Release tags should be created only after the ZIP has been verified in
Rider.
