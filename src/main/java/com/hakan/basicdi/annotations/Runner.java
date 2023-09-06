package com.hakan.basicdi.annotations;

import com.hakan.basicdi.module.Module;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Runner annotation to define runner methods. These methods
 * will be executed after the injection process is started,
 * and the methods must be inside the class that is implemented
 * with {@link Module}.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Runner {

}
