package com.websolverpro.`dagger-basics`.datasource

import retrofit2.http.GET

interface LoginRetrofitService {
    @GET(".")
    fun getUser(): User
}
