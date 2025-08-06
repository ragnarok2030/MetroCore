package net.metroCore.Core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public final class ReflectionUtil {
    private ReflectionUtil() {}

    public static Optional<Field> getField(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return Optional.of(f);
        } catch (NoSuchFieldException ignored) {
            return Optional.empty();
        }
    }

    public static Optional<Method> getMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            Method m = clazz.getDeclaredMethod(name, params);
            m.setAccessible(true);
            return Optional.of(m);
        } catch (NoSuchMethodException ignored) {
            return Optional.empty();
        }
    }

    public static <T> T getFieldValue(Field f, Object instance) {
        try {
            //noinspection unchecked
            return (T)f.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
