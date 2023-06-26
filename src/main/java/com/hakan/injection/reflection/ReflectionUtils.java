package com.hakan.injection.reflection;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * ReflectionUtils is a utility class to
 * make the reflection processes easier.
 */
public class ReflectionUtils {

    /**
     * Returns the constructor from the given type
     * which is annotated with the given annotation
     * or the default constructor if there is no
     * constructor annotated with the given annotation.
     *
     * @param type       the type
     * @param annotation the annotation
     * @return the constructor
     */
    @SneakyThrows
    public static Constructor<?> getConstructor(@Nonnull Class<?> type,
                                                @Nonnull Class<? extends Annotation> annotation) {
        for (Constructor<?> constructor : type.getDeclaredConstructors())
            if (constructor.isAnnotationPresent(annotation))
                return constructor;
        return type.getDeclaredConstructor();
    }


    /**
     * Scans everywhere in the given base package
     * and returns the classes which are found.
     *
     * @param basePackage the base package
     * @return the classes
     */
    @SneakyThrows
    public static @Nonnull Set<Class<?>> findClasses(@Nonnull String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        BufferedReader reader = getReader(basePackage);

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.endsWith(".class")) classes.addAll(findClasses(basePackage + "." + line));
            else classes.add(Class.forName(basePackage + "." + line.substring(0, line.lastIndexOf('.'))));
        }

        return classes;
    }

    /**
     * Returns the reader for the given base package.
     *
     * @param basePackage the base package
     * @return the reader
     */
    @SneakyThrows
    private static @Nonnull BufferedReader getReader(@Nonnull String basePackage) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(basePackage.replaceAll("[.]", "/"));

        if (stream == null)
            throw new RuntimeException("no package found for " + basePackage);
        if (stream.available() == 0)
            throw new RuntimeException("no class found for " + basePackage);

        return new BufferedReader(new InputStreamReader(stream));
    }
}
