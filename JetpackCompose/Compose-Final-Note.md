### Compose Frequently Used Functions:
- `rememberCoroutineScope` -> it creates a coroutine scope bound to the composable. that scope can be used anywhere to launch coroutines `scope.launch {}`. The scope will be destroyed when the composable that called it is removed from tree.

- `rememberAsState`

- `rememberLazyListState`