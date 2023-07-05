package com.hakan.basicdi.entity.impl;

import com.hakan.basicdi.entity.AbstractEntity;
import com.hakan.basicdi.entity.Scope;
import com.hakan.basicdi.module.Module;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * ProviderEntity is an entity class that
 * contains method which is used to
 * provide dependencies to the class.
 */
public class ProviderEntity extends AbstractEntity {

    private final Method method;
    private final Object methodInstance;

    /**
     * Constructor of {@link ProviderEntity}.
     *
     * @param module module
     * @param method method
     */
    public ProviderEntity(@Nonnull Module module,
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
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public @Nonnull Object createInstance() {
        if (super.scope == Scope.SINGLETON && super.instance != null)
            return super.instance;


        Object[] parameters = Arrays.stream(this.method.getParameterTypes())
                .map(parameterType -> super.module.getEntity(parameterType).getInstance())
                .toArray();

        return super.instance = this.method.invoke(this.methodInstance, parameters);
    }
}
