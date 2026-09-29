package local.codex.rider;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ManagedValueApplicatorTest {
  @Test
  void enablingCapturesBaselineAndAppliesTarget() throws Exception {
    FakeBaselineStore baselines = new FakeBaselineStore();
    FakeValueAccess value = new FakeValueAccess("original");

    boolean changed = new ManagedValueApplicator(baselines)
      .apply("setting", false, true, "target", value);

    assertTrue(changed);
    assertEquals("target", value.read());
    assertEquals("original", baselines.get("setting"));
  }

  @Test
  void disablingRestoresCapturedBaseline() throws Exception {
    FakeBaselineStore baselines = new FakeBaselineStore();
    baselines.put("setting", "original");
    FakeValueAccess value = new FakeValueAccess("target");

    boolean changed = new ManagedValueApplicator(baselines)
      .apply("setting", true, false, "target", value);

    assertTrue(changed);
    assertEquals("original", value.read());
    assertFalse(baselines.contains("setting"));
  }

  @Test
  void disablingPreservesLaterManualChange() throws Exception {
    FakeBaselineStore baselines = new FakeBaselineStore();
    baselines.put("setting", "original");
    FakeValueAccess value = new FakeValueAccess("manual");

    boolean changed = new ManagedValueApplicator(baselines)
      .apply("setting", true, false, "target", value);

    assertFalse(changed);
    assertEquals("manual", value.read());
    assertFalse(baselines.contains("setting"));
  }

  @Test
  void nullBaselineIsRestored() throws Exception {
    FakeBaselineStore baselines = new FakeBaselineStore();
    baselines.put("setting", null);
    FakeValueAccess value = new FakeValueAccess("target");

    new ManagedValueApplicator(baselines).apply("setting", true, false, "target", value);

    assertNull(value.read());
  }

  private static final class FakeBaselineStore implements ManagedValueBaselineStore {
    private final Map<String, String> values = new HashMap<>();
    private final Map<String, Boolean> present = new HashMap<>();

    @Override
    public boolean contains(String key) {
      return present.containsKey(key);
    }

    @Override
    public String get(String key) {
      return values.get(key);
    }

    @Override
    public void put(String key, String value) {
      present.put(key, true);
      values.put(key, value);
    }

    @Override
    public void remove(String key) {
      present.remove(key);
      values.remove(key);
    }
  }

  private static final class FakeValueAccess implements ManagedValueAccess {
    private String value;

    private FakeValueAccess(String value) {
      this.value = value;
    }

    @Override
    public String read() {
      return value;
    }

    @Override
    public void write(String value) {
      this.value = value;
    }
  }
}
