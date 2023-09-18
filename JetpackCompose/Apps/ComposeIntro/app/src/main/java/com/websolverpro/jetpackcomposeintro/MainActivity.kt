package com.websolverpro.jetpackcomposeintro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.websolverpro.jetpackcomposeintro.ui.theme.JetpackComposeIntroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeIntroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android Again")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box (contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.pirate), contentDescription = "A Pirate", contentScale = ContentScale.FillBounds ,modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier
            .background(color = Color.Red.copy(alpha = 0.7F), shape = RoundedCornerShape(50))
            .padding(20.dp, 20.dp)
        ) {
            Text(
                text = "Hello $name!",
                modifier = modifier,
                color = Color.White
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeIntroTheme {
        Greeting("Android Again")
    }
}