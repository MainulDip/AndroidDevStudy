package com.websolverpro.dagger2basics.datasource

import retrofit2.Response
import retrofit2.http.GET

interface LoginRetrofitService {
    @GET(".")
    suspend fun getUser(): User
}
