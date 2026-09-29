package local.codex.rider;

import com.intellij.ide.util.PropertiesComponent;

final class PropertiesValueAccess implements ManagedValueAccess {
  private final PropertiesComponent properties;
  private final String key;

  PropertiesValueAccess(PropertiesComponent properties, String key) {
    this.properties = properties;
    this.key = key;
  }

  @Override
  public String read() {
    return properties.getValue(key);
  }

  @Override
  public void write(String value) {
    if (value == null) {
      properties.unsetValue(key);
      return;
    }

    properties.setValue(key, value);
  }
}
