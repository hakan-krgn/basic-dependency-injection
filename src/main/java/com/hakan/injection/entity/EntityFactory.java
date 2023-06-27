package com.hakan.injection.entity;

import com.hakan.injection.annotations.Component;
import com.hakan.injection.annotations.Provide;
import com.hakan.injection.annotations.Service;
import com.hakan.injection.entity.impl.EmptyEntity;
import com.hakan.injection.entity.impl.InjectorEntity;
import com.hakan.injection.entity.impl.ProviderEntity;
import com.hakan.injection.module.Module;
import com.hakan.injection.utils.AnnotationUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public class EntityFactory {

    /**
     * Creates an entity with the class type,
     * and if constructor which is annotated
     * with @Autowired exists, InjectorEntity
     * will be created, otherwise EmptyEntity
     *
     * @param module module
     * @param type   type
     * @return entity
     */
    public static @Nonnull AbstractEntity create(@Nonnull Module module,
                                                 @Nonnull Class<?> type) {
        if (AnnotationUtils.isPresent(type, Service.class))
            return new InjectorEntity(module, type, AnnotationUtils.find(type, Service.class).scope());
        if (AnnotationUtils.isPresent(type, Component.class))
            return new InjectorEntity(module, type, AnnotationUtils.find(type, Component.class).scope());

        return new EmptyEntity(module, type);
    }

    /**
     * Creates an entity with the class type
     * and method which is annotated with @Provide
     *
     * @param module module
     * @param method method
     * @return entity
     */
    public static @Nonnull AbstractEntity create(@Nonnull Module module,
                                                 @Nonnull Method method) {
        if (!AnnotationUtils.isPresent(method, Provide.class))
            throw new RuntimeException("no @Provide annotation found for method " + method.getName());

        return new ProviderEntity(module, method);
    }
}
