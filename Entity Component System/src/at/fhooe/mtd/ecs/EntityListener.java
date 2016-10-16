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
 * The interface for entity listeners. Entity listeners get informed when
 * entities are added or removed.
 */
public interface EntityListener {

    /**
     * Invoked if a new entity is added.
     * 
     * @param e
     *            the new entity
     */
    public void entityAdded(Entity e);
    
    /**
     * Invoked if an entity is removed.
     * 
     * @param e
     *            the entity about to be removed
     */
    public void entityRemoved(Entity e);
    
}
