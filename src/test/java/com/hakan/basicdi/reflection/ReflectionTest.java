package com.hakan.basicdi.reflection;

import com.hakan.basicdi.annotations.Autowired;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ReflectionTest")
class ReflectionTest {

    private final @Nonnull Reflection reflection1 = new Reflection(ReflectionTest.class);
    private final @Nonnull Reflection reflection2 = new Reflection("com.hakan.basicdi");


    @Test
    void scanFields1() {
        Set<Field> fields = this.reflection1.getFieldsAnnotatedWith(Nonnull.class);

        assertFalse(fields.isEmpty());
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("reflection1")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("reflection2")));
    }

    @Test
    void scanFields2() {
        Set<Field> fields = this.reflection2.getFieldsAnnotatedWith(Nonnull.class);

        assertFalse(fields.isEmpty());
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("reflection1")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("reflection2")));
    }


    @Test
    void scanMethods1() {
        Set<Method> methods = this.reflection1.getMethodsAnnotatedWith(Test.class);

        assertFalse(methods.isEmpty());
        assertTrue(methods.stream().anyMatch(method -> method.getName().equals("scanMethods1")));
        assertTrue(methods.stream().anyMatch(method -> method.getName().equals("scanMethods2")));
    }

    @Test
    void scanMethods2() {
        Set<Method> methods = this.reflection2.getMethodsAnnotatedWith(Test.class);

        assertFalse(methods.isEmpty());
        assertTrue(methods.stream().anyMatch(method -> method.getName().equals("scanMethods1")));
        assertTrue(methods.stream().anyMatch(method -> method.getName().equals("scanMethods2")));
    }


    @Test
    void scanClasses1() {
        Set<Class<?>> classes = this.reflection1.getTypesAnnotatedWith(DisplayName.class);

        assertFalse(classes.isEmpty());
        assertTrue(classes.stream().anyMatch(clazz -> clazz.equals(this.getClass())));
    }

    @Test
    void scanClasses2() {
        Set<Class<?>> classes = this.reflection2.getTypesAnnotatedWith(DisplayName.class);

        assertFalse(classes.isEmpty());
        assertTrue(classes.stream().anyMatch(clazz -> clazz.equals(this.getClass())));
    }


    @Test
    void scanConstructors1() {
        Set<Constructor<?>> constructors = this.reflection1.getConstructorsAnnotatedWith(Autowired.class);

        assertTrue(constructors.isEmpty());
    }

    @Test
    void scanConstructors2() {
        Set<Constructor<?>> constructors = this.reflection2.getConstructorsAnnotatedWith(Autowired.class);

        assertFalse(constructors.isEmpty());
        assertTrue(constructors.stream().anyMatch(constructor -> constructor.getName().contains("ExampleService")));
    }
}
