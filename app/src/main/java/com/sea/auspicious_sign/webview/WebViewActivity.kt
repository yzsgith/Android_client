package com.sea.auspicious_sign.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.sea.auspicious_sign.R
import java.net.URL

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var webViewBridge: WebViewBridge
    private val mainHandler = Handler(Looper.getMainLooper())

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.webView)
        webViewBridge = WebViewBridge(this, webView)

        configureWebView()
        loadUrl()   // 默认加载网络地址

        // 处理返回事件（兼容手势返回）
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

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = false
            allowContentAccess = false
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
            // 缓存设置：避免旧数据干扰
            cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
        }

        // 模拟器兼容：如果软件渲染可解决崩溃问题，取消注释下行
        // webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("WebView", "onPageStarted: $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "onPageFinished: $url")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e("WebView", "Error: ${error?.description} (${error?.errorCode})")
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("WebView", "HTTP error: ${errorResponse?.statusCode}")
            }

            override fun onRenderProcessGone(
                view: WebView?,
                detail: android.webkit.RenderProcessGoneDetail?
            ): Boolean {
                Log.e("WebView", "Render process gone, detail: $detail")
                // 尝试重启 Activity（可选）
                finish()
                return true
            }
        }

        webView.addJavascriptInterface(webViewBridge, "AndroidBridge")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            webView.removeJavascriptInterface("accessibility")
            webView.removeJavascriptInterface("accessibilityTraversal")
        }
    }

    private fun loadUrl() {
        // 方式一：直接加载网络地址（需要 adb reverse 或真机）
        val url = "http://localhost:8080"
        webView.loadUrl(url)

        // 方式二：如果方式一失败，可尝试先加载简单 HTML 测试（取消注释下方代码）
        /*
        webView.loadDataWithBaseURL(
            null,
            "<html><body><h1>Test from data</h1></body></html>",
            "text/html",
            "UTF-8",
            null
        )
        */

        // 方式三：通过网络请求获取 HTML 字符串再加载（绕过 loadUrl 可能的问题）
        /*
        thread {
            try {
                val html = URL(url).readText()
                mainHandler.post {
                    webView.loadDataWithBaseURL(url, html, "text/html", "UTF-8", null)
                }
            } catch (e: Exception) {
                Log.e("WebView", "Fetch HTML failed", e)
            }
        }
        */
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}