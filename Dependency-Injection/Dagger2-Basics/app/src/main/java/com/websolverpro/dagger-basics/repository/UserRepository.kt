package com.websolverpro.`dagger-basics`.repository

import com.websolverpro.`dagger-basics`.datasource.UserLocalDataSource
import com.websolverpro.`dagger-basics`.datasource.UserRemoteDataSource

/**
 * Merges local and remote fetch
 */
class UserRepository (
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {  }