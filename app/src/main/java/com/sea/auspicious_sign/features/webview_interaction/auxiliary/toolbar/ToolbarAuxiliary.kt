package com.sea.auspicious_sign.features.webview_interaction.auxiliary.toolbar

import androidx.appcompat.app.AppCompatActivity
import com.sea.auspicious_sign.annotations.WebViewAuxiliary
import com.sea.auspicious_sign.core.WebViewAuxiliaryInitializer
import com.sea.auspicious_sign.features.webview_interaction.core.WebViewActivity

@WebViewAuxiliary
class ToolbarAuxiliary : WebViewAuxiliaryInitializer {
    override suspend fun initialize(activity: Any):Unit {
        // 假设 WebViewActivity 有一个扩展函数 enableToolbar()
        (activity as? WebViewActivity)?.enableToolbar()
    }


}