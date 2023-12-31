package com.hakan.basicdi;

import com.hakan.basicdi.module.Module;
import com.hakan.basicdi.module.impl.CombinerModule;

import javax.annotation.Nonnull;

/**
 * Injector is a class that provides dependency injection
 * for the objects that are registered by the {@link Module}.
 */
public class Injector {

    /**
     * Creates a new {@link Injector} instance with the given {@link Module}.
     *
     * @param modules modules
     * @return new instance of {@link Injector}
     */
    public static @Nonnull Injector of(@Nonnull Module... modules) {
        return new Injector(new CombinerModule(modules)).configure();
    }



    private final Module module;

    /**
     * Constructor of {@link Injector}.
     *
     * @param module module
     */
    private Injector(@Nonnull Module module) {
        this.module = module;
    }

    /**
     * Gets the module of the {@link Injector}.
     *
     * @return module
     */
    public @Nonnull Module getModule() {
        return this.module;
    }

    /**
     * Returns the instance of the given class.
     *
     * @param clazz class
     * @param <T>   type
     * @return instance
     */
    public @Nonnull <T> T getInstance(@Nonnull Class<T> clazz) {
        return this.module.getInstance(clazz);
    }

    /**
     * Initializes the {@link Injector}.
     *
     * @return this
     */
    public @Nonnull Injector configure() {
        this.module.configure();
        return this;
    }

    /**
     * Creates all instances of the entities
     * that are bound to this injector.
     *
     * @return this
     */
    public @Nonnull Injector create() {
        this.module.create();
        return this;
    }
}
