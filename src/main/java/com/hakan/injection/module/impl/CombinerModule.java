package com.hakan.injection.module.impl;

import com.hakan.injection.module.Module;

public class CombinerModule extends Module {

    private final Module[] modules;

    public CombinerModule(Module... modules) {
        this.modules = modules;
    }

    @Override
    public void configure() {
        for (Module module : this.modules) {
            module.configure();
            this.install(module);
        }
    }
}
