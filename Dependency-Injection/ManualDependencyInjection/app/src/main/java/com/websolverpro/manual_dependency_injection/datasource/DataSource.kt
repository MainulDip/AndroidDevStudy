package com.websolverpro.manual_dependency_injection.datasource

class UserLocalDataSource { }
class UserRemoteDataSource( private val loginService: User) { var userId = loginService.userId }