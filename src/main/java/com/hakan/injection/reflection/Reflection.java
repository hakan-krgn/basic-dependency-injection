package com.hakan.injection.reflection;

import com.hakan.injection.utils.AnnotationUtils;

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

/**
 * Reflection is a class that provides reflection operations
 * for the given base package or class.
 */
public class Reflection {

    private final Set<Class<?>> types;

    /**
     * Constructor of {@link Reflection}.
     *
     * @param basePackage the base package to scan
     */
    public Reflection(@Nonnull String basePackage) {
        this.types = ReflectionUtils.findClasses(basePackage);
    }

    /**
     * Constructor of {@link Reflection}.
     *
     * @param clazz the class to scan
     */
    public Reflection(@Nonnull Class<?> clazz) {
        this.types = new HashSet<>(Collections.singletonList(clazz));
    }

    /**
     * Returns all the fields annotated
     * with the given annotation from the
     * scanned classes.
     *
     * @param annotation the annotation
     * @return the fields
     */
    public @Nonnull Set<Field> getFieldsAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .filter(type -> !type.isAnnotation())
                .flatMap(type -> Arrays.stream(type.getDeclaredFields()))
                .filter(field -> AnnotationUtils.isPresent(field, annotation))
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the methods annotated
     * with the given annotation from the
     * scanned classes.
     *
     * @param annotation the annotation
     * @return the methods
     */
    public @Nonnull Set<Method> getMethodsAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .filter(type -> !type.isAnnotation())
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(method -> AnnotationUtils.isPresent(method, annotation))
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the constructors annotated
     * with the given annotation from the
     * scanned classes.
     *
     * @param annotation the annotation
     * @return the constructors
     */
    public @Nonnull Set<Constructor<?>> getConstructorsAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .filter(type -> !type.isAnnotation())
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .filter(constructor -> AnnotationUtils.isPresent(constructor, annotation))
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the types annotated
     * with the given annotation from the
     * scanned classes.
     *
     * @param annotation the annotation
     * @return the types
     */
    public @Nonnull Set<Class<?>> getTypesAnnotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        return this.types.stream()
                .filter(type -> !type.isAnnotation())
                .filter(type -> AnnotationUtils.isPresent(type, annotation))
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the subtypes of the
     * give type from the scanned classes.
     *
     * @param type the type
     * @return the subtypes
     */
    public @Nonnull Set<Class<?>> getSubtypesOf(@Nonnull Class<?> type) {
        return this.types.stream()
                .filter(assign -> !assign.equals(type))
                .filter(type::isAssignableFrom)
                .collect(Collectors.toSet());
    }
}
