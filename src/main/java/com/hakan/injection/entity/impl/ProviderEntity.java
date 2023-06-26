package com.hakan.injection.entity.impl;

import com.hakan.injection.annotations.PostConstruct;
import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * ProviderEntity is an entity class that
 * contains method which is used to
 * provide dependencies to the class.
 */
public class ProviderEntity extends AbstractEntity {

    private final Method method;

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
        if (super.instance != null && super.scope == Scope.SINGLETON)
            return super.instance;


        Class<?>[] parameterTypes = this.method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            AbstractEntity _entity = super.module.getEntityByClass(parameterTypes[i]);
            parameters[i] = _entity.getInstance();

            if (parameters[i] == null)
                parameters[i] = super.module.createInstance(_entity);
        }

        super.parameters = parameters;
        super.instance = this.method.invoke(super.module, super.parameters);

        for (Method method : super.reflection.getMethodsAnnotatedWith(PostConstruct.class))
            method.invoke(super.instance);

        return super.instance;
    }
}
