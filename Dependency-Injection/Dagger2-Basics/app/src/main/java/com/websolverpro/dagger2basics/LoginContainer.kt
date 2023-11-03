package com.websolverpro.dagger2basics

import com.websolverpro.dagger2basics.datasource.LoginUserData
import com.websolverpro.dagger2basics.factories.LoginViewModelFactory
import com.websolverpro.dagger2basics.repository.UserRepository

class LoginContainer(val userRepository: UserRepository) {

    val loginData = LoginUserData()

    val loginViewModelFactory = LoginViewModelFactory(userRepository)
}