### Overviews:
Dagger is a fully static, compile-time dependency injection framework for Java, Kotlin, and Android

### Dagger Mechanism:
Dagger automatically generates code that

* Generates the container to hold application object/s (`application graph` or `AppContainer`)
* Creates factories for the classes available in the application graph. 
* Decides whether to reuse a dependency or create a new instance through the use of scopes.
* Creates containers for specific flows using Dagger sub-components. This improves your app's performance by releasing objects in memory when they're no longer needed.

Dagger automatically does all of this at build time as long as you declare dependencies of a class and specify how to satisfy them using annotations.

### Dagger Runtime Operations:
At build time, Dagger walks through code and:
- Builds and validates dependency graphs, ensuring that:
    Every object's dependencies can be satisfied, so there are no runtime exceptions.
- No dependency cycles exist, so there are no infinite loops.
- Generates the classes that are used at runtime to create the actual objects and their dependencies.

### @Components and Dependency Graph Container:
Dagger creates a graph of the dependencies (`container`) for the project from a `interface` annotate it with `@Component`. From the container (`interface`) it injects dependencies when and where they are needed based on declaration.

Inside the @Component interface, you can define functions that return instances of the classes you need (i.e. UserRepository). @Component tells Dagger to generate a container with all the dependencies required to satisfy the types it exposes. This is called a Dagger component; it contains a graph that consists of the objects that Dagger knows how to provide and their respective dependencies.

### Simple Dagger Implementation:
Just for mimicking complexity, assume, we are fetching a User's Credential from a remote database, the network call return's a the the User's Credentials (Just Id, in this case)