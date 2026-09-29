package local.codex.rider;

import java.util.Objects;

final class StartupProfileOptions {
  private final boolean compactTreeIndentsEnabled;
  private final boolean differentiateProjectsDisabled;
  private final boolean mainToolbarEnabled;
  private final boolean searchEverywherePreviewEnabled;
  private final boolean compactUiDensityEnabled;
  private final boolean separateMainMenuEnabled;
  private final boolean islandsThemeFlagsResetEnabled;
  private final boolean selectedTabUnderlineEnabled;
  private final boolean classicToIslandsFlagResetEnabled;
  private final boolean newUiEnabled;
  private final boolean treeIndentEnabled;
  private final boolean projectIconSizeEnabled;
  private final boolean editorColorsEnabled;

  StartupProfileOptions(
    boolean compactTreeIndentsEnabled,
    boolean differentiateProjectsDisabled,
    boolean mainToolbarEnabled,
    boolean searchEverywherePreviewEnabled,
    boolean compactUiDensityEnabled,
    boolean separateMainMenuEnabled,
    boolean islandsThemeFlagsResetEnabled,
    boolean selectedTabUnderlineEnabled,
    boolean classicToIslandsFlagResetEnabled,
    boolean newUiEnabled,
    boolean treeIndentEnabled,
    boolean projectIconSizeEnabled,
    boolean editorColorsEnabled
  ) {
    this.compactTreeIndentsEnabled = compactTreeIndentsEnabled;
    this.differentiateProjectsDisabled = differentiateProjectsDisabled;
    this.mainToolbarEnabled = mainToolbarEnabled;
    this.searchEverywherePreviewEnabled = searchEverywherePreviewEnabled;
    this.compactUiDensityEnabled = compactUiDensityEnabled;
    this.separateMainMenuEnabled = separateMainMenuEnabled;
    this.islandsThemeFlagsResetEnabled = islandsThemeFlagsResetEnabled;
    this.selectedTabUnderlineEnabled = selectedTabUnderlineEnabled;
    this.classicToIslandsFlagResetEnabled = classicToIslandsFlagResetEnabled;
    this.newUiEnabled = newUiEnabled;
    this.treeIndentEnabled = treeIndentEnabled;
    this.projectIconSizeEnabled = projectIconSizeEnabled;
    this.editorColorsEnabled = editorColorsEnabled;
  }

  static StartupProfileOptions fromSettings(ClassicishDarkSettings settings) {
    return new StartupProfileOptions(
      settings.isCompactTreeIndentsEnabled(),
      settings.isDifferentiateProjectsDisabled(),
      settings.isMainToolbarEnabled(),
      settings.isSearchEverywherePreviewEnabled(),
      settings.isCompactUiDensityEnabled(),
      settings.isSeparateMainMenuEnabled(),
      settings.isIslandsThemeFlagsResetEnabled(),
      settings.isSelectedTabUnderlineEnabled(),
      settings.isClassicToIslandsFlagResetEnabled(),
      settings.isNewUiEnabled(),
      settings.isTreeIndentEnabled(),
      settings.isProjectIconSizeEnabled(),
      settings.isEditorColorsEnabled()
    );
  }

  static StartupProfileOptions enabledDefaults() {
    return new StartupProfileOptions(
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true
    );
  }

  static StartupProfileOptions disabledDefaults() {
    return new StartupProfileOptions(
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false
    );
  }

  boolean isCompactTreeIndentsEnabled() {
    return compactTreeIndentsEnabled;
  }

  boolean isDifferentiateProjectsDisabled() {
    return differentiateProjectsDisabled;
  }

  boolean isMainToolbarEnabled() {
    return mainToolbarEnabled;
  }

  boolean isSearchEverywherePreviewEnabled() {
    return searchEverywherePreviewEnabled;
  }

  boolean isCompactUiDensityEnabled() {
    return compactUiDensityEnabled;
  }

  boolean isSeparateMainMenuEnabled() {
    return separateMainMenuEnabled;
  }

  boolean isIslandsThemeFlagsResetEnabled() {
    return islandsThemeFlagsResetEnabled;
  }

  boolean isSelectedTabUnderlineEnabled() {
    return selectedTabUnderlineEnabled;
  }

  boolean isClassicToIslandsFlagResetEnabled() {
    return classicToIslandsFlagResetEnabled;
  }

  boolean isNewUiEnabled() {
    return newUiEnabled;
  }

  boolean isTreeIndentEnabled() {
    return treeIndentEnabled;
  }

  boolean isProjectIconSizeEnabled() {
    return projectIconSizeEnabled;
  }

  boolean isEditorColorsEnabled() {
    return editorColorsEnabled;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof StartupProfileOptions options)) {
      return false;
    }

    return compactTreeIndentsEnabled == options.compactTreeIndentsEnabled
      && differentiateProjectsDisabled == options.differentiateProjectsDisabled
      && mainToolbarEnabled == options.mainToolbarEnabled
      && searchEverywherePreviewEnabled == options.searchEverywherePreviewEnabled
      && compactUiDensityEnabled == options.compactUiDensityEnabled
      && separateMainMenuEnabled == options.separateMainMenuEnabled
      && islandsThemeFlagsResetEnabled == options.islandsThemeFlagsResetEnabled
      && selectedTabUnderlineEnabled == options.selectedTabUnderlineEnabled
      && classicToIslandsFlagResetEnabled == options.classicToIslandsFlagResetEnabled
      && newUiEnabled == options.newUiEnabled
      && treeIndentEnabled == options.treeIndentEnabled
      && projectIconSizeEnabled == options.projectIconSizeEnabled
      && editorColorsEnabled == options.editorColorsEnabled;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      compactTreeIndentsEnabled,
      differentiateProjectsDisabled,
      mainToolbarEnabled,
      searchEverywherePreviewEnabled,
      compactUiDensityEnabled,
      separateMainMenuEnabled,
      islandsThemeFlagsResetEnabled,
      selectedTabUnderlineEnabled,
      classicToIslandsFlagResetEnabled,
      newUiEnabled,
      treeIndentEnabled,
      projectIconSizeEnabled,
      editorColorsEnabled
    );
  }
}
