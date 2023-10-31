package com.websolverpro.dagger_hilt_basics.domain.repository

interface MyRepository {
    suspend fun doNetworkCall()
}