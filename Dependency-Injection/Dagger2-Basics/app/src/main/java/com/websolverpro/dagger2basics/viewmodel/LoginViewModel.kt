package com.websolverpro.dagger2basics.viewmodel

import com.websolverpro.dagger2basics.repository.UserRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(userRepository: UserRepository) {

}
