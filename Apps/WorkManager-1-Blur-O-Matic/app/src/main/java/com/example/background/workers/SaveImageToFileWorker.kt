package com.example.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SaveImageToFileWorker(ctx: Context, params: WorkerParameters ): Worker(ctx, params) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}