package local.codex.rider;

final class NameFormats {
  private NameFormats() {
  }

  static String capitalize(String value) {
    return Character.toUpperCase(value.charAt(0)) + value.substring(1);
  }

  static String toUpperSnake(String value) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);

      if (Character.isUpperCase(c) && i > 0) {
        result.append('_');
      }

      result.append(Character.toUpperCase(c));
    }

    return result.toString();
  }
}
