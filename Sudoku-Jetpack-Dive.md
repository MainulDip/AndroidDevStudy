## Overview:
This doc is focused on the SudokuAppJetpack.

### CoroutineContext:
Persistent context for the coroutine. It is an indexed set of Element instances. An element of the coroutine context is a singleton context by itself. An indexed set is a mix between a set and a map. Every element in this set has a unique Key.

We can use Dispatchers to tell Coroutines which threads to run on.

Note: for some operatons we want a separate thread besides UI main thread to do something (like writing to files)

```kt
override fun provideUIContext(): CoroutineContext {
    return Dispatchers.Main
}

override fun provideIOContext(): CoroutineContext {
    return Dispatchers.IO
}
```

### Storage and Persistence Package:
- Long term storage (persistance)
- Protocol Buffers (Jetpack Datastore)
- File system storage
- Repository and Data Source implementation
### Protocol Buffers:
Like JSON, it's a serialization language using .proto files