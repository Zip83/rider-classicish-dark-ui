package local.codex.rider;

interface StartupProfileStep {
  String label();

  void apply() throws Exception;
}
