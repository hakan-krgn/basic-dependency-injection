package com.hakan.injection.entity.impl;

import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;

import javax.annotation.Nonnull;

/**
 * InjectorEntity is an entity class that
 * contains constructor which is used to
 * inject dependencies to the class.
 */
public class EmptyEntity extends AbstractEntity {

    /**
     * Constructor of {@link EmptyEntity}.
     *
     * @param module module
     * @param type   type
     */
    public EmptyEntity(@Nonnull Module module,
                       @Nonnull Class<?> type) {
        super(module, type, Scope.SINGLETON);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull Object createInstance() {
        return super.instance;
    }
}
