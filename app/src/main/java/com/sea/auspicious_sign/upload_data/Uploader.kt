// TODO: 作用 -- 执行 UploadRequest，通过 OkHttpClient 发送请求，支持自动重试和超时
package com.sea.auspicious_sign.upload_data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * 上传执行器，负责发送 HTTP 请求并处理重试逻辑
 * @param client OkHttpClient 实例，用于执行网络请求
 */
class Uploader(private val client: OkHttpClient) {

    /**
     * 执行上传请求，支持自动重试和超时（30秒）
     * @param request 上传请求参数对象
     * @return 返回 [Result] 包装的 [Response] 对象，成功时包含响应，失败时包含异常
     * @throws IOException 当重试次数耗尽且最后一次请求失败时，返回的 Result 中包含 IOException
     */
    suspend fun execute(request: UploadRequest): Result<Response> {
        var attempt = 1
        var delayMs = request.retryPolicy.initialDelayMs

        while (attempt <= request.retryPolicy.maxAttempts) {
            try {
                val okHttpRequest = buildOkHttpRequest(request)
                val response = withTimeout(30_000) {
                    client.newCall(okHttpRequest).execute()
                }
                if (response.isSuccessful) {
                    return Result.success(response)
                } else {
                    if (attempt < request.retryPolicy.maxAttempts) {
                        Log.w("Uploader", "Attempt $attempt failed with code ${response.code}, retry after ${delayMs}ms")
                        delay(delayMs)
                        delayMs = (delayMs * request.retryPolicy.backoffFactor).toLong()
                        attempt++
                        response.close()
                        continue
                    } else {
                        return Result.failure(IOException("Upload failed after ${request.retryPolicy.maxAttempts} attempts, last code: ${response.code}"))
                    }
                }
            } catch (e: Exception) {
                if (attempt < request.retryPolicy.maxAttempts) {
                    Log.w("Uploader", "Attempt $attempt failed with ${e.message}, retry after ${delayMs}ms")
                    delay(delayMs)
                    delayMs = (delayMs * request.retryPolicy.backoffFactor).toLong()
                    attempt++
                } else {
                    return Result.failure(e)
                }
            }
        }
        return Result.failure(IOException("Unexpected end of retry loop"))
    }

    private fun buildOkHttpRequest(request: UploadRequest): Request {
        val body = request.body?.toRequestBody("application/json".toMediaType())
            ?: RequestBody.create(null, "")
        return Request.Builder()
            .url(request.url)
            .method(request.method, body)
            .apply {
                request.headers.forEach { (k, v) -> header(k, v) }
            }
            .build()
    }
}
