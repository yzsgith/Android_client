package com.sea.auspicious_sign.webview

import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 为 WebView 应用安全配置的扩展函数
 */
fun WebView.applySecuritySettings() {
    settings.apply {
        // 禁用文件跨域访问
        allowFileAccess = false
        allowContentAccess = false
        // 禁止混合内容（HTTP 和 HTTPS 混合）
        mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
        // 禁用保存密码
        savePassword = false
        // 如果不需要定位，可禁用
        setGeolocationEnabled(false)
    }

    // 设置 WebViewClient 只允许加载特定域名（白名单）
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            // TODO: 替换为你的服务器域名
            val allowedHost = "your-server.com"
            return if (url?.contains(allowedHost) == true) {
                false  // 允许加载
            } else {
                true   // 拦截外部链接
            }
        }
    }

    // 移除 JavaScript 接口中危险的系统对象（Android 4.2 以上默认安全）
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        removeJavascriptInterface("searchBoxJavaBridge_")
        removeJavascriptInterface("accessibility")
        removeJavascriptInterface("accessibilityTraversal")
    }
}