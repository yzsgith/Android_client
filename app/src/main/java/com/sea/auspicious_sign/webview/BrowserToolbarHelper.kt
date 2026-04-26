// TODO: 作用 -- 为 WebViewActivity 提供浏览器工具栏功能（后退、前进、地址栏、退出等），使用单例缓存
package com.sea.auspicious_sign.webview

import android.util.Log
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sea.auspicious_sign.R

/**
 * 浏览器工具栏辅助类，封装所有 UI 控件和交互逻辑。
 */
class BrowserToolbarHelper(private val activity: WebViewActivity) {
    private val webView: WebView = activity.findViewById(R.id.webView)
    private val backButton: ImageButton = activity.findViewById(R.id.backButton)
    private val forwardButton: ImageButton = activity.findViewById(R.id.forwardButton)
    private val goButton: Button = activity.findViewById(R.id.goButton)
    private val exitButton: Button = activity.findViewById(R.id.exitButton)
    private val urlEditText: EditText = activity.findViewById(R.id.urlEditText)

    init {
        setupButtons()
        setupUrlInput()
        updateButtonsState()
    }

    private fun setupButtons() {
        backButton.setOnClickListener {
            Toast.makeText(activity, "后退按钮被点击", Toast.LENGTH_SHORT).show()
            Log.i("event1","backButton")
            if (webView.canGoBack()) webView.goBack() }
        forwardButton.setOnClickListener {
            Toast.makeText(activity, "前进按钮被点击", Toast.LENGTH_SHORT).show()
            Log.i("event1","forwardButton")
            if (webView.canGoForward()) webView.goForward() }
        goButton.setOnClickListener {
            Toast.makeText(activity, "执行按钮被点击", Toast.LENGTH_SHORT).show()
            Log.i("event1","goButton")
            loadUrlFromInput() }
        exitButton.setOnClickListener {
            Toast.makeText(activity, "退出按钮被点击", Toast.LENGTH_SHORT).show()
            Log.i("event1","exitButton")
            activity.finish() }
    }

    private fun setupUrlInput() {
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrlFromInput()
                true
            } else false
        }
    }

    private fun loadUrlFromInput() {
        var url = urlEditText.text.toString().trim()
        if (url.isEmpty()) return
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        webView.loadUrl(url)
    }

    private fun updateButtonsState() {
        backButton.isEnabled = webView.canGoBack()
        forwardButton.isEnabled = webView.canGoForward()
    }

    fun onPageStarted(url: String?) {
        url?.let { urlEditText.setText(it) }
        updateButtonsState()
    }

    fun onPageFinished() {
        updateButtonsState()
    }
}

// 缓存单例，避免重复创建
private val toolbarCache = mutableMapOf<WebViewActivity, BrowserToolbarHelper>()

/**
 * 为 WebViewActivity 初始化浏览器工具栏（一行调用）。后续可通过 onPageStarted/onPageFinished 更新状态。
 */
fun WebViewActivity.initToolbar(): BrowserToolbarHelper {
    return toolbarCache.getOrPut(this) {
        BrowserToolbarHelper(this)
    }
}