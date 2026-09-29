package local.codex.rider;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import java.util.HashMap;
import java.util.Map;

@State(
  name = "local.codex.rider.ClassicishDarkSettings",
  storages = @Storage("rider-classicish-dark-ui.xml")
)
public final class ClassicishDarkSettings implements PersistentStateComponent<ClassicishDarkSettings.StoredState> {
  public static final class StoredState {
    public boolean extendedToolWindowsUiReminderEnabled = true;
    public boolean compactTreeIndentsEnabled = true;
    public boolean differentiateProjectsDisabled = true;
    public boolean mainToolbarEnabled = true;
    public boolean searchEverywherePreviewEnabled = true;
    public boolean compactUiDensityEnabled = true;
    public boolean separateMainMenuEnabled = true;
    public boolean islandsThemeFlagsResetEnabled = true;
    public boolean selectedTabUnderlineEnabled = true;
    public boolean classicToIslandsFlagResetEnabled = true;
    public boolean newUiEnabled = true;
    public boolean treeIndentEnabled = true;
    public boolean projectIconSizeEnabled = true;
    public boolean editorColorsEnabled = true;
    public Map<String, String> baselineValues = new HashMap<>();
  }

  private StoredState state = new StoredState();

  public static ClassicishDarkSettings getInstance() {
    return ApplicationManager.getApplication().getService(ClassicishDarkSettings.class);
  }

  @Override
  public StoredState getState() {
    return state;
  }

  @Override
  public void loadState(StoredState state) {
    this.state = state;
  }

  boolean isExtendedToolWindowsUiReminderEnabled() {
    return state.extendedToolWindowsUiReminderEnabled;
  }

  void setExtendedToolWindowsUiReminderEnabled(boolean enabled) {
    state.extendedToolWindowsUiReminderEnabled = enabled;
  }

  boolean isCompactTreeIndentsEnabled() {
    return state.compactTreeIndentsEnabled;
  }

  void setCompactTreeIndentsEnabled(boolean enabled) {
    state.compactTreeIndentsEnabled = enabled;
  }

  boolean isDifferentiateProjectsDisabled() {
    return state.differentiateProjectsDisabled;
  }

  void setDifferentiateProjectsDisabled(boolean enabled) {
    state.differentiateProjectsDisabled = enabled;
  }

  boolean isMainToolbarEnabled() {
    return state.mainToolbarEnabled;
  }

  void setMainToolbarEnabled(boolean enabled) {
    state.mainToolbarEnabled = enabled;
  }

  boolean isSearchEverywherePreviewEnabled() {
    return state.searchEverywherePreviewEnabled;
  }

  void setSearchEverywherePreviewEnabled(boolean enabled) {
    state.searchEverywherePreviewEnabled = enabled;
  }

  boolean isCompactUiDensityEnabled() {
    return state.compactUiDensityEnabled;
  }

  void setCompactUiDensityEnabled(boolean enabled) {
    state.compactUiDensityEnabled = enabled;
  }

  boolean isSeparateMainMenuEnabled() {
    return state.separateMainMenuEnabled;
  }

  void setSeparateMainMenuEnabled(boolean enabled) {
    state.separateMainMenuEnabled = enabled;
  }

  boolean isIslandsThemeFlagsResetEnabled() {
    return state.islandsThemeFlagsResetEnabled;
  }

  void setIslandsThemeFlagsResetEnabled(boolean enabled) {
    state.islandsThemeFlagsResetEnabled = enabled;
  }

  boolean isSelectedTabUnderlineEnabled() {
    return state.selectedTabUnderlineEnabled;
  }

  void setSelectedTabUnderlineEnabled(boolean enabled) {
    state.selectedTabUnderlineEnabled = enabled;
  }

  boolean isClassicToIslandsFlagResetEnabled() {
    return state.classicToIslandsFlagResetEnabled;
  }

  void setClassicToIslandsFlagResetEnabled(boolean enabled) {
    state.classicToIslandsFlagResetEnabled = enabled;
  }

  boolean isNewUiEnabled() {
    return state.newUiEnabled;
  }

  void setNewUiEnabled(boolean enabled) {
    state.newUiEnabled = enabled;
  }

  boolean isTreeIndentEnabled() {
    return state.treeIndentEnabled;
  }

  void setTreeIndentEnabled(boolean enabled) {
    state.treeIndentEnabled = enabled;
  }

  boolean isProjectIconSizeEnabled() {
    return state.projectIconSizeEnabled;
  }

  void setProjectIconSizeEnabled(boolean enabled) {
    state.projectIconSizeEnabled = enabled;
  }

  boolean isEditorColorsEnabled() {
    return state.editorColorsEnabled;
  }

  void setEditorColorsEnabled(boolean enabled) {
    state.editorColorsEnabled = enabled;
  }

  void setProfileOptions(StartupProfileOptions options) {
    state.compactTreeIndentsEnabled = options.isCompactTreeIndentsEnabled();
    state.differentiateProjectsDisabled = options.isDifferentiateProjectsDisabled();
    state.mainToolbarEnabled = options.isMainToolbarEnabled();
    state.searchEverywherePreviewEnabled = options.isSearchEverywherePreviewEnabled();
    state.compactUiDensityEnabled = options.isCompactUiDensityEnabled();
    state.separateMainMenuEnabled = options.isSeparateMainMenuEnabled();
    state.islandsThemeFlagsResetEnabled = options.isIslandsThemeFlagsResetEnabled();
    state.selectedTabUnderlineEnabled = options.isSelectedTabUnderlineEnabled();
    state.classicToIslandsFlagResetEnabled = options.isClassicToIslandsFlagResetEnabled();
    state.newUiEnabled = options.isNewUiEnabled();
    state.treeIndentEnabled = options.isTreeIndentEnabled();
    state.projectIconSizeEnabled = options.isProjectIconSizeEnabled();
    state.editorColorsEnabled = options.isEditorColorsEnabled();
  }

  boolean hasBaseline(String key) {
    return state.baselineValues.containsKey(key);
  }

  String getBaseline(String key) {
    return state.baselineValues.get(key);
  }

  void putBaseline(String key, String value) {
    state.baselineValues.put(key, value);
  }

  void removeBaseline(String key) {
    state.baselineValues.remove(key);
  }
}
