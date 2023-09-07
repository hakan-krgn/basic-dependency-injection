package com.hakan.basicdi.reflection;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
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
    public static @Nonnull Set<Class<?>> findClasses(@Nonnull String basePackage) {
        return findClasses0(basePackage);
    }



    /**
     * Scans everywhere in the given base package
     * and returns the classes which are found.
     * <p>
     * This method uses the jar file to scan the classes.
     *
     * @param basePackage the base package
     * @return the classes
     */
    @SneakyThrows
    private static @Nonnull Set<Class<?>> findClasses0(@Nonnull String basePackage) {
        URL jarPath = ReflectionUtils.class.getProtectionDomain().getCodeSource().getLocation();

        if (!jarPath.toString().endsWith(".jar")) return findClasses1(basePackage);
        if (jarPath.toString().endsWith(".zip")) throw new RuntimeException("this is not a jar!");


        Set<Class<?>> classes = new HashSet<>();
        String packagePath = basePackage.replace(".", File.separator);
        ZipInputStream zip = new ZipInputStream(Files.newInputStream(Paths.get(jarPath.toURI())));

        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            String className = entry.getName().replace("/", File.separator);
            if (!className.startsWith(packagePath) || !className.endsWith(".class")) continue;

            try {
                classes.add(Class.forName(className.replace(File.separator, ".").substring(0, className.length() - 6)));
            } catch (Error | Exception ignored) {

            }
        }

        return classes;
    }

    /**
     * Scans everywhere in the given base package
     * and returns the classes which are found.
     * <p>
     * This method uses the class loader to scan the classes.
     *
     * @param basePackage the base package
     * @return the classes
     */
    private static @Nonnull Set<Class<?>> findClasses1(@Nonnull String basePackage) {
        Set<Class<?>> classes = new HashSet<>();

        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(basePackage.replace(".", File.separator));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        reader.lines().forEach(_className -> {
            String className = basePackage + "." + _className.replace(".class", "");
            loadClass(className, classes::add, () -> classes.addAll(findClasses1(className)));
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
