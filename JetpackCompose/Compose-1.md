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

### Theming and Color Scheme and Surface:
inside `setContent:AppNameTheme` the Surface( color = MaterialTheme.colorScheme.background ) {...} wrapper Set the background color (if Theme.kt has any background property) and if the colorScheme has `onBackground` property defined, the text be that

* Surface is a component in the Compose Material library. It follows general Material Design patterns.
Assigning Theme's Color from `res/values/colors.xml` using `R.color.*` will not work out-of-the-box. Best practice is to Assign colors directly using `Color(color = 0xXXXXXXXX)` and use it from the `Theme.kt` file

Also, From any descendant composables, three properties of MaterialTheme can be retrieved and modified: `colorScheme`, `typography` and `shapes`.

```kotlin
// using MaterialTheme.typography.bodyLarge.copy retrieving and modifying a predefined style
Text(text = "Hello, ", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
```

### Importing:
Choose `androidx.compose.*` for compiler and runtime classes `androidx.compose.ui.*` for UI toolkit and libraries

### Modifier List:
https://developer.android.com/jetpack/compose/modifiers-list

- `Modifier.weight(1f)` : The weight modifier makes the element fill all available space, making it flexible, effectively pushing away the other elements that don't have a weight, which are called inflexible. it kinda opposite of `fillMaxWidth()`
- `Modifier.verticalScroll(state: rememberScrollState(),...)` : Modify element to allow to scroll vertically when height of the content is bigger than max constraints allow.

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
### Other state variable:
`rememberScrollState()` : Create and remember the ScrollState based on the currently appropriate scroll configuration to allow changing scroll position or observing scroll behavior. Used to persist scroll position on Column/Row Composable like `Modifier.verticalScroll(state: rememberScrollState(),...)`

### LazyRow and LazyColumn:
LazyColumn and LazyRow are equivalent to RecyclerView in Android Views. LazyColumn renders only the visible items on screen, allowing performance gains when rendering a big list.

Some props are `contentPadding, reverseLayout: Boolean, horizontalArrangement: Arrangement.Horizontal, verticalAlignment: Alignment.Vertical, flingBehavior: FlingBehavior, userScrollEnabled: Boolean`
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
### LazyHorizontalGrid:
Instead of creating a LazyRow and let each item hold a Column with multiple Composable elements, `LazyHorizontalGrid` is a nicer mapping from items to grid elements. 

Props as similar like LazyRow and LazyColumn.

```kotlin
LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(168.dp)
    ) {
        items(DataList) { item ->
            ChildComposable(...)
        }
    }
```

### Vertical/Horizontal Scrolling Lazy<Layout> or Layout:
Lazy based layout provide out of the box scrollable feature, but Lazy layouts are good for Big amount of lists. When a list has only a limited number of elements, a simple Column or Row can be made scrollable adding the scroll behavior manually using `verticalScroll` or `horizontalScroll` modifiers and `ScrollState` prop . The ScrollState prop is used to create persistent scroll behavior using `rememberScrollState`
```kotlin
Column( Modifier.verticalScroll(rememberScrollState()) ) {...}
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
```kotlin

// animate based on property change using Modifier
Modifier.animateContentSize(
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

### Material3 Icon
add artifacts ``
```kotlin
IconButton(onClick = { expanded = !expanded }) {
    Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, contentDescription = if (expanded) "Show less" else "Show more")
}
```

### Alignments Of Children From Parent Con (Column, Row, Box):
Alignment can be used on the *`parent` container (Column, Row, Box) to position its children.

On `Column`'s `cross/horizontal axis` children's alignment is set using `horizontalAlignment: Alignment.<Prop>`, Ex. `Start`, `CenterHorizontally`, `End`

On `Row`'s `cross/vertical axis` children's alignment is set using `verticalAlignment: Alignment.<Prop>`, Ex. `Top, CenterVertically, Bottom`

On `Box`, children's both `vertical` and `horizontal` alignment can be set using `TopStart, TopCenter, TopEnd, CenterStart, Center, CenterEnd, BottomStart, BottomCenter, BottomEnd`

Note: Column and Row has Cross and Main Axis. Column's main axis is the `Vertical` and Row's Main axis is `Horizontal`

Row's Main axis alignment/arrangement can be set using `horizontalArrangement: Arrangement.<Prop>`. EX. `Equal Weight, Space Between, Space Around, Space Evenly, End (LTR), Center, Start (LTR), spacedBy(Value.dp)`

Column's Main axis alignment/arrangement can be set using `verticalArrangement: Arrangement.<Prop>`. Ex. `Equal Weight, Space Between, Space Around, Space Evenly, Top, Center, Bottom, spacedBy(Value.dp)`

### Slot API:
Material components make heavy use of slot APIs, a pattern Compose introduces to bring in a layer of customization on top of composables. Slot-based layouts leave single/multiple empty space in the UI for the developer to fill as they wish. You can use them to create more flexible layouts. Like `TopAppBar` allows the content for title, navigationIcon, and actions along with the composable child callback.

Another slot-based layout `Scaffold` provides slots for the most common top-level Material components, such as TopAppBar, BottomAppBar, FloatingActionButton, and Drawer
Docs : https://developer.android.com/jetpack/compose/layouts/basics#slot-based-layouts


### Navigation (NavigationBar/NavigationBarItem and NavigationRail/NavigationRailItem):
Inside `NavigationBar` (From Compose Material library) navigation item can be assigned horizontally using `NavigationBarItem`, which will get styled automatically by Material Lib.

```kotlin
NavigationBar (containerColor = MaterialTheme.colorScheme.surfaceVariant, modifier = modifier) { // this RowScope
    NavigationBarItem(
        selected = true,
        onClick = { /*TODO*/ },
        icon = {
            Icon(imageVector = Icons.Default.Spa, contentDescription = "")
        },
        label = {
            Text(text = "Home")
        })
}
```
`NavigationRail` provide Vertical interface for Navigation Items as `NavigationRailItem`.
### Scaffold:
Scaffold implements the basic material design visual layout structure.  It contains slots for various Material components to construct full layout.
```kotlin
// Signature
@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
): Unit
```

Example:
```kotlin
AppTheme {
    Scaffold (bottomBar = { AppBottomNavigation()}) { paddingValues ->
        AppContainerHome(
            modifier = Modifier.padding(
                paddingValues
            )
        )
    }
}
```
- Scaffold hoists state using rememberScaffoldState.
### Window Size For Portrait/Landscape Layout Rendering:
There are three window size class widths: Compact, Medium and Expanded. When the app is in portrait mode it is Compact width, when it is in landscape mode it is Expanded width.
Note: calculateWindowSize() is still experimental 
```kotlin
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            MySootheApp(windowSizeClass)
        }
    }
}

@Composable
fun ComposeApp(windowSize: WindowSizeClass) {
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> LLayoutPortrait()
        else -> LayoutLandscape()
    }
}
```