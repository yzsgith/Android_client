// TODO: 作用 -- 加载服务器前端页面，配置 WebView 安全策略，并注册 JS 桥接接口
package com.sea.auspicious_sign.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.RenderProcessGoneDetail
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.sea.auspicious_sign.R

/**
 * WebView 展示 Activity，负责加载指定的 URL，并暴露原生方法给前端 JavaScript 使用。
 *
 * 执行本任务前必须满足以下隐性依赖：
 * - 已在 AndroidManifest.xml 中声明本 Activity。
 * - 已添加 INTERNET 权限（若加载网络 URL）。
 * - 若加载 HTTP 明文流量，需在清单中设置 `android:usesCleartextTraffic="true"`。
 *
 * @see WebViewBridge
 * @see [webview_interaction.plantuml] 对应时序图
 */
class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var webViewBridge: WebViewBridge

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.webView)
        webViewBridge = WebViewBridge(this, webView)

        configureWebView()
        loadUrl()

        // 处理返回事件（兼容手势返回和物理返回键）
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    /**
     * 配置 WebView 的各项设置和安全策略。
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        webView.settings.apply {
            javaScriptEnabled = true               // 启用 JS
            domStorageEnabled = true               // 启用 localStorage
            allowFileAccess = false                // 禁止访问文件系统（安全）
            allowContentAccess = false             // 禁止 Content Provider
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
            // 缓存设置：避免旧数据干扰
            cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("WebView", "onPageStarted: $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "onPageFinished: $url")
            }

            // 处理渲染进程崩溃（模拟器常见问题）
            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                Log.e("WebViewActivity", "Render process gone, detail: $detail")
                finish()
                return true
            }
        }

        // 添加 JS 桥接接口
        webView.addJavascriptInterface(webViewBridge, "AndroidBridge")

        // 移除危险的 JavaScript 接口（Android 4.2 以上）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            webView.removeJavascriptInterface("accessibility")
            webView.removeJavascriptInterface("accessibilityTraversal")
        }
    }

    /**
     * 加载目标 URL。
     * 默认加载本地测试服务器的地址（可通过 Intent 传递自定义 URL）。
     */
    private fun loadUrl() {
        // 方式一：直接加载网络地址（需要 adb reverse 或真机）
        val url = "http://localhost:8080"
        webView.loadUrl(url)
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}