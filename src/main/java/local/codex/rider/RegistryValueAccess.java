package local.codex.rider;

import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.registry.RegistryValue;

final class RegistryValueAccess implements ManagedValueAccess {
  private final RegistryValue value;

  RegistryValueAccess(String key) {
    value = Registry.get(key);
  }

  @Override
  public String read() {
    return value.asString();
  }

  @Override
  public void write(String newValue) {
    value.setValue(newValue);
  }
}
