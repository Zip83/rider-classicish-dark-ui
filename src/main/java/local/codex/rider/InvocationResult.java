package local.codex.rider;

record InvocationResult(boolean called, Object value) {
  static InvocationResult called(Object value) {
    return new InvocationResult(true, value);
  }

  static InvocationResult notCalled() {
    return new InvocationResult(false, null);
  }
}
