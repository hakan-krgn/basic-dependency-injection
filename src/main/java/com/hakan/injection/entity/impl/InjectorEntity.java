package com.hakan.injection.entity.impl;

import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

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
     * @param module      module
     * @param type        type
     * @param constructor constructor
     * @param scope       scope
     */
    public InjectorEntity(@Nonnull Module module,
                          @Nonnull Class<?> type,
                          @Nonnull Constructor<?> constructor,
                          @Nonnull Scope scope) {
        super(module, type, scope);
        this.constructor = constructor;
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
        Class<?>[] parameterTypes = this.constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            AbstractEntity _entity = super.module.getEntityByClass(parameterTypes[i]);
            parameters[i] = _entity.getInstance();

            if (parameters[i] == null)
                parameters[i] = super.module.createInstance(_entity);
        }

        return super.instance = this.constructor.newInstance(this.parameters = parameters);
    }
}
