package com.sea.auspicious_sign.features.webview_interaction.auxiliary.toolbar

import com.sea.auspicious_sign.annotations.WebViewAuxiliary
import com.sea.auspicious_sign.core.WebViewAuxiliaryInitializer
import com.sea.auspicious_sign.features.webview_interaction.core.WebViewActivity

@WebViewAuxiliary
class ToolbarAuxiliary : WebViewAuxiliaryInitializer {
    override suspend fun initialize(activity: Any) {
        // 实际启用工具栏的代码
        (activity as? WebViewActivity)?.enableToolbar()
    }
}