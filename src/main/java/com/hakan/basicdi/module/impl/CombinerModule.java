package com.hakan.basicdi.module.impl;

import com.hakan.basicdi.module.Module;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CombinerModule is a module class that
 * combines multiple modules into one.
 */
public class CombinerModule extends Module {

    private final Set<Module> modules;

    /**
     * Constructor of {@link CombinerModule}.
     *
     * @param modules modules
     */
    public CombinerModule(@Nonnull Module... modules) {
        this(Arrays.asList(modules));
    }

    /**
     * Constructor of {@link CombinerModule}.
     *
     * @param modules modules
     */
    public CombinerModule(@Nonnull List<Module> modules) {
        this(new HashSet<>(modules));
    }

    /**
     * Constructor of {@link CombinerModule}.
     *
     * @param modules modules
     */
    public CombinerModule(@Nonnull Set<Module> modules) {
        this.modules = modules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure() {
        this.modules.forEach(this::install);
    }
}
