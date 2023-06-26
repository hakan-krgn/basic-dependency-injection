package com.hakan.injection.entity.impl;

import com.hakan.injection.annotations.Autowired;
import com.hakan.injection.annotations.PostConstruct;
import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;
import com.hakan.injection.reflection.ReflectionUtils;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * InjectorEntity is an entity class that
 * contains constructor which is used to
 * inject dependencies to the class.
 */
public class InjectorEntity extends AbstractEntity {

    private final Constructor<?> constructor;

    /**
     * Constructor of {@link InjectorEntity}.
     *
     * @param module module
     * @param type   type
     * @param scope  scope
     */
    public InjectorEntity(@Nonnull Module module,
                          @Nonnull Class<?> type,
                          @Nonnull Scope scope) {
        super(module, type, scope);
        this.constructor = ReflectionUtils.getConstructor(type, Autowired.class);
    }

    /**
     * Returns the constructor of the class.
     *
     * @return constructor
     */
    public @Nonnull Constructor<?> getConstructor() {
        return this.constructor;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public @Nonnull Object createInstance() {
        if (super.instance != null && super.scope == Scope.SINGLETON)
            return super.instance;


        Class<?>[] parameterTypes = this.constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            AbstractEntity _entity = super.module.getEntityByClass(parameterTypes[i]);
            parameters[i] = _entity.getInstance();

            if (parameters[i] == null)
                parameters[i] = super.module.createInstance(_entity);
        }

        super.parameters = parameters;
        super.instance = this.constructor.newInstance(super.parameters);

        for (Method method : super.reflection.getMethodsAnnotatedWith(PostConstruct.class))
            method.invoke(super.instance);

        return super.instance;
    }
}
