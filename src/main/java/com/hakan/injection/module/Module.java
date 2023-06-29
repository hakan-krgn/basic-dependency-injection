package com.hakan.injection.module;

import com.hakan.injection.annotations.Order;
import com.hakan.injection.annotations.Provide;
import com.hakan.injection.entity.AbstractEntity;
import com.hakan.injection.entity.EntityFactory;
import com.hakan.injection.reflection.Reflection;

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
public abstract class Module implements Comparable<Module> {

    private final Reflection reflection;
    private final Set<AbstractEntity> entities;
    private final int priority;

    /**
     * Constructor of {@link Module}
     */
    public Module() {
        this.entities = new LinkedHashSet<>();
        this.reflection = new Reflection(this.getClass());
        this.reflection.getMethodsAnnotatedWith(Provide.class).forEach(this::bind);
        this.priority = this.getClass().isAnnotationPresent(Order.class) ?
                this.getClass().getAnnotation(Order.class).value() : 100;
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
    public final void create() {
        this.entities.forEach(AbstractEntity::createInstance);
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
     * Compares this module with the specified module
     * for order.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this module is less than,
     * equal to, or greater than the specified module.
     */
    @Override
    public int compareTo(@Nonnull Module o) {
        return Integer.compare(this.priority, o.priority);
    }



    /**
     * Configures the module
     * by binding the entities.
     */
    public abstract void configure();
}
