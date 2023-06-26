package com.hakan.injection.module;

import com.hakan.injection.annotations.Autowired;
import com.hakan.injection.annotations.Provide;
import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.Scope;
import com.hakan.injection.entity.impl.InjectorEntity;
import com.hakan.injection.entity.impl.ProviderEntity;
import com.hakan.injection.reflection.Reflection;
import com.hakan.injection.reflection.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked"})
public abstract class Module {

    private final Reflection reflection;
    private final Set<AbstractEntity> entities;

    public Module() {
        this.entities = new HashSet<>();
        this.reflection = new Reflection(this.getClass());
        this.reflection.getMethodsAnnotatedWith(Provide.class)
                .forEach(this::bind);
    }


    public final <T> T getInstance(Class<? extends T> clazz) {
        return (T) this.getEntityByClass(clazz).getInstance();
    }

    public final AbstractEntity bind(Class<?> clazz) {
        return this.bind(ReflectionUtils.getConstructor(clazz, Autowired.class));
    }

    public final AbstractEntity bind(Constructor<?> constructor) {
        return this.bind(AbstractEntity.byType(this, constructor.getDeclaringClass(), constructor));
    }

    public final AbstractEntity bind(Method method) {
        return this.bind(AbstractEntity.byMethod(this, method.getReturnType(), method));
    }

    public final AbstractEntity bind(AbstractEntity entity) {
        this.entities.add(entity);
        return entity;
    }

    public final void install(Module module) {
        this.entities.addAll(module.entities);
    }

    public final void create() {
        this.entities.forEach(this::createInstance);
    }


    public abstract void configure();



    private AbstractEntity getEntityByClass(Class<?> clazz) {
        return this.entities.stream()
                .filter(entity -> entity.getType().equals(clazz) || entity.getSubTypes().contains(clazz))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no inject entity found for class " + clazz.getName()));
    }

    @SneakyThrows
    private Object createInstance(AbstractEntity entity) {
        if (entity.getScope() == Scope.SINGLETON && entity.getInstance() != null)
            return entity.getInstance();

        if (entity instanceof InjectorEntity) {
            InjectorEntity injectorEntity = (InjectorEntity) entity;

            Constructor<?> constructor = injectorEntity.getConstructor();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                AbstractEntity _entity = this.getEntityByClass(parameterTypes[i]);
                parameters[i] = _entity.getInstance();

                if (parameters[i] == null)
                    parameters[i] = this.createInstance(_entity);
            }

            return entity.withParameters(parameters).createInstance();
        } else if (entity instanceof ProviderEntity) {
            ProviderEntity providerEntity = (ProviderEntity) entity;

            Method method = providerEntity.getMethod();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] parameters = new Object[method.getParameterCount()];

            for (int i = 0; i < parameterTypes.length; i++) {
                AbstractEntity _entity = this.getEntityByClass(parameterTypes[i]);
                parameters[i] = _entity.getInstance();

                if (parameters[i] == null)
                    parameters[i] = this.createInstance(_entity);
            }

            return entity.withParameters(parameters).createInstance();
        }

        throw new RuntimeException("unknown entity type " + entity.getClass().getName());
    }
}
