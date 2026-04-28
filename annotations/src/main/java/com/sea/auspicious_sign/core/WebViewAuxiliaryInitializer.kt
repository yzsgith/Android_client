package com.sea.auspicious_sign.core




/**
 * 辅助功能初始化接口，所有被 [WebViewAuxiliary] 标记的类必须实现此接口。
 */
interface WebViewAuxiliaryInitializer {
    /**
     * 在 WebViewActivity 创建时被调用，用于执行辅助功能的初始化逻辑。
     * @param activity 目标 Activity（通常为 WebViewActivity）
     */
    suspend fun initialize(activity: Any)
}
