package local.codex.rider;

import java.lang.reflect.Field;

final class ReflectiveValueWriter {
  private final ReflectionInvoker invoker;
  private final ValueConverter valueConverter;

  ReflectiveValueWriter(ReflectionInvoker invoker, ValueConverter valueConverter) {
    this.invoker = invoker;
    this.valueConverter = valueConverter;
  }

  void setValue(Object target, String baseName, Object value) {
    String suffix = NameFormats.capitalize(baseName);

    if (invoker.invokeBestEffort(target, "set" + suffix, value).called()) {
      return;
    }

    String[] fieldNames = {
      baseName,
      NameFormats.toUpperSnake(baseName),
      "my" + suffix
    };

    for (String fieldName : fieldNames) {
      if (setField(target, fieldName, value)) {
        return;
      }
    }
  }

  private boolean setField(Object target, String fieldName, Object value) {
    Class<?> type = target.getClass();

    while (type != null) {
      if (setDeclaredField(target, type, fieldName, value)) {
        return true;
      }

      type = type.getSuperclass();
    }

    return false;
  }

  private boolean setDeclaredField(Object target, Class<?> owner, String fieldName, Object value) {
    try {
      Field field = owner.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, valueConverter.convertValue(field.getType(), value));
      return true;
    }
    catch (ReflectiveOperationException | IllegalArgumentException ignored) {
      return false;
    }
  }
}
