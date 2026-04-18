package com.sea.auspicious_sign

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.*
import com.sea.auspicious_sign.sensor.upload.SensorUploadWorker
import java.util.concurrent.TimeUnit

class AuspiciousSignApplication : Application() {
    // AuspiciousSignApplication.kt

    override fun onCreate() {
        super.onCreate()
        scheduleUploadWork()
    }

    private fun scheduleUploadWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = PeriodicWorkRequestBuilder<SensorUploadWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "sensor_upload",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadRequest
        )
    }
}