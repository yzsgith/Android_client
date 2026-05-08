// TODO: 作用 -- 浏览器工具栏辅助类，封装后退、前进、地址栏、退出等交互逻辑
package com.sea.auspicious_sign.features.webview_interaction.auxiliary.toolbar

import android.util.Log
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sea.auspicious_sign.R

/**
 * 浏览器工具栏辅助类，负责绑定布局中的控件并处理用户交互。
 * 该类不修改核心 Activity 逻辑，通过组合方式提供可选增强功能。
 *
 * @param activity 宿主 Activity（需包含对应 ID 的控件）
 * @param webView 关联的 WebView 实例，用于页面导航
 */
class BrowserToolbarHelper(private val activity: AppCompatActivity, private val webView: WebView) {
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

    /**
     * 设置后退、前进、前往、退出按钮的点击监听。
     */
    private fun setupButtons() {
        backButton.setOnClickListener { if (webView.canGoBack()) webView.goBack() }
        forwardButton.setOnClickListener { if (webView.canGoForward()) webView.goForward() }
        goButton.setOnClickListener { loadUrlFromInput() }
        exitButton.setOnClickListener { activity.finish() }
    }

    /**
     * 设置地址栏的软键盘“前往”动作监听。
     */
    private fun setupUrlInput() {
        Log.i("testEvent","go in setupUrlInput1")
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrlFromInput()
                true
            } else false
        }
    }

    /**
     * 从地址栏获取用户输入的 URL，规范格式后加载到 WebView。
     */
    private fun loadUrlFromInput() {
        Log.i("testEvent","go in loadUrlFromInput2")
        var url = urlEditText.text.toString().trim()
        if (url.isEmpty()) return
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        webView.loadUrl(url)
    }

    /**
     * 根据 WebView 当前的导航历史，更新后退/前进按钮的启用状态。
     */
    private fun updateButtonsState() {
        Log.i("testEvent","go in updateButtonsState3")
        backButton.isEnabled = webView.canGoBack()
        forwardButton.isEnabled = webView.canGoForward()
    }

    /**
     * 当页面开始加载时调用，更新地址栏文本和按钮状态。
     * @param url 正在加载的 URL
     */
    fun onPageStarted(url: String?) {
        Log.i("testEvent","go in onPageStarted4")
        url?.let { urlEditText.setText(it) }
        updateButtonsState()
    }

    /**
     * 当页面加载完成时调用，更新按钮状态。
     */
    fun onPageFinished() {
        Log.i("testEvent","go in onPageFinished5")
        updateButtonsState()
    }
}
