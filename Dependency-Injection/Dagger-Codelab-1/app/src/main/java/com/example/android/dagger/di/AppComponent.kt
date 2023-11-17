package com.example.android.dagger.di

import android.content.Context
import com.example.android.dagger.registration.RegistrationActivity
import dagger.BindsInstance
import dagger.Component

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
