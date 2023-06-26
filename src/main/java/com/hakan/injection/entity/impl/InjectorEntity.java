package com.hakan.injection.entity.impl;

import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.module.Module;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

public class InjectorEntity extends AbstractEntity {

    private final Constructor<?> constructor;

    public InjectorEntity(Module module,
                          Class<?> type,
                          Constructor<?> constructor,
                          Scope scope) {
        super(module, type, scope);
        this.constructor = constructor;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }


    @Override
    @SneakyThrows
    public Object createInstance() {
        return super.instance = this.constructor.newInstance(this.parameters);
    }
}
