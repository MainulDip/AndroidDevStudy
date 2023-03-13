package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R
import java.io.DataOutput

class BlurWorker(val ctx: Context, val params: WorkerParameters): Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = ctx.applicationContext

        makeStatusNotification("Bluring The Cup Cake", appContext)

        try {
            val picture = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake
            )

            val bluredImg =  blurBitmap(picture, appContext)
            val storagePath = writeBitmapToFile(appContext, bluredImg)

            makeStatusNotification("Finished Bluring and the File Path is $storagePath", appContext)
        } catch (e: Exception) {
            Log.e("Blur Not Succeed", "Error applying blur")
//            throw Throwable("Error: $e")
            return Result.failure()
        }

        return Result.success()
    }
}