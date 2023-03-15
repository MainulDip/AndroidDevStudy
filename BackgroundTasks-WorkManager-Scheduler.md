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


### Data Input Object:
