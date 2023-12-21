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

### Working with LiveData and Nul Safety:
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

