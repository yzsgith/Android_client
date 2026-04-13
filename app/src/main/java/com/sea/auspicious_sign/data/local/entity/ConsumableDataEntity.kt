package com.sea.auspicious_sign.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sea.auspicious_sign.domain.model.ConsumableData

@Entity(tableName = "consumable_data")
data class ConsumableDataEntity(
    @PrimaryKey
    val id: String,
    val transformId: String,  // 关联的转化数据 ID
    val type: String,
    val resultJson: String,
    val synced: Boolean = false // 是否已同步到云端
) : ConsumableData