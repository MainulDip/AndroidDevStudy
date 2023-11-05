package com.websolverpro.dagger2basics.datasource

import javax.inject.Inject


class UserLocalDataSource @Inject constructor() { }
class UserRemoteDataSource @Inject constructor ( private val loginService: User ) { var userId = loginService.userId }