### Overviews:
Topics to be covered. App Components (except Activity and Content Providers).
`Service `
- Start/Stop Service, Multi Threading, AsyncTask Service
- Bound Service
- Intent Service
- JobScheduler and JobService
- Broadcast Receiver (create & register), Run Service after device boot
- JetPack WorkManager (Create and run schedule task with WM)
- Foreground Service
- Restart Service After Reboot

### Service and Intent Service:
https://developer.android.com/guide/components/services

### Intent Service (Deprecated, Use WorkManager):
Intent service runs in BackGround Thread by default. See an Working example below
```kotlin
// Custom IntentService Class
class MyIntentService: IntentService("MyIntentService Debug Identifier") {
    
    /**
     * Making Singleton access to stop the service later
     */
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MyIntentService
        var isRunning = false
        
        fun stopServiceCustom() {
            Log.d("MyInstanceServiceLog.d", "MyIntent Service is stopping : stopServiceCustom() called")
            isRunning = false
            instance.stopSelf()
        }
    }
    
    override fun onHandleIntent(p0: Intent?) {
        try {
            isRunning = true
            while(isRunning) {
                Log.d("MyInstanceServiceLog.d", "Service is running onHandleIntent() call")
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}


// Call the service from activity (start and stop btn pressed)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Without View binding
         */
//        setContentView(R.layout.activity_main)
//        val btnStart = findViewById<Button>(R.id.btnStart)
//        btnStart.setOnClickListener {
//                Log.d("First Button Call", "onCreate: Start btn clicked")
//        }

        /**
         * Using View binding
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            btnStart.setOnClickListener {
                Log.d("First Button Call", "onCreate: Start btn clicked")
                Intent(this@MainActivity, MyIntentService::class.java).also {
                    startService(it)
                    ftext.text = "Service Started"
                }
            }

            btnEnd.setOnClickListener {
                Log.d("Second Button Call", "onCreate: End btn clicked")
                MyIntentService.stopServiceCustom()
                ftext.text = "Service Stopped"
            }
        }

    }
}


// add manifest entry for the service
// ...
// <application>
//     ...
//     <activity ...>
//     <service android:name=".MyIntentService"/>
// </application>
```
### Service (General):
Service runs in ui/main thread by default. So we need to assign a background thread to not to block/freeze ui.
```kotlin

```
### Broadcast and Broadcast Receivers:
Docs https://developer.android.com/guide/components/broadcasts
`Broadcast` - Sending Intent To another app. In short, it can be system wide events, those are send by the android system and can receive/listen by an app. 
`Broadcast Receiver` - Receiving an Intent from another app or from system and handling it silently.

Broadcast examples, `System Boot Completed`, `Incoming-Call Broadcast`, `Airplane Mode Toggle`, etc.

Broadcast Receivers are listeners and action handles on those events. Like, if `Incoming-Call` triggered, other app could react on these.

The are 2 Kind of Broadcast Receivers,
1. `Dynamic` -> Dynamic Broadcast Receiver needs the app to be in active mode. We attach the BroadCast Receiver inside `onCreate` using `registerReceiver()` and un-attach on `onDestroy` using `unregisterReceiver()`
```kotlin
class AirPlaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            /**
             * Check if AirPaneMode is on or off
             */
            val isTurnedOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON
            ) != 0

            println("Is AirPlane Mode enabled ? = $isTurnedOn")
        }
    }
}

/**
* Register this from the package
*/
class MainActivity : ComponentActivity() {

    private val airPlaneModeReceiver = AirPlaneModeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Register for receiving Broadcast
         */
        registerReceiver(
            airPlaneModeReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )
    }

    /**
     * Unregister from receiving Broadcast when activity destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneModeReceiver)
    }
}
```

2. `Static / Manifest Declared Receiver` -> this type of receiver doesn't require for app to be in active state. These are hooked from the manifest and system package manager register the receiver when the app is installed. Also it needs `<uses-permission .../>` and `<receiver>...</receiver>` declaration in manifest. Supported broadcast list is https://developer.android.com/guide/components/broadcast-exceptions.

```xml
<manifest ...>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

    <application>
    ...
        <activity>...</activity>
        <receiver android:name=".BootCompletedReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
    </application>

</manifest>
```
```kotlin
private const val TAG = "TAG Custom Boot"

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: $context")
        Log.d(TAG, "onReceive: $intent")
        Log.d(TAG, "onReceive: ${intent?.action}")
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "onReceive: Boot Completed Notify Receiver From Custom BroadCast Class")
        }
    }
}
```
### Sending Broadcast To Other App:
```kotlin
// sending an broadcast, any app can receive this using intent?.action == "TEST_ACTION_FROM_THIS_APP" from a `BroadcastReceiver` class
sendBroadcast(Intent("TEST_ACTION_FROM_THIS_APP"))

// Receiving above broadcast
// It's same, define a CustomReceiver form the BroadcastReceiver()
// register and unregister the receiver from view
```

### BackGround Work
https://developer.android.com/guide/background.