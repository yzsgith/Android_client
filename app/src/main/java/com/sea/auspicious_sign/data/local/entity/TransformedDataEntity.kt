package com.sea.auspicious_sign.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sea.auspicious_sign.domain.model.TransformedData

@Entity(tableName = "transformed_data")
data class TransformedDataEntity(
    @PrimaryKey
    val id: String,
    val rawDataId: String,    // 关联的原始数据 ID
    val type: String,
    val resultJson: String,
    val createdAt: Long
) : TransformedData