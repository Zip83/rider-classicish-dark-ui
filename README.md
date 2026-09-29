# Rider Classic-ish Dark UI

Unofficial JetBrains Rider plugin/profile for a calmer compact Rider Dark UI:
old-style icon mapping, tighter layout tweaks, and startup-applied appearance
settings without installing the full Classic UI plugin.

This project is not affiliated with JetBrains.

For the closest classic-style layout, JetBrains'
[Extended Tool Windows UI](https://plugins.jetbrains.com/plugin/34198-extended-tool-windows-ui)
can be installed as an optional companion plugin. Rider Classic-ish Dark UI
shows a reminder when it is missing or disabled, and applies the remaining icon,
theme, toolbar, font, and compactness tweaks without requiring it.

## What It Does

- Suppresses New UI / Islands icon remapping through `iconMapperSuppressor`.
- Applies the initial compact Rider Dark UI profile once.
- Keeps the main toolbar enabled and separated from the main menu.
- Keeps selected tab styling closer to underline-only behavior.
- Tries to keep project tree indentation and icon size tighter.
- Selects `_@user_Rider Dark` editor colors if that scheme exists.
- Recommends JetBrains' Extended Tool Windows UI for the classic-style tool
  window layout and shows a startup dialog if it is missing or disabled.
- Avoids keymap changes.
- Respects later manual IDE settings changes by not reapplying the profile after
  the first successful startup application.

The runtime tweaks are intentionally best-effort and are applied once. Rider EAP
builds can rename or move internal UI settings; when that happens, the plugin
skips the individual tweak instead of breaking IDE startup.
Most tweaks use normal IntelliJ Platform APIs. A small isolated reflection
fallback remains for Rider EAP UI settings that are not exposed as stable public
plugin APIs.

Plugin settings are available under
`Settings | Appearance & Behavior | Rider Classic-ish Dark UI`. Applying or
saving the page immediately applies changed appearance options. Turning an
option off restores the value captured before the plugin changed it, unless the
value was changed manually in Rider afterward. Internal migration switches are
kept in a separate Advanced section.

Extended Tool Windows UI is detected independently and is not a plugin
dependency. Rider Classic-ish Dark UI does not enable it or reproduce its tool
window layout.

## Install

1. Optionally install JetBrains'
   [Extended Tool Windows UI](https://plugins.jetbrains.com/plugin/34198-extended-tool-windows-ui)
   plugin in Rider for the closest classic-style tool window layout.
2. Download the latest `rider-classicish-dark-ui-*.zip` from
   [GitHub Releases](https://github.com/Zip83/rider-classicish-dark-ui/releases).
3. In Rider, open `Settings | Plugins`.
4. Use the gear menu and choose `Install Plugin from Disk...`.
5. Select the downloaded plugin ZIP and restart Rider.

## Compatibility

Current target:

- Rider 2026.3 EAP / IntelliJ Platform build `263.*`

Tested and tuned with:

- JetBrains Rider 2026.3 EAP 3
- Build `RD-263.5153.35`
- Built on September 17, 2026
- Source revision `3651cd2f8e2cd`

The Gradle build currently resolves Rider as `2026.3-EAP3-SNAPSHOT`, matching
the original test environment. When moving to a newer EAP, update
`platformVersion` in `gradle.properties`.

For newer Rider versions, start by updating:

- `gradle.properties`
- `src/main/resources/META-INF/plugin.xml`

## Contributing

See `CONTRIBUTING.md` for development workflow and `AGENTS.md` for repository
rules used by people and AI agents. The short version: keep strict SRP, use
braces for all control-flow bodies, keep runtime tweaks best-effort, and do not
publish a release before the target Rider EAP is manually checked.

## Build

The main build path is GitHub Actions. It uses the IntelliJ Platform Gradle
Plugin on a GitHub-hosted runner and uploads the plugin ZIP as an artifact.
Workflow actions are intentionally kept on current major versions to avoid
deprecated GitHub Actions Node runtimes. CI also runs the IntelliJ Platform
Gradle Plugin structure and project-configuration verification tasks before
publishing an artifact.

```powershell
gradle buildPlugin
```

The installable ZIP is produced under:

```text
build/distributions
```

If you do not have Gradle locally, push to GitHub and let the runner build it.
The release workflow attaches the same ZIP to GitHub Releases.

There is also a development fallback script that compiles against an installed
local Rider EAP. It is only for local iteration, not the normal install path:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\build-local.ps1
```

## Release

Push a version tag:

```powershell
git tag v0.1.0-rc1
git push origin v0.1.0-rc1
```

GitHub Actions will build the plugin and attach the ZIP from
`build/distributions` to a GitHub Release. The plugin version is derived from
the tag by removing the leading `v`, so `v0.1.0-rc1` produces plugin version
`0.1.0-rc1`.

## License

Apache-2.0
