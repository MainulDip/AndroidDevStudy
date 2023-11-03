package com.websolverpro.dagger2basics

import com.websolverpro.dagger2basics.datasource.LoginRetrofitService
import com.websolverpro.dagger2basics.datasource.User
import com.websolverpro.dagger2basics.datasource.UserLocalDataSource
import com.websolverpro.dagger2basics.datasource.UserRemoteDataSource
import com.websolverpro.dagger2basics.factories.LoginViewModelFactory
import com.websolverpro.dagger2basics.repository.UserRepository
import com.websolverpro.dagger2basics.viewmodel.LoginViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private lateinit var loginViewModel: LoginViewModel

    val retrofit2 by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com//todos/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginRetrofitService::class.java)
    }

//    val someusr = User(12,12,"Something", true)


    val remoteDataSource = UserRemoteDataSource(runBlocking { retrofit2.getUser() })

//    val remoteDataSource = UserRemoteDataSource(someusr)

    val localDataSource = UserLocalDataSource()

    val userRepository = UserRepository(localDataSource, remoteDataSource)

    // LoginContainer will be null when the user is NOT in the login flow
    var loginContainer: LoginContainer? = null

    val loginViewModelFactory = LoginViewModelFactory(userRepository)


}