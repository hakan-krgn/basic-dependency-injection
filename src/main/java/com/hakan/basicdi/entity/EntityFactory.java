package com.hakan.basicdi.entity;

import com.hakan.basicdi.annotations.Component;
import com.hakan.basicdi.annotations.Provide;
import com.hakan.basicdi.annotations.Service;
import com.hakan.basicdi.entity.impl.EmptyEntity;
import com.hakan.basicdi.entity.impl.InjectorEntity;
import com.hakan.basicdi.entity.impl.ProviderEntity;
import com.hakan.basicdi.module.Module;
import com.hakan.basicdi.utils.AnnotationUtils;

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
