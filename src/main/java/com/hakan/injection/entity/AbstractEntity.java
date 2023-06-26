package com.hakan.injection.entity;

import com.hakan.injection.annotations.Component;
import com.hakan.injection.annotations.Service;
import com.hakan.injection.entity.impl.InjectorEntity;
import com.hakan.injection.entity.impl.ProviderEntity;
import com.hakan.injection.module.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractEntity {

    public static @Nonnull AbstractEntity byType(@Nonnull Module module,
                                                 @Nonnull Class<?> type,
                                                 @Nonnull Constructor<?> constructor) {
        if (type.isAnnotationPresent(Service.class))
            return new InjectorEntity(module, type, constructor, type.getAnnotation(Service.class).scope());
        if (type.isAnnotationPresent(Component.class))
            return new InjectorEntity(module, type, constructor, type.getAnnotation(Component.class).scope());

        throw new RuntimeException("no annotation found for class " + type.getName());
    }

    public static @Nonnull AbstractEntity byMethod(@Nonnull Module module,
                                                   @Nonnull Class<?> type,
                                                   @Nonnull Method method) {
        return new ProviderEntity(module, type, method);
    }



    protected Module module;
    protected Scope scope;
    protected Object instance;
    protected Object[] parameters;

    protected Class<?> type;
    protected List<Class<?>> subTypes;

    public AbstractEntity(@Nonnull Module module,
                          @Nonnull Class<?> type,
                          @Nonnull Scope scope) {
        this.type = type;
        this.scope = scope;
        this.module = module;
        this.subTypes = new ArrayList<>();
        this.subTypes.add(type.getSuperclass());
        this.subTypes.addAll(Arrays.asList(type.getInterfaces()));
    }

    public Object getInstance() {
        return (this.scope == Scope.SINGLETON) ? this.instance : this.createInstance();
    }

    public @Nullable Object[] getParameters() {
        return this.parameters;
    }

    public @Nonnull Module getModule() {
        return this.module;
    }

    public @Nonnull Scope getScope() {
        return this.scope;
    }

    public @Nonnull Class<?> getType() {
        return this.type;
    }

    public @Nonnull List<Class<?>> getSubTypes() {
        return this.subTypes;
    }


    public @Nonnull AbstractEntity withScope(@Nonnull Scope scope) {
        this.scope = scope;
        return this;
    }

    public @Nonnull AbstractEntity withInstance(@Nonnull Object instance) {
        this.instance = instance;
        return this.withScope(Scope.SINGLETON);
    }

    public @Nonnull AbstractEntity withParameters(@Nonnull Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public @Nonnull AbstractEntity withType(@Nonnull Class<?> type) {
        this.type = type;
        return this;
    }

    public @Nonnull AbstractEntity withSubTypes(@Nonnull List<Class<?>> subTypes) {
        this.subTypes = subTypes;
        return this;
    }



    public abstract @Nonnull Object createInstance();
}
