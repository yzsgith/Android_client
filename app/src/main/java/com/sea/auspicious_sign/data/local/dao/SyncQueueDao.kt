package com.sea.auspicious_sign.data.local.dao

import androidx.room.*
import com.sea.auspicious_sign.data.local.entity.SyncQueueEntity
import com.sea.auspicious_sign.sync.SyncStatus

@Dao
interface SyncQueueDao {
    @Insert
    suspend fun insert(queueItem: SyncQueueEntity)

    @Update
    suspend fun update(queueItem: SyncQueueEntity)

    @Query("SELECT * FROM sync_queue WHERE sync_status = :status ORDER BY retry_count ASC")
    suspend fun getByStatus(status: SyncStatus): List<SyncQueueEntity>

    @Query("DELETE FROM sync_queue WHERE sync_status = :status")
    suspend fun deleteByStatus(status: SyncStatus)

    // TODO: 添加重试次数更新等
}