package com.websolverpro.service_1

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MyIntentService: IntentService("MyIntentService Debug Identifier") {
    
    /**
     * Making Singleton access to stop later
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