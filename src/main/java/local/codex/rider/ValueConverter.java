package local.codex.rider;

final class ValueConverter {
  Object convertValue(Class<?> targetType, Object value) {
    if (value == null) {
      return null;
    }

    Class<?> wrapped = wrap(targetType);

    if (wrapped.isAssignableFrom(value.getClass())) {
      return value;
    }

    if (wrapped == Integer.class) {
      return Integer.parseInt(value.toString());
    }

    if (wrapped == Boolean.class) {
      return Boolean.parseBoolean(value.toString());
    }

    if (targetType.isEnum()) {
      return convertEnum(targetType, value);
    }

    return value.toString();
  }

  Class<?> wrap(Class<?> type) {
    if (!type.isPrimitive()) {
      return type;
    }

    if (type == boolean.class) {
      return Boolean.class;
    }

    if (type == int.class) {
      return Integer.class;
    }

    if (type == long.class) {
      return Long.class;
    }

    if (type == double.class) {
      return Double.class;
    }

    if (type == float.class) {
      return Float.class;
    }

    if (type == short.class) {
      return Short.class;
    }

    if (type == byte.class) {
      return Byte.class;
    }

    if (type == char.class) {
      return Character.class;
    }

    return type;
  }

  private Object convertEnum(Class<?> targetType, Object value) {
    for (Object constant : targetType.getEnumConstants()) {
      if (((Enum<?>) constant).name().equals(value.toString())) {
        return constant;
      }
    }

    return value.toString();
  }
}
