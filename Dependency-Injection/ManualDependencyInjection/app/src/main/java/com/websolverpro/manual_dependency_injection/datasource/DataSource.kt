package com.websolverpro.manual_dependency_injection.datasource

import com.websolverpro.manual_dependency_injection.LoginService

class UserLocalDataSource { }
class UserRemoteDataSource( private val loginService: User) { var userId = loginService.userId }