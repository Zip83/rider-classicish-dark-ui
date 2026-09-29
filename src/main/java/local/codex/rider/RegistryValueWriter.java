package local.codex.rider;

import com.intellij.openapi.util.registry.Registry;

final class RegistryValueWriter {
  void setValue(String key, String value) {
    Registry.get(key).setValue(value);
  }
}
