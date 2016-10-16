# Version History

## Version 1.3.1

Date: *2016-06-??*

- Made `EngineSystem` abstract.

## Version 1.3.0
Date: *2016-05-03*

- Convenience methods in `Component` are final now.
- `Engine` class offers option to iterate through all registered systems.
- Added support for entity listeners that get informed if entities get added or removed.
- Added method to `Entity` class to retrieve a list of components of a specific type.
- Added simple event system using signals to transmit events.
- Added `removeAll` method which removes all entities at once.

## Version 1.2.0
Date: *2016-04-16*

- Fixed bug in component base class preventing components to get activated.
- Fixed several bugs in entity class.
- Added convenience method `getSystem` to component base class. 
- Added convenience method `getComponent` to component base class. 
- Added convenience method `getEngine` to component base class. 

## Version 1.1.0
Date: *2016-04-15*

- Added JavaDoc comments
- Improved access modifiers for several methods
- Components are now deactivated in reverse order


## Version 1.0.0
Date: *2016-04-14*

- Initial Version