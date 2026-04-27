// TODO: 作用 -- 为 WebViewActivity 提供启用浏览器工具栏的扩展函数
package com.sea.auspicious_sign.features.webview_interaction.auxiliary.toolbar

import com.sea.auspicious_sign.R
import com.sea.auspicious_sign.features.webview_interaction.core.WebViewActivity

private val toolbarCache = mutableMapOf<WebViewActivity, BrowserToolbarHelper>()

/**
 * 为 [WebViewActivity] 启用浏览器工具栏（后退、前进、地址栏、退出按钮）。
 * 该扩展函数是辅助功能，可独立插拔，不修改核心类。
 *
 * 使用示例：
 * ```
 * class WebViewActivity : AppCompatActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         // ... 初始化 WebView
 *         enableToolbar()  // 启用工具栏
 *         // 需要在 WebViewClient 中手动调用 helper 的 onPageStarted/Finished 以同步状态
 *     }
 * }
 * ```
 *
 * @receiver WebViewActivity 实例
 */
fun WebViewActivity.enableToolbar() {
    toolbarCache.getOrPut(this) {
        BrowserToolbarHelper(this, findViewById(R.id.webView))
    }
    // 完整集成还需要在 WebViewClient 的生命周期中调用 helper 的 onPageStarted/onPageFinished
}
