package com.sea.auspicious_sign.annotations

/**
 * 标记一个类是 WebView 交互功能的辅助组件（如工具栏、下载管理器等）。
 * 被此注解标记的类必须实现 [WebViewAuxiliaryInitializer] 接口。
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class WebViewAuxiliary
