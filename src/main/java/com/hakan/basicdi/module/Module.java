package com.hakan.basicdi.module;

import com.hakan.basicdi.annotations.PostConstruct;
import com.hakan.basicdi.annotations.Provide;
import com.hakan.basicdi.entity.AbstractEntity;
import com.hakan.basicdi.entity.EntityFactory;
import com.hakan.basicdi.reflection.Reflection;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Module is an abstract class that
 * contains entities which are
 * created by the methods and constructors
 * that are annotated with @Provide and @Autowired
 * annotations.
 * <p>
 * Module is used to create instances of the classes
 * that are annotated with @Service or @Component
 * annotations.
 */
@SuppressWarnings({"unchecked"})
public abstract class Module {

    private final Reflection reflection;
    private final Set<AbstractEntity> entities;

    /**
     * Constructor of {@link Module}
     */
    public Module() {
        this.entities = new LinkedHashSet<>();
        this.reflection = new Reflection(this.getClass());
        this.reflection.getMethodsAnnotatedWith(Provide.class).forEach(this::bind);
    }

    /**
     * Creates an abstract entity from the
     * class and binds it to the module.
     *
     * @param clazz class
     * @return abstract entity
     */
    public final @Nonnull AbstractEntity bind(@Nonnull Class<?> clazz) {
        return this.bind(EntityFactory.create(this, clazz));
    }

    /**
     * Creates an abstract entity from the
     * method and binds it to the module.
     *
     * @param method method
     * @return abstract entity
     */
    public final @Nonnull AbstractEntity bind(@Nonnull Method method) {
        return this.bind(EntityFactory.create(this, method));
    }

    /**
     * Binds the entity to the module.
     *
     * @param entity entity
     * @return abstract entity
     */
    public final @Nonnull AbstractEntity bind(@Nonnull AbstractEntity entity) {
        this.entities.add(entity);
        return entity;
    }


    /**
     * Installs the all entities of the module
     * to the current module.
     *
     * @param module module to install
     */
    public final void install(@Nonnull Module module) {
        module.configure();
        module.entities.forEach(entity -> entity.withModule(this));

        this.entities.addAll(module.entities);
    }

    /**
     * Creates all instances of the entities
     * that are bound to the module.
     */
    @SneakyThrows
    public final void create() {
        for (AbstractEntity entity : this.entities) {
            Object instance = entity.createInstance();
            Reflection reflection = new Reflection(instance.getClass());

            for (Method method : reflection.getMethodsAnnotatedWith(PostConstruct.class))
                method.invoke(instance);
        }
    }


    /**
     * Gets the instance of the
     * entity by the class type.
     *
     * @param clazz class type
     * @param <T>   type
     * @return instance
     */
    public final @Nonnull <T> T getInstance(@Nonnull Class<? extends T> clazz) {
        return (T) this.getEntity(clazz).getInstance();
    }

    /**
     * Gets the entity by the class type.
     *
     * @param clazz class type
     * @return entity
     */
    public final @Nonnull AbstractEntity getEntity(@Nonnull Class<?> clazz) {
        return this.entities.stream()
                .filter(entity -> entity.getType().equals(clazz) || entity.getSubTypes().contains(clazz))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no inject entity found for class " + clazz.getName()));
    }



    /**
     * Configures the module
     * by binding the entities.
     */
    public abstract void configure();
}
