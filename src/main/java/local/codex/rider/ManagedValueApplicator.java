package local.codex.rider;

import java.util.Objects;

final class ManagedValueApplicator {
  private final ManagedValueBaselineStore baselines;

  ManagedValueApplicator(ManagedValueBaselineStore baselines) {
    this.baselines = baselines;
  }

  boolean apply(
    String key,
    boolean previouslyEnabled,
    boolean enabled,
    String targetValue,
    ManagedValueAccess access
  ) throws Exception {
    if (previouslyEnabled == enabled) {
      return false;
    }

    if (enabled) {
      if (!baselines.contains(key)) {
        baselines.put(key, access.read());
      }

      access.write(targetValue);
      return true;
    }

    if (!baselines.contains(key)) {
      return false;
    }

    String currentValue = access.read();
    if (Objects.equals(currentValue, targetValue)) {
      access.write(baselines.get(key));
    }

    baselines.remove(key);
    return Objects.equals(currentValue, targetValue);
  }
}
