package com.hakan.injection.module;

import com.hakan.injection.annotations.Provide;
import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.AbstractEntityFactory;
import com.hakan.injection.entity.impl.EmptyEntity;
import com.hakan.injection.entity.impl.InjectorEntity;
import com.hakan.injection.entity.impl.ProviderEntity;
import com.hakan.injection.reflection.Reflection;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashSet;
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
        this.entities = new HashSet<>();
        this.reflection = new Reflection(this.getClass());
        this.reflection.getMethodsAnnotatedWith(Provide.class).forEach(this::bind);
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
        return (T) this.getEntityByClass(clazz).getInstance();
    }

    /**
     * Creates an abstract entity from the
     * class and binds it to the module.
     *
     * @param clazz class
     * @return abstract entity
     */
    public final @Nonnull AbstractEntity bind(@Nonnull Class<?> clazz) {
        return this.bind(AbstractEntityFactory.create(this, clazz));
    }

    /**
     * Creates an abstract entity from the
     * method and binds it to the module.
     *
     * @param method method
     * @return abstract entity
     */
    public final @Nonnull AbstractEntity bind(@Nonnull Method method) {
        return this.bind(AbstractEntityFactory.create(this, method));
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
        this.entities.addAll(module.entities);
    }

    /**
     * Creates all instances of the entities
     * that are bound to the module.
     */
    public final void create() {
        this.entities.forEach(this::createInstance);
    }


    /**
     * Configures the module
     * by binding the entities.
     */
    public abstract void configure();



    /**
     * Gets the entity by the class type.
     *
     * @param clazz class type
     * @return entity
     */
    public @Nonnull AbstractEntity getEntityByClass(@Nonnull Class<?> clazz) {
        return this.entities.stream()
                .filter(entity -> entity.getType().equals(clazz) || entity.getSubTypes().contains(clazz))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no inject entity found for class " + clazz.getName()));
    }

    /**
     * Creates an instance of the entity.
     *
     * @param entity entity
     * @return instance
     */
    @SneakyThrows
    public @Nonnull Object createInstance(@Nonnull AbstractEntity entity) {
        if (entity instanceof EmptyEntity) {
            EmptyEntity emptyEntity = (EmptyEntity) entity;
            return emptyEntity.createInstance();
        } else if (entity instanceof InjectorEntity) {
            InjectorEntity injectorEntity = (InjectorEntity) entity;
            return injectorEntity.createInstance();
        } else if (entity instanceof ProviderEntity) {
            ProviderEntity providerEntity = (ProviderEntity) entity;
            return providerEntity.createInstance();
        }

        throw new RuntimeException("unknown entity type " + entity.getClass().getName());
    }
}
