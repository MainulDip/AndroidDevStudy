## Overview
This is a personalized study repo to jump-start Android app development using Kotlin, Android API and or some Java

### Android DataStore (jetpack exclusive):
Jetpack DataStore is a data storage option using key-value pairs (Preferences DataStore) or typed objects with protocol buffers. It uses coroutines and Flows to store data asynchronously, consistently, and transactionally.

DataStore Docs: https://developer.android.com/topic/libraries/architecture/datastore

### Serialization, Deserialization and Data Persistence:
Serialization is a mechanism of converting the state (Data) of an object into a byte stream. That object can then be saved to a database or transferred over a network. Deserialization is the reverse process where the byte stream is used to recreate the actual Java object in memory. This mechanism is used to persist the object (Data).In Android, we cannot just pass objects to activities. To do this the objects must either implement a Serializable (standard Java interface) or Parcelable (custom Serializable implementation) interface.

> JSON (JavaScript): JSON Serialization means to convert an JavaScript object into a string format to save into a file, database or tranfer over network. JSON deserialization is its inverse operation, to convert string to JavaScript Object.

### Java/Kotlin Serialization, JSON Libraries
- Standard Native Serialization Library: This is a native Java solution, so it doesn't require any external libraries

- Gson: Open-source library from Google. it provides toJson() and fromJson() methods to convert Java objects to JSON and vice versa.

- Jackson: Another Java to/from JSON Library that provides multiple approaches to work with JSON data.

- Protocol Buffer: Also known as Protobuf. Supports multiple languages and Proto3 provides com.google.protobuf.util.JsonFormat utility classes to convert protobuf messages to/from JSON format. (Google Product).

- Apache Thrift: Originally developed by Facebook and is currently maintained by Apache. It supports cross-language serialization with lower overhead. It has TJSONProtocol, TCompactProtocol, TBinaryProtocol, etc.

- YAML Based: YAML Beans, SnakeYAML etc.

- Retrofit and Moshi [ can be used with coroutine ]

### Android Coroutine