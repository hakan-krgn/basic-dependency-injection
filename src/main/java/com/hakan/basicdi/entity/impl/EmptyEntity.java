package com.hakan.basicdi.entity.impl;

import com.hakan.basicdi.annotations.Component;
import com.hakan.basicdi.annotations.Service;
import com.hakan.basicdi.entity.AbstractEntity;
import com.hakan.basicdi.entity.Scope;
import com.hakan.basicdi.module.Module;

import javax.annotation.Nonnull;

/**
 * EmptyEntity is an entity class that
 * contains constructor which is used to
 * inject dependencies to the class.
 * <p>
 * This class of entity cannot have
 * {@link Service} or {@link Component}
 * annotation.
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
