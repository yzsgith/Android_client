// TODO: 作用 -- 自定义 Application，创建全局的 Uploader 和基础请求模板，供 SensorUploadWorker 使用（避免 WorkManager 重复初始化）
package com.sea.auspicious_sign

import android.app.Application

import com.sea.auspicious_sign.upload_data.RetryPolicy
import com.sea.auspicious_sign.upload_data.UploadRequest
import com.sea.auspicious_sign.upload_data.Uploader

import com.sea.auspicious_sign.utils.AppPreferences
import com.sea.auspicious_sign.utils.dataStore
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

/**
 * 自定义 Application 类
 * 负责创建全局的 Uploader 和基础请求模板，供 SensorUploadWorker 通过单例获取。
 * 注意：不再手动初始化 WorkManager，避免与默认的 WorkManagerInitializer 冲突。
 */
class AuspiciousSignApplication : Application() {

    /** 上传执行器单例，供 Worker 使用 */
    lateinit var uploader: Uploader
        private set

    /** 基础上传请求模板（URL 从 DataStore 动态读取），供 Worker 复制并填充 body */
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

        // 3. 创建 Uploader 实例
        uploader = Uploader(OkHttpClient())
    }
}