// TODO: 作用 -- 定期将未同步的传感器原始数据分批上传，依赖从 Application 单例获取
package com.sea.auspicious_sign.sensor.upload

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sea.auspicious_sign.AuspiciousSignApplication
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.upload_data.UploadRequest
import com.sea.auspicious_sign.upload_data.Uploader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * 传感器数据上传 Worker
 * 从数据库分批读取未同步数据，构造 JSON 数组，通过 Uploader 发送到服务器
 */
class SensorUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val application = context.applicationContext as AuspiciousSignApplication
    private val uploader = application.uploader
    private val baseRequest = application.baseUploadRequest
    private val database = AppDatabase.getInstance(context)
    private val batchSize = 50   // 每批最多上传 50 条，避免内存溢出

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("UploadWorker", "doWork() started")

        try {
            var hasMore = true
            while (hasMore) {
                // 1. 获取一批未同步数据（最多 batchSize 条）
                val unsynced = database.rawDataDao().getUnsyncedBatch(batchSize).first()
                if (unsynced.isEmpty()) {
                    hasMore = false
                    break
                }

                // 2. 构造当前批次的 JSON 数组
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

                // 3. 从基础模板复制，替换 body，生成当前批次的请求
                val uploadRequest = baseRequest.copy(body = jsonArray.toString())
                val result = uploader.execute(uploadRequest)

                // 4. 处理上传结果
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    if (response.isSuccessful) {
                        // 上传成功，更新数据库中记录的同步标志
                        unsynced.forEach { entity ->
                            database.rawDataDao().update(entity.copy(synced = true))
                        }
                        Log.d("UploadWorker", "Uploaded batch of ${unsynced.size} records")
                    } else {
                        Log.e("UploadWorker", "Batch upload failed, HTTP code: ${response.code}")
                        return@withContext Result.retry()
                    }
                    response.close()
                } else {
                    Log.e("UploadWorker", "Batch upload error", result.exceptionOrNull())
                    return@withContext Result.retry()
                }

                // 批次间稍作延迟，避免过于密集
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