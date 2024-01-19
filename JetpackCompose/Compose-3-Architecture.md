### Architecture of JetPack Compose UI:
It's a quick magnet part of the Architecture part of https://developer.android.com/jetpack/compose/documentation docs.

### Composition and Node Tree:
Composition can be described as a node tree, where each node is a Composable function.
Composition is the first part of UI phases (Composition, Layout and Drawing)

### Compose Lifecycle:
The lifecycle of a composable is defined by the following events: entering the Composition, getting recomposed 0 or more times, and leaving the Composition.

A Composition can only be produced by an initial composition and updated by recomposition. The only way to modify a Composition is through recomposition.

Composition disposal: https://stackoverflow.com/questions/77076623/what-does-composition-exactly-mean-in-jetpack-compose

#### Recomposition and `key` for Smart recomposition
Recomposition will only trigger if @Composable's inputs are changed (Started by any state changes form parent Composable). For non-Stable types, it's always recompose, see Stable Section Below.

The instance of a composable in Composition is identified by its call site. Internally Compose Compiler preserve execution order and the call site information in order to keep the instances distinct from each other. 

- if child composable are different function, only the function that's input had been changed will recompose. Other children's instance will be preserved. 

- if children are same function (like looping) with different param/inputs, when inserting/deleting a new item any places other than last of a list, a unique key (id) should be used to skip recomposition for those that's not been changed. 
Its the `Smart` recomposition.

```kotlin
Column {
    for (movie in movies) {
        key(movie.id) { // Unique ID for this movie
            MovieOverview(movie)
        }
    }
}
```

Some composables have built-in support for the key composable. For example, LazyColumn accepts specifying a custom key in the items DSL.

```kotlin
@Composable
fun MoviesScreenLazy(movies: List<Movie>) {
    LazyColumn {
        items(movies, key = { movie -> movie.id }) { movie ->
            MovieOverview(movie)
        }
    }
}
```

#### @Stable:
Composables can skip recomposition if all the inputs are stable and haven't changed. To make a non-stable type treat with smart recomposition, it needs to be marked with @Stable.

- Stable types -> all primitive value types (Boolean, Int, Long, Float, Char, etc), String, all function types (lambda), and MutableState

- Non-Stable types -> Interfaces, mutable public properties of class/object/data-class.

* If Compose is not able to infer the stability of a type, annotate the type with @Stable to allow Compose to favor smart recompositions.

```kotlin
// Marking the type as stable to favor skipping and smart recomposition.
@Stable
interface UiState<T : Result<T>> {
    val value: T?
    val exception: Throwable?

    val hasError: Boolean
        get() = exception != null
}
```
Docs: https://developer.android.com/jetpack/compose/lifecycle

### Observable State holders (mutableStateOf | StateFlow (Flow) | LiveData):
```kotlin
class MyViewModel : ViewModel() {
    // private val _uiState = mutableStateOf<UiState>(UiState.SignedOut)
    private val _uiState = MutableLiveData<UiState>(UiState.SignedOut)
    val uiState: LiveData<UiState>
        get() = _uiState

    // ...
}

@Composable
fun MyComposable(viewModel: MyViewModel) {
    val uiState = viewModel.uiState.observeAsState()
    // ...
}
```

### 1. Simple State in Composable Functions:

### 2. State holder class (plain kotlin class to hold complex UI logics):

### 3. ViewModel State Holder (Business Logic States):


### Navigation in Compose

### Compose Effects and Side Effects:
An `effect` is a composable function that doesn't emit UI and causes side effects to run when a composition completes.

Even though, Composables should be side effect free, sometimes side-effects are necessary, for example, to trigger a one-off event such as showing a snackbar or navigate to another screen given a certain state condition. These actions should be called from a controlled environment that is aware of the lifecycle of the composable.

* When you need to make changes to the state of the app, you should use the Effect APIs so that those side effects are executed in a predictable manner.

Docs: https://developer.android.com/jetpack/compose/side-effects

#### LaunchedEffect:run for calling suspend functions from Composables:
`LaunchedEffect(someState){...}` should be used to call suspend functions safely from inside a composable. For example, showing a Snackbar in a Scaffold is done with the SnackbarHostState.showSnackbar function, which is a suspend function. 
```kotlin
// Signature
@Composable
public fun LaunchedEffect(
    key1: Any?,
    block: suspend CoroutineScope.() -> Unit
): Unit
```

```kotlin
@Composable
fun MyScreen(
    state: UiState<List<Movie>>,
    snackbarHostState: SnackbarHostState
) {

    // If the UI state contains an error, show snackbar
    if (state.hasError) {

        // `LaunchedEffect` will cancel and re-launch if
        // `scaffoldState.snackbarHostState` changes
        LaunchedEffect(snackbarHostState) {
            // Show snackbar using a coroutine, when the coroutine is cancelled the
            // snackbar will automatically dismiss. This coroutine will cancel whenever
            // `state.hasError` is false, and only start when `state.hasError` is true
            // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
            snackbarHostState.showSnackbar(
                message = "Error message",
                actionLabel = "Retry message"
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        // ...
    }
}
```

#### RememberCoroutineScope to call suspend fun outside of Composables:
Use `rememberCoroutineScope` In order to launch a coroutine outside of a composable, but scoped so that it will be automatically canceled once it leaves the composition.

```kotlin
@Composable
fun MoviesScreen(snackbarHostState: SnackbarHostState) {

    // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Button(
                onClick = {
                    // Create a new coroutine in the event handler to show a snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar("Something happened!")
                    }
                }
            ) {
                Text("Press me")
            }
        }
    }
}
```

#### rememberUpdatedState | returns the updated state without re-running Effects (LaunchEffect):
To create an effect that matches the lifecycle of the call site, a never-changing constant like Unit or true is passed as a parameter (ie, `LaunchEffect(true){...}`). So on recomposition, it will not call again until finished (if there is some computation going on). Inside that block after the computation finished, the state can be changed mean while. To get the updated state, `rememberUpdatedState` need to be used to initialize the state like `val updatedState by rememberUpdatedState(someFun)`

```kotlin
@Composable
fun LandingScreen(onTimeout: () -> Unit) {

    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        delay(10000) // say, computation will take 10s
        currentOnTimeout() // after computation, this call will provide the updated state/result (as if its been changed in this 10 seconds)
    }

    /* Landing screen content */
}
```

* StackOverflow note: https://stackoverflow.com/questions/69085027/difference-between-remember-and-rememberupdatedstate-in-jetpack-compose

#### DisposableEffect | effects that require cleanup:
For side effects that need to be cleaned up after the keys change or if the composable leaves the Composition, use DisposableEffect. If the DisposableEffect keys change, the composable needs to dispose (do the cleanup for) its current effect, and reset by calling the effect again.
```kotlin
@Composable
fun HomeScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit, // Send the 'started' analytics event
    onStop: () -> Unit // Send the 'stopped' analytics event
) {
    // Safely update the current lambdas when a new one is provided
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                currentOnStart()
            } else if (event == Lifecycle.Event.ON_STOP) {
                currentOnStop()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /* Home screen content */
}
```

#### SideEffect: publish Compose state to non-Compose code:
To share Compose state with objects not managed by compose, use the SideEffect composable. Using a SideEffect guarantees that the effect executes after every successful recomposition (not before recomposition finished).
```kotlin
@Composable
fun rememberFirebaseAnalytics(user: User): FirebaseAnalytics {
    val analytics: FirebaseAnalytics = remember {
        FirebaseAnalytics()
    }

    // On every successful composition, update FirebaseAnalytics with
    // the userType from the current User, ensuring that future analytics
    // events have this metadata attached
    SideEffect {
        analytics.setUserProperty("userType", user.userType)
    }
    return analytics
}
```

#### produceState: convert non-Compose state into Compose state:
produceState launches a coroutine scoped to the Composition that can push values into a returned State. Use it to convert non-Compose state into Compose state, for example bringing external subscription-driven state such as Flow, LiveData, or RxJava into the Composition.

The producer is launched when produceState enters the Composition, and will be cancelled when it leaves the Composition. The returned State conflates; setting the same value won't trigger a recomposition.
```kotlin
@Composable
fun loadNetworkImage(
    url: String,
    imageRepository: ImageRepository = ImageRepository()
): State<Result<Image>> {

    // Creates a State<T> with Result.Loading as initial value
    // If either `url` or `imageRepository` changes, the running producer
    // will cancel and will be re-launched with the new inputs.
    return produceState<Result<Image>>(initialValue = Result.Loading, url, imageRepository) {

        // In a coroutine, can make suspend calls
        val image = imageRepository.load(url)

        // Update State with either an Error or Success result.
        // This will trigger a recomposition where this State is read
        value = if (image == null) {
            Result.Error
        } else {
            Result.Success(image)
        }
    }
}
```

#### derivedStateOf: convert one or multiple state objects into another state:
use the derivedStateOf function when your inputs to a composable are changing more often (and causing recompose) but the composable only needs to react to changes once it crosses a certain threshold.
```kotlin
@Composable
// When the messages parameter changes, the MessageList
// composable recomposes. derivedStateOf does not
// affect this recomposition.
fun MessageList(messages: List<Message>) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            // ...
        }

        // Show the button if the first visible item is past
        // the first item. We use a remembered derived state to
        // minimize unnecessary compositions
        val showButton by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(visible = showButton) {
            ScrollToTopButton()
        }
    }
}
```

### Phases:

### UI Architecture:
### Architectural Layering:

### CompositionLocal:
### Navigation:
