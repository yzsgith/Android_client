// TODO: 作用 -- 封装一次 HTTP 上传请求所需的所有参数，包括 URL、方法、头、请求体及重试策略
package com.sea.auspicious_sign.network

/**
 * 重试策略，定义请求失败后的重试行为
 * @property maxAttempts 最大尝试次数（包括第一次请求）
 * @property initialDelayMs 首次重试前的初始延迟（毫秒）
 * @property backoffFactor 退避因子，每次重试延迟 = 上一次延迟 * backoffFactor
 */
data class RetryPolicy(
    val maxAttempts: Int = 3,
    val initialDelayMs: Long = 1000,
    val backoffFactor: Double = 2.0
) {
    companion object {
        /** 默认重试策略：最多3次，初始延迟1秒，指数退避因子2 */
        val DEFAULT = RetryPolicy()
    }
}

/**
 * 上传请求数据类，将一次上传所需的所有信息封装为一个不可变对象
 * @property url 完整的请求 URL（包含协议、域名、路径）
 * @property method HTTP 方法，如 "GET", "POST", "PUT" 等，默认 "POST"
 * @property headers 请求头映射，默认包含 "Content-Type: application/json"
 * @property body 请求体字符串，通常为 JSON 格式，默认为 null
 * @property retryPolicy 重试策略，默认为 [RetryPolicy.DEFAULT]
 */
data class UploadRequest(
    val url: String,
    val method: String = "POST",
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    val body: String? = null,
    val retryPolicy: RetryPolicy = RetryPolicy.DEFAULT
)