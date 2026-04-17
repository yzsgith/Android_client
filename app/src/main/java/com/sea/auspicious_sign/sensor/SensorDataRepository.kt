package com.sea.auspicious_sign.sensor

import android.content.Context
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.data.local.entity.RawDataEntity
import com.sea.auspicious_sign.domain.model.RawData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SensorDataRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)

    suspend fun insertRawData(raw: RawData) = withContext(Dispatchers.IO) {
        val entity = RawDataEntity.fromDomain(raw)
        database.rawDataDao().insert(entity)
    }

    suspend fun getUnsyncedData(): List<RawDataEntity> = withContext(Dispatchers.IO) {
        // getUnsynced() 返回 Flow<List<RawDataEntity>>，使用 first() 获取当前值
        database.rawDataDao().getUnsynced().first()
    }


}