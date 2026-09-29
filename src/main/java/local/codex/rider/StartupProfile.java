package local.codex.rider;

import java.util.List;

final class StartupProfile {
  private final List<StartupProfileStep> steps;
  private final StartupProfileStepRunner runner;

  private StartupProfile(List<StartupProfileStep> steps, StartupProfileStepRunner runner) {
    this.steps = steps;
    this.runner = runner;
  }

  static StartupProfile createDefault() {
    ReflectionInvoker invoker = new ReflectionInvoker();
    ValueConverter valueConverter = new ValueConverter();
    ReflectiveValueWriter valueWriter = new ReflectiveValueWriter(invoker, valueConverter);
    RegistryValueWriter registryValueWriter = new RegistryValueWriter();

    return new StartupProfile(
      List.of(
        new UiSettingsProfileStep(invoker, valueWriter),
        new PropertiesProfileStep(),
        new RegistryProfileStep(registryValueWriter),
        new EditorColorsProfileStep()
      ),
      new StartupProfileStepRunner()
    );
  }

  void apply() {
    for (StartupProfileStep step : steps) {
      runner.run(step);
    }
  }
}
