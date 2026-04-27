// TODO: 作用 -- 提供 WebView 与 JavaScript 交互的桥接接口
package com.sea.auspicious_sign.features.webview_interaction.core

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast

/**
 * 为 JavaScript 提供原生调用能力的桥接类。
 * 通过 `addJavascriptInterface` 注册到 WebView，前端可调用其 `@JavascriptInterface` 方法。
 *
 * @param context 上下文（用于显示 Toast 等）
 * @param webView 关联的 WebView 实例（用于执行 JavaScript 回调）
 */
class WebViewBridge(
    private val context: Context,
    private val webView: WebView
) {
    /**
     * 显示一个短暂的提示消息。
     * @param message 要显示的文本
     */
    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 启动后台自动上传任务（由 WorkManager 调度）。
     * @param intervalMs 上传间隔（毫秒）
     */
    @JavascriptInterface
    fun startAutoUpload(intervalMs: Int) {
        // TODO: 调用 WorkManager 启动传感器自动上传任务
        android.util.Log.d("WebViewBridge", "startAutoUpload called with interval: $intervalMs")
    }

    /**
     * 停止后台自动上传任务。
     */
    @JavascriptInterface
    fun stopAutoUpload() {
        // TODO: 取消 WorkManager 任务
        android.util.Log.d("WebViewBridge", "stopAutoUpload called")
    }

    /**
     * 获取一批未上传的传感器数据（JSON 格式）。
     * @return JSON 数组字符串，例如 `"[{\"id\":\"1\",\"type\":\"heart_rate\",\"value\":72}]"`
     */
    @JavascriptInterface
    fun getSensorDataBatch(): String {
        // TODO: 从 Room 中获取一批未上传的传感器数据，返回 JSON 字符串
        return "[]"
    }

    /**
     * 调起系统文件选择器，并将用户选择的文件上传到服务器。
     */
    @JavascriptInterface
    fun uploadFileFromPicker() {
        // TODO: 调起系统文件选择器，并将结果上传到 Netlify
        android.util.Log.d("WebViewBridge", "uploadFileFromPicker called")
    }
}
