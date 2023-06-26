package com.hakan.injection.reflection;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Reflection {

    private final Set<Class<?>> types;

    public Reflection(@Nonnull String basePackage) {
        this.types = ReflectionUtils.findClasses(basePackage);
    }

    public Reflection(@Nonnull Class<?> clazz) {
        this.types = new HashSet<>(Collections.singletonList(clazz));
    }

    public @Nonnull Set<Field> getFieldsAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredFields()))
                .filter(field -> field.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public @Nonnull Set<Method> getMethodsAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public @Nonnull Set<Constructor<?>> getConstructorsAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .filter(constructor -> constructor.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public @Nonnull Set<Class<?>> getTypesAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .filter(type -> type.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public @Nonnull Set<Class<?>> getSubTypesOf(@Nonnull Class<?> type) {
        return this.types.stream()
                .filter(assign -> !assign.equals(type))
                .filter(type::isAssignableFrom)
                .collect(Collectors.toSet());
    }
}
