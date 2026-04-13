package com.sea.auspicious_sign.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey
    val id: String,
    val consumableDataId: String,
    val syncStatus: SyncStatus,
    val retryCount: Int = 0
)

enum class SyncStatus {
    PENDING, SUCCESS, FAILED
}
