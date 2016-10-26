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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The engine is actual management class of this framework. If accomplishes
 * several tasks:
 * 
 * <ul>
 * <li>Management of (active) entities.</li>
 * <li>Service locator for all kind of systems (services) within the
 * application.</li>
 * <li>Central update mechanism of attached systems.</li>
 * </ul>
 * 
 * <p>
 * In general there will be only one instance of an engine within an application
 * at a time. It is fine to create, initialize and destroy an engine for each
 * individual state of the application. The update method of the engine should
 * be called once each cycle of the main loop, passing the actual delta time as
 * parameter.
 * </p>
 * 
 * <p><strong>Example</strong></p>
 * <pre>
 * public class PlayState extends GameState {
 * 
 *     private Engine engine;
 *     
 *     public void enterState() {
 *         engine = new Engine();
 *         engine.addSystem(new RenderSystem());
 *         engine.addSystem(new CollisionSystem());
 *         engine.addSystem(new PhysicsSystem());
 *         //...
 *     }
 *     
 *     public void exitState() {
 *         engine.dispose();
 *         engine = null;
 *     }
 * 
 *     public void update(double dt) {
 *         engine.update(dt);
 *     } 
 * }
 * </pre>
 *
 */
public final class Engine {

    /** The list of entities added to this engine. */
    private List<Entity> entities = new ArrayList<>();
    
    /** A map for all the different view of the entities. */
    private Map<EntityFamily, List<Entity>> views = new HashMap<>();
    
    /** List of pending commands. */
    private List<Command> commands = new ArrayList<>();
    
    /** List of added systems. */
    private List<EngineSystem> systems = new ArrayList<>();

    /** Registered entity listeners. */
    private List<EntityListener> listeners = new CopyOnWriteArrayList<EntityListener>();    
    
    /** Indicates if an update cycle is currently in progress. */
    private boolean updating;
    
    
    /**
     * Adds the specified entity listener to this engine.
     * 
     * @param l
     *            the entity listener to be added
     */
    public void addEntityListener(EntityListener l) {
        assert !listeners.contains(l) : "listener already added " + l;
        listeners.add(l);
    }
    
    /**
     * Removes the specified entity listeners from this engine.
     * 
     * @param l
     *            the entity listener to be removed
     */
    public void removeEntityListener(EntityListener l) {
        listeners.remove(l);
    }
    
    /**
     * Adds the specified entity to this engine. If the entity is added during
     * an update cycle, the entity will be added when the update cycle is
     * complete.
     * 
     * @param e
     *            the entity to be added
     */
    public void addEntity(Entity e) {
        if (updating) {
            commands.add(() -> { addEntityInternal(e); });
        } else {
            addEntityInternal(e);
        }
    }
    
    /**
     * Removes all entities. If this method is invoked during an update cycle,
     * the command queued and executed when the update cycle is complete.
     */
    public void removeAll() {
        if (updating) {
            commands.add(() -> { removeAllInternal(); });            
        } else {
            removeAllInternal();
        }
    }

    /**
     * Removes the specified entity to this engine. If the entity is removed
     * during an update cycle, the entity will be removed when the update cycle
     * is complete.
     * 
     * @param e the entity to be removed
     */
    public void removeEntity(Entity e) {
        if (updating) {
            commands.add(() -> { removeEntityInternal(e); });
        } else {
            removeEntityInternal(e);
        }
    }
    
    /**
     * The method that actually adds an entity.
     * 
     * @param e
     *            the entity to be added
     */
    private void addEntityInternal(Entity e) {
        if (e.getEngine() != null) {
            throw new IllegalArgumentException(
                    "entity already added to an engine");
        }
        assert e.getEngine() == null;
        assert !e.isActivated();
        assert !entities.contains(e);
        
        entities.add(e);
        e.setEngine(this);
        e.activate();
        
        addEntityToViews(e);

        // inform listeners
        for (EntityListener l : listeners) {
            l.entityAdded(e);
        }
    }
    
    /**
     * Adds the specified entity to the corresponding views.
     * 
     * @param e
     *            the entity to be added
     */
    private void addEntityToViews(Entity e) {
        for (EntityFamily family : views.keySet()) {
            if (family.isMember(e)) {
                views.get(family).add(e);
            }
        }
    }

    /**
     * The method that actually removes the entity.
     * 
     * @param e
     *            the entity to be removed
     */
    private void removeEntityInternal(Entity e) {
        if (e.getEngine() != this) {
            // silently ignore this event (best practice)
            return;
        }
        assert e.getEngine() == this;
        assert e.isActivated();
        assert entities.contains(e);

        // inform listeners as long as the entity is still active
        for (EntityListener l : listeners) {
            l.entityRemoved(e);
        }
        
        // actually remove entity
        e.deactivate();
        e.setEngine(null);
        entities.remove(e);
       
        removeEntityFromViews(e);        
    }
    
    /**
     * The method that actually removes all entities.
     */
    private void removeAllInternal() {
        while (!entities.isEmpty()) {
            removeEntityInternal(entities.get(0));
        }
    }

    /**
     * Removes the specified entity from the corresponding views.
     * 
     * @param e
     *            the entity to be removed
     */
    private void removeEntityFromViews(Entity e) {
        for (List<Entity> view : views.values()) {
            view.remove(e);
        }
    }

    /**
     * Updates this engine and its attached systems. This method should be
     * called once each main cycle.
     * 
     * @param dt
     *            the elapsed time in seconds
     */
    public void update(double dt) {
        assert updating == false;
        updating = true;
        
        // update systems
        for (EngineSystem s : systems) {
        	if (s.isEnabled()) {
                s.update(dt);        		
        	}
        }
        
        // execute pending commands
        for (Command cmd : commands) {
            cmd.execute();
        }
        commands.clear();
        
        updating = false;
    }
    
    /**
     * Returns a list that will contain only entities which are members of the
     * specified family. It is safe to keep a reference of the returned list.
     * The list will be updated each update cycle and show reflect the current
     * state.
     * 
     * <p>
     * The returned list cannot be modified. Any attempt to do so will result in
     * an {@code UnsupportedOperationException}.
     * </p>
     * 
     * @param family
     *            the entity family this requested list should show
     * @return the list of entities
     */
    public List<Entity> getEntities(EntityFamily family) {
        List<Entity> view = views.get(family);
        if (view == null) {
            view = new ArrayList<>();
            views.put(family, view);
            initView(family, view);
        }
        return Collections.unmodifiableList(view);
    }
    
    /**
     * Initializes the specified view.
     * 
     * @param family
     *            the entity family the new view should contain
     * @param view
     *            the view that should be initialized
     */
    private void initView(EntityFamily family, List<Entity> view) {
        assert view.isEmpty();
        for (Entity e : entities) {
            if (family.isMember(e)) {
                view.add(e);
            }
        }
    }

    /**
     * Adds the specified system to this engine. Systems must not be added
     * during an update cycle. This method will call the {@code addedToEngine}
     * method of the specified system.
     * 
     * @param s
     *            the system to be added
     * @throws IllegalStateException
     *             if an update cycle is currently in progress
     * @throws IllegalArgumentException
     *             if the specified system has already been added
     */
    public void addSystem(EngineSystem s) throws IllegalStateException,
            IllegalArgumentException {
        if (updating) {
            throw new IllegalStateException("cannot add system while updating");
        }
        
        if (systems.contains(s)) {
            throw new IllegalArgumentException("system already added");
        }
        
        s.setEngine(this);
        systems.add(s);
        s.addedToEngine(this);
    }
    
    /***
     * Removes the specified system from this engine. Systems must not be
     * removed during an update cycle. This method will call the
     * {@code removedFromEngine} method of the specified system.
     * 
     * @param s
     *            the system to be added
     * @throws IllegalStateException
     *             if an update cycle is currently in progress
     * @throws IllegalArgumentException
     *             if the specified system in unknown
     */
    public void removeSystem(EngineSystem s) throws IllegalStateException,
            IllegalArgumentException {
        if (updating) {
            throw new IllegalStateException(
                    "cannot remove system while updating");
        }
        
        if (!systems.contains(s)) {
            throw new IllegalArgumentException("system is unknown");
        }
        s.removedFromEngine(this);
        systems.remove(s);
        s.setEngine(null);        
    }
        
    /**
     * Returns {@code true} if there exists at least one system that matches the
     * specified interface of class.
     * 
     * @param clazz
     *            the interface or class the requested system must implement
     * @return {@code true} if this engine as matching system, {@code false}
     *         otherwise
     */
    public boolean hasSystem(Class<?> clazz) {
        for (EngineSystem s : systems) {
            if (clazz.isInstance(s)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retrieves the first system that matches the specified interface of class.
     * 
     * @param <T>
     *            type parameter used to avoid casts, irrelevant when calling
     *            this method
     * @param clazz
     *            the interface or class the requested system must implement
     * @return the system that matches the requirements
     * @throws IllegalArgumentException
     *             in case no suitable system could be found
     */
    public <T> T getSystem(Class<T> clazz) throws IllegalArgumentException {
        for (EngineSystem s : systems) {
            if (clazz.isInstance(s)) {
                return clazz.cast(s);
            }
        }
        
        throw new IllegalArgumentException("system not found "
                + clazz.getName());
    }
    
    /**
     * Disposes this engine. All acquired resources will be released, all
     * entities will be deactivated and destroyed, all attached systems will be
     * detached and removed.
     * 
     * <p>
     * Note: The attached systems will be removed in reverse order to resolve
     * dependencies without conflicts.
     * </p>
     * 
     * @throws IllegalStateException
     *             in case this method is called during an update cycle
     */
    public void dispose() throws IllegalStateException {
        if (updating) {
            throw new IllegalStateException("dispose not allowed during update");
        }
        
        // dispose entities
        for (Entity e : entities) {
            if (e.isActivated()) {
                e.deactivate();        
                e.setEngine(null);
            }
        }
        entities.clear();
        views.clear();

        // dispose systems
        for (int i = systems.size() - 1; i >= 0; --i) {
            EngineSystem s = systems.get(i);
            s.removedFromEngine(this);
            s.setEngine(null);
        }                
        systems.clear();
    }
    
    /**
     * Returns the number of system registered at this engine.
     * 
     * @return the number of systems
     */
    public int getNumOfSystems() {
        return systems.size();
    }

    /**
     * Returns the system with the specified index.
     * 
     * @param idx
     *            the index of the system to be returned
     * @return the requested system
     * @throws IndexOutOfBoundsException
     *             in case the specified index is {@code<} 0 or {@code>=} number
     *             of registered systems
     */
    public EngineSystem getSystem(int idx) throws IndexOutOfBoundsException {
        return systems.get(idx);
    }
    
    /**
     * Returns the number of active entities within this engine.
     * 
     * @return the number of entities
     */
    public int getNumOfEntities() {
        return entities.size();
    }
    
    
    /////////////////////////////////////////////////
    /////// Inner classes and interfaces
    /////////////////////////////////////////////////
    
    /**
     * Interface for the command pattern.
     */
    private interface Command {
        void execute();
    }
       
}
