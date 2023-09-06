package com.hakan.basicdi.entity.impl;

import com.hakan.basicdi.annotations.Autowired;
import com.hakan.basicdi.annotations.PostConstruct;
import com.hakan.basicdi.entity.AbstractEntity;
import com.hakan.basicdi.entity.Scope;
import com.hakan.basicdi.module.Module;
import com.hakan.basicdi.reflection.ReflectionUtils;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * ClassEntity is an entity class that
 * contains constructor and fields which is
 * annotated with {@link Autowired} annotation
 * to create an instance of the class.
 */
public class ClassEntity extends AbstractEntity {

    private final Set<Field> fields;
    private final Set<Method> postConstructMethods;
    private final Constructor<?> constructor;

    /**
     * Constructor of {@link ClassEntity}.
     *
     * @param module module
     * @param type   type
     * @param scope  scope
     */
    public ClassEntity(@Nonnull Module module,
                       @Nonnull Class<?> type,
                       @Nonnull Scope scope) {
        super(module, type, scope);
        this.fields = super.reflection.getFieldsAnnotatedWith(Autowired.class);
        this.constructor = ReflectionUtils.getConstructor(type, Autowired.class);
        this.postConstructMethods = super.reflection.getMethodsAnnotatedWith(PostConstruct.class);

        this.constructor.setAccessible(true);
        this.fields.forEach(field -> field.setAccessible(true));
        this.postConstructMethods.forEach(method -> method.setAccessible(true));
    }

    /**
     * Returns the fields of the class
     * which are annotated with {@link Autowired}.
     *
     * @return fields
     */
    public @Nonnull Set<Field> getFields() {
        return this.fields;
    }

    /**
     * Returns the constructor of the class
     * which is annotated with {@link Autowired}
     * or has no parameters.
     *
     * @return constructor
     */
    public @Nonnull Constructor<?> getConstructor() {
        return this.constructor;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public @Nonnull Object createInstance() {
        if (super.scope == Scope.SINGLETON && super.instance != null)
            return super.instance;


        Object[] parameters = Arrays.stream(this.constructor.getParameterTypes())
                .filter(parameterType -> !parameterType.isArray())
                .filter(parameterType -> !parameterType.isPrimitive())
                .map(parameterType -> super.module.getInstance(parameterType))
                .toArray();

        super.instance = this.constructor.newInstance(parameters);

        for (Field field : this.fields)
            field.set(super.instance, super.module.getInstance(field.getType()));
        for (Method method : this.postConstructMethods)
            method.invoke(super.instance);

        return super.instance;
    }
}
