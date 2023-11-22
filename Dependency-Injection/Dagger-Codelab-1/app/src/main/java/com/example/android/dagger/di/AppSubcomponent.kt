package com.example.android.dagger.di

import com.example.android.dagger.login.LoginComponent
import com.example.android.dagger.registration.RegistrationComponent
import dagger.Module

@Module(subcomponents = [RegistrationComponent::class, LoginComponent::class])
interface AppSubcomponent {
}