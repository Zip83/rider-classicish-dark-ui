package local.codex.rider;

import com.intellij.openapi.editor.colors.EditorColorsManager;

final class EditorColorsProfileStep implements StartupProfileStep {
  private static final String CLASSIC_RIDER_DARK_SCHEME = "_@user_Rider Dark";
  private final StartupProfileOptions previousOptions;
  private final StartupProfileOptions options;
  private final ManagedValueApplicator applicator;

  EditorColorsProfileStep(
    StartupProfileOptions previousOptions,
    StartupProfileOptions options,
    ManagedValueApplicator applicator
  ) {
    this.previousOptions = previousOptions;
    this.options = options;
    this.applicator = applicator;
  }

  @Override
  public String label() {
    return "editor colors";
  }

  @Override
  public void apply() throws Exception {
    EditorColorsManager manager = EditorColorsManager.getInstance();
    applicator.apply(
      "editor.globalScheme",
      previousOptions.isEditorColorsEnabled(),
      options.isEditorColorsEnabled(),
      CLASSIC_RIDER_DARK_SCHEME,
      new EditorSchemeValueAccess(manager)
    );
  }
}
