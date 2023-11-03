package com.websolverpro.`dagger-basics`

import com.websolverpro.`dagger-basics`.datasource.LoginRetrofitService
import com.websolverpro.`dagger-basics`.datasource.UserLocalDataSource
import com.websolverpro.`dagger-basics`.datasource.UserRemoteDataSource
import com.websolverpro.`dagger-basics`.factories.LoginViewModelFactory
import com.websolverpro.`dagger-basics`.repository.UserRepository
import com.websolverpro.`dagger-basics`.viewmodel.LoginViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private lateinit var loginViewModel: LoginViewModel

    var retrofit2 = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/todos/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginRetrofitService::class.java)

    val remoteDataSource = UserRemoteDataSource(retrofit2.getUser())

    val localDataSource = UserLocalDataSource()

    val userRepository = UserRepository(localDataSource, remoteDataSource)

    // LoginContainer will be null when the user is NOT in the login flow
    var loginContainer: LoginContainer? = null

    val loginViewModelFactory = LoginViewModelFactory(userRepository)


}