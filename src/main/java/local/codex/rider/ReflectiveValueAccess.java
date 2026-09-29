package local.codex.rider;

import java.lang.reflect.Field;

final class ReflectiveValueAccess implements ManagedValueAccess {
  private final Object target;
  private final String baseName;
  private final ReflectionInvoker invoker;
  private final ReflectiveValueWriter writer;

  ReflectiveValueAccess(
    Object target,
    String baseName,
    ReflectionInvoker invoker,
    ReflectiveValueWriter writer
  ) {
    this.target = target;
    this.baseName = baseName;
    this.invoker = invoker;
    this.writer = writer;
  }

  @Override
  public String read() throws Exception {
    String suffix = NameFormats.capitalize(baseName);
    InvocationResult getter = invoker.invokeBestEffort(target, "get" + suffix);
    if (!getter.called()) {
      getter = invoker.invokeBestEffort(target, "is" + suffix);
    }
    if (getter.called()) {
      return canonicalValue(getter.value());
    }

    String[] fieldNames = {
      baseName,
      NameFormats.toUpperSnake(baseName),
      "my" + suffix
    };
    for (String fieldName : fieldNames) {
      Field field = findField(target.getClass(), fieldName);
      if (field != null) {
        field.setAccessible(true);
        return canonicalValue(field.get(target));
      }
    }

    throw new NoSuchFieldException(baseName);
  }

  @Override
  public void write(String value) {
    writer.setValue(target, baseName, value);
  }

  private Field findField(Class<?> type, String fieldName) {
    Class<?> current = type;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      }
      catch (NoSuchFieldException ignored) {
        current = current.getSuperclass();
      }
    }

    return null;
  }

  private String canonicalValue(Object value) {
    if (value instanceof Enum<?> enumValue) {
      return enumValue.name();
    }

    return value == null ? null : value.toString();
  }
}
