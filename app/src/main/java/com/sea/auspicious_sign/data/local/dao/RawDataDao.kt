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

    /**
     * 分批查询未同步的原始数据
     * @param limit 每批最大记录数
     * @return Flow 发射包含最多 [limit] 条记录的列表，当数据库变化时会重新查询
     */
    @Query("SELECT * FROM raw_data WHERE synced = 0 ORDER BY timestamp ASC LIMIT :limit")
    fun getUnsyncedBatch(limit: Int): Flow<List<RawDataEntity>>
}