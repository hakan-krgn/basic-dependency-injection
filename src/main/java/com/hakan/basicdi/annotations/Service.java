package com.hakan.basicdi.annotations;

import com.hakan.basicdi.entity.Scope;

import javax.annotation.Nonnull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service annotation is used to specify
 * the scope of the class and inject dependencies
 * to the class automatically.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * Scope of the class.
     * <p>
     * Default value is SINGLETON, and
     * it means that class will be
     * instantiated only once.
     *
     * @return scope
     */
    @Nonnull
    Scope scope() default Scope.SINGLETON;
}
