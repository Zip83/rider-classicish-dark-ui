package local.codex.rider;

import com.intellij.ide.util.PropertiesComponent;

final class PropertiesProfileStep implements StartupProfileStep {
  private final StartupProfileOptions options;
  private final StartupProfileOptions previousOptions;
  private final ManagedValueApplicator applicator;

  PropertiesProfileStep(
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
    return "properties";
  }

  @Override
  public void apply() throws Exception {
    PropertiesComponent properties = PropertiesComponent.getInstance();

    apply(properties, "ide.islands.ab3", previousOptions.isIslandsThemeFlagsResetEnabled(),
      options.isIslandsThemeFlagsResetEnabled(), null);
    apply(properties, "ide.islands.new.darcula", previousOptions.isIslandsThemeFlagsResetEnabled(),
      options.isIslandsThemeFlagsResetEnabled(), null);
    apply(properties, "selected.color.option.type", previousOptions.isSelectedTabUnderlineEnabled(),
      options.isSelectedTabUnderlineEnabled(), "TAB_UNDERLINE");
  }

  private void apply(
    PropertiesComponent properties,
    String key,
    boolean previouslyEnabled,
    boolean enabled,
    String targetValue
  ) throws Exception {
    applicator.apply(
      "property." + key,
      previouslyEnabled,
      enabled,
      targetValue,
      new PropertiesValueAccess(properties, key)
    );
  }
}
