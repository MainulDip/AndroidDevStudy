package com.websolverpro.dagger2basics.repository

import com.websolverpro.dagger2basics.datasource.UserLocalDataSource
import com.websolverpro.dagger2basics.datasource.UserRemoteDataSource
import javax.inject.Inject

/**
 * Merges local and remote fetch
 */
class UserRepository @Inject constructor (
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {
    val userFromRepo = remoteDataSource.userId
}