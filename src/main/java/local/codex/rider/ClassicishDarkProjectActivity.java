package local.codex.rider;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;

public final class ClassicishDarkProjectActivity implements ProjectActivity {
  private final StartupProfileApplicationGate applicationGate = new StartupProfileApplicationGate();
  private final ExtendedToolWindowsUiAdvisor extendedToolWindowsUiAdvisor = new ExtendedToolWindowsUiAdvisor();

  @Override
  public Object execute(
    @NotNull Project project,
    @NotNull Continuation<? super Unit> continuation
  ) {
    ClassicishDarkSettings settings = ClassicishDarkSettings.getInstance();

    if (settings.isExtendedToolWindowsUiReminderEnabled()) {
      extendedToolWindowsUiAdvisor.showIfNeeded(project);
    }

    if (applicationGate.shouldApply()) {
      StartupProfile.create(
        StartupProfileOptions.disabledDefaults(),
        StartupProfileOptions.fromSettings(settings)
      ).apply();
      applicationGate.markApplied();
    }

    return Unit.INSTANCE;
  }
}
