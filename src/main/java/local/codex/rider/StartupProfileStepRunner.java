package local.codex.rider;

final class StartupProfileStepRunner {
  void run(StartupProfileStep step) {
    try {
      step.apply();
    }
    catch (Throwable t) {
      System.err.println("[Rider Classic-ish Dark UI] skipped " + step.label() + ": " + t);
    }
  }
}
