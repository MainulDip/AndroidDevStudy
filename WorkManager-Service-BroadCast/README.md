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

### BackGround Work
https://developer.android.com/guide/background.