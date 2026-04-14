package com.sea.auspicious_sign.data.local.dao

import androidx.room.*
import com.sea.auspicious_sign.data.local.entity.ConsumableDataEntity

@Dao
interface ConsumableDataDao {
    @Insert
    suspend fun insert(vararg data: ConsumableDataEntity)

    @Query("SELECT * FROM consumable_data WHERE synced = 0")
    suspend fun getUnsynced(): List<ConsumableDataEntity>

    @Query("UPDATE consumable_data SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("SELECT * FROM consumable_data WHERE transform_id = :transformId")
    suspend fun getByTransformId(transformId: String): ConsumableDataEntity?
}
