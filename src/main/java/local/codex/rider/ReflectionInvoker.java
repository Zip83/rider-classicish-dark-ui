package local.codex.rider;

import java.lang.reflect.Method;

final class ReflectionInvoker {
  private final ValueConverter valueConverter = new ValueConverter();

  Object invokeStatic(String className, String methodName) throws Exception {
    Class<?> clazz = Class.forName(className);
    Method method = clazz.getMethod(methodName);

    return method.invoke(null);
  }

  InvocationResult invokeBestEffort(Object target, String methodName, Object... args) {
    try {
      Method method = findMethod(target.getClass(), methodName, args);

      if (method == null) {
        return InvocationResult.notCalled();
      }

      method.setAccessible(true);

      return InvocationResult.called(method.invoke(target, args));
    }
    catch (Throwable t) {
      return InvocationResult.notCalled();
    }
  }

  private Method findMethod(Class<?> clazz, String methodName, Object[] args) {
    for (Method method : clazz.getMethods()) {
      if (hasSignature(method, methodName, args)) {
        return method;
      }
    }

    return null;
  }

  private boolean hasSignature(Method method, String methodName, Object[] args) {
    if (!method.getName().equals(methodName)) {
      return false;
    }

    if (method.getParameterCount() != args.length) {
      return false;
    }

    return acceptsArguments(method.getParameterTypes(), args);
  }

  private boolean acceptsArguments(Class<?>[] parameterTypes, Object[] args) {
    for (int i = 0; i < parameterTypes.length; i++) {
      if (!acceptsArgument(parameterTypes[i], args[i])) {
        return false;
      }
    }

    return true;
  }

  private boolean acceptsArgument(Class<?> parameterType, Object arg) {
    if (arg == null) {
      return true;
    }

    return valueConverter.wrap(parameterType).isAssignableFrom(arg.getClass());
  }
}
