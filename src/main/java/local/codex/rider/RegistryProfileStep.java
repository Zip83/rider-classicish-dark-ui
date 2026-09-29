package local.codex.rider;

final class RegistryProfileStep implements StartupProfileStep {
  private final ManagedValueApplicator applicator;
  private final StartupProfileOptions previousOptions;
  private final StartupProfileOptions options;

  RegistryProfileStep(
    StartupProfileOptions previousOptions,
    StartupProfileOptions options,
    ManagedValueApplicator applicator
  ) {
    this.previousOptions = previousOptions;
    this.options = options;
    this.applicator = applicator;
  }

  @Override
  public String label() {
    return "registry";
  }

  @Override
  public void apply() throws Exception {
    apply("switched.from.classic.to.islands", previousOptions.isClassicToIslandsFlagResetEnabled(),
      options.isClassicToIslandsFlagResetEnabled(), "false");
    apply("ide.experimental.ui", previousOptions.isNewUiEnabled(), options.isNewUiEnabled(), "true");
    apply("ide.ui.tree.indent", previousOptions.isTreeIndentEnabled(), options.isTreeIndentEnabled(), "8");
    apply("ide.project.icon.size", previousOptions.isProjectIconSizeEnabled(),
      options.isProjectIconSizeEnabled(), "16");
  }

  private void apply(String key, boolean previouslyEnabled, boolean enabled, String targetValue) throws Exception {
    applicator.apply(
      "registry." + key,
      previouslyEnabled,
      enabled,
      targetValue,
      new RegistryValueAccess(key)
    );
  }
}
