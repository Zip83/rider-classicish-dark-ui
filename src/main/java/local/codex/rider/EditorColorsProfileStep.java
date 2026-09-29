package local.codex.rider;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;

final class EditorColorsProfileStep implements StartupProfileStep {
  private static final String CLASSIC_RIDER_DARK_SCHEME = "_@user_Rider Dark";

  @Override
  public String label() {
    return "editor colors";
  }

  @Override
  public void apply() {
    EditorColorsManager manager = EditorColorsManager.getInstance();
    EditorColorsScheme scheme = manager.getScheme(CLASSIC_RIDER_DARK_SCHEME);

    if (scheme != null) {
      manager.setGlobalScheme(scheme);
    }
  }
}
