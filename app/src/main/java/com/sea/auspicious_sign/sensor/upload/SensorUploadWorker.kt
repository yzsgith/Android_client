// TODO: 作用 -- 定期将未同步的传感器原始数据分批上传，使用外部传入的 UploadRequest 模板
package com.sea.auspicious_sign.sensor.upload

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.network.UploadRequest
import com.sea.auspicious_sign.network.Uploader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * 传感器数据上传 Worker
 * @param context 上下文
 * @param params WorkManager 参数
 * @param uploader 上传执行器实例
 * @param baseRequest 基础请求模板（URL、headers、重试策略已配置，body 为空）
 * @param batchSize 每批上传的数据条数，默认 50
 */
class SensorUploadWorker(
    context: Context,
    params: WorkerParameters,
    private val uploader: Uploader,
    private val baseRequest: UploadRequest,
    private val batchSize: Int = 50
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getInstance(context)

    /**
     * 执行上传任务，分批从数据库读取未同步数据，构造 JSON 并发送请求
     * @return [Result] 成功返回 [Result.success]，失败或需要重试返回 [Result.retry]
     */
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("UploadWorker", "doWork() started")

        try {
            var hasMore = true
            while (hasMore) {
                // 1. 获取一批未同步数据
                val unsynced = database.rawDataDao().getUnsyncedBatch(batchSize).first()
                if (unsynced.isEmpty()) {
                    hasMore = false
                    break
                }

                // 2. 构造 JSON 数组
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

                // 3. 从模板复制生成当前批次的请求（替换 body）
                val uploadRequest = baseRequest.copy(body = jsonArray.toString())
                val result = uploader.execute(uploadRequest)

                // 4. 处理上传结果
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    if (response.isSuccessful) {
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

                // 批次间短暂延迟，避免网络拥塞
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