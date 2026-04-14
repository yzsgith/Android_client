package com.sea.auspicious_sign.data.local.dao

import androidx.room.*
import com.sea.auspicious_sign.data.local.entity.RawDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RawDataDao {
    @Insert
    suspend fun insert(vararg rawData: RawDataEntity)

    @Query("SELECT * FROM raw_data WHERE id = :id")
    suspend fun getById(id: String): RawDataEntity?

    @Query("SELECT * FROM raw_data WHERE synced = 0 ORDER BY timestamp ASC")
    fun getUnsynced(): Flow<List<RawDataEntity>>
}