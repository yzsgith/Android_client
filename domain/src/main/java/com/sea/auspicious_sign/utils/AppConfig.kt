package com.sea.auspicious_sign.utils

data class AppConfig(
    val baseUrl: String = "https://your-netlify-app.netlify.app",
    val uploadIntervalMinutes: Int = 15,
    val autoUploadEnabled: Boolean = true,
    val maxRetryCount: Int = 3
)
