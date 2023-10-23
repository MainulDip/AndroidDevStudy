## Overview:
All about Networking and concurrency usages.

### Threads:
* Don't Use Thread Directly:
    - Unpredictable : direct use of thread can return inconsistences/different/unpredictable results when ran multiple time, As the processor switches between sets of instructions on different threads, the exact time a thread is executed and when a thread is paused is beyond your control.
    - Low-Performance/resource-hungry: Creating, switching, and managing threads takes up system resources
    - Race-condition: when multiple threads try to access the same value in memory at the same time. Race conditions can result in hard to reproduce, random looking bugs, which may cause your the to crash, often unpredictably.

### Kotlin Coroutine:
Coroutines enable multitasking, but provide another level of abstraction over simply working with threads. One key feature of coroutines is the ability to store state, so that they can be halted and resumed. It provide singnaling with other coroutines (cooperative multitasking)
* CoroutineScope: is a context that enforces cancellation and other rules to its children and their children recursively. ie: launch() and async().
* Job: a cancelable unit of work with a lifecycle, inside a CoroutineScope. ie: launch()
* Dispatcher: it manages which backing thread the coroutine will use for its execution, removing the responsibility of when and where to use a new thread from the developer. Main for UI thread, Default, IO, or Unconfined will use other threads.
* launch() function creates a coroutine from the enclosed code wrapped in a cancelable Job object. launch() is used when a return value is not needed outside the confines of the coroutine.
* runBlocking:  starts a new coroutine and blocks the current thread. Not much use in Android directly. runBlocking() itself is not a suspend function. 
```kotlin
expect fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> T): T
// see for expect-actual https://medium.com/@uwaisalqadri/kotlin-multiplatform-mobile-concept-of-expect-actual-f967d2e948ce 
```
* async(): it returns a Deferred (a cancelable Job that can hold a reference to a future value), Deferred just serves as a placeholder. A Deferred (also called a Promise or Future in other languages) guarantees that a value will be returned to this object at a later time. To initiate that the current line of code needs to wait for the output of a Deferred, you can call await() on it

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
### Exception Handling For Networking:
Most of the time the exceptions can't be caught during the compile time. So better use a try-catch block to handle the exception in runtime.
```kotlin
try {
    // Network Request
}
catch (e: SomeException) {
    // handle the exception to avoid abrupt termination.
}
```
### Moshi (Android JSON Parser):
It Converts JSON string to Kotiln Object.
```kotlin
// add moshi to build.gradle (Module)
// Moshi
implementation 'com.squareup.moshi:moshi-kotlin:1.13.0'
// use with retrofit2
// Retrofit with Moshi Converter
implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
```

* Mapping JSON key to Kotlin key:
```
import com.squareup.moshi.Json

/**
* Here the JSON Key from Api call is converted to kotlin style key and are mapped
*/

data class MarsPhoto(val id: String, @Json(name = "img_src") val imgSrcUrl: String)
```

### Moshi with Retrofit (using converter):
```kotlin
private const val BASE_URL = "https://someapiroute.com"

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {

    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

// * Singleton pattern in kotlin
// * Each time app calls MarsApi.retrofitService, the caller will access the same singleton Retrofit object
// * that implements MarsApiService which is created on the first access by lazy

object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
```


### Coil (Image Loading):
An image loading library for Android backed by Kotlin Coroutines. Docs: https://coil-kt.github.io/coil/
```kotlin
// inside build.gradle (Module)
// Coil
implementation "io.coil-kt:coil:1.1.1"

// implementation
// URL
imageView.load("https://www.example.com/image.jpg")

// File
imageView.load(File("/path/to/image.jpg"))
```
### Binding Adapter (Custom xml attribute setter binding):
For each xml attribute, android system use specific setter method to handle the value settings/implementation. 
To use a custom type attribute, the setter needs to be supplied
```xml
<ImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:imageUrl="@{product.imageUrl}"/>
<!-- here, imageUri is a custom attribute, so we need to provide the implementation of the setters -->
```

* The @BindingAdapter annotation tells data binding to execute this binding adapter when a View item has the imageUrl attribute. While building, android studio will search for the custom attributes setter and pick from any file where it exists with the @BindingAdapter annotation. Good practice to keep them a separate file (i.e. BindingAdapters.kt)

```kotlin
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}
```
### ListAdapter:
ListAdapter is a subclass of the RecyclerView.Adapter class for presenting List data in a RecyclerView, including computing diffs between Lists on a background thread.

* DifUtil:  The advantage of using DiffUtil in ListAdapter is => every time some item in the RecyclerView is added, removed or changed, the whole list doesn't get refreshed. Only the items that have been changed are refreshed.
* Docs: https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter

```kotlin
class PhotoGridAdapter : ListAdapter<MarsPhoto, PhotoGridAdapter.MarsPhotoViewHolder>(DiffCallback) {

    /**
    * Inner ViewHolder Class [for single item view] */
    class MarsPhotoViewHolder (private var binding: GridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(MarsPhoto: MarsPhoto) {
            binding.photo = MarsPhoto
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPhotoViewHolder {
        return MarsPhotoViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MarsPhotoViewHolder, position: Int) {
        val marsPhoto = getItem(position)
        holder.bind(marsPhoto)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MarsPhoto>() {

        // check if ids are equal, see docs/ide Hints
        override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        // check if the data/content are same, it also determine if data is changed or not
        override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }
    }
}
```

### android:clipToPadding:
To elemenate constant padding issue, this can be set to false.

### Coroutine Implementation In Android:
Docs: https://developer.android.com/kotlin/coroutines
viewModelScope is a predefined CoroutineScope that is included with the ViewModel KTX extensions. Note that all coroutines must run in a scope. A CoroutineScope manages one or more related coroutines