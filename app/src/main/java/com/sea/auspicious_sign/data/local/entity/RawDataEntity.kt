package com.sea.auspicious_sign.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sea.auspicious_sign.domain.model.RawData

@Entity(tableName = "raw_data")
data class RawDataEntity(
    @PrimaryKey
    val id: String,
    val type: String,          // 数据类型标识，如 "heart_rate", "image"
    val timestamp: Long,
    val dataJson: String,      // 原始数据的 JSON 表示
    val blobKey: String? = null // 若存于 Netlify Blobs，则记录 key
) : RawData