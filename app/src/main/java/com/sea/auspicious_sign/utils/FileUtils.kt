// FileUtils.kt
package com.sea.auspicious_sign.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun getSensorDataDir(context: Context): File {
        val dir = File(context.filesDir, "sensor_data")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    // TODO: 实现图片压缩、Base64 编解码、URI 转字节流等方法
    fun uriToBytes(context: Context, uri: Uri): ByteArray? {
        // TODO: 实现转换
        return null
    }
}