# Component-Utils

Utility functions for Stuart Sierra's [Component][] library.

[Component]: https://github.com/stuartsierra/component

## Installation

Add the following dependency to your project.clj file:

```clojure
[listora/component-utils "0.1.0"]
```

## Usage

This library currently just includes verbose versions of `start`,
`stop`, `start-system` and `stop-system`. These will log a message via
[Timbre][] when a component is started or stopped.

```clojure
(require '[listora.util.component :refer [verbose-start-system]])

(verbose-start-system your-system)
```

[Timbre]: https://github.com/ptaoussanis/timbre

## License

Copyright © 2014 Listora

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
