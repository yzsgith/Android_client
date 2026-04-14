package com.sea.auspicious_sign.webview

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast

class WebViewBridge(
    private val context: Context,
    private val webView: WebView
) {

    @JavascriptInterface
    fun showToast(message: String) {
        // 测试用：JS 调用原生 Toast
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun startAutoUpload(intervalMs: Int) {
        // TODO: 调用 WorkManager 启动传感器自动上传任务
        // 示例：通知用户
        android.util.Log.d("WebViewBridge", "startAutoUpload called with interval: $intervalMs")
    }

    @JavascriptInterface
    fun stopAutoUpload() {
        // TODO: 取消 WorkManager 任务
        android.util.Log.d("WebViewBridge", "stopAutoUpload called")
    }

    @JavascriptInterface
    fun getSensorDataBatch(): String {
        // TODO: 从 Room 中获取一批未上传的传感器数据，返回 JSON 字符串
        return "[]"
    }

    @JavascriptInterface
    fun uploadFileFromPicker() {
        // TODO: 调起系统文件选择器，并将结果上传到 Netlify
        android.util.Log.d("WebViewBridge", "uploadFileFromPicker called")
    }
}