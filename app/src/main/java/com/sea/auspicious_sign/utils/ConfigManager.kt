// TODO: 作用 -- 动态读取外部配置文件 config.json，提供 API 基础 URL，支持运行时切换服务器端点
package com.sea.auspicious_sign.utils

import android.content.Context
import org.json.JSONObject
import java.io.File

object ConfigManager {
    private const val CONFIG_FILE = "config.json"
    private const val DEFAULT_BASE_URL = "https://httpbin.org/post"

    private var cachedBaseUrl: String? = null

    fun getApiBaseUrl(context: Context): String {
        cachedBaseUrl?.let { return it }
        val configFile = File(context.getExternalFilesDir(null), CONFIG_FILE)
        val baseUrl = if (configFile.exists()) {
            try {
                val json = JSONObject(configFile.readText())
                json.optString("api_base_url", DEFAULT_BASE_URL)
            } catch (e: Exception) {
                DEFAULT_BASE_URL
            }
        } else {
            DEFAULT_BASE_URL
        }
        cachedBaseUrl = baseUrl
        return baseUrl
    }

    fun refresh() {
        cachedBaseUrl = null
    }
}
