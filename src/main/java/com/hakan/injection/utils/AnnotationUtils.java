package com.hakan.injection.utils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * AnnotationUtils is a utility class for
 * finding annotations in classes, methods,
 * constructors and fields.
 */
@SuppressWarnings({"unchecked"})
public class AnnotationUtils {

    /**
     * Checks if the given class has the given annotation.
     *
     * @param clazz           class
     * @param annotationClass annotation class
     * @return true if the class has the given annotation
     */
    public static boolean isPresent(@Nonnull Class<?> clazz,
                                    @Nonnull Class<? extends Annotation> annotationClass) {
        return findAll(clazz).stream().anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    /**
     * Checks if the given method has the given annotation.
     *
     * @param method          method
     * @param annotationClass annotation class
     * @return true if the method has the given annotation
     */
    public static boolean isPresent(@Nonnull Method method,
                                    @Nonnull Class<? extends Annotation> annotationClass) {
        return findAll(method).stream().anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    /**
     * Checks if the given constructor has the given annotation.
     *
     * @param constructor     constructor
     * @param annotationClass annotation class
     * @return true if the constructor has the given annotation
     */
    public static boolean isPresent(@Nonnull Constructor<?> constructor,
                                    @Nonnull Class<? extends Annotation> annotationClass) {
        return findAll(constructor).stream().anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    /**
     * Checks if the given field has the given annotation.
     *
     * @param field           field
     * @param annotationClass annotation class
     * @return true if the field has the given annotation
     */
    public static boolean isPresent(@Nonnull Field field,
                                    @Nonnull Class<? extends Annotation> annotationClass) {
        return findAll(field).stream().anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }


    /**
     * Finds all annotations of the given class.
     *
     * @param clazz           class
     * @param annotationClass annotation class
     * @param <T>             annotation type
     * @return annotations
     */
    public static @Nonnull <T extends Annotation> T find(@Nonnull Class<?> clazz,
                                                         @Nonnull Class<T> annotationClass) {
        return (T) findAll(clazz).stream()
                .filter(annotation -> annotation.annotationType().equals(annotationClass)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("annotation not found: " + annotationClass.getName()));
    }

    /**
     * Finds the annotation of the given method.
     *
     * @param method          method
     * @param annotationClass annotation class
     * @param <T>             annotation type
     * @return annotation
     */
    public static @Nonnull <T extends Annotation> T find(@Nonnull Method method,
                                                         @Nonnull Class<T> annotationClass) {
        return (T) findAll(method).stream()
                .filter(annotation -> annotation.annotationType().equals(annotationClass)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("annotation not found: " + annotationClass.getName()));
    }

    /**
     * Finds the annotation of the given constructor.
     *
     * @param constructor     constructor
     * @param annotationClass annotation class
     * @param <T>             annotation type
     * @return annotation
     */
    public static @Nonnull <T extends Annotation> T find(@Nonnull Constructor<?> constructor,
                                                         @Nonnull Class<T> annotationClass) {
        return (T) findAll(constructor).stream()
                .filter(annotation -> annotation.annotationType().equals(annotationClass)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("annotation not found: " + annotationClass.getName()));
    }

    /**
     * Finds the annotation of the given field.
     *
     * @param field           field
     * @param annotationClass annotation class
     * @param <T>             annotation type
     * @return annotation
     */
    public static @Nonnull <T extends Annotation> T find(@Nonnull Field field,
                                                         @Nonnull Class<T> annotationClass) {
        return (T) findAll(field).stream()
                .filter(annotation -> annotation.annotationType().equals(annotationClass)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("annotation not found: " + annotationClass.getName()));
    }



    /**
     * Finds all annotations of the given class.
     *
     * @param clazz class
     * @return annotations
     */
    private static @Nonnull Set<Annotation> findAll(@Nonnull Class<?> clazz) {
        Set<Annotation> annotations = new HashSet<>();
        Arrays.stream(clazz.getDeclaredAnnotations())
                .filter(annotation -> !annotation.annotationType().equals(Target.class))
                .filter(annotation -> !annotation.annotationType().equals(Retention.class))
                .filter(annotation -> !annotation.annotationType().equals(Documented.class))
                .forEach(annotation -> {
                    annotations.add(annotation);
                    annotations.addAll(findAll(annotation.annotationType()));
                });
        return annotations;
    }

    /**
     * Finds all annotations of the given method.
     *
     * @param method method
     * @return annotations
     */
    private static @Nonnull Set<Annotation> findAll(@Nonnull Method method) {
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation annotation : method.getAnnotations()) {
            annotations.add(annotation);
            annotations.addAll(findAll(annotation.annotationType()));
        }
        return annotations;
    }

    /**
     * Finds all annotations of the given constructor.
     *
     * @param constructor constructor
     * @return annotations
     */
    private static @Nonnull Set<Annotation> findAll(@Nonnull Constructor<?> constructor) {
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation annotation : constructor.getAnnotations()) {
            annotations.add(annotation);
            annotations.addAll(findAll(annotation.annotationType()));
        }
        return annotations;
    }

    /**
     * Finds all annotations of the given field.
     *
     * @param field field
     * @return annotations
     */
    private static @Nonnull Set<Annotation> findAll(@Nonnull Field field) {
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation annotation : field.getAnnotations()) {
            annotations.add(annotation);
            annotations.addAll(findAll(annotation.annotationType()));
        }
        return annotations;
    }
}
