package com.sea.auspicious_sign.domain.repository

import android.content.Context
import com.sea.auspicious_sign.data.local.AppDatabase
import com.sea.auspicious_sign.data.local.entity.ConsumableDataEntity
import com.sea.auspicious_sign.data.local.entity.RawDataEntity
import com.sea.auspicious_sign.data.local.entity.TransformedDataEntity
import com.sea.auspicious_sign.domain.model.RawData
import com.sea.auspicious_sign.domain.processor.ProcessorRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataProcessRepository(private val context: Context) {
    private val database = AppDatabase.getInstance(context)

    suspend fun processRawData(raw: RawData) = withContext(Dispatchers.IO) {
        // 1. 存储原始数据
        val rawEntity = RawDataEntity.fromDomain(raw)   // 修正：fromRaw → fromDomain
        database.rawDataDao().insert(rawEntity)

        // 2. 获取对应的处理器链
        val chain = ProcessorRegistry.getChain(raw::class)
        if (chain == null) {
            // 无处理器，直接标记为消费数据（原始数据即为最终数据）
            val consumable = ConsumableDataEntity.fromDomain(
                rawDataId = rawEntity.id,
                consumable = raw as? com.sea.auspicious_sign.domain.model.ConsumableData
                    ?: error("Raw data does not implement ConsumableData and no processor found")
            )
            database.consumableDataDao().insert(consumable)
            return@withContext
        }

        // 3. 执行处理链
        val result = chain.execute(raw)

        // 4. 存储转化数据和消费数据（根据 result 类型）
        when (result) {
            is TransformedDataEntity -> database.transformedDataDao().insert(result)
            is ConsumableDataEntity -> database.consumableDataDao().insert(result)
            else -> {
                // 如果 result 是领域模型，需要转换为 Entity
                // TODO: 完善类型转换
            }
        }
    }
}