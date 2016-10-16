/*******************************************************************************
 * Copyright (c) 2016 Roman Divotkey, Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *  
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *  
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/
package at.fhooe.mtd.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The component container class for entities.
 */
public final class Entity {

    /** The list of components this entity is composed of. */
    private List<Component> components = new ArrayList<>();
    
    /** Provides fast access to components based on its type. */
    private Map<Class<?>, Component> cache = new HashMap<>();
    
    /** Indicates if this entity has been activated. */
    private boolean activated;

    /** A reference to the engine this entity has been added to. */
    private Engine engine;
    
    
    /**
     * Sets the references to the engine this entity belongs to. This method is
     * called by the engine when this entity is added.
     * 
     * @param e
     *            the engine this entity belongs to
     */
    void setEngine(Engine e) {
        engine = e;
    }
    
    /**
     * Return the engine this entity has been attached to.
     * 
     * @return the engine of this entity or {@code null} if this entity has not
     *         been added to an entity yet
     */
    public Engine getEngine() {
        return engine;
    }
        
    /**
     * Returns {@code true} if this entity has a component of the specified
     * type. A type can either be an interface of a class.
     * 
     * <pre>
     * Entity bar = getEntityFromSomeWhere();
     * 
     * if (bar.hasComponent(Foo.clazz)) {
     *     System.out.println(&quot;Entity bar has component of type Foo&quot;);
     * }
     * </pre>
     * 
     * @param clazz
     *            the class or interface the requested component must implement
     * @return {@code true} if the requested component is part of this entity
     */
    public boolean hasComponent(Class<?> clazz) {
        if (cache.containsKey(clazz)) {
            return true;
        }
        
        for (Component c : components) {
            if (clazz.isInstance(c)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retrieves the component of this entity of the specified type. A type can
     * either be an interface of a class.
     * 
     * <p>
     * If more than one component implements the specified type, the first
     * component that matches the requirements will be returned.
     * </p>
     * 
     * <p>
     * This method returns throws an {@code IllegalArgumentException} in case no
     * component can be found that matches the requested type. This is a
     * deliberate design decision to avoid repetitive checks against
     * {@code null} references. In almost all cases one can assume that the
     * requested component must be part of the used entity. If this fails, this
     * is most likely an error condition that should be indicated as fast as
     * possible (<i>fail-fast</i>). In cases where it is not certain that an
     * entity has contains a certain type of component the {@link #hasComponent}
     * can be used test this.
     * </p>
     * 
     * <p>
     * <strong>Example</strong>
     * </p>
     * 
     * <pre>
     * public interface CollisionHandler {
     *     public void handleCollision();
     * }
     * 
     * public class Foo extends Component implements CollisionHandler {
     * 
     *     public void handleCollision() {
     *         //...
     *     }
     *     
     *     public void doSomething() {
     *         //...
     *     }
     *     
     *     //...
     * }
     * 
     * Entity bar = getEntityFromSomeWhere();
     * bar.addComponent(new Foo());
     * //..
     * 
     * // Invoke collision handling without knowing about Foo
     * bar.getComponent(CollisionHandler.class).handleCollision();
     * //..
     *  
     * // Retrieve component Foo
     * Foo foo = bar.getComponent(Foo.class);
     * foo.handleCollision();
     * foo.doSomething();
     * </pre>
     * 
     * @param <T>
     *            type parameter used to avoid casts, irrelevant when calling
     *            this method
     * @param clazz
     *            the class or interface the requested component must implement
     * @return the component that implements the specified interface of class
     * @throws IllegalArgumentException
     *             if no component with the specified type could be found
     */
    public <T> T getComponent(Class<T> clazz) throws IllegalArgumentException {
        Component cached = cache.get(clazz);
        if (cached != null) {
            return clazz.cast(cached);
        }
        
        for (Component c : components) {
            if (clazz.isInstance(c)) {
                cache.put(clazz, c);
                return clazz.cast(c);
            }
        }
        throw new IllegalArgumentException("component not found "
                + clazz.getName());
    }
    
    /**
     * Retrieves a list of all components of the specified type.
     * 
     * <p>
     * <strong>Note: </strong> A new list object will be created each time this
     * method is called. Do not use this method too often.
     * </p>
     * 
     * @param <T>
     *            type parameter used to avoid casts, irrelevant when calling
     *            this method
     * @param clazz
     *            the class or interface the requested components must implement
     * @return a list with components of the specified type (may be empty)
     */
    public <T> List<T> getAllComponents(Class<T> clazz) {
        ArrayList<T> result = new ArrayList<T>();
        for (Component c : components) {
            if (clazz.isInstance(c)) {
                result.add(clazz.cast(c));
            }
        }
        
        return result;
    }
    
    /**
     * Adds the specified component to this entity. Component cannot be added if
     * this entity has already been added to an engine and activated.
     * 
     * @param c
     *            the component to be added
     */
    public void addComponent(Component c) {
        if (isActivated() || engine != null) {
            throw new IllegalStateException(
                    "cannot add component to activated entity");
        }
        if (c.getEntity() != null) {
            throw new IllegalArgumentException(
                    "component already attached an entity");
        }
        components.add(c);
        c.setEntity(this);
        if (isActivated() && !c.isActivated()){
            c.activateInternal();
        }
    }
        
    /**
     * Returns the activation state of this entity.
     * 
     * @return {@code true} if this entity has been activated, {@code false}
     *          otherwise
     */
    boolean isActivated() {
        return activated;
    }
    
    /**
     * Activates this entity. This method is called by the engine this entity
     * has been added to. Activating an entity will activate its components in
     * exactly the order its components have been added.
     */
    void activate() {
        if (isActivated()) {
            throw new IllegalStateException("entity already activated");
        }

        // activate components
        for (Component c : components) {
            if (!c.isActivated()) {
                c.activateInternal();
            }
        }
        activated = true;
    }
    
    /**
     * Deactivates this entity. This method is called by the engine this entity
     * has been added to. Deactivating an entity will deactivate its component
     * in reverse order to ensure dependencies are resolved correctly.
     */
    void deactivate() {
        if (!isActivated()) {
            throw new IllegalStateException("entity not activated");
        }
        
        // deactivate components in reverse order
        for (int i = components.size() - 1; i >= 0; --i) {
            Component c = components.get(i);
            if (c.isActivated()) {
                c.deactivateInternal();
            }
        }              
        activated = false;        
    }

}
