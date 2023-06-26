package com.hakan.injection;

import com.hakan.injection.module.Module;
import com.hakan.injection.module.impl.CombinerModule;

public class Injector {

    public static Injector of(Module... modules) {
        return new Injector(new CombinerModule(modules)).init();
    }



    private final Module module;

    private Injector(Module module) {
        this.module = module;
    }

    public <T> T getInstance(Class<T> clazz) {
        return this.module.getInstance(clazz);
    }


    private Injector init() {
        this.module.configure();
        this.module.create();
        return this;
    }
}
