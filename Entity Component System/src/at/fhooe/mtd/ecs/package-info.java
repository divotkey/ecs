/**
 * This package contains interfaces and classes to provide a simple but somewhat
 * complete component-based entity management system (ECS).
 * 
 * <h2>Version History</h2>
 *  
 * <h3>Version 1.3.1</h3>
 * <p><i>Release Date: unpublished</i></p>
 * <ul>
 * <li>Made {@code EngineSystem} abstract.</li>
 * <li>Added abstract {@code IteratingSystem} that processes one family of entities.</li>
 * <li>Added some additional JavaDoc comments.</li>
 * </ul>
 * 
 * <h3>Version 1.3.0</h3>
 * <p><i>Release Date: 2016-05-03</i></p>
 * <ul>
 * <li>Convenience methods in {@code Component} are final now.</li>
 * <li>{@code Engine} class offers option to iterate through all registered systems.</li>
 * <li>Added support for entity listeners that get informed if entities get added or removed.</li>
 * <li>Added method to {@code Entity} class to retrieve a list of components of a specific type.</li>
 * <li>Added simple event system using signals to transmit events.</li>
 * <li>Added {@code removeAll} method which removes all entities at once.</li>
 * </ul>
 * 
 * <h2>Version History</h2>
 *  
 * <h3>Version 1.2.0</h3>
 * <p><i>Release Date: 2016-04-16</i></p>
 * <ul>
 * <li>Fixed bug in component base class preventing components to get activated.</li>
 * <li>Fixed several bugs in entity class.</li>
 * <li>Added convenience method {@code getSystem} to component base class.</li>
 * <li>Added convenience method {@code getComponent} to component base class.</li>
 * <li>Added convenience method {@code getEngine} to component base class.</li>
 * </ul>
 * 
 * <h3>Version 1.1.0</h3>
 * <p><i>Release Date: 2016-04-15</i></p>
 * <ul>
 * <li>Added JavaDoc comments.</li>
 * <li>Improved access modifiers for several methods.</li>
 * <li>Components are now deactivated in reverse order.</li>
 * </ul>
 *  
 * <h3>Version 1.0.0</h3>
 * <p><i>Release Date: 2016-04-14</i></p>
 * <ul>
 * <li>Initial version.</li>
 * </ul>
 */

package at.fhooe.mtd.ecs;