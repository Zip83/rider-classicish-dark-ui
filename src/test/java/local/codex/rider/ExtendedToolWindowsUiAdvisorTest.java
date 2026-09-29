package local.codex.rider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ExtendedToolWindowsUiAdvisorTest {
  @Test
  void classifiesMissingPlugin() {
    assertEquals(
      ExtendedToolWindowsUiAdvisor.Availability.NOT_INSTALLED,
      ExtendedToolWindowsUiAdvisor.classify(false, false, false)
    );
  }

  @Test
  void classifiesDisabledPlugin() {
    assertEquals(
      ExtendedToolWindowsUiAdvisor.Availability.DISABLED,
      ExtendedToolWindowsUiAdvisor.classify(true, true, false)
    );
    assertEquals(
      ExtendedToolWindowsUiAdvisor.Availability.DISABLED,
      ExtendedToolWindowsUiAdvisor.classify(true, false, true)
    );
  }

  @Test
  void classifiesEnabledPlugin() {
    assertEquals(
      ExtendedToolWindowsUiAdvisor.Availability.ENABLED,
      ExtendedToolWindowsUiAdvisor.classify(true, false, false)
    );
  }
}
