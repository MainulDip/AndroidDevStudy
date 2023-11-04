package com.websolverpro.dagger2basics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.websolverpro.dagger2basics.datasource.LoginUserData
//import com.google.gson.GsonBuilder
import com.websolverpro.dagger2basics.ui.theme.DaggerBasicsTheme
import com.websolverpro.dagger2basics.viewmodel.LoginViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject lateinit var loginViewModel: LoginViewModel
    private lateinit var loginData: LoginUserData
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MyApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContent {
            DaggerBasicsTheme {

//                val appContainer = (application as MyApplication).appContainer
//                val appContainer2 = (application as MyApplication).appContainer
//                loginViewModel = appContainer.loginViewModelFactory.create()
//
//                // Login flow has started. Populate loginContainer in AppContainer
//                appContainer.loginContainer = LoginContainer(appContainer.userRepository)
//
//                loginViewModel = appContainer.loginContainer!!.loginViewModelFactory.create()
//                loginData = appContainer.loginContainer!!.loginData


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
//        appContainer.loginContainer = null
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
    DaggerBasicsTheme {
        Greeting("Android")
    }
}