package com.sea.auspicious_sign.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.sea.auspicious_sign.data.local.dao.*
import com.sea.auspicious_sign.data.local.entity.*

@Database(
    entities = [
        RawDataEntity::class,
        TransformedDataEntity::class,
        ConsumableDataEntity::class,
        SyncQueueEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rawDataDao(): RawDataDao
    abstract fun transformedDataDao(): TransformedDataDao
    abstract fun consumableDataDao(): ConsumableDataDao
    abstract fun syncQueueDao(): SyncQueueDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
