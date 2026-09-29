package local.codex.rider;

final class UiSettingsProfileStep implements StartupProfileStep {
  private final ReflectionInvoker invoker;
  private final ReflectiveValueWriter valueWriter;
  private final ManagedValueApplicator applicator;
  private final StartupProfileOptions previousOptions;
  private final StartupProfileOptions options;

  UiSettingsProfileStep(
    ReflectionInvoker invoker,
    ReflectiveValueWriter valueWriter,
    ManagedValueApplicator applicator,
    StartupProfileOptions previousOptions,
    StartupProfileOptions options
  ) {
    this.invoker = invoker;
    this.valueWriter = valueWriter;
    this.applicator = applicator;
    this.previousOptions = previousOptions;
    this.options = options;
  }

  @Override
  public String label() {
    return "ui settings";
  }

  @Override
  public void apply() throws Exception {
    Object settings = invoker.invokeStatic("com.intellij.ide.ui.UISettings", "getInstance");

    boolean changed = false;
    changed |= apply(settings, "compactTreeIndents", previousOptions.isCompactTreeIndentsEnabled(),
      options.isCompactTreeIndentsEnabled(), "true");
    changed |= apply(settings, "differentiateProjects", previousOptions.isDifferentiateProjectsDisabled(),
      options.isDifferentiateProjectsDisabled(), "false");
    changed |= apply(settings, "showMainToolbar", previousOptions.isMainToolbarEnabled(),
      options.isMainToolbarEnabled(), "true");
    changed |= apply(settings, "showPreviewInSearchEverywhere", previousOptions.isSearchEverywherePreviewEnabled(),
      options.isSearchEverywherePreviewEnabled(), "true");
    changed |= apply(settings, "uiDensity", previousOptions.isCompactUiDensityEnabled(),
      options.isCompactUiDensityEnabled(), "COMPACT");
    changed |= apply(settings, "showMainMenuMode", previousOptions.isSeparateMainMenuEnabled(),
      options.isSeparateMainMenuEnabled(), "SEPARATE_TOOLBAR");

    if (changed) {
      invoker.invokeBestEffort(settings, "fireUISettingsChanged");
    }
  }

  private boolean apply(
    Object settings,
    String name,
    boolean previouslyEnabled,
    boolean enabled,
    String targetValue
  ) throws Exception {
    return applicator.apply(
      "ui." + name,
      previouslyEnabled,
      enabled,
      targetValue,
      new ReflectiveValueAccess(settings, name, invoker, valueWriter)
    );
  }
}
