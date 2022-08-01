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

### Android DataStore (jetpack exclusive):
Jetpack DataStore is a data storage option using key-value pairs (Preferences DataStore) or typed objects with protocol buffers. It uses coroutines and Flow to store data asynchronously, consistently, and transactionally.

DataStore Docs: https://developer.android.com/topic/libraries/architecture/datastore

### Serialization, Deserialization and Data Persistance:
Serialization is a mechanism of converting the state (Data) of an object into a byte stream. That object can then be saved to a database or transferred over a network. Deserialization is the reverse process where the byte stream is used to recreate the actual Java object in memory. This mechanism is used to persist the object (Data).

In Android we cannot just pass objects to activities. To do this the objects must either implement Serializable (standard Java interface) or Parcelable (custom Serializable implementation) interface.

> JSON (JavaScript): JSON Serialization means to convert an JavaScript object into a string format to save into a file, database or tranfer over network. JSON deserialization is its inverse operation, to convert string to JavaScript Object.

### Java/Kotlin Serialization, JSON Libraries:
- Standard Native Serialization Library: This is a native Java solution, so it doesn't require any external libraries

- Gson: Open-source library from Google. it provides toJson() and fromJson() methods to convert Java objects to JSON and vice versa.

- Jackson: Another Java to/from JSON Library that provides multiple approaches to work with JSON data.

- Protocol Buffer: Also know as Protobuf. Supports multiple languages and Proto3 provide com.google.protobuf.util.JsonFormat utility classes to convert protobuf messages to/from JSON format. (Google Product).

- Apache Thrift: Originally developed by Facebook and is currently maintained by Apache. It supports cross-language serialization with lower overhead. It has TJSONProtocol, TCompactProtocol, TBinaryProtocol, etc.

- YAML Based: YAML Beans, SnakeYAML, etc.

### Coroutine Dispatchers and withContext:
Docs: https://developer.android.com/kotlin/coroutines