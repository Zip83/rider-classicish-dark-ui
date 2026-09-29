package local.codex.rider;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;

final class ExtendedToolWindowsUiDialog extends DialogWrapper {
  private final JBCheckBox doNotAskAgain = new JBCheckBox("Don't ask again");
  private final JPanel panel;

  ExtendedToolWindowsUiDialog(Project project, ExtendedToolWindowsUiAdvisor.Availability availability) {
    super(project, true);
    setTitle("Extended Tool Windows UI is recommended");
    setOKButtonText("Open Plugins");
    setCancelButtonText("Not now");

    String stateDescription = availability == ExtendedToolWindowsUiAdvisor.Availability.DISABLED
      ? "The plugin is installed but disabled."
      : "The plugin is not installed.";
    panel = FormBuilder.createFormBuilder()
      .addComponent(new JBLabel(stateDescription), 1)
      .addComponent(new JBLabel("Rider Classic-ish Dark UI works without it."), 1)
      .addComponent(new JBLabel("The JetBrains plugin provides the classic-style tool window layout."), 1)
      .addComponent(doNotAskAgain, 1)
      .getPanel();

    init();
  }

  boolean isDoNotAskAgainSelected() {
    return doNotAskAgain.isSelected();
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    return panel;
  }
}
