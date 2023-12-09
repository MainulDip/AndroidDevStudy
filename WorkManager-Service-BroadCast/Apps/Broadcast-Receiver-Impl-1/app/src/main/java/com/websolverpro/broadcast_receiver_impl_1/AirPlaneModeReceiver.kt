package com.websolverpro.broadcast_receiver_impl_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

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