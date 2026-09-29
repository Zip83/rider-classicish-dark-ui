package local.codex.rider;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

public final class ClassicishDarkStartupActivity implements StartupActivity {
  private final StartupProfile profile = StartupProfile.createDefault();
  private final StartupProfileApplicationGate applicationGate = new StartupProfileApplicationGate();
  private final ExtendedToolWindowsUiAdvisor extendedToolWindowsUiAdvisor = new ExtendedToolWindowsUiAdvisor();

  @Override
  public void runActivity(Project project) {
    extendedToolWindowsUiAdvisor.notifyIfNeeded(project);

    if (!applicationGate.shouldApply()) {
      return;
    }

    profile.apply();
    applicationGate.markApplied();
  }
}
