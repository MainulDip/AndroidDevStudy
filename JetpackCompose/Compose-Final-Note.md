### Compose Frequently Used Functions:
- `rememberCoroutineScope` -> it creates a coroutine scope bound to the composable. that scope can be used anywhere to launch coroutines `scope.launch {}`. The scope will be destroyed when the composable that called it is removed from tree.

- `rememberAsState`

- `LazyListState` -> A state object that can be hoisted to control and observe scrolling. In most cases, this will be created via rememberLazyListState. It's kind of a eventListener on scrolling event of the composable that call/initialize (using `rememberLazyListState()`) it.
```kotlin
// initializing LazyListState
val lazyListState = rememberLazyListState() // returns a LazyListState

// will return true/false when the composable will start and stop scrolling
Log.d("rememberLazyListState", "lazyListState.isScrollInProgress => ${lazyListState.isScrollInProgress}").

// Most of the time this is used to capture LazyList Position
// can be used to store scrolling position on Shared Preferences and 
// restore that exact position (list items top index) when will app launch next time
```

- `rememberLazyListState` -> Creates a LazyListState that is remembered across compositions (re-compositions). 