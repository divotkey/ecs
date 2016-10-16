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

import java.util.HashSet;

import java.util.Set;

/**
 * An entity family specified criteria which must be met by an entity in order
 * to be considered to be a member of this family. This version currently
 * supports a list of component types as criterion, where all types must exists
 * within the entity (and operation).
 * 
 * <p>
 * <strong>Note</strong>: It is safe to compare different instance of Family
 * objects using the {@code equals} method. Both methods {@code equals} and
 * {@code hashCode} are implemented by this class. This means that Families can
 * be used as keys in associative containers like {@code HashMap} as keys.
 * </p>
 * 
 * <p>
 * An entity family can be created by using the static factory method
 * {@link create} which takes an arbitrary number of parameters that specified
 * the different types of components this family must contain in order to accept
 * an entity as member.
 * </p>
 * <p>
 * <strong>Example</strong>
 * </p>
 * 
 * <pre>
 * Family f1 = Family.create(Pose.class, Visual.class);
 * Family f2 = Family.create(Pose.class, Collider.class);
 * Family f3 = Family.create(Pose.class, ItemLogic.class, Destroyable.class);
 * </pre>
 * 
 */
public class EntityFamily {

    /** The set of types an entity must contain as component. */
    private Set<Class<?>> types = new HashSet<>();

    /**
     * Creates a new instance of an entity family.
     * 
     * @param types
     *            the types of components an entity must contain in order to be
     *            member of this family
     * @return the new instance
     */
    public static EntityFamily create(Class<?>... types) {
        EntityFamily family = new EntityFamily();
        for (Class<?> type : types) {
            family.types.add(type);
        }
        return family;
    }
    
    /**
     * The constructor is private in order to force the static factory method to
     * be used.
     */
    private EntityFamily() {
        // intentionally left empty
    }
    
    /**
     * Tests if the specified entity is a member of this family.
     * 
     * @param e
     *            the entity to be tested
     * @return {@code true} if the entity is a member, {@code false} otherwise
     */
    public boolean isMember(Entity e) {
        for (Class<?> type : types) {
            if (!e.hasComponent(type)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((types == null) ? 0 : types.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityFamily other = (EntityFamily) obj;
        if (types == null) {
            if (other.types != null)
                return false;
        } else if (!types.equals(other.types))
            return false;
        return true;
    }
    
}
