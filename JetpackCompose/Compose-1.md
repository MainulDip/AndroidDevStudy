### Overview:
Modern toolkit for building native Android UI with declarative functions using Kotlin instead of the XML based layout.

### Main Components:
* MainActivity: Starting point, which extends the ComponentActivity
* setContent | ComponentActivity.setContent : Creates the root view of the UI. 
* Composable function:
@Composable annotation makes it into a composable function, it's the building block of JetPack Compose. It describe the UI/Views and provide data dependencies. Any function including a @Composable function needs to be itself @Composable
* Theme:
All the theming (Material) files are inside ui.theme (on default compose template). The main theme function is a composable. And there is a @Composable "Surface" from Material 3, usually all other components go inside this.
* @Preview:
@Preview annotation can display the composable functions call without building the project and running on an emulator.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeIntroTheme { // Composable Theme Function, Which call Material 3 
                Surface( // also a Material Theme's Composable Function
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
```
### Layout Basic:
- Column : arrange elements vertically
- Row : arrange items horizontally
- Box : For Stacking Components
- Spacer : provide horizontal spacing