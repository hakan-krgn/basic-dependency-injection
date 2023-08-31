package com.hakan.basicdi.reflection;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
    public static @Nonnull Constructor<?> getConstructor(@Nonnull Class<?> type,
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

        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(basePackage.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        reader.lines().forEach(_className -> {
            String className = basePackage + "." + _className.replace(".class", "");
            loadClass(className, classes::add, () -> classes.addAll(findClasses(className)));
        });

        return classes;
    }


    /**
     * Loads the class from the given class name
     * and calls the given {@link Consumer} if
     * the class is found, otherwise calls the
     * given {@link Runnable}.
     *
     * @param className class name
     * @param present   consumer
     * @param absent    runnable
     */
    private static void loadClass(@Nonnull String className,
                                  @Nonnull Consumer<Class<?>> present,
                                  @Nonnull Runnable absent) {
        try {
            present.accept(Class.forName(className));
        } catch (ClassNotFoundException e) {
            absent.run();
        }
    }
}
