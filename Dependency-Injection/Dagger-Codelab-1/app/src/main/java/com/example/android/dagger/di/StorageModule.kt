package com.example.android.dagger.di

import com.example.android.dagger.storage.SharedPreferencesStorage
import com.example.android.dagger.storage.Storage
import dagger.Binds
import dagger.Module

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