package local.codex.rider;

interface ManagedValueAccess {
  String read() throws Exception;

  void write(String value) throws Exception;
}
