package com.hakan.injection.reflection;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {

    @SneakyThrows
    public static Constructor<?> getConstructor(Class<?> type,
                                                Class<? extends Annotation> annotation) {
        for (Constructor<?> constructor : type.getDeclaredConstructors())
            if (constructor.isAnnotationPresent(annotation))
                return constructor;
        return type.getDeclaredConstructor();
    }


    @SneakyThrows
    public static Set<Class<?>> findClasses(String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        BufferedReader reader = getReader(basePackage);

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.endsWith(".class")) classes.addAll(findClasses(basePackage + "." + line));
            else classes.add(Class.forName(basePackage + "." + line.substring(0, line.lastIndexOf('.'))));
        }

        return classes;
    }

    @SneakyThrows
    private static BufferedReader getReader(String basePackage) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(basePackage.replaceAll("[.]", "/"));

        if (stream == null)
            throw new RuntimeException("no package found for " + basePackage);
        if (stream.available() == 0)
            throw new RuntimeException("no class found for " + basePackage);

        return new BufferedReader(new InputStreamReader(stream));
    }
}
