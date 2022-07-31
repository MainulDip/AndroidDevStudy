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
Docs : [developers.google.com/protocol-buffers/docs/proto3](https://developers.google.com/protocol-buffers/docs/proto3)

Like JSON, it's a serialization language using .proto files.
```proto
syntax = "proto3";

option java_package = "com.bracketcove.sudokuappjetpack";
option java_multiple_files = true; // for multiple file rather than single file

message GameSettings {
  int32 boundary = 9;
  ProtoDifficulty difficulty = 2;

  enum ProtoDifficulty {
    UNKNOWN = 0;
    EASY = 1;
    MEDIUM = 2;
    HARD = 3;
  }
}
```

### Android DataStore and Serializer Package: