## Overview:
All about Networking and concurrency usages.

### Threads:
* Don't Use Thread Directly:
    - Unpredictable : direct use of thread can return inconsistance/different/unpredictable results when ran multiple time, As the processor switches between sets of instructions on different threads, the exact time a thread is executed and when a thread is paused is beyond your control.
    -  Low-Performance/resource-hungry: Creating, switching, and managing threads takes up system resources
    - Race-condition: when multiple threads try to access the same value in memory at the same time. Race conditions can result in hard to reproduce, random looking bugs, which may cause your the to crash, often unpredictably.

### Kotlin Coroutine:
Coroutines enable multitasking, but provide another level of abstraction over simply working with threads. One key feature of coroutines is the ability to store state, so that they can be halted and resumed. It provide singnaling with other coroutines (cooperative multitasking)
* CoroutineScope: is a context that enforces cancellation and other rules to its children and their children recursively. ie: launch() and async()
* Job: a cancelable unit of work with a lifecycle, inside a CoroutineScope. ie: launch()
* Dispatcher: it manages which backing thread the coroutine will use for its execution, removing the responsibility of when and where to use a new thread from the developer. Main for UI thread, Default, IO, or Unconfined will use other threads.
* launch() function creates a coroutine from the enclosed code wrapped in a cancelable Job object. launch() is used when a return value is not needed outside the confines of the coroutine.
* runBlocking:  starts a new coroutine and blocks the current thread. Not much use in Android directly. runBlocking() itself is not a suspend function. 
```kotlin
expect fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> T): T
```
* async(): it returns a Deferred (a cancelable Job that can hold a reference to a future value), Deferred just serves as a placeholder. A Deferred (also called a Promise or Future in other languages) guarantees that a value will be returned to this object at a later time. To initiate that the current line of code needs to wait for the output of a Deferred, you can call await() on it.

* suspend fn: Whenever a function calls another suspend function, then it should also be a suspend function. If a function does not call a suspend function, then it does not need to be a suspend function itself. Like runBlocking() call any suspend functions (async, launch) it call it by lambda block, which is a suspend function.

Note: the lambda passed to runBlocking and async are suspend functions, but runBlocking and async are not suspend fn itself

### HTTP requests:
- multiple parameters are in a GET request are separated by "&"
- POST request need to send "Content-Type"
### HTTP responses:
* status:
    - 100 to 199: 
    - 200 to 299: Success
    - 300 to 399: 
    - 400 to 499: Client Error
    - 500 to 599: Server Error 
* Content-type:
    - text/html; charset=UTF8
* Content-Encoding:
    - br
### REST server (REpresentational State Transfer):
 - Client-Server architecture: Client and Server are Seperated
 - Resource exposed as URIs (Unified Resources Identifiers)
 - Uniform interface for CRUD operations
 - Stateless: Doesn't need to remember cliens states between requests, each HTTP request (GET/POST/PUT/DELETE) should contain necessary headers, parameters, etc.

### Rerofit (Server Communication):
It's a well supported library to communicate with the server by REST api.
```kotiln
// add module build.gradle dependency
// Retrofit 
implementation "com.squareup.retrofit2:retrofit:2.9.0"
// Retrofit with Scalar Converter
implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
```

### Java 8 Language Support:
Android Gradle plugin provides built-in support for using certain Java 8 language features.
```kotlin
// module's build.gradle
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
  
  kotlinOptions {
    jvmTarget = '1.8'
  }
```

### ViewModelScope:
ViewModelScope is the built-in coroutine scope defined for each ViewModel in an app. Any coroutine launched in this scope is automatically canceled if the ViewModel is cleared (so no background data consumption, etc.). This is used to launch the coroutine and make the any network call in the background
```kotlin
private fun getMarsPhotos() {
    viewModelScope.launch {
        // making api call
        val listResult = MarsApi.retrofitService.getPhotos()
        // changing viewModel's state when success
        _status.value = listResult
    }
}
```

### Internet Permission:
This is required to access internet and ensure extra layer of privacy protection.
```txt
# to ask internet permission, add user-permission as direct child of <manifest> tag in manifests/AndroidManifest.xml
# Also app need to be uninstall and reinstll again to test

<manifest>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
</manifest>
```
