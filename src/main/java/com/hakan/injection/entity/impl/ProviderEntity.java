package com.hakan.injection.entity.impl;

import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public class ProviderEntity extends AbstractEntity {

    private final Method method;

    public ProviderEntity(@Nonnull Module module,
                          @Nonnull Class<?> type,
                          @Nonnull Method method) {
        super(module, type, Scope.SINGLETON);
        this.method = method;
    }

    public @Nonnull Method getMethod() {
        return this.method;
    }


    @Override
    @SneakyThrows
    public @Nonnull Object createInstance() {
        return super.instance = this.method.invoke(super.module, this.parameters);
    }
}
