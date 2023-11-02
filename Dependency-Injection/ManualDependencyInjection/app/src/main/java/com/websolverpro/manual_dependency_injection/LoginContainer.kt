package com.websolverpro.manual_dependency_injection

import com.websolverpro.manual_dependency_injection.datasource.LoginUserData
import com.websolverpro.manual_dependency_injection.factories.LoginViewModelFactory
import com.websolverpro.manual_dependency_injection.repository.UserRepository

class LoginContainer(val userRepository: UserRepository) {

    val loginData = LoginUserData()

    val loginViewModelFactory = LoginViewModelFactory(userRepository)
}