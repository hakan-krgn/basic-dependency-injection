package com.hakan.injection;

import com.hakan.injection.module.Module;
import com.hakan.injection.module.impl.CombinerModule;

import javax.annotation.Nonnull;

public class Injector {

    public static @Nonnull Injector of(@Nonnull Module... modules) {
        return new Injector(new CombinerModule(modules)).init();
    }



    private final Module module;

    private Injector(@Nonnull Module module) {
        this.module = module;
    }

    public @Nonnull <T> T getInstance(@Nonnull Class<T> clazz) {
        return this.module.getInstance(clazz);
    }


    private @Nonnull Injector init() {
        this.module.configure();
        this.module.create();
        return this;
    }
}
