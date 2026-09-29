package local.codex.rider;

interface ManagedValueBaselineStore {
  boolean contains(String key);

  String get(String key);

  void put(String key, String value);

  void remove(String key);
}
