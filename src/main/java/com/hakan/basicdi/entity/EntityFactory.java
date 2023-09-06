package com.hakan.basicdi.entity;

import com.hakan.basicdi.annotations.Component;
import com.hakan.basicdi.annotations.Provide;
import com.hakan.basicdi.annotations.Runner;
import com.hakan.basicdi.annotations.Service;
import com.hakan.basicdi.entity.impl.ClassEntity;
import com.hakan.basicdi.entity.impl.EmptyEntity;
import com.hakan.basicdi.entity.impl.MethodProviderEntity;
import com.hakan.basicdi.entity.impl.MethodRunnerEntity;
import com.hakan.basicdi.module.Module;

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
        if (type.isAnnotationPresent(Service.class))
            return new ClassEntity(module, type, type.getAnnotation(Service.class).scope());
        if (type.isAnnotationPresent(Component.class))
            return new ClassEntity(module, type, type.getAnnotation(Component.class).scope());

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
        if (method.isAnnotationPresent(Provide.class))
            return new MethodProviderEntity(module, method);
        if (method.isAnnotationPresent(Runner.class))
            return new MethodRunnerEntity(module, method);

        throw new RuntimeException("no @Provide or @Runner annotation found for method " + method.getName());
    }
}
