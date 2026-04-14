package com.sea.auspicious_sign.data.local.dao

import androidx.room.*
import com.sea.auspicious_sign.data.local.entity.TransformedDataEntity

@Dao
interface TransformedDataDao {
    @Insert
    suspend fun insert(vararg data: TransformedDataEntity)

    @Query("SELECT * FROM transformed_data WHERE raw_data_id = :rawDataId")
    suspend fun getByRawId(rawDataId: String): List<TransformedDataEntity>
}