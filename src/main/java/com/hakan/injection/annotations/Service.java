package com.hakan.injection.annotations;

import com.hakan.injection.entity.Scope;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    @Nonnull
    Scope scope() default Scope.SINGLETON;
}
