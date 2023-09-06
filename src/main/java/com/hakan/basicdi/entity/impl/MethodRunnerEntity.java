package com.hakan.basicdi.entity.impl;

import com.hakan.basicdi.annotations.Provide;
import com.hakan.basicdi.entity.AbstractEntity;
import com.hakan.basicdi.entity.Scope;
import com.hakan.basicdi.module.Module;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * MethodEntity is an entity class that
 * contains method which is annotated
 * with {@link Provide} annotation to
 * create an instance of return type
 * the method.
 */
public class MethodRunnerEntity extends AbstractEntity {

    private final Method method;
    private final Object methodInstance;

    /**
     * Constructor of {@link MethodRunnerEntity}.
     *
     * @param module module
     * @param method method
     */
    public MethodRunnerEntity(@Nonnull Module module,
                              @Nonnull Method method) {
        super(module, method.getReturnType(), Scope.SINGLETON);
        this.method = method;
        this.methodInstance = module;
    }

    /**
     * Returns the method of the class.
     *
     * @return method
     */
    public @Nonnull Method getMethod() {
        return this.method;
    }

    /**
     * Returns the method instance of the class.
     *
     * @return method instance
     */
    public @Nonnull Object getMethodInstance() {
        return this.methodInstance;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public @Nonnull Object createInstance() {
        if (this.method.getReturnType() != Runnable.class)
            throw new RuntimeException("return type of method must be java.lang.Runnable!");


        Object[] parameters = Arrays.stream(this.method.getParameterTypes())
                .filter(parameterType -> !parameterType.isArray())
                .filter(parameterType -> !parameterType.isPrimitive())
                .map(parameterType -> super.module.getInstance(parameterType))
                .toArray();

        super.instance = this.method.invoke(this.methodInstance, parameters);
        ((Runnable) super.instance).run();

        return super.instance;
    }
}
