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

import java.util.List;

/**
 * An engine system that processes a component family.
 */
public abstract class IteratingSystem extends EngineSystem {

	/** The family of components this system processes. */
	private EntityFamily family;
	
	/** View to the currently present entities which match the family. */
	private List<Entity> entities;
	
	/**
	 * Creates a new instance
	 * 
	 * @param family
	 *            the family of entity components this system processes
	 * @throws NullPointerException
	 *             in case the specified entity family is null
	 */
	public IteratingSystem(EntityFamily family) {
		if (family == null) {
			throw new NullPointerException("entity family must not be null");
		}
		this.family = family;
	}
	
	@Override
	public void addedToEngine(Engine e) {
		entities = e.getEntities(family);
	}

	@Override
	public void removedFromEngine(Engine e) {
		entities = null;
	}

	@Override
	public void update(double dt) {
		for (int i = 0; i < entities.size(); ++i) {
			processEntity(entities.get(i), dt);
		}
	}

	/**
	 * Returns the list of entities this system processes.
	 * 
	 * @return the list of entities.
	 */
	public final List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Processes one entity of the family.
	 * 
	 * @param entity
	 *            the entity to process
	 * @param dt
	 *            the elapsed time in seconds
	 */
	protected abstract void processEntity(Entity e, double dt);

}
