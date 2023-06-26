package com.hakan.injection.entity;

import com.hakan.injection.annotations.Component;
import com.hakan.injection.annotations.Provide;
import com.hakan.injection.annotations.Service;
import com.hakan.injection.entity.impl.InjectorEntity;
import com.hakan.injection.entity.impl.ProviderEntity;
import com.hakan.injection.module.Module;
import com.hakan.injection.reflection.Reflection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AbstractEntity is an entity class that
 * contains information about the class
 * that will be injected to the classes,
 * and also contains the instance of the class.
 */
public abstract class AbstractEntity {

    /**
     * Creates an entity with the class type
     * and constructor which is annotated
     * with @Autowired
     *
     * @param module      module
     * @param type        type
     * @param constructor constructor
     * @return entity
     */
    public static @Nonnull AbstractEntity byType(@Nonnull Module module,
                                                 @Nonnull Class<?> type,
                                                 @Nonnull Constructor<?> constructor) {
        if (type.isAnnotationPresent(Service.class))
            return new InjectorEntity(module, type, constructor, type.getAnnotation(Service.class).scope());
        if (type.isAnnotationPresent(Component.class))
            return new InjectorEntity(module, type, constructor, type.getAnnotation(Component.class).scope());

        throw new RuntimeException("no @Service or @Component annotation found for class " + type.getName());
    }

    /**
     * Creates an entity with the class type
     * and method which is annotated with @Provide
     *
     * @param module module
     * @param type   type
     * @param method method
     * @return entity
     */
    public static @Nonnull AbstractEntity byMethod(@Nonnull Module module,
                                                   @Nonnull Class<?> type,
                                                   @Nonnull Method method) {
        if (!method.isAnnotationPresent(Provide.class))
            throw new RuntimeException("no @Provide annotation found for method " + method.getName());

        return new ProviderEntity(module, type, method);
    }



    protected Module module;
    protected Scope scope;
    protected Object instance;
    protected Object[] parameters;
    protected Reflection reflection;

    protected Class<?> type;
    protected List<Class<?>> subTypes;

    /**
     * Constructor of {@link AbstractEntity}.
     *
     * @param module module
     * @param type   type
     * @param scope  scope
     */
    public AbstractEntity(@Nonnull Module module,
                          @Nonnull Class<?> type,
                          @Nonnull Scope scope) {
        this.type = type;
        this.scope = scope;
        this.module = module;
        this.subTypes = new ArrayList<>();
        this.reflection = new Reflection(type);
        this.subTypes.add(type.getSuperclass());
        this.subTypes.addAll(Arrays.asList(type.getInterfaces()));
    }

    /**
     * Returns the instance of the class.
     * Also, if scope is prototype, it will
     * create a new instance every time.
     *
     * @return instance
     */
    public final Object getInstance() {
        return (this.scope == Scope.SINGLETON) ? this.instance : this.createInstance();
    }

    /**
     * Returns the parameters of the class.
     *
     * @return parameters
     */
    public final @Nullable Object[] getParameters() {
        return this.parameters;
    }

    /**
     * Creates an instance of the class.
     *
     * @return instance
     */
    public final @Nonnull Module getModule() {
        return this.module;
    }

    /**
     * Returns the scope of the class.
     *
     * @return scope
     */
    public final @Nonnull Scope getScope() {
        return this.scope;
    }

    /**
     * Returns the type of the class.
     *
     * @return type
     */
    public final @Nonnull Class<?> getType() {
        return this.type;
    }

    /**
     * Returns the subtypes of the class.
     *
     * @return subtypes
     */
    public final @Nonnull List<Class<?>> getSubTypes() {
        return this.subTypes;
    }


    /**
     * Sets the scope of the class.
     *
     * @param scope scope
     * @return entity
     */
    public final @Nonnull AbstractEntity withScope(@Nonnull Scope scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Sets the instance of the class.
     *
     * @param instance instance
     * @return entity
     */
    public final @Nonnull AbstractEntity withInstance(@Nonnull Object instance) {
        this.instance = instance;
        return this.withScope(Scope.SINGLETON);
    }

    /**
     * Sets the parameters of the class.
     *
     * @param parameters parameters
     * @return entity
     */
    public final @Nonnull AbstractEntity withParameters(@Nonnull Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Sets the type of the class.
     *
     * @param type type
     * @return entity
     */
    public final @Nonnull AbstractEntity withType(@Nonnull Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the subtypes of the class.
     *
     * @param subTypes subtypes
     * @return entity
     */
    public final @Nonnull AbstractEntity withSubTypes(@Nonnull List<Class<?>> subTypes) {
        this.subTypes = subTypes;
        return this;
    }



    /**
     * Creates an instance of the class
     * by calling the constructor or method.,
     * <p>
     * This method is abstract because it
     * will be implemented in the child classes.
     *
     * @return instance
     */
    public abstract @Nonnull Object createInstance();
}
