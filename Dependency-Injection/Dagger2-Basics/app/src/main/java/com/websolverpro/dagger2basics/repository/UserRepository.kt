package com.websolverpro.dagger2basics.repository

import com.websolverpro.dagger2basics.datasource.UserLocalDataSource
import com.websolverpro.dagger2basics.datasource.UserRemoteDataSource

/**
 * Merges local and remote fetch
 */
class UserRepository (
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {  }