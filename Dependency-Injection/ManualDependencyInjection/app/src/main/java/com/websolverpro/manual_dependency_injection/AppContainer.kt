package com.websolverpro.manual_dependency_injection

import com.websolverpro.manual_dependency_injection.datasource.LoginRetrofitService
import com.websolverpro.manual_dependency_injection.datasource.UserLocalDataSource
import com.websolverpro.manual_dependency_injection.datasource.UserRemoteDataSource
import com.websolverpro.manual_dependency_injection.factories.LoginViewModelFactory
import com.websolverpro.manual_dependency_injection.repository.UserRepository
import com.websolverpro.manual_dependency_injection.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private lateinit var loginViewModel: LoginViewModel

    var retrofit2 = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/todos/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginRetrofitService::class.java)

    val remoteDataSource = UserRemoteDataSource(runBlocking { retrofit2.getUser() })

    val localDataSource = UserLocalDataSource()

    val userRepository = UserRepository(localDataSource, remoteDataSource)

    // LoginContainer will be null when the user is NOT in the login flow
    var loginContainer: LoginContainer? = null

    val loginViewModelFactory = LoginViewModelFactory(userRepository)


}