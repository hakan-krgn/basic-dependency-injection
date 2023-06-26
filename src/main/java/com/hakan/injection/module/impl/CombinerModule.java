package com.hakan.injection.module.impl;

import com.hakan.injection.module.Module;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinerModule extends Module {

    private final Set<Module> modules;

    public CombinerModule(@Nonnull Module... modules) {
        this(Arrays.asList(modules));
    }

    public CombinerModule(@Nonnull List<Module> modules) {
        this(new HashSet<>(modules));
    }

    public CombinerModule(@Nonnull Set<Module> modules) {
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
