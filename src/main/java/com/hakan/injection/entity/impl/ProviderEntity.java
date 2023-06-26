package com.hakan.injection.entity.impl;

import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.module.Module;
import com.hakan.injection.entity.Scope;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

public class ProviderEntity extends AbstractEntity {

    private final Method method;

    public ProviderEntity(Module module,
                          Class<?> type,
                          Method method) {
        super(module, type, Scope.SINGLETON);
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }


    @Override
    @SneakyThrows
    public Object createInstance() {
        return super.instance = this.method.invoke(super.module, this.parameters);
    }
}
