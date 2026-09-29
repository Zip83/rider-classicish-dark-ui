package local.codex.rider;

import com.intellij.ide.util.PropertiesComponent;

final class StartupProfileApplicationGate {
  private static final String PROFILE_APPLIED_KEY = "io.github.zip83.rider.classicish.dark.ui.profile.applied";

  boolean shouldApply() {
    return !properties().getBoolean(PROFILE_APPLIED_KEY, false);
  }

  void markApplied() {
    properties().setValue(PROFILE_APPLIED_KEY, true, false);
  }

  void resetApplied() {
    properties().unsetValue(PROFILE_APPLIED_KEY);
  }

  private PropertiesComponent properties() {
    return PropertiesComponent.getInstance();
  }
}
