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

### CoroutineContext == CoroutineDispatchers:s