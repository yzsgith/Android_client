// TODO: 作用 -- WebView 安全配置扩展函数
package com.sea.auspicious_sign.features.webview_interaction.core

import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 为 WebView 应用安全配置扩展函数。
 * 包括禁止文件访问、禁止混合内容、设置域名白名单和移除危险 JavaScript 接口。
 *
 * @receiver WebView 实例
 */
fun WebView.applySecuritySettings() {
    settings.apply {
        allowFileAccess = false
        allowContentAccess = false
        mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
        savePassword = false
        setGeolocationEnabled(false)
    }

    webViewClient = object : WebViewClient() {
        /**
         * 拦截 URL 加载，只允许白名单域名。
         * @param view 当前 WebView
         * @param url 要加载的 URL
         * @return true 表示拦截，false 表示允许加载
         */
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val allowedHost = "your-server.com"
            return if (url?.contains(allowedHost) == true) false else true
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        removeJavascriptInterface("searchBoxJavaBridge_")
        removeJavascriptInterface("accessibility")
        removeJavascriptInterface("accessibilityTraversal")
    }
}
