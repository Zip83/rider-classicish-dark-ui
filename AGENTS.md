# Rider Classic-ish Dark UI Repository Rules

This file defines the binding working rules for this repository.

## Core Principles

- Preserve strict SRP across every source file, class, method, helper, script,
  workflow, and document.
- Prefer small readable units with one clear reason to change.
- Do not split code mechanically. Split when responsibilities diverge.
- A small orchestrator is acceptable when it only composes focused child units
  and controls execution flow.
- Split a unit when describing it honestly requires "and", such as "read and
  mutate", "reflect and convert", or "build and install".
- Prefer existing local patterns, helpers, and folder conventions over new
  abstractions.
- Introduce an abstraction only when it removes real complexity, reduces
  meaningful duplication, or matches an established local pattern.

## Java Plugin Code

- Keep IntelliJ startup wiring separate from concrete UI profile mutations.
- Keep each startup profile step focused on one settings area: UI settings,
  properties, registry values, editor colors, or another explicit boundary.
- Use documented IntelliJ Platform APIs directly when they are available.
- Keep reflection isolated to Rider EAP UI settings that are not available
  through stable public APIs.
- Keep value conversion separate from reflection method lookup and field lookup.
- Runtime tweaks must be best-effort unless the plugin cannot safely continue.
  One failed tweak must not break IDE startup.
- Apply the initial runtime profile once. Explicit Settings changes apply
  immediately and must respect later manual IDE settings changes.
- Do not change keymaps from plugin code.
- Do not depend on the Classic UI plugin.
- Keep JetBrains' Extended Tool Windows UI optional but recommended unless the
  repository direction changes.

## Style

- Use braces for every control-flow statement body where the language permits a
  braceless body. This includes single-statement `if`, `else`, loop, `catch`,
  and switch or switch-like branch bodies.
- Concise expression bodies, such as Java lambdas that return a value directly,
  do not require block braces unless readability suffers.
- Keep text files UTF-8 without BOM.
- Write repository instructions, comments, commit messages, and public
  documentation in English.
- Add comments sparingly. Prefer clear names and small units first.

## Ownership And Boundaries

- Read this file and the relevant README before editing.
- Keep code, documentation, workflows, and scripts in the folder that owns the
  reason to change.
- Keep edits narrow. Avoid unrelated refactors, formatting churn, generated
  metadata changes, or release changes unless they are part of the requested
  work.
- Do not promote code into a shared helper only because it might be reused
  later.
- Preserve compatibility notes for the tested Rider EAP build when changing
  plugin metadata or platform dependencies.

## Build And Release

- The normal user install path is Rider's `Install Plugin from Disk...` action
  using the ZIP produced by `buildPlugin`.
- Do not make PowerShell scripts the primary installation path.
- Keep GitHub Actions on current action major versions and avoid deprecated
  GitHub Actions Node runtimes.
- Do not create or update a release tag until the plugin has been tested in the
  target Rider EAP and the user explicitly asks for a release.

## Quality Gates

- Run the broadest practical build after source, metadata, Gradle, or workflow
  changes.
- Use `gradle buildPlugin` when Gradle is available.
- If Gradle is unavailable locally, run the local fallback build script and let
  GitHub Actions perform the canonical Gradle build after push.
- Documentation-only changes do not require a local build unless they affect
  release, install, or compatibility instructions.
- Do not treat a failing build, dirty verification, or skipped required check as
  acceptable partial completion.
