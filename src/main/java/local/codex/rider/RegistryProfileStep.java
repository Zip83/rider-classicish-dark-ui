package local.codex.rider;

final class RegistryProfileStep implements StartupProfileStep {
  private final RegistryValueWriter registryValueWriter;

  RegistryProfileStep(RegistryValueWriter registryValueWriter) {
    this.registryValueWriter = registryValueWriter;
  }

  @Override
  public String label() {
    return "registry";
  }

  @Override
  public void apply() throws Exception {
    String[][] values = {
      {"switched.from.classic.to.islands", "false"},
      {"ide.experimental.ui", "true"},
      {"ide.ui.tree.indent", "8"},
      {"ide.project.icon.size", "16"}
    };

    for (String[] value : values) {
      registryValueWriter.setValue(value[0], value[1]);
    }
  }
}
