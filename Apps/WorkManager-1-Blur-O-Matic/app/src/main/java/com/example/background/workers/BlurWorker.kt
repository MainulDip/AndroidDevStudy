package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI

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