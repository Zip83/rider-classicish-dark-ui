# Changelog

## Unreleased

- Use a plugin-owned notification group for the Extended Tool Windows UI
  reminder, so the reminder does not depend on JetBrains' IDE/plugin updates
  notification group.
- Stop setting Rider's internal tool-window stripe manager property. Existing
  user settings are left untouched.

## 0.1.0

- Initial public release of Rider Classic-ish Dark UI.
- Suppress New UI / Islands icon remapping without installing the full Classic
  UI plugin.
- Apply compact Rider Dark UI startup tweaks once for toolbar placement, selected
  tab styling, project tree density, icon size, registry flags, and editor
  colors, while respecting later manual IDE settings changes.
- Recommend JetBrains' Extended Tool Windows UI plugin for the closest
  classic-style tool window layout and show a startup reminder if it is missing
  or disabled.
- Package the plugin as a standard JetBrains plugin ZIP for Rider's
  `Install Plugin from Disk...` flow.
- Derive release plugin versions from Git tags, for example `v0.1.0-rc1`
  produces plugin version `0.1.0-rc1`.
- Add GitHub Actions build, release, package-structure verification, and
  project-configuration verification workflows.
- Add repository contribution and AI-agent rules for strict SRP, braced control
  flow, build checks, and release discipline.
- Tested and tuned against Rider 2026.3 EAP 3 build `RD-263.5153.35`.
