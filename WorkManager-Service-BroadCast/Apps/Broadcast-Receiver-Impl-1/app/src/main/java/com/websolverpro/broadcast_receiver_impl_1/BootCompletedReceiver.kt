package com.websolverpro.broadcast_receiver_impl_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

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