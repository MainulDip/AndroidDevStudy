package com.websolverpro.dagger_hilt_basics.data.remote

import retrofit2.http.GET

interface MyAPI {
    @GET("test")
    suspend fun doNetworkCall()
}