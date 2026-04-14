package com.sea.auspicious_sign.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.data.remote.NetlifyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getInstance(context)
    private val api: NetlifyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://your-netlify-app.netlify.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetlifyApi::class.java)
    }

    override suspend fun doWork(): Result {
        return try {
            val unsynced = database.consumableDataDao().getUnsynced()
            if (unsynced.isEmpty()) return Result.success()
            // TODO: 遍历 unsynced，调用 api.syncMapping() 同步
            // 成功后调用 database.consumableDataDao().markSynced(it.id)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
