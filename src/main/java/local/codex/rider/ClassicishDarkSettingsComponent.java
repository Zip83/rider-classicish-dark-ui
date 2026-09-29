package local.codex.rider;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

final class ClassicishDarkSettingsComponent {
  private final JBCheckBox extendedToolWindowsUiReminder = new JBCheckBox(
    "Show a reminder when JetBrains' Extended Tool Windows UI is missing or disabled"
  );
  private final JBCheckBox compactUiDensity = new JBCheckBox("Use compact UI density");
  private final JBCheckBox compactTreeIndents = new JBCheckBox("Use compact tree positioning");
  private final JBCheckBox treeIndent = new JBCheckBox("Set project tree indent to 8 px");
  private final JBCheckBox projectIconSize = new JBCheckBox("Set project icon size to 16 px");
  private final JBCheckBox separateMainMenu = new JBCheckBox("Keep main menu above the main toolbar");
  private final JBCheckBox mainToolbar = new JBCheckBox("Show the main toolbar");
  private final JBCheckBox selectedTabUnderline = new JBCheckBox("Use underline for the selected editor tab");
  private final JBCheckBox differentiateProjects = new JBCheckBox("Disable per-project title coloring");
  private final JBCheckBox searchEverywherePreview = new JBCheckBox("Keep Search Everywhere preview enabled");
  private final JBCheckBox editorColors = new JBCheckBox("Select _@user_Rider Dark editor color scheme");
  private final JBCheckBox islandsThemeFlagsReset = new JBCheckBox("Clear Rider Islands theme migration flags");
  private final JBCheckBox classicToIslandsFlagReset = new JBCheckBox("Clear Classic UI to Islands migration flag");
  private final JBCheckBox newUi = new JBCheckBox("Keep JetBrains New UI enabled");
  private final JPanel panel;

  ClassicishDarkSettingsComponent() {
    ExtendedToolWindowsUiAdvisor advisor = new ExtendedToolWindowsUiAdvisor();
    JBLabel companionStatus = new JBLabel(advisor.statusText());
    JButton openPlugins = new JButton("Open Plugins");
    openPlugins.addActionListener(event -> {
      ShowSettingsUtil.getInstance().showSettingsDialog(currentProject(), "Plugins");
    });

    panel = FormBuilder.createFormBuilder()
      .addComponent(new JBLabel("Companion plugin"), 1)
      .addComponent(companionStatus, 1)
      .addComponent(extendedToolWindowsUiReminder, 1)
      .addComponent(openPlugins, 1)
      .addSeparator()
      .addComponent(new JBLabel("Appearance"), 1)
      .addComponent(compactUiDensity, 1)
      .addComponent(compactTreeIndents, 1)
      .addComponent(treeIndent, 1)
      .addComponent(projectIconSize, 1)
      .addComponent(separateMainMenu, 1)
      .addComponent(mainToolbar, 1)
      .addComponent(selectedTabUnderline, 1)
      .addComponent(editorColors, 1)
      .addComponent(differentiateProjects, 1)
      .addComponent(searchEverywherePreview, 1)
      .addSeparator()
      .addComponent(new JBLabel("Advanced (restart may be required)"), 1)
      .addComponent(islandsThemeFlagsReset, 1)
      .addComponent(classicToIslandsFlagReset, 1)
      .addComponent(newUi, 1)
      .addComponentFillVertically(new JPanel(), 0)
      .getPanel();
  }

  JComponent getPreferredFocusedComponent() {
    return compactUiDensity;
  }

  JPanel getPanel() {
    return panel;
  }

  StartupProfileOptions createProfileOptions() {
    return new StartupProfileOptions(
      compactTreeIndents.isSelected(),
      differentiateProjects.isSelected(),
      mainToolbar.isSelected(),
      searchEverywherePreview.isSelected(),
      compactUiDensity.isSelected(),
      separateMainMenu.isSelected(),
      islandsThemeFlagsReset.isSelected(),
      selectedTabUnderline.isSelected(),
      classicToIslandsFlagReset.isSelected(),
      newUi.isSelected(),
      treeIndent.isSelected(),
      projectIconSize.isSelected(),
      editorColors.isSelected()
    );
  }

  boolean isExtendedToolWindowsUiReminderEnabled() {
    return extendedToolWindowsUiReminder.isSelected();
  }

  void setExtendedToolWindowsUiReminderEnabled(boolean enabled) {
    extendedToolWindowsUiReminder.setSelected(enabled);
  }

  void setProfileOptions(StartupProfileOptions options) {
    compactTreeIndents.setSelected(options.isCompactTreeIndentsEnabled());
    differentiateProjects.setSelected(options.isDifferentiateProjectsDisabled());
    mainToolbar.setSelected(options.isMainToolbarEnabled());
    searchEverywherePreview.setSelected(options.isSearchEverywherePreviewEnabled());
    compactUiDensity.setSelected(options.isCompactUiDensityEnabled());
    separateMainMenu.setSelected(options.isSeparateMainMenuEnabled());
    islandsThemeFlagsReset.setSelected(options.isIslandsThemeFlagsResetEnabled());
    selectedTabUnderline.setSelected(options.isSelectedTabUnderlineEnabled());
    classicToIslandsFlagReset.setSelected(options.isClassicToIslandsFlagResetEnabled());
    newUi.setSelected(options.isNewUiEnabled());
    treeIndent.setSelected(options.isTreeIndentEnabled());
    projectIconSize.setSelected(options.isProjectIconSizeEnabled());
    editorColors.setSelected(options.isEditorColorsEnabled());
  }

  private Project currentProject() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    return projects.length == 0 ? null : projects[0];
  }
}
