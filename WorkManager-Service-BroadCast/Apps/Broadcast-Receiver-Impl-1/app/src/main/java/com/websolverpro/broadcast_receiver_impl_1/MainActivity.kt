package com.websolverpro.broadcast_receiver_impl_1

import android.content.Intent
import android.content.IntentFilter
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
import com.websolverpro.broadcast_receiver_impl_1.ui.theme.BroadcastReceiverImpl1Theme

class MainActivity : ComponentActivity() {

    private val airPlaneModeReceiver = AirPlaneModeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Register for receiving Broadcast
         */
        registerReceiver(
            airPlaneModeReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        setContent {
            BroadcastReceiverImpl1Theme {
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

    /**
     * Unregister from receiving Broadcast when activity destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneModeReceiver)
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
    BroadcastReceiverImpl1Theme {
        Greeting("Android")
    }
}