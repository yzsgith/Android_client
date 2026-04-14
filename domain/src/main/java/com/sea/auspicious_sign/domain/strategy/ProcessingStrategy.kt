package com.sea.auspicious_sign.domain.strategy

import com.sea.auspicious_sign.domain.model.RawData

enum class StorageTarget {
    LOCAL_ONLY,   // 仅存本地
    REMOTE_ONLY,  // 仅存远程
    BOTH          // 双写
}

interface ProcessingStrategy {
    fun shouldProcessLocally(data: RawData): Boolean
    fun getStorageTarget(data: RawData): StorageTarget
}
