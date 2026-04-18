// TODO: 作用 -- 定期将未同步的传感器原始数据分批上传到可配置的服务器端点
package com.sea.auspicious_sign.sensor.upload

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.utils.AppPreferences
import com.sea.auspicious_sign.utils.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class SensorUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getInstance(context)
    private val client = OkHttpClient()
    private val dataStore = context.dataStore
    private val BATCH_SIZE = 50   // 每批最多上传50条，避免内存溢出

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("UploadWorker", "doWork() started")

        try {
            // 1. 读取动态配置的 API 基础 URL
            val apiBaseUrl = AppPreferences.getApiBaseUrl(dataStore)
            // 2. 拼接完整端点（假设后端接口为 /api/sensor-data）
            val fullUrl = if (apiBaseUrl.endsWith("/api/sensor-data")) {
                apiBaseUrl
            } else {
                "$apiBaseUrl/api/sensor-data"
            }
            Log.d("UploadWorker", "Upload URL: $fullUrl")

            var hasMore = true
            while (hasMore) {
                // 3. 分批获取未同步数据
                val unsynced = database.rawDataDao().getUnsyncedBatch(BATCH_SIZE).first()
                if (unsynced.isEmpty()) {
                    hasMore = false
                    break
                }

                // 4. 构造当前批次的 JSON 数组
                val jsonArray = JSONArray()
                unsynced.forEach { entity ->
                    val obj = JSONObject().apply {
                        put("id", entity.id)
                        put("type", entity.type)
                        put("timestamp", entity.timestamp)
                        put("dataJson", entity.dataJson)
                    }
                    jsonArray.put(obj)
                }

                // 5. 创建请求体
                val body = jsonArray.toString()
                    .toRequestBody("application/json".toMediaType())

                // 6. 构建 POST 请求
                val request = Request.Builder()
                    .url(fullUrl)
                    .post(body)
                    .build()

                // 7. 执行请求
                val response = client.newCall(request).execute()

                // 8. 处理响应
                if (response.isSuccessful) {
                    // 上传成功，更新本批次数据的同步标志
                    unsynced.forEach { entity ->
                        database.rawDataDao().update(entity.copy(synced = true))
                    }
                    Log.d("UploadWorker", "Uploaded batch of ${unsynced.size} records")
                } else {
                    Log.e("UploadWorker", "Batch upload failed, HTTP code: ${response.code}")
                    return@withContext Result.retry()
                }

                // 可选：批次之间稍作延迟，避免过于密集
                delay(200)
            }

            Log.d("UploadWorker", "All batches uploaded successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("UploadWorker", "Upload error", e)
            Result.retry()
        }
    }
}