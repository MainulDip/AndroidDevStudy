package com.websolverpro.dagger2basics.datasource

import javax.inject.Inject

class UserLocalDataSource @Inject constructor() { }
//class UserRemoteDataSource @Inject constructor ( private val loginService: User = User(777)) { var userId = loginService.userId }
class UserRemoteDataSource @Inject constructor () { }