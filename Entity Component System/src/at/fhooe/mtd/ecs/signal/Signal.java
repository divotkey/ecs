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
package at.fhooe.mtd.ecs.signal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The base class for all kinds of signals. Signals transmit events to
 * registered listeners.
 *
 * @param <T>
 *            the type of object that is added as parameter to a dispatched
 *            event
 */
public class Signal<T> {

    /** The registered listeners. */
    private List<SignalListener<T>> listeners = new CopyOnWriteArrayList<>();
    
    /**
     * Adds the specified listener to this signal.
     * 
     * @param l
     *            the listener to be added
     */
    public void addSignalListener(SignalListener<T> l) {
        assert !listeners.contains(l) : "signal listener alreaedy added";
        listeners.add(l);
    }
    
    /**
     * Removes the specified listener from this signal.
     * 
     * @param l
     *            the listener to be removed
     */
    public void removeSignalListener(SignalListener<T> l) {
        listeners.remove(l);
    }
    
    /**
     * Dispatches the event to all registered listeners.
     * 
     * @param o
     *            the object that comes along with the dispatched event
     */
    public void dispatch(T o) {
        for (SignalListener<T> l : listeners) {
            l.receive(o);
        }
    }
    
}
