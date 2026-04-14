package com.sea.auspicious_sign.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transformed_data")
data class TransformedDataEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "raw_data_id")
    val rawDataId: String,
    val type: String,
    @ColumnInfo(name = "result_json")
    val resultJson: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)