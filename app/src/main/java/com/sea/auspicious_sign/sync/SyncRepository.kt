package com.sea.auspicious_sign.sync

import android.content.Context
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.data.remote.NetlifyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SyncRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val api: NetlifyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://your-netlify-app.netlify.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetlifyApi::class.java)
    }

    suspend fun enqueueSync(consumableDataId: String) {
        // TODO: 插入同步队列
    }

    suspend fun processPendingSync() {
        val pending = database.consumableDataDao().getUnsynced()
        for (item in pending) {
            try {
                // TODO: 调用 api.syncMapping(item.toMap())
                // 成功后标记为已同步
                database.consumableDataDao().markSynced(item.id)
            } catch (e: Exception) {
                // 重试逻辑交给 Worker
            }
        }
    }
}
