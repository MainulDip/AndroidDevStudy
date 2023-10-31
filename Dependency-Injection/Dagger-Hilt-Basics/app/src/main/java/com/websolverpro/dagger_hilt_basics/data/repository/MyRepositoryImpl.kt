package com.websolverpro.dagger_hilt_basics.data.repository

import com.websolverpro.dagger_hilt_basics.data.remote.MyAPI
import com.websolverpro.dagger_hilt_basics.domain.repository.MyRepository

class MyRepositoryImpl(private val api: MyAPI): MyRepository {
    override suspend fun doNetworkCall() {
        TODO("Not yet implemented")
    }
}