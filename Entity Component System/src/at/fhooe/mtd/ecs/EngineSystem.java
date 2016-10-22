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
 * A engine system maintains a certain aspect of the engine.
 * <p>
 * The most common case is to process a certain family of entities (e.g.
 * position update). However, due to the fact that engine systems are accessible
 * by all other systems and get updated each cycle of the game loop, engine
 * system can be used to realize other tasks that do not directly process
 * entities (e.g. camera management or centralized input processing).
 * </p>
 */
public abstract class EngineSystem {

    /** A reference to the engine this system belongs to. */
    private Engine engine;
    
    /**
     * Sets the reference to the engine this system belongs to.
     * 
     * @param e
     *            the engine
     */
    final void setEngine(Engine e) {
        engine = e;
    }
    
    /**
    /* Returns the engine this system belongs to.
     * 
     * @return the engine of this system
     */
    public final Engine getEngine() {
        return engine;
    }

    /**
     * Invoked when this system is added to an engine. This method can be
     * overwritten by derived classes to provide initialization code.
     * 
     * <p>
     * The engine provided as parameter is identical to the engine that will be
     * returned by the {@link #getEngine} method.
     * </p>
     * 
     * @param e
     *            the engine this system has been added to
     */
    public void addedToEngine(Engine e) {}
    
    /**
     * Invoked when this system is removed from an engine. This method can be
     * overwritten by derived classes to provide clean up code. This method 
     * will also be called when the engine gets disposed.
     * 
     * <p>
     * The engine provided as parameter is identical to the engine that will be
     * returned by the {@link #getEngine} method.
     * </p>
     * 
     * @param e
     *            the engine this system has been added to
     */
    public void removedFromEngine(Engine e) {}
    
    /**
     * Called by the engine when the engines update method is invoked. In
     * general this method will be called once each game loop cycle.
     * 
     * @param dt
     *            the delta time in seconds
     */
    public void update(double dt) { }
    
}
