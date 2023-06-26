package com.hakan.injection.entity;

import com.hakan.injection.annotations.Service;
import com.hakan.injection.module.Module;
import com.hakan.injection.annotations.Component;
import com.hakan.injection.entity.impl.InjectorEntity;
import com.hakan.injection.entity.impl.ProviderEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractEntity {

    public static AbstractEntity byType(Module module, Class<?> type, Constructor<?> constructor) {
        if (type.isAnnotationPresent(Service.class))
            return new InjectorEntity(module, type, constructor, type.getAnnotation(Service.class).scope());
        if (type.isAnnotationPresent(Component.class))
            return new InjectorEntity(module, type, constructor, type.getAnnotation(Component.class).scope());

        throw new RuntimeException("no annotation found for class " + type.getName());
    }

    public static AbstractEntity byMethod(Module module, Class<?> type, Method method) {
        return new ProviderEntity(module, type, method);
    }



    protected Module module;
    protected Scope scope;
    protected Object instance;
    protected Object[] parameters;

    protected Class<?> type;
    protected List<Class<?>> subTypes;

    public AbstractEntity(Module module,
                          Class<?> type,
                          Scope scope) {
        this.type = type;
        this.scope = scope;
        this.module = module;
        this.subTypes = new ArrayList<>();
        this.subTypes.add(type.getSuperclass());
        this.subTypes.addAll(Arrays.asList(type.getInterfaces()));
    }

    public Module getModule() {
        return this.module;
    }

    public Scope getScope() {
        return this.scope;
    }

    public Object getInstance() {
        return (this.scope == Scope.SINGLETON) ? this.instance : this.createInstance();
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public Class<?> getType() {
        return this.type;
    }

    public List<Class<?>> getSubTypes() {
        return this.subTypes;
    }


    public AbstractEntity withScope(Scope scope) {
        this.scope = scope;
        return this;
    }

    public AbstractEntity withInstance(Object instance) {
        this.instance = instance;
        return this.withScope(Scope.SINGLETON);
    }

    public AbstractEntity withParameters(Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public AbstractEntity withType(Class<?> type) {
        this.type = type;
        return this;
    }

    public AbstractEntity withSubTypes(List<Class<?>> subTypes) {
        this.subTypes = subTypes;
        return this;
    }



    public abstract Object createInstance();
}
