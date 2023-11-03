package com.websolverpro.dagger2basics.datasource

class UserLocalDataSource { }
class UserRemoteDataSource( private val loginService: User) { var userId = loginService.userId }