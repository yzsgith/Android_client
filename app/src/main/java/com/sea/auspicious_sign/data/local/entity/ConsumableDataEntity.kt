package com.sea.auspicious_sign.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sea.auspicious_sign.domain.model.ConsumableData
import com.sea.auspicious_sign.domain.model.HeartRateAlert
import com.sea.auspicious_sign.domain.model.StepCount
import com.sea.auspicious_sign.sync.SyncStatus
import com.google.gson.Gson

@Entity(tableName = "consumable_data")
data class ConsumableDataEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "transform_id")
    val transformId: String? = null,
    @ColumnInfo(name = "raw_data_id")
    val rawDataId: String? = null,
    val type: String,
    @ColumnInfo(name = "result_json")
    val resultJson: String,
    val synced: Boolean = false,
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
) {
    fun toDomain(): ConsumableData? {
        return when (type) {
            "heart_rate_alert" -> Gson().fromJson(resultJson, HeartRateAlert::class.java)
            "step_count" -> Gson().fromJson(resultJson, StepCount::class.java)
            else -> null
        }
    }

    companion object {
        fun fromDomain(
            transformId: String? = null,
            rawDataId: String? = null,
            consumable: ConsumableData
        ): ConsumableDataEntity {
            val type = when (consumable) {
                is HeartRateAlert -> "heart_rate_alert"
                is StepCount -> "step_count"
                else -> "unknown"
            }
            val json = Gson().toJson(consumable)
            return ConsumableDataEntity(
                id = java.util.UUID.randomUUID().toString(),
                transformId = transformId,
                rawDataId = rawDataId,
                type = type,
                resultJson = json,
                createdAt = System.currentTimeMillis()
            )
        }
    }
}