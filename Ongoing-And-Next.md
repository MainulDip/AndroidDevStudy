## So what's next to the Advanced Android Journey:
NB: Topics followed by *** are to check first.
* *** Dagger-Hilt, android project structures
* *** Android Notification.
* *** RX-Java, RX-Android and Retrofit
* *** AGP: Android Gradle Plugin
* Android Manifest Merge Conflict [Create intentional merge conflict and then resolve it]
* Data Structure and Algorithm:
    - Sort
        - Bubble Sort
        - Intersection Sort
        - Selection Sort
        - Quick Sort
        - Merge Sort

    - Searching
        - Binary Search
        - Rotated Binary Search
        - Ternary Search


* *** Android Build Process
    - R.Java
    - Dalvik (Formarly Used)
    - ART (Android Runtime)
    - JIT (Just In Time)
    - APK Packaging

* Generated Files Inside the App
* Java/Kotlin Generated Files
* Kotlin to Java Transformations

* Android Interview Questions (Practically Implement and Understand Deeply)

### More Advanced:
* *** C and C++ Application Integration In Android NDK (Native Development Kit)
    - NDK Guide: https://developer.android.com/ndk/guides
    - RenderScript: https://developer.android.com/guide/topics/renderscript/compute (Deprecated)
    - RenderScript Migration: https://developer.android.com/guide/topics/renderscript/migrate
    - JNI (Java Native Interface): https://www.baeldung.com/jni
        - https://www.baeldung.com/java-native
- Android AR/VR/3d.
### Next Android Specific:
- Dagger-Hilt and Dagger2
    - Ongoing : Dagger-CodeLab-1
    - Brushing-Up Navigation, NavControllers and Menu (Menu-Navigation.md, Munu-Apps)
    - Quick-Note-Android.md
- Clean Code and MVVM
- Room and Room Migration
- Retrofit
- Notification
- Work Manager
- Application Component
    - Service
    - BroadcastReceiver
    - ContentProvider
    - Activity (Done)



    * Service ( https://youtu.be/EwwB_wZhBaw?si=pt7_jNyae5AoOUFi )
    - Start/Stop Service, Multi Threading, AsyncTask Service
    - Bound Service
    - Intent Service ( https://youtu.be/n6hwehPazac?si=QCycCZPPcaHwwue1 )
    - JobScheduler and JobService
    - Broadcast Receiver (create & register), Run Service after device boot (https://youtu.be/HDVyFsFUuVg?si=nxKPt_Yf84yMv3BV) ( https://youtu.be/HDVyFsFUuVg?si=L21XtXMrjYA7iWtB )
    - JetPack WorkManager (Create and run schedule task with WM)
    - Foreground Service ( https://youtu.be/YZL-_XJSClc?si=Q2x09nqOoFR_uYCf )
    - Restart Service After Reboot


* Caching ( https://youtu.be/qQVCtkg-O7w?si=WZy9oDS0QeOIxapZ )
```kotlin
println("Cachedir : ${applicationContext.cacheDir} and External Cache Dir : ${applicationContext.externalCacheDir}")
```

### Compose Layout Next
- https://developer.android.com/jetpack/compose/phases
- https://developer.android.com/jetpack/compose/layouts
- https://developer.android.com/jetpack/compose/layouts/custom

### Animation:
- TransitionSpec and AnimationSpec
- keyframes
- animatable
### Next:
1. StateFlow, SharedFlow, cold/hot flow, shareIn -> https://developer.android.com/kotlin/flow/stateflow-and-sharedflow

2. Sealed class with inner data class and StateFlow use -> https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow


```kotlin
class LatestNewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))
    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    init {
        viewModelScope.launch {
            newsRepository.favoriteLatestNews
                // Update View with the latest favorite news
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { favoriteNews ->
                    _uiState.value = LatestNewsUiState.Success(favoriteNews)
                }
        }
    }
}


// Represents different states for the LatestNews screen
sealed class LatestNewsUiState {
    data class Success(val news: List<ArticleHeadline>): LatestNewsUiState()
    data class Error(val exception: Throwable): LatestNewsUiState()
}

class LatestNewsActivity : AppCompatActivity() {
    private val latestNewsViewModel = // getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                latestNewsViewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is LatestNewsUiState.Success -> showFavoriteNews(uiState.news)
                        is LatestNewsUiState.Error -> showError(uiState.exception)
                    }
                }
            }
        }
    }
}
```