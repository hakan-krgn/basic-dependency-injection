package com.hakan.basicdi.reflection;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

        String separator = System.getProperty("file.separator");
        String packagePath = basePackage.replace(".", separator);
        URI jarPath = ReflectionUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        ZipInputStream zip = new ZipInputStream(Files.newInputStream(Paths.get(jarPath)));

        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            String className = entry.getName().replace("/", separator);
            if (!className.startsWith(packagePath) || !className.endsWith(".class")) continue;

            try {
                classes.add(Class.forName(className.replace(separator, ".").substring(0, className.length() - 6)));
            } catch (Error | Exception ignored) {

            }
        }

        return classes;
    }
}
