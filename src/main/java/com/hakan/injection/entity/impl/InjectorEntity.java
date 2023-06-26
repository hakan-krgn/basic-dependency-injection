package com.hakan.injection.entity.impl;

import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

public class InjectorEntity extends AbstractEntity {

    private final Constructor<?> constructor;

    public InjectorEntity(@Nonnull Module module,
                          @Nonnull Class<?> type,
                          @Nonnull Constructor<?> constructor,
                          @Nonnull Scope scope) {
        super(module, type, scope);
        this.constructor = constructor;
    }

    public @Nonnull Constructor<?> getConstructor() {
        return this.constructor;
    }


    @Override
    @SneakyThrows
    public @Nonnull Object createInstance() {
        return super.instance = this.constructor.newInstance(this.parameters);
    }
}
