package com.websolverpro.`dagger-basics`

import com.websolverpro.`dagger-basics`.datasource.LoginUserData
import com.websolverpro.`dagger-basics`.factories.LoginViewModelFactory
import com.websolverpro.`dagger-basics`.repository.UserRepository

class LoginContainer(val userRepository: UserRepository) {

    val loginData = LoginUserData()

    val loginViewModelFactory = LoginViewModelFactory(userRepository)
}