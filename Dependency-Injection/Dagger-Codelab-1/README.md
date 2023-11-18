### Project Overview:
Link: https://github.com/android/codelab-android-dagger

* User Registration
- main.MainActivity will navigate the non-registered user to EnterDetailsFragment. then onCreateView will set up liveData observer and collect user's data
- After Adding Credentials, Next Button press will Trigger EnterDetailsViewModel's validate fn
- On validation success, fields will be stored, and as the property is a
- observed live data, if it changes, it will trigger the observe callback declared
- inside onCreateView of this (EnterDetailsFragment) class, which will again trigger
- RegistrationViewModel's updateUserData fn and trigger
- the RegistrationActivity's onDetailsEntered fn, from there, will call
- TermsAndCondition Fragment, if accepted, will call again the RegistrationActivity's
- onTermsAndConditionsAccepted() fn, which will call registrationViewModel.registerUser(),
- and finally it will call UserManager's register fn, from there the SharedPreference Store
- will be populated, and will instantiate UserDataRepository with current UserManager Object

### Include Dagger to a Project:
```kotlin
// app level build.gradle

plugins {
//    id 'com.android.application'
//    id 'kotlin-android'
//    id 'kotlin-android-extensions'
//    id 'kotlin-kapt'
}

// ...

dependencies {
    // ...
    // def dagger_version = "2.40"
    // implementation "com.google.dagger:dagger:$dagger_version"
    // kapt "com.google.dagger:dagger-compiler:$dagger_version"
}
```
### Dagger @Component:
An interface annotated with `@Component` will make Dagger generate code with all the dependencies required to satisfy the parameters of the methods it exposes. Dagger will create a Container like as manual dependency injection. @Component Interface is the entry point.

* All Dagger Interfaces are usually put inside a separate package/directory named `di`
### @Module, @Binds and @BindsInstance annotations:
`@Module` : use this to tell Dagger what implementation of an `Interface` we want to use. This will be passed as @Component param. Modules are a way to encapsulate how to provide objects in a semantic way by grouping similar task

@Provides : 
 
`@Bind` : Inside @Module interface, @Binds is used to tell Dagger which implementation it needs to use when providing an interface.

`@BindsInstance` : it tells Dagger that it needs to add that instance in the graph, for objects that are constructed outside of the graph (e.g. instances of Context). This is used inside `@Component` and `@Component.Factory` inside of it. 

```kotlin
@Component(modules = [StorageModule::class])
interface AppComponent {

    /**
     * it's a factory for a/this component
     * A factory is a type with a single method that returns a new component
     * instance each time it is called. The parameters of that method allow the caller to
     * provide the modules, dependencies and bound instances required by the component
     */
    @Component.Factory
    interface Factory {
        /**
         * There must be exactly one abstract method,
         * which must return the component type or one of its supertypes
         * kt's function without body inside interface is abstract fn
         * from Application, Context will be passed using
         * DaggerAppComponent.factory().create(applicationContext)
        */
        fun create(@BindsInstance context: Context): AppComponent
    }

    /**
     * We're telling Dagger that
     * RegistrationActivity will requests (field) injection and that it has to
     * provide the dependencies which are annotated with @Inject
     */
    fun inject(activity: RegistrationActivity)
}



/**
 * @Module is used to instruct Dagger what implementation of
 * an `Interface` we want to use. This will be passed as @Component param.
 * Modules are a way to encapsulate how to provide
 * objects in a semantic way by grouping similar task
 */
@Module
interface StorageModule {
    /**
     * @Binds instruct Dagger of which implementation it needs to use when providing an interface.
     * the return type should be that interface and implementations are defined inside parameter's type
     */
    @Binds
    fun provideStorage(storage: SharedPreferencesStorage) : Storage
}
```

### Creating DaggerAppComponent Graph & Requesting Injection From Activity:
Application class is a good place where the DaggerAppComponent Graph can be created. Also Context can be passed easily from there.
```kotlin
open class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
```
From Activity with `@Inject` annotated field, Request needs to be made to do the actual injection. Needs to be done before super.onCreate in onCreate.
```kotlin
class RegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        /**
         * Ask/Request Dagger Graph to Inject the dependency
         * Perform this before calling super.onCreate inside Activity to avoid issues with fragment restoration
         * during the restore phase, Activity will attach fragments that might want to access activity bindings.
         */
        (application as MyApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // ...
    }
    
    // Other Code
}
```
### Scopes :
By Scopes in Dagger means "to scope a type to the Component's lifecycle". Scoping a type to a Component means that the same instance of that type will be used every time the type needs to be provided/injected.

If we annotate a Component with @Singleton, all the classes also annotated with @Singleton will be scoped to its lifetime.

