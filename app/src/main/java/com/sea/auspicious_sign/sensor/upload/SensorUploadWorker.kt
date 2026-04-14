package com.sea.auspicious_sign.sensor.upload

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.data.remote.NetlifyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SensorUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getInstance(context)
    private val api: NetlifyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://your-netlify-app.netlify.app/") // TODO: 替换为实际 baseUrl
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetlifyApi::class.java)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val unsynced = database.rawDataDao().getUnsynced().first()
            if (unsynced.isEmpty()) return@withContext Result.success()

            // TODO: 将 unsynced 数据打包发送到 NetlifyApi.uploadSensorData()
            // val response = api.uploadSensorData(unsynced)
            // if (response.isSuccessful) {
            //     unsynced.forEach { database.rawDataDao().markSynced(it.id) }
            //     Result.success()
            // } else {
            //     Result.retry()
            // }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}