package local.codex.rider;

import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;

final class ExtendedToolWindowsUiAdvisor {
  enum Availability {
    ENABLED("Enabled"),
    DISABLED("Disabled"),
    NOT_INSTALLED("Not installed");

    private final String displayName;

    Availability(String displayName) {
      this.displayName = displayName;
    }

    String displayName() {
      return displayName;
    }
  }

  private static final Logger LOG = Logger.getInstance(ExtendedToolWindowsUiAdvisor.class);
  private static final PluginId PLUGIN_ID = PluginId.getId("com.intellij.extendedToolWindowsUi");
  private static final ShowOnceGate DIALOG_GATE = new ShowOnceGate();

  Availability availability() {
    boolean installed = PluginManagerCore.getPlugin(PLUGIN_ID) != null;
    return classify(installed, PluginManagerCore.isDisabled(PLUGIN_ID), isDisabledInConfigFile());
  }

  String statusText() {
    return "Extended Tool Windows UI: " + availability().displayName();
  }

  void showIfNeeded(Project project) {
    Availability availability = availability();
    LOG.info("Extended Tool Windows UI state: " + availability);

    if (availability == Availability.ENABLED) {
      return;
    }

    ApplicationManager.getApplication().invokeLater(() -> showDialog(project, availability));
  }

  private void showDialog(Project project, Availability availability) {
    if (project.isDisposed() || !DIALOG_GATE.tryEnter()) {
      return;
    }

    ExtendedToolWindowsUiDialog dialog = new ExtendedToolWindowsUiDialog(project, availability);
    dialog.show();

    if (dialog.isDoNotAskAgainSelected()) {
      ClassicishDarkSettings.getInstance().setExtendedToolWindowsUiReminderEnabled(false);
    }

    if (dialog.isOK()) {
      ShowSettingsUtil.getInstance().showSettingsDialog(project, "Plugins");
    }
  }

  private boolean isDisabledInConfigFile() {
    Path disabledPluginsPath = PathManager.getConfigDir().resolve("disabled_plugins.txt");
    if (!Files.isRegularFile(disabledPluginsPath)) {
      return false;
    }

    try {
      return Files.readAllLines(disabledPluginsPath).stream()
        .map(String::trim)
        .anyMatch(PLUGIN_ID.getIdString()::equals);
    }
    catch (Exception e) {
      LOG.warn("Cannot read disabled plugins file: " + disabledPluginsPath, e);
      return false;
    }
  }

  static Availability classify(boolean installed, boolean disabledByApi, boolean disabledByConfig) {
    if (!installed) {
      return Availability.NOT_INSTALLED;
    }

    if (disabledByApi || disabledByConfig) {
      return Availability.DISABLED;
    }

    return Availability.ENABLED;
  }
}
