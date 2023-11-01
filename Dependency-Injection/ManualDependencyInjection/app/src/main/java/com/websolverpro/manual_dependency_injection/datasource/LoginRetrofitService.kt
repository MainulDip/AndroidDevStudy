package com.websolverpro.manual_dependency_injection.datasource

import retrofit2.Call
import retrofit2.http.GET

interface LoginRetrofitService {
    @GET(".")
    fun getUser(): User
}
