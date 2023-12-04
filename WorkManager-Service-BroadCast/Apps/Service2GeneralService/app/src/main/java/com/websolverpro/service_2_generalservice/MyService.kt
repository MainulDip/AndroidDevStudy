package com.websolverpro.service_2_generalservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

const val TAG = "MyService"

class MyService: Service() {
    
    init {
        Log.d(TAG, "MyService started running: ")
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString = intent?.getStringExtra("EXTRA_DATA")
        dataString?.let {
            Log.d(TAG, dataString)
        }

        /**
         * For Long Running Task Start a new Thread
         * so that it will not block/freeze the main ui thread
         */

        Thread {
            Log.d(TAG, "Task Started From Another Thread")
            println("Hello")
            Thread.sleep(1000)
            Log.d(TAG, "Task Completed From Another Thread")
        }.start()

        /**
         * Return can be
         * START_NOT_STICKY (if killed, will not be recreated)
         * START_STICKY (will be recreated when possible with null intent,  if killed)
         * START_REDELIVER_INTENT ( STICKY + Provided intent )
         * or `super.onStartCommand(intent, flags, startId)`
         * */

        return START_NOT_STICKY
    }
}