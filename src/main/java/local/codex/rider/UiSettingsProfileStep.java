package local.codex.rider;

final class UiSettingsProfileStep implements StartupProfileStep {
  private final ReflectionInvoker invoker;
  private final ReflectiveValueWriter valueWriter;

  UiSettingsProfileStep(ReflectionInvoker invoker, ReflectiveValueWriter valueWriter) {
    this.invoker = invoker;
    this.valueWriter = valueWriter;
  }

  @Override
  public String label() {
    return "ui settings";
  }

  @Override
  public void apply() throws Exception {
    Object settings = invoker.invokeStatic("com.intellij.ide.ui.UISettings", "getInstance");

    valueWriter.setValue(settings, "compactTreeIndents", true);
    valueWriter.setValue(settings, "differentiateProjects", false);
    valueWriter.setValue(settings, "showMainToolbar", true);
    valueWriter.setValue(settings, "showPreviewInSearchEverywhere", true);
    valueWriter.setValue(settings, "uiDensity", "COMPACT");
    valueWriter.setValue(settings, "showMainMenuMode", "SEPARATE_TOOLBAR");

    invoker.invokeBestEffort(settings, "fireUISettingsChanged");
  }
}
