package com.sea.auspicious_sign.domain.strategy

import com.sea.auspicious_sign.domain.model.RawData

/**
 * 存储目标枚举
 */
enum class StorageTarget {
    LOCAL_ONLY,      // 仅存本地
    REMOTE_ONLY,     // 仅存远程
    BOTH             // 双写
}

/**
 * 处理策略接口，用于决定是否本地处理、存储目标等
 */
interface ProcessingStrategy {
    /**
     * 判断是否应该本地处理该数据
     * @param data 原始数据
     * @return true 表示本地处理，false 表示直接存储或上传云端处理
     */
    fun shouldProcessLocally(data: RawData): Boolean

    /**
     * 获取处理后的存储目标
     * @param data 原始数据（或处理后的数据）
     * @return 存储目标
     */
    fun getStorageTarget(data: RawData): StorageTarget
}
