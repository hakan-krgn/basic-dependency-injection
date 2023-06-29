package com.hakan.injection.entity;

import com.hakan.injection.module.Module;
import com.hakan.injection.reflection.Reflection;

import javax.annotation.Nonnull;
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

    protected Module module;
    protected Scope scope;
    protected Object instance;
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
    public final @Nonnull Object getInstance() {
        if (this.instance == null || this.scope == Scope.PROTOTYPE)
            return this.createInstance();
        return this.instance;
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
     * Sets the module of the class.
     *
     * @param module module
     * @return entity
     */
    public final @Nonnull AbstractEntity withModule(@Nonnull Module module) {
        this.module = module;
        return this;
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
