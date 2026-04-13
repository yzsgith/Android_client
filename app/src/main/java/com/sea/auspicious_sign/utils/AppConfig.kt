package com.sea.auspicious_sign.utils

object AppConfig {
    // 服务器基础 URL
    const val BASE_URL = "https://your-netlify-app.netlify.app"

    // 上传间隔（毫秒）
    const val UPLOAD_INTERVAL_MS = 30000L // 30秒

    // 是否允许自动采集（默认开启）
    var isAutoCollectionEnabled: Boolean = true
        private set

    fun setAutoCollectionEnabled(enabled: Boolean) {
        isAutoCollectionEnabled = enabled
        // TODO: 持久化到 DataStore
    }
}
