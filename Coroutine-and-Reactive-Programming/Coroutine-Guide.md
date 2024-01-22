### Coroutine With Lifecycle Aware Components:
Lifecycle Aware Means, if the underlying/caller Fragment/Activity destroyed, the Coroutine will be canceled.

* By default all of these run on Main Thread, Use `.launch(Dispatchers.IO)` to use IO Threads

https://developer.android.com/topic/libraries/architecture/coroutines

`viewModelScope.launch (context = Dispatchers.IO) {}` -> use from viewModel
`viewLifecycleOwner.lifecycleScope.launch (context = Dispatchers.IO) {}` -> use form Fragment
`lifecycleScope.launch (context = Dispatchers.IO) { }` -> use from Activity 

### Non Lifecycle Aware Component:
`GlobalScope.launch(){}` -> coroutine will not be canceled until finished
`CoroutineScope(Dispatchers.IO).launch {}` -> doesn't cancel until finished

### Difference between viewModelScope and CoroutineScope:
```kotlin
/**
* By default viewModelScope.launch runs on main thread
* 
*/
viewModelScope.launch {
    // call another suspend function to do some task, usually call DAO
    // this is lifecycle Aware, if any view using this viewModel destroyed, the coroutine will be canaled 
}

// CoroutineContext and Dispatcher are same thing
CoroutineScope(Dispatchers.IO).launch {
    // this is non-lifecycle Aware, the coroutine will not be canceled until finished/returned.
}

// call from Fragment
viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO) {
    // call suspend function
}

```

### CoroutineContext == CoroutineDispatchers

### LiveData (Build Outside UI and consume by Observe inside UI) and Nul Safety:
LiveData initialization is not Nullable. `LiveData<T>`, there is no `<T?>`. But to read the (set) the value or observe of changes, it requires null safety.

Signatures ->
`public fun <T> LiveData<T>.observeAsState(): State<T?>` && `LiveData<T>.getValue() : T?` (getValue is assessable through value?.prop)

Little refresh of the LiveData
```kotlin
/* 1. Initialize the LiveData with empty initial value/state, usually inside ViewModel or in Room's DAO */

val currentName: MutableLiveData<T> by lazy {
    MutableLiveData<T>() // with empty initial state
} // inside ViewModel

// Inside Room's DAO
@Query("SELECT * FROM items")
fun getItems(): LiveData<List<T>>

// -----------------------------------------------


/* 2. Observe in View (Activity/Fragment/Compose) from the viewModel */


/* Inside Compose ----------- */
val item: Item? by someViewModel.item.observeAsState() // observing form Compose

item?.let {
    // do something with the item (it) when data gets updated.
}

// when not using the `by` delegation, LiveData<T>.value : T? can be used to read updates

/* ---------------------- */

/* with no DataBinding inside Activity/Fragment, Build an Observe */

val itemObserver = Observer<T> {
    // someTextView.text = it.name
}

// start observing from the main thread
someViewModel.item.observe( viewLifecycleOwner, itemObserver)

// or use direct callback option with the observer
someViewModel.item.observe(viewLifecycleOwner) { item ->
    println(item.toString())
    // do something when the item value is updated in the viewModel
}

// ------------ 2way Data Binding -----------------

// With 2 way data binding, we pass the whose viewModel into the xml view. So no need to Observe, it's included inside DataBinding Library


// -----------------------------------------------


/* 3. Updating LiveData */
button.setOnClickListener {
    val anotherName = "John Doe"
    viewModel.name.setValue(anotherName)
}
```

### Flow (Builder/flow{....} to fetch & emit data | Collect data inside consumer/UI):


* Flow Builders

- `flowOf(...)` functions to create a flow from a fixed set of values.

- `asFlow()` extension functions on various types to convert them into flows.

- `flow { ... }` builder function to construct arbitrary flows from sequential calls to emit function.

- `channelFlow { ... }` builder function to construct arbitrary flows from potentially concurrent calls to the send function.

- `MutableStateFlow` and `MutableSharedFlow` define the corresponding constructor functions to create a hot flow that can be directly updated.

`StateFlow vs Flow` -> https://stackoverflow.com/questions/69551675/android-flow-vs-stateflow


### Flow Intermediaries operators to modify the stream of data:
These operators are functions that, when applied to a stream of data, set up a chain of operations that aren't executed until the values are consumed in the future

```kotlin
val flowA = flowOf(1, 2, 3)
    .map { it + 1 } // Will be executed in ctxA
    .flowOn(ctxA) // Changes the upstream context: flowOf and map

// Now we have a context-preserving flow: it is executed somewhere but this information is encapsulated in the flow itself

val filtered = flowA // ctxA is encapsulated in flowA
   .filter { it == 3 } // Pure operator without a context yet

withContext(Dispatchers.Main) {
    // All non-encapsulated operators will be executed in Main: filter and single
    val result = filtered.single()
    myUi.text = result
}
```
docs -> https://developer.android.com/kotlin/flow
docs -> https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/


### Flow collectors (Terminal Operators):
Terminal operators are functions that initiate the collection of data from the Flow.

* `Collect` -> start collection from flow. Its a suspend function and can be called from another suspend fn.

```kotlin
val numbersFlow = flowOf(1, 2, 3, 4, 5)

// collect is a suspend fun
numbersFlow.collect { number -> 
    println("Received number: $number")
}
```

* `toList` and `toSet` -> collect flow the data into a list or a set

```kotlin
val numbersFlow = flowOf(1, 2, 3, 4, 5)

val numbersList = numbersFlow.toList()
println(numbersList) // prints: [1, 2, 3, 4, 5]
```
* `first` -> collects the data until the first item that matches a given condition is found. Then it stops the collection and returns that item

```kotlin
val numbersFlow = flowOf(1, 2, 3, 4, 5)

val firstEvenNumber = numbersFlow.first { it % 2 == 0 }
println(firstEvenNumber) // prints: 2
```

* reduce -> aggregate the flow data in specified way

```kotlin
val numbersFlow = flowOf(1, 2, 3, 4, 5)

val product = numbersFlow.reduce { accumulator, number -> accumulator * number }
println(product) // prints: 120
```







### MutableStateFlow(value), StateFlow vs Flow:
1. StateFlow used with `collectAsStateWithLifecycle()` ( `Lifecycle.repeatOnLifecycle` in non Compose) will automatically Stop collection when the reader view/composition-node is removed/disposed. Flow/StateFlow on its own will not stop emitting when the app is in the background.

2. A StateFlow is useful for representing state (such as properties in a ViewModels). Flow is not for maintaining a value, its for emitting (like button-pressed/event)

https://stackoverflow.com/questions/69551675/android-flow-vs-stateflow

### Next:
1. StateFlow, SharedFlow, cold/hot flow, shareIn -> https://developer.android.com/kotlin/flow/stateflow-and-sharedflow


### StateFlow with ViewModel and SealedClass Example:

<details>

<summary>Sealed Class With Inner Data Class and Outer inheritance</summary>

```kotlin
sealed class LatestNewsUiState {
    data class Success(val news: List<String>): LatestNewsUiState()
    data class Error(val exception: String): LatestNewsUiState()
}

fun handleResponse(call: LatestNewsUiState) {
    when (call) {
        is LatestNewsUiState.Error -> println(call.exception)
        is LatestNewsUiState.Success -> println(call.news.joinToString(", "))
        is Processing -> println(call.status)
    }
}

fun main() {
    val networkCallProcessing = Processing()
    handleResponse(networkCallProcessing)

    val netWorkCallSuccess = LatestNewsUiState.Success(listOf("News One", "News Two", "News Three", "News Four"))
    handleResponse(netWorkCallSuccess)

    val newWorkCallError = LatestNewsUiState.Error("Bad Request")
    handleResponse(newWorkCallError)

    println("Type Checking with `is`=> 7 is an Int: ${7 is Int}")
}

class Processing: LatestNewsUiState() {
    val status = "Network call is being processed now"
}
```

</details>



<details>

<summary>StateFlow with ViewModel and SealedClass Example</summary>

```kotlin
class LatestNewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))
    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    init {
        viewModelScope.launch {
            newsRepository.favoriteLatestNews
                // Update View with the latest favorite news
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { favoriteNews ->
                    _uiState.value = LatestNewsUiState.Success(favoriteNews)
                }
        }
    }
}


// Represents different states for the LatestNews screen
sealed class LatestNewsUiState {
    data class Success(val news: List<ArticleHeadline>): LatestNewsUiState()
    data class Error(val exception: Throwable): LatestNewsUiState()
}

class LatestNewsActivity : AppCompatActivity() {
    private val latestNewsViewModel = // getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                latestNewsViewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is LatestNewsUiState.Success -> showFavoriteNews(uiState.news)
                        is LatestNewsUiState.Error -> showError(uiState.exception)
                    }
                }
            }
        }
    }
}
```

</details>