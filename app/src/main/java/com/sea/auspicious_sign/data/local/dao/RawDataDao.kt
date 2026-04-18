package com.sea.auspicious_sign.data.local.dao

import androidx.room.*
import com.sea.auspicious_sign.data.local.entity.RawDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RawDataDao {
    @Insert
    suspend fun insert(vararg rawData: RawDataEntity)

    @Update
    suspend fun update(vararg rawData: RawDataEntity)   // 添加这一行

    @Query("SELECT * FROM raw_data WHERE id = :id")
    suspend fun getById(id: String): RawDataEntity?

    @Query("SELECT * FROM raw_data WHERE synced = 0 ORDER BY timestamp ASC")
    fun getUnsynced(): Flow<List<RawDataEntity>>

    @Query("DELETE FROM raw_data WHERE id = :id")
    suspend fun deleteById(id: String)
    // RawDataDao.kt
    @Query("SELECT * FROM raw_data WHERE synced = 0 ORDER BY timestamp ASC LIMIT :limit")
    fun getUnsyncedBatch(limit: Int): Flow<List<RawDataEntity>>
}