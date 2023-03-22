## Overview:
This part covers some quick jump-starting instruction set to run Background Tasks Scheduler in Android.

### WorkManager:
It's a part of the Android Jetpack and Architecture Component. It's a combination of opportunistic and guaranteed execution techniques. 
- Opportunistic execution means that WorkManager will do the background work as soon as it can. 
- Guaranteed execution means that WorkManager will take care of the logic to start background work under a variety of situations, even if you navigate away from your app.

Docs: https://developer.android.com/topic/libraries/architecture/workmanager/
Background Work Scheduler Guide: https://developer.android.com/guide/background/

### Config WorkManager:
app/build.gradle
```kotlin
dependencies {
    // WorkManager dependency
    implementation "androidx.work:work-runtime-ktx:$versions.work"
}
```
### WorkManager classes:
- Worker: This is where you put the code for the actual work you want to perform in the background. You'll extend this class and override the doWork() method.
- WorkRequest: This represents a request to do some work. You'll pass in your Worker as part of creating your WorkRequest. When making the WorkRequest you can also specify things like Constraints on when the Worker should run.
- WorkManager: This class schedules your WorkRequest and makes it run. It schedules WorkRequests in a way that spreads out the load on system resources while honoring the constraints you specify.


### Data Input Object (Result):
Input and output are passed in and out via Data objects from the worker. Data objects are lightweight containers for key/value pairs. They are meant to store a small amount of data that might pass into and out of WorkRequests.
```kotlin
/**
 ** Call from ViewModel
**/
internal fun applyBlur(blurLevel: Int) {
    val oneTimeWorkRequest = OneTimeWorkRequestBuilder<BlurWorker>()
        .setInputData(createInputDataForUri())
        .build()
    workManager.enqueue(oneTimeWorkRequest)
}

// data passing
private fun createInputDataForUri(): Data {
    val builder = Data.Builder()
    imageUri?.let {
        builder.putString(KEY_IMAGE_URI, imageUri.toString())
    }
    return builder.build()
}

/**
 ** worker class
*/
class BlurWorker(val ctx: Context, val params: WorkerParameters): Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = ctx.applicationContext

        // Worker extends ListenableWorker, so inputData is available here
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Bluring The Cup Cake", appContext)

        try {
//            val picture = BitmapFactory.decodeResource(
//                appContext.resources,
//                R.drawable.android_cupcake
//            )

            if (TextUtils.isEmpty(resourceUri)) {
                Log.e("doWorkConstErrors", "Invalid input uri")
                throw IllegalArgumentException("Invalid Constant Input URI")
            }

            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))

            val blurredImg =  blurBitmap(picture, appContext)
            val outputUri = writeBitmapToFile(appContext, blurredImg)

            makeStatusNotification("Finished Bluring and the File Path is $outputUri", appContext)

            // using kotlin infix function to create a Pair, can be written in non-infix way to (sth1.to(sth2)
            // infix is the heart of Kotlin DSL
            // also kotlin has no tuple time, pair and triple is used to create 
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            return Result.success(outputData)

        } catch (e: Exception) {
            Log.e("Blur Not Succeed", "Error applying blur")
//            throw Throwable("Error: $e")
            return Result.failure()
        }
    }
}

```

### WorkRequest Chaining and WorkManager:
WorkerRequests can be run in order or parallel. The output of one WorkRequest becomes the input of the next WorkRequest in the chain.
```kotlin
// cleanup for chaining
var cleanupRequest = OneTimeWorkRequest.from(CleanupWorker::class.java)

val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
    .setInputData(createInputDataForUri())
    .build()

// single work request
// workManager.enqueue(blurRequest)

// save for chaining
val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java).build()

// actual chaining of workers one by one
workManager.beginWith(cleanupRequest)
    .then(blurRequest)
    .then(save)
    .enqueue()
```
### unique work for workmanager:
to ensure the first data sync finish before starting a new one
beginUniqueWork is used with the workManager instance
```kotlin
// change beginWith with beginUniqueWork
workManager
    .beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java)
    )
```
### Work Status and Tag with LiveData:
WorkRequest status can be retrieved by getting a LiveData that holds a WorkInfo object. WorkInfo is an object that contains details about the current state of a WorkRequest. Like BLOCKED, CANCELLED, ENQUEUED, FAILED, RUNNING or SUCCEEDED status and if any output data from the work when the workRequest finished 

* There are 3 different ways to get LiveData<WorkInfo> or LiveData<List<WorkInfo>>
 - getWorkInfoByIdLiveData : LiveData<WorkInfo>
 - getWorkInfosForUniqueWorkLiveData : LiveData<List<WorkInfo>>
 - getWorkInfosByTagLiveData : LiveData<List<WorkInfo>>

 * Work Tag: in ViewModel, a tag can be used to label a work (or works), so all the similar tag works status can be retrieved.
```kotlin
val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
    .addTag(TAG_OUTPUT_CONSTANT)
    .build()
```

### WorkInfo of the WorkManager:
```kotlin
// from viewModel
internal val outputWorkInfos: LiveData<List<WorkInfo>>

init {
    outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
}

// from activity
override fun onCreate(savedInstanceState: Bundle?) {
    // other code
    viewModel.outputWorkInfos.observe(this, workInfosObserver())
}

private fun workInfosObserver(): Observer<List<WorkInfo>> {
    return Observer { listOfWorkInfo ->

        // If there are no matching work info, do nothing
        if (listOfWorkInfo.isNullOrEmpty()) {
            return@Observer
        }

        // get the first workInfo object
        val workInfo = listOfWorkInfo[0]

        Log.d("workInfo", "workInfosObserver: workInfo = $workInfo")
        // workInfosObserver: workInfo = WorkInfo{mId='e74e2632-882d-44e1-a3be-58500cdaa558', mState=SUCCEEDED, mOutputData=Data {KEY_IMAGE_URI : content://media/external/images/media/35, }, mTags=[OUTPUT, com.example.background.workers.SaveImageToFileWorker], mProgress=Data {}}
    }
}
```

### Work Cancelation:
The work/s can be canceled using the id, by tag and by unique chain name. For Unique work, the "uniqueWorkName" is required to target the work
```kotlin
// work cancellation
internal  fun cancelWork() {
    workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
}
```


### Work Constraints:
These are certain condition check for the device state, ie, device is charging, etc. Constraints.Builder is used to create and adding it to workRequest hooked it up.

```kotlin
// constraints
val constraints = Constraints.Builder()
    .setRequiresCharging(true)
    .build()

// Add WorkRequest to save the image to the filesystem
val workRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
    .setConstraints(constraints)
    .addTag(TAG_OUTPUT)
    .build()
```