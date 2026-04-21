// TODO: 作用 -- 自定义 WorkerFactory，为 SensorUploadWorker 注入 Uploader 和 baseRequest
package com.sea.auspicious_sign.sensor.upload

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sea.auspicious_sign.network.UploadRequest
import com.sea.auspicious_sign.network.Uploader

/**
 * 自定义 WorkerFactory，用于创建带有依赖的 [SensorUploadWorker]
 * @param uploader 上传执行器实例
 * @param baseRequest 基础请求模板
 * @param batchSize 每批上传数据条数
 */
class SensorUploadWorkerFactory(
    private val uploader: Uploader,
    private val baseRequest: UploadRequest,
    private val batchSize: Int = 50
) : WorkerFactory() {

    /**
     * 创建 Worker 实例
     * @param className Worker 的完整类名
     * @param context 上下文
     * @param params Worker 参数
     * @return 创建的 Worker 实例，如果类名不匹配则返回 null
     */
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SensorUploadWorker::class.java.name -> {
                SensorUploadWorker(appContext, workerParameters, uploader, baseRequest, batchSize)
            }
            else -> null
        }
    }
}
