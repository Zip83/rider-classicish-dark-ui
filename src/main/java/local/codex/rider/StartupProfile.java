package local.codex.rider;

import java.util.ArrayList;
import java.util.List;

final class StartupProfile {
  private final List<StartupProfileStep> steps;
  private final StartupProfileStepRunner runner;

  private StartupProfile(List<StartupProfileStep> steps, StartupProfileStepRunner runner) {
    this.steps = steps;
    this.runner = runner;
  }

  static StartupProfile createDefault() {
    return create(StartupProfileOptions.disabledDefaults(), StartupProfileOptions.enabledDefaults());
  }

  static StartupProfile create(StartupProfileOptions options) {
    return create(StartupProfileOptions.disabledDefaults(), options);
  }

  static StartupProfile create(StartupProfileOptions previousOptions, StartupProfileOptions options) {
    ReflectionInvoker invoker = new ReflectionInvoker();
    ValueConverter valueConverter = new ValueConverter();
    ReflectiveValueWriter valueWriter = new ReflectiveValueWriter(invoker, valueConverter);
    ManagedValueApplicator applicator = new ManagedValueApplicator(
      new SettingsBaselineStore(ClassicishDarkSettings.getInstance())
    );
    List<StartupProfileStep> selectedSteps = new ArrayList<>();

    if (hasUiSettingsChanges(previousOptions, options)) {
      selectedSteps.add(new UiSettingsProfileStep(invoker, valueWriter, applicator, previousOptions, options));
    }
    if (hasPropertiesChanges(previousOptions, options)) {
      selectedSteps.add(new PropertiesProfileStep(previousOptions, options, applicator));
    }
    if (hasRegistryChanges(previousOptions, options)) {
      selectedSteps.add(new RegistryProfileStep(previousOptions, options, applicator));
    }
    if (previousOptions.isEditorColorsEnabled() != options.isEditorColorsEnabled()) {
      selectedSteps.add(new EditorColorsProfileStep(previousOptions, options, applicator));
    }

    return new StartupProfile(selectedSteps, new StartupProfileStepRunner());
  }

  private static boolean hasUiSettingsChanges(StartupProfileOptions previous, StartupProfileOptions current) {
    return previous.isCompactTreeIndentsEnabled() != current.isCompactTreeIndentsEnabled()
      || previous.isDifferentiateProjectsDisabled() != current.isDifferentiateProjectsDisabled()
      || previous.isMainToolbarEnabled() != current.isMainToolbarEnabled()
      || previous.isSearchEverywherePreviewEnabled() != current.isSearchEverywherePreviewEnabled()
      || previous.isCompactUiDensityEnabled() != current.isCompactUiDensityEnabled()
      || previous.isSeparateMainMenuEnabled() != current.isSeparateMainMenuEnabled();
  }

  private static boolean hasPropertiesChanges(StartupProfileOptions previous, StartupProfileOptions current) {
    return previous.isIslandsThemeFlagsResetEnabled() != current.isIslandsThemeFlagsResetEnabled()
      || previous.isSelectedTabUnderlineEnabled() != current.isSelectedTabUnderlineEnabled();
  }

  private static boolean hasRegistryChanges(StartupProfileOptions previous, StartupProfileOptions current) {
    return previous.isClassicToIslandsFlagResetEnabled() != current.isClassicToIslandsFlagResetEnabled()
      || previous.isNewUiEnabled() != current.isNewUiEnabled()
      || previous.isTreeIndentEnabled() != current.isTreeIndentEnabled()
      || previous.isProjectIconSizeEnabled() != current.isProjectIconSizeEnabled();
  }

  void apply() {
    for (StartupProfileStep step : steps) {
      runner.run(step);
    }
  }
}
