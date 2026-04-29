// TODO: 作用 -- WebView 交互功能的主入口 Activity，负责加载网页、配置安全策略和注册 JS 桥接
package com.sea.auspicious_sign.features.webview_interaction.core

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sea.auspicious_sign.R
import com.sea.auspicious_sign.generated.AuxiliaryRegistry
import kotlinx.coroutines.launch

/**
 * WebView 展示 Activity，是 `webview_interaction` 功能的核心入口。
 * 负责加载指定 URL，配置 WebView 安全策略，并注册 [WebViewBridge] 供 JavaScript 调用。
 *
 * 隐性依赖：
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

    /**
     * Activity 创建时的回调，初始化视图、配置 WebView 并加载 URL。
     *
     * @param savedInstanceState 保存的实例状态（可为 null）
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.webView)
        webViewBridge = WebViewBridge(this, webView)

        configureWebView()
        loadUrl()

        //启动辅助功能
        lifecycleScope.launch {
            AuxiliaryRegistry.initializeAll(this@WebViewActivity)
        }

        // 处理返回事件（兼容手势返回和物理返回键）
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                /**
                 * 处理返回键/手势：如果 WebView 可以后退则后退，否则关闭 Activity。
                 */
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
     * - 启用 JavaScript 和 DOM 存储。
     * - 禁用文件访问和混合内容。
     * - 设置 WebViewClient 以监听页面加载和渲染进程崩溃。
     * - 添加 JS 桥接接口并移除危险接口。
     */
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

            /**
             * 处理 WebView 渲染进程崩溃（模拟器常见问题）。
             * @param view 发生崩溃的 WebView
             * @param detail 崩溃详情
             * @return true 表示已处理，Activity 将关闭
             */
            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {
                Log.e("WebViewActivity", "Render process gone, detail: $detail")
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

    /**
     * 加载目标 URL。
     * URL 优先从 Intent 的 "url" 附加数据中获取，否则使用默认本地测试地址。
     */
    private fun loadUrl() {
        val url = intent.getStringExtra("url") ?: "http://localhost:8080"
        webView.loadUrl(url)
    }

    /**
     * Activity 销毁时释放 WebView 资源。
     */
    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

    companion object {
        // 存储辅助功能初始化回调，key 为唯一标识，value 为接收 Activity 实例的挂起函数
        val auxiliaryHooks = mutableMapOf<String, suspend (WebViewActivity) -> Unit>()
    }
}