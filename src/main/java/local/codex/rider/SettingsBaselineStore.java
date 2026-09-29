package local.codex.rider;

final class SettingsBaselineStore implements ManagedValueBaselineStore {
  private static final String NULL_VALUE = "N";
  private static final String PRESENT_VALUE_PREFIX = "V";

  private final ClassicishDarkSettings settings;

  SettingsBaselineStore(ClassicishDarkSettings settings) {
    this.settings = settings;
  }

  @Override
  public boolean contains(String key) {
    return settings.hasBaseline(key);
  }

  @Override
  public String get(String key) {
    String encoded = settings.getBaseline(key);
    if (NULL_VALUE.equals(encoded)) {
      return null;
    }

    return encoded.substring(PRESENT_VALUE_PREFIX.length());
  }

  @Override
  public void put(String key, String value) {
    settings.putBaseline(key, value == null ? NULL_VALUE : PRESENT_VALUE_PREFIX + value);
  }

  @Override
  public void remove(String key) {
    settings.removeBaseline(key);
  }
}
