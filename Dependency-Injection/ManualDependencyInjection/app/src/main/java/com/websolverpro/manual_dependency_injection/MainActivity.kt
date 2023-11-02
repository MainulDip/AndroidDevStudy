package com.websolverpro.manual_dependency_injection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.websolverpro.manual_dependency_injection.datasource.LoginRetrofitService
import com.websolverpro.manual_dependency_injection.datasource.LoginUserData
import com.websolverpro.manual_dependency_injection.datasource.User
//import com.google.gson.GsonBuilder
import com.websolverpro.manual_dependency_injection.datasource.UserLocalDataSource
import com.websolverpro.manual_dependency_injection.datasource.UserRemoteDataSource
import com.websolverpro.manual_dependency_injection.repository.UserRepository
import com.websolverpro.manual_dependency_injection.ui.theme.ManualDependencyInjectionTheme
import com.websolverpro.manual_dependency_injection.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginData: LoginUserData
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManualDependencyInjectionTheme {

                val appContainer = (application as MyApplication).appContainer
                loginViewModel = appContainer.loginViewModelFactory.create()

                // Login flow has started. Populate loginContainer in AppContainer
                appContainer.loginContainer = LoginContainer(appContainer.userRepository)

                loginViewModel = appContainer.loginContainer!!.loginViewModelFactory.create()
                loginData = appContainer.loginContainer!!.loginData


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }


    override fun onDestroy() {
        // Login flow is finishing
        // Removing the instance of loginContainer in the AppContainer
        appContainer.loginContainer = null
        super.onDestroy()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ManualDependencyInjectionTheme {
        Greeting("Android")
    }
}