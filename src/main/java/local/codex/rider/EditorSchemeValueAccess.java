package local.codex.rider;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;

final class EditorSchemeValueAccess implements ManagedValueAccess {
  private final EditorColorsManager manager;

  EditorSchemeValueAccess(EditorColorsManager manager) {
    this.manager = manager;
  }

  @Override
  public String read() {
    return manager.getGlobalScheme().getName();
  }

  @Override
  public void write(String value) {
    EditorColorsScheme scheme = manager.getScheme(value);
    if (scheme != null) {
      manager.setGlobalScheme(scheme);
    }
  }
}
