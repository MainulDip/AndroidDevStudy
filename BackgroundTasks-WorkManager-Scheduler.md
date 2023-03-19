## Overview:
This part covers some quick jump starting instruction set to run Background Tasks Scheduler in Android.

### WorkManager:
It's a part of Android Jetpack and Architecture Componet. It's a combination of opportunistic and guaranteed execution technique. 
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
- WorkManager: This class actually schedules your WorkRequest and makes it run. It schedules WorkRequests in a way that spreads out the load on system resources, while honoring the constraints you specify.


### Data Input Object (Result):
Input and output is passed in and out via Data objects from worker. Data objects are lightweight containers for key/value pairs. They are meant to store a small amount of data that might pass into and out from WorkRequests.
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
beginUniqueWork is used with workManager instance
```kotlin
// change beginWith with beginUniqueWork
workManager
    .beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java)
    )
```