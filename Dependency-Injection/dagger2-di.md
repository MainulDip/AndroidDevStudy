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

### Dagger Interface Placement:
usually in Android agger graph is created/called-from application class because it allow te graph (DependencyGraph) to be in memory as long as the app is running. 
```kotlin
// Definition of the Application graph
@Component
interface ApplicationComponent { ... }

// appComponent lives in the Application class to share its lifecycle
class MyApplication: Application() {
    // Reference to the application graph that is used across the whole app
    val appComponent = DaggerApplicationComponent.create()
}
```

### @Components and Dependency Graph Container:
Dagger creates a graph of the dependencies (`container`) for the project from a `interface` annotate it with `@Component`. From the container (`interface`) it injects dependencies when and where they are needed based on declaration.

Inside the @Component interface, functions defined signature tells Dagger to generate a container with all the dependencies required to satisfy the types it exposes. The @Component interface contains a graph that consists of the objects that Dagger knows how to provide and their respective dependencies.

When the project is built, Dagger generates an implementation of the `ApplicationGraph` interface based on the @Component: (DaggerApplicationGraph). With its annotation processor, Dagger creates a dependency graph that consists of the relationships between all the injectable classes with mentioned entry point.

### Constructor, Field Injection and Activity/Fragment:
Some System Owned class (Activity/Fragment/etc) are instantiated by the system, to provide/inject a dependency here `Field Injection` is used by the same `@Inject` annotation. Dagger requires that injected fields cannot be private.
```kotlin
class LoginActivity: Activity() {
    // You want Dagger to provide an instance of LoginViewModel from the graph
    @Inject lateinit var loginViewModel: LoginViewModel
}
```

Also with the Field Injection, The Dagger's interface need to declare a function with the Activity/Fragment-Class in parameter. It that tells Dagger that Some class/Activity/Fragment (`system class`) wants to access the graph and requests injection. Naming this fn `inject` is a convention. For Multiple Classes, each needs to be declared inside dagger's @Component interface.


```kotlin
@Component(modules = [SingleUser::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}
```

To inject an object in the activity, you'd use the appComponent defined in your Application class and call the inject() method, passing in an instance of the activity that requests injection.

When using activities, inject Dagger in the activity's onCreate() method before calling super.onCreate() to avoid issues with fragment restoration. During the restore phase in super.onCreate(), an activity attaches fragments that might want to access activity bindings.

When using fragments, inject Dagger in the fragment's onAttach() method. In this case, it can be done before or after calling super.onAttach().

```kotlin
class LoginActivity: Activity() {
    // You want Dagger to provide an instance of LoginViewModel from the graph
    @Inject lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make Dagger instantiate @Inject fields in LoginActivity
        (applicationContext as MyApplication).appComponent.inject(this)
        // Now loginViewModel is available

        super.onCreate(savedInstanceState)
    }
}

// @Inject tells Dagger how to create instances of LoginViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) { ... }
```

### Dagger Module:
To Fetch Data either from Network-Call or Offline, the @Module and @Provides annotations describe dagger that how to provide those when required.

Also, the Dagger's @Component interface need to have those module defined as @Components parameters.

```kotlin
data class User @Inject constructor (val  userId: Int)

@Module
class SingleUser () {

    private var theUser = User(77)

    @Provides
    fun getUsers(): User {
        return theUser
    }
}

/**
* The SingleUser's getUser will be called when required.
*/
@Component(modules = [SingleUser::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}
```
### Dagger Scopes:
Scope annotations are used to limit the lifetime of an object to the lifetime of its component. 
- @Singleton is the only scope annotation that comes with the javax.inject package.
```kotlin
@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: LoginActivity)
}

@Singleton
class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) { ... }

@Module
class NetworkModule {
    // Way to scope types inside a Dagger Module
    @Singleton
    @Provides
    fun provideLoginRetrofitService(): LoginRetrofitService { ... }
}
```
### Simple Dagger Implementation:
Just for mimicking complexity, assume, we are fetching a User's Credential from a remote database, the network call return's a the the User's Credentials (Just Id, in this case).