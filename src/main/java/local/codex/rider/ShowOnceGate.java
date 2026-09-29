package local.codex.rider;

final class ShowOnceGate {
  private boolean shown;

  synchronized boolean tryEnter() {
    if (shown) {
      return false;
    }

    shown = true;
    return true;
  }
}
