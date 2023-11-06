### What is dependency injection?
In software engineering, dependency injection is a programming technique in which an object or function receives other objects or functions that it requires, as opposed to creating them internally. 

Dependency injection aims to separate the concerns of constructing objects and using them, leading to loosely coupled programs. The pattern ensures that an object or function which wants to use a given service should not have to know how to construct those services. 

Instead, the receiving 'client' (object or function) is provided with its dependencies by external code (an 'injector'), which it is not aware of. Dependency injection makes implicit dependencies explicit.

Example:

Classes often require references to other classes. For example, a Car class might need a reference to an Engine class. These required classes are called dependencies, and in this example the Car class is dependent on having an instance of the Engine class to run.

There are three ways for a class to get an object it needs:

   1. The class constructs the dependency it needs. In the example above, Car would create and initialize its own instance of Engine.
   2. Grab it from somewhere else. Some Android APIs, such as Context getters and getSystemService(), work this way.
   3. Have it supplied as a parameter. The app can provide these dependencies when the class is constructed or pass them in to the functions that need each dependency. In the example above, the Car constructor would receive Engine as a parameter.

The third option is dependency injection! With this approach you take the dependencies of a class and provide them rather than having the class instance obtain them itself.

```kotlin
class Car(private val engine: Engine) {
    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val engine = Engine()
    val car = Car(engine)
    car.start()
}
```
### Service Locator (DI alternative):
The service locator pattern is a design pattern used in software development to encapsulate the processes involved in obtaining a service with a strong abstraction layer. This pattern uses a central registry known as the "service locator", which on request returns the information necessary to perform a certain task.

* it makes software harder to test
* some engineer Critics is an `anti-pattern`

### DI lib for Android and Java:
- Hilt : Built on top of Dagger, maintained by google
- Dagger2 : Maintained by google but more lower-level than Hilt, hence complex
- Google Guice (pronounced like "juice") : https://github.com/google/guice
### Manual Dependency Injection (Container Pattern):
`Singleton pattern` to share/reuse objects: Dependency Injection using singleton pattern makes testing more difficult because all tests share the same singleton instance.

`Container Pattern` to manage Dependencies: To solve the issue of reusing objects and inject those for different scenarios (app/test), a dependencies container class can be used to get dependencies. All instances provided by this container can be public