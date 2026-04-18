package com.sea.auspicious_sign

// MyUploadWorker.kt


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class MyUploadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // 1. 获取输入参数
        val userId = inputData.getInt("USER_ID", -1)
        val data = inputData.getString("DATA") ?: ""

        Log.d("MyUploadWorker", "开始上传，userId=$userId, data=$data")

        // 模拟耗时上传操作（如网络请求）
        delay(2000)

        // 假设上传成功，可以附带输出数据
        return if (userId > 0 && data.isNotBlank()) {
            // 2. 返回成功并附带结果
            Result.success(workDataOf("upload_id" to System.currentTimeMillis()))
        } else {
            // 参数无效，标记失败（不再重试）
            Result.failure()
        }
    }
}