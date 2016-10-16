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

/**
 * The base class for all components.
 */
public abstract class Component {

    /** A reference to the entity to witch this component belongs. */
    private Entity entity;
    
    /** Indicates if this component has been activated. */
    private boolean activated;

    /**
     * Sets the reference to the entity this component has been added to. This
     * method is called by the entity when the component is added.
     * 
     * @param e
     *            the parent entity
     */
    final void setEntity(Entity e) {
        entity = e;
    }

    /**
     * Returns the entity this component belongs to.
     * 
     * @return the entity of this component or {@code null} if this component
     *         has not been added to an entity yet
     */
    public final Entity getEntity() {
        return entity;
    }
    
    /**
     * A convenience method to retrieve a system. This method uses the engine
     * this component's entity is attach to and retrieves the requested system.
     * <p>
     * Calling this method is equivalent to
     * </p>
     * 
     * <pre>
     * getEntity().getEngine().getSystem(...)
     * </pre>
     * 
     * @param <T>
     *            type parameter used to avoid casts, irrelevant when calling
     *            this method
     * @param clazz
     *            the interface or class the requested system must implement
     * @return the system that matches the requirements
     * @throws IllegalArgumentException
     *             in case no suitable system could be found
     * @throws NullPointerException
     *             if this component has not been added to an entity
     */
    final protected <T> T getSystem(Class<T> clazz) throws IllegalArgumentException,
            NullPointerException {
        return entity.getEngine().getSystem(clazz);
    }
    
    
    /**
     * A convenience method to retrieve a component. This method retrieves a
     * component from this componen's entity.
     * <p>
     * Calling this method is equivalent to
     * </p>
     * 
     * <pre>
     * getEntity().getComponent(...)
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
     * @throws NullPointerException
     *             if this component has not been added to an entity
     */
    final protected <T> T getComponent(Class<T> clazz)
            throws IllegalArgumentException, NullPointerException {
        return entity.getComponent(clazz);
    }
    
    /**
     * A convenience method to retrieve a component's engine. This method
     * retrieves the engine this component's entity is attached to.
     * <p>
     * Calling this method is equivalent to
     * </p>
     * 
     * <pre>
     * getEntity().getEngine()
     * </pre>
     * 
     * @return the engine of this componen's entity or {@code null} if this
     *         entity has not been added to an entity yet
     * @throws NullPointerException
     *             if this component has not been added to an entity
     */
    final protected Engine getEngine() throws NullPointerException {
        return entity.getEngine();
    }
    
    /**
     * Returns the state of this component.
     * 
     * @return {@code true} if this component is activated, otherwise
     *         {@code false}
     */
    protected final boolean isActivated() {
        return activated;
    }

    /**
     * This method is invoked when this component gets activated. It can be
     * overwritten by derived classed to initialize the component.
     */
    protected void activate() { }

    /**
     * Activates this component. This method is called by its parent entity when
     * the entity itself gets activated.
     */
    final void activateInternal() {
        if (isActivated()) {
            throw new IllegalStateException("component already activated");
        }
        activate();
        activated = true;
    }
    
    /**
     * This method is invoked when this component gets deactivated. It can be
     * overwritten by derived classed to clean up the component.
     */
    protected void deactivate() { }
    
    /**
     * Deactivates this component. This method is called by its parent entity
     * when the entity itself gets deactivated.
     */
    final void deactivateInternal() {
        if (!isActivated()) {
            throw new IllegalStateException("component not activated");
        }
        deactivate();
        activated = false;
    }
}
