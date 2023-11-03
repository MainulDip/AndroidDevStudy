package com.websolverpro.`dagger-basics`.factories

import com.websolverpro.`dagger-basics`.repository.UserRepository
import com.websolverpro.`dagger-basics`.viewmodel.LoginViewModel

// Definition of a Factory interface with a function to create objects of a type
interface Factory<T> {
    fun create(): T
}

// Factory for LoginViewModel.
// Since LoginViewModel depends on UserRepository, in order to create instances of
// LoginViewModel, you need an instance of UserRepository that you pass as a parameter.
class LoginViewModelFactory(private val userRepository: UserRepository) : Factory<LoginViewModel> {
    override fun create(): LoginViewModel {
        return LoginViewModel(userRepository)
    }
}