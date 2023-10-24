package com.websolverpro.jetpack_compose_basics

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websolverpro.jetpack_compose_basics.ui.theme.JetpackComposeBasicsTheme
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeBasicsTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {

    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Column(modifier = modifier.padding(vertical = 4.dp)) {

        if (shouldShowOnboarding) { // Where does this come from?
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            LazyColumn() {

                items(items = names) {name ->
                        Greeting(name = name)
                }
            }
        }
    }
}

@Composable
private fun Greeting(name: String) {

    // define state
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

//    val extraPadding = if (expanded) 48.dp else 0.dp

//    val extraPadding by animateDpAsState(
//        if (expanded) 48.dp else 0.dp, label = "",
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        )
//    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row {
            Column(modifier = Modifier
                .weight(1f)
//                .padding(bottom = extraPadding)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .padding(24.dp)) {
                Text(text = "Hello, ", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                Text(text = name, style = MaterialTheme.typography.bodyLarge)
                if (expanded) {
                    Text(
                        text = ("Composem ipsum color sit lazy, " +
                                "padding theme elit, sed do bouncy. ").repeat(4),
                    )
                }
            }

            Column(modifier = Modifier
//                .fillMaxWidth()
                .padding(24.dp)) {
//                ElevatedButton(
//                    onClick = { expanded = !expanded }
//                ) {
//                    Text(if (expanded) "Show less" else "Show more")
//                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, contentDescription = if (expanded) "Show less" else "Show more")
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    JetpackComposeBasicsTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
)
@Composable
fun GreetingPreview() {
    JetpackComposeBasicsTheme {
        MyApp()
    }
}

@Preview(widthDp = 320)
@Composable
fun GreetingPreview_2() {
    JetpackComposeBasicsTheme {
        Surface(
            color = MaterialTheme.colorScheme.primary
        ) {
            Greeting(name = "Good")
        }
    }
}