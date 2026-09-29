package local.codex.rider;

import com.intellij.ide.util.PropertiesComponent;

final class PropertiesProfileStep implements StartupProfileStep {
  @Override
  public String label() {
    return "properties";
  }

  @Override
  public void apply() {
    PropertiesComponent properties = PropertiesComponent.getInstance();

    properties.unsetValue("ide.islands.ab3");
    properties.unsetValue("ide.islands.new.darcula");
    properties.setValue("selected.color.option.type", "TAB_UNDERLINE");
  }
}
