### Overview:
Modern toolkit for building native Android UI with declarative functions using Kotlin instead of the XML based layout.

### Main Components:
* MainActivity: Starting point, which extends the ComponentActivity
* setContent | ComponentActivity.setContent : Creates the root view of the UI. 
* Composable functions:
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

### Color Scheme and Surface:
inside `setContent:AppNameTheme` the Surface( color = MaterialTheme.colorScheme.background ) {...} wrapper Set the background color (if Theme.kt has any background property) and if the colorScheme has `onBackground` property defined, the text be that

Assigning Theme's Color from `res/values/colors.xml` using `R.color.*` will not work out-of-the-box. Best practice is to Assign colors directly using `Color(color = 0xXXXXXXXX)` and use it from the `Theme.kt` file

### Importing:
Choose `androidx.compose.*` for compiler and runtime classes `androidx.compose.ui.*` for UI toolkit and libraries

### Modifier List:
https://developer.android.com/jetpack/compose/modifiers-list

- `Modifier.weight(1f)` : The weight modifier makes the element fill all available space, making it flexible, effectively pushing away the other elements that don't have a weight, which are called inflexible. it kinda opposite of `fillMaxWidth()`

### Recomposition and State:
Compose apps transform data into UI by calling composable functions. If data changes, Compose re-executes these functions with the new data, creating an updated UI through recomposition. Compose also looks at what data is needed by an individual composable so that it only needs to recompose components whose data has changed and skip recomposing those that are not affected.

* Composable functions can execute frequently and in any order, anybody must not rely on the ordering in which the code is executed, or on how many times this function will be recomposed

* State and MutableState (`mutableStateOf(...)`) are interfaces that hold some value and trigger UI updates (recompositions) whenever that value changes.

* remember : To preserve state across recomposition, persist the mutable state using `remember { mutableStateOf(...) }`. it is used to guard against recomposition, so the state is not reset.

```kotlin
var state = remember { mutableStateOf (...) }
// access or modify :  `state.value = ...`

var state by remember { mutableStateOf (...) }
// access or modify : `state = ...`
```
Note: Delegated Properties : https://kotlinlang.org/docs/delegated-properties.html

### remember vs rememberSaveable (persist on configuration changes, like rotation):
Instead of using remember, rememberSaveable will save each state surviving configuration changes (such as rotations) and process death.
```kotlin
var state by rememberSaveable { mutableStateOf(true) } // using rememberSaveable to define config persistent state
```

### State Hoisting (lifting or elevating state):
In Composable functions, state that is read or modified by multiple functions should live in a common ancestor—this process is called state hoisting.

- State Hoisting is can be done by passing state to it's children using callback function

```kotlin
@Composable
fun Parent (/* args */) {

    // state
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Column {
        if (shouldShowOnboarding) {
            // modify state by passing callback fn as target's click handler 
            Child(onContinueClicked = { shouldShowOnboarding = false })
        } else { /* do other things */}
    }
}


/**
* passing state through callback lambda
*/
@Composable
fun Child(onContinueClicked: () -> Unit) {

    Button(
        onClick = onContinueClicked
    ) {
        Text("...")
    }
}
```

### LazyRow and LazyColumn:
LazyColumn and LazyRow are equivalent to RecyclerView in Android Views. LazyColumn renders only the visible items on screen, allowing performance gains when rendering a big list.
```kotlin
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
private fun Parent(
    // creating a list of 1000 numbers
    names: List<String> = List(1000) { "$it" } 
) {
    LazyColumn {
        // using LazyListScope.items() to emit new composable when scrolled
        items(items = names) { name ->
            Child(name = name)
        }
    }
}
```

### Animation:
`animateDpAsState` returns a State object whose value will continuously be updated by the animation until it finishes. It takes a "target value" whose type is Dp

```kotlin
/**
* @Composable
public fun animateDpAsState(
    targetValue: Dp, // accepts Float, Color, Offset, etc too
    animationSpec: AnimationSpec<Dp>, // Physics animation will be used by default.
    label: String,
    finishedListener: ((Dp) -> Unit)?
): State<Dp>
*
*/
val extraPadding by animateDpAsState(
    if (expanded) 48.dp else 0.dp,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)
```

* androidx.compose.animation.core
* https://developer.android.com/jetpack/compose/animation/introduction

### Animation Bug:
* properties can never be negative, otherwise it could crash the app.