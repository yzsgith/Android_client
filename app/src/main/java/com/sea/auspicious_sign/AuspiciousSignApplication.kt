
// TODO: 作用 -- 自定义 Application，初始化 WorkManager 并配置上传依赖（URL 从 DataStore 读取）
package com.sea.auspicious_sign

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.sea.auspicious_sign.network.RetryPolicy
import com.sea.auspicious_sign.network.UploadRequest
import com.sea.auspicious_sign.network.Uploader
import com.sea.auspicious_sign.sensor.upload.SensorUploadWorkerFactory
import com.sea.auspicious_sign.utils.AppPreferences
import com.sea.auspicious_sign.utils.dataStore
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

/**
 * 自定义 Application 类
 * 负责初始化 WorkManager 并注入传感器上传所需的依赖（Uploader 和基础请求模板）
 */
class AuspiciousSignApplication : Application() {

    /** 基础上传请求模板，供 [SensorUploadWorker] 使用，URL 从 DataStore 动态读取 */
    lateinit var baseUploadRequest: UploadRequest
        private set

    override fun onCreate() {
        super.onCreate()

        // 1. 从 DataStore 读取 API 基础 URL 并拼接完整端点
        val dataStore = this.dataStore
        // 使用 runBlocking 阻塞主线程（Application.onCreate 允许短暂阻塞）
        val apiBaseUrl = runBlocking { AppPreferences.getApiBaseUrl(dataStore) }
        val fullUrl = if (apiBaseUrl.endsWith("/api/sensor-data")) {
            apiBaseUrl
        } else {
            "$apiBaseUrl/api/sensor-data"
        }

        // 2. 创建基础请求模板（不包含 body，后续由 Worker 填充）
        baseUploadRequest = UploadRequest(
            url = fullUrl,
            method = "POST",
            headers = mapOf("Content-Type" to "application/json"),
            retryPolicy = RetryPolicy.DEFAULT
        )

        // 3. 创建 Uploader 和自定义 WorkerFactory
        val uploader = Uploader(OkHttpClient())
        val factory = SensorUploadWorkerFactory(uploader, baseUploadRequest)

        // 4. 初始化 WorkManager，使用自定义工厂
        val config = Configuration.Builder()
            .setWorkerFactory(factory)
            .build()
        WorkManager.initialize(this, config)
    }
}