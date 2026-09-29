package local.codex.rider;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

final class ExtendedToolWindowsUiAdvisor {
  private static final String NOTIFICATION_GROUP_ID = "Rider Classic-ish Dark UI";
  private static final PluginId EXTENDED_TOOL_WINDOWS_UI_ID = PluginId.getId("com.intellij.extendedToolWindowsUi");
  private static final String EXTENDED_TOOL_WINDOWS_UI_URL =
    "https://plugins.jetbrains.com/plugin/34198-extended-tool-windows-ui";
  private static boolean notificationShown;

  void notifyIfNeeded(Project project) {
    if (notificationShown) {
      return;
    }

    if (isInstalledAndEnabled()) {
      return;
    }

    notificationShown = true;

    Notification notification = NotificationGroupManager.getInstance()
      .getNotificationGroup(NOTIFICATION_GROUP_ID)
      .createNotification(
        "Extended Tool Windows UI is recommended",
        "Rider Classic-ish Dark UI works without it, but JetBrains' Extended Tool Windows UI gives the closest classic-style tool window layout.",
        NotificationType.INFORMATION
      );

    notification.addAction(NotificationAction.createSimple("Open plugin page", () -> {
      BrowserUtil.browse(EXTENDED_TOOL_WINDOWS_UI_URL);
    }));
    notification.addAction(NotificationAction.createSimple("Open Plugins settings", () -> {
      ShowSettingsUtil.getInstance().showSettingsDialog(project, "Plugins");
    }));
    notification.notify(project);
  }

  private boolean isInstalledAndEnabled() {
    return PluginManagerCore.getPlugin(EXTENDED_TOOL_WINDOWS_UI_ID) != null
      && !PluginManagerCore.isDisabled(EXTENDED_TOOL_WINDOWS_UI_ID);
  }
}
