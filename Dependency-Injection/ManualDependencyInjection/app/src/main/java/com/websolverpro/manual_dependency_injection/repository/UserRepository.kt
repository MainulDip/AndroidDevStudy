package com.websolverpro.manual_dependency_injection.repository

import com.websolverpro.manual_dependency_injection.datasource.UserLocalDataSource
import com.websolverpro.manual_dependency_injection.datasource.UserRemoteDataSource

class UserRepository (
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {  }