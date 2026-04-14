package com.sea.auspicious_sign.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sea.auspicious_sign.sync.SyncStatus

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "consumable_data_id")
    val consumableDataId: String,
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus,
    @ColumnInfo(name = "retry_count")
    val retryCount: Int = 0,
    @ColumnInfo(name = "last_attempt_time")
    val lastAttemptTime: Long? = null,
    @ColumnInfo(name = "error_message")
    val errorMessage: String? = null
)