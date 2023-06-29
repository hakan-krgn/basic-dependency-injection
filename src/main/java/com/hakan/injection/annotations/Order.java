package com.hakan.injection.annotations;

import com.hakan.injection.module.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Order annotation is used to sorting the
 * module executions that are subclasses
 * of {@link Module}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

    /**
     * The order of the module.
     *
     * @return the order of the module.
     */
    int value();
}
