package com.sea.auspicious_sign.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sea.auspicious_sign.domain.model.RawData
import com.sea.auspicious_sign.domain.model.RawHeartRate
import com.sea.auspicious_sign.domain.model.RawAccelerometer
import com.google.gson.Gson

@Entity(tableName = "raw_data")
data class RawDataEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val timestamp: Long,
    val dataJson: String,
    val blobKey: String? = null,
    val synced: Boolean = false
) {
    fun toDomain(): RawData? {
        return when (type) {
            "heart_rate" -> Gson().fromJson(dataJson, RawHeartRate::class.java)
            "accelerometer" -> Gson().fromJson(dataJson, RawAccelerometer::class.java)
            else -> null
        }
    }

    companion object {
        fun fromDomain(raw: RawData): RawDataEntity {
            val type = when (raw) {
                is RawHeartRate -> "heart_rate"
                is RawAccelerometer -> "accelerometer"
                else -> "unknown"
            }
            val json = Gson().toJson(raw)
            return RawDataEntity(
                id = java.util.UUID.randomUUID().toString(),
                type = type,
                timestamp = System.currentTimeMillis(),
                dataJson = json,
                synced = false
            )
        }
    }
}