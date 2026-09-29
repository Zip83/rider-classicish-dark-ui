package local.codex.rider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ShowOnceGateTest {
  @Test
  void entersOnlyOnce() {
    ShowOnceGate gate = new ShowOnceGate();

    assertTrue(gate.tryEnter());
    assertFalse(gate.tryEnter());
  }
}
