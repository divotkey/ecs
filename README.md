# Entity Component System

This <em>Entity Component System</em> (ECS) represents a manageable
set of classes and interfaces that realize a component-based entity
management for interactive applications and games.

The ECS does not contain any references to other external packages or
libraries and can be used together with any game or graphics library
to created applications that need entity management. However, due to
the fact that is was originally intended to be used together with the
<em>Simple Game Library</em> (SGL), it follows the intention and
spirit of SGL. This means that the way it is implemented is more
dedicated to idea of being readable and comprehensible and not of
being the fastest component system on this planet.

Nonetheless, ECS happened to turn out as usable and fast-enough
component system in each and every case of its reported usage. In
almost any case where performance issues need to be addressed, the
component-system is unlikely to be the bottleneck.

If you tend to disagree regarding these performance
considerations, this is perfectly fine - but please do not tell us or
start a discussion. Go ahead and use another component-system and be
happy with it and so are we.

## Disclaimer

THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED
TO ADDRESS ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.

## Version History

### Version 1.3.1
Release Date: unpublished

* Made [EngineSystem](https://github.com/divotkey/ecs/blob/master/Entity%20Component%20System/src/at/fhooe/mtd/ecs/EngineSystem.java) abstract.
* Added abstract [IteratingSystem](https://github.com/divotkey/ecs/blob/master/Entity%20Component%20System/src/at/fhooe/mtd/ecs/IteratingSystem.java) that processes one
    family of entities.
* Added some additional JavaDoc comments.
* System can be enabled or disabled.

### Version 1.3.0
Release Date: 2016-05-03

* Convenience methods in [Component](https://github.com/divotkey/ecs/blob/master/Entity%20Component%20System/src/at/fhooe/mtd/ecs/Component.java) are final now.
* [Engine](https://github.com/divotkey/ecs/blob/master/Entity%20Component%20System/src/at/fhooe/mtd/ecs/Engine.java) class offers option to iterate through all registered systems.
* Added support for entity listeners that get informed if entities get added or removed.
* Added method to [Entity](https://github.com/divotkey/ecs/blob/master/Entity%20Component%20System/src/at/fhooe/mtd/ecs/Entity.java) class to retrieve a list of components of a specific type.
* Added simple event system using signals to transmit events.
* Added `removeAll` method which removes all entities at once.

### Version 1.2.0
Release Date: 2016-04-16

* Fixed bug in component base class preventing components to get activated.
* Fixed several bugs in entity class.
* Added convenience method `getSystem()` to component base class.
* Added convenience method `getComponent()` to component base class.
* Added convenience method `getEngine()` to component base class.

### Version 1.1.0
Release Date: 2016-04-15

* Added JavaDoc comments.
* Improved access modifiers for several methods.
* Components are now deactivated in reverse order.

###  Version 1.0.0
Release Date: 2016-04-14

* Initial version.
