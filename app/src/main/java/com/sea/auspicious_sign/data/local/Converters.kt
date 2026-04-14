package com.sea.auspicious_sign.data.local

import androidx.room.TypeConverter
import com.sea.auspicious_sign.sync.SyncStatus

class Converters {
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): Int = status.ordinal

    @TypeConverter
    fun toSyncStatus(value: Int): SyncStatus = SyncStatus.values()[value]
}
