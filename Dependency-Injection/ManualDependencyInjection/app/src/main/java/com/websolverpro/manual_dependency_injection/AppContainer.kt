package com.websolverpro.manual_dependency_injection

import com.websolverpro.manual_dependency_injection.datasource.LoginRetrofitService
import com.websolverpro.manual_dependency_injection.datasource.UserLocalDataSource
import com.websolverpro.manual_dependency_injection.datasource.UserRemoteDataSource
import com.websolverpro.manual_dependency_injection.repository.UserRepository
import com.websolverpro.manual_dependency_injection.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer: CoroutineScope by MainScope() {

    private lateinit var loginViewModel: LoginViewModel

    var retrofit2 = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/todos/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginRetrofitService::class.java)

    val remoteDataSource = UserRemoteDataSource(retrofit2.getUser())

    val localDataSource = UserLocalDataSource()

    val userRepository = UserRepository(localDataSource, remoteDataSource)


}