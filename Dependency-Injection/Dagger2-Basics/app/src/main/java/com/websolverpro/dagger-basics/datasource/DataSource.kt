package com.websolverpro.`dagger-basics`.datasource

class UserLocalDataSource { }
class UserRemoteDataSource( private val loginService: User) { var userId = loginService.userId }