// TODO: 作用 -- 定义所有配置项的键、默认值，并提供 DataStore 读写挂起函数和 Flow 观察
package com.sea.auspicious_sign.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

object AppPreferences {
    // 配置键
    val API_BASE_URL = stringPreferencesKey("api_base_url")
    val UPLOAD_INTERVAL = intPreferencesKey("upload_interval")
    val ENABLE_HEART_RATE = booleanPreferencesKey("enable_heart_rate")
    val ENABLE_ACCEL = booleanPreferencesKey("enable_accel")

    // 默认值（公开，供重置使用）
    const val DEFAULT_API_BASE_URL = "http://localhost:8888"
    const val DEFAULT_UPLOAD_INTERVAL = 15
    const val DEFAULT_ENABLE_HEART_RATE = true
    const val DEFAULT_ENABLE_ACCEL = true

    // ---------- Flow 方法（供 UI 观察） ----------
    fun getApiBaseUrlFlow(dataStore: DataStore<Preferences>): Flow<String> =
        dataStore.data.map { it[API_BASE_URL] ?: DEFAULT_API_BASE_URL }

    fun getUploadIntervalFlow(dataStore: DataStore<Preferences>): Flow<Int> =
        dataStore.data.map { it[UPLOAD_INTERVAL] ?: DEFAULT_UPLOAD_INTERVAL }

    fun getEnableHeartRateFlow(dataStore: DataStore<Preferences>): Flow<Boolean> =
        dataStore.data.map { it[ENABLE_HEART_RATE] ?: DEFAULT_ENABLE_HEART_RATE }

    fun getEnableAccelFlow(dataStore: DataStore<Preferences>): Flow<Boolean> =
        dataStore.data.map { it[ENABLE_ACCEL] ?: DEFAULT_ENABLE_ACCEL }

    // ---------- 挂起函数（单次读取） ----------
    suspend fun getApiBaseUrl(dataStore: DataStore<Preferences>): String =
        dataStore.data.map { it[API_BASE_URL] ?: DEFAULT_API_BASE_URL }.first()

    suspend fun getUploadInterval(dataStore: DataStore<Preferences>): Int =
        dataStore.data.map { it[UPLOAD_INTERVAL] ?: DEFAULT_UPLOAD_INTERVAL }.first()

    suspend fun isHeartRateEnabled(dataStore: DataStore<Preferences>): Boolean =
        dataStore.data.map { it[ENABLE_HEART_RATE] ?: DEFAULT_ENABLE_HEART_RATE }.first()

    suspend fun isAccelEnabled(dataStore: DataStore<Preferences>): Boolean =
        dataStore.data.map { it[ENABLE_ACCEL] ?: DEFAULT_ENABLE_ACCEL }.first()

    // ---------- 写入挂起函数 ----------
    suspend fun setApiBaseUrl(dataStore: DataStore<Preferences>, url: String) {
        dataStore.edit { it[API_BASE_URL] = url }
    }

    suspend fun setUploadInterval(dataStore: DataStore<Preferences>, interval: Int) {
        dataStore.edit { it[UPLOAD_INTERVAL] = interval }
    }

    suspend fun setHeartRateEnabled(dataStore: DataStore<Preferences>, enabled: Boolean) {
        dataStore.edit { it[ENABLE_HEART_RATE] = enabled }
    }

    suspend fun setAccelEnabled(dataStore: DataStore<Preferences>, enabled: Boolean) {
        dataStore.edit { it[ENABLE_ACCEL] = enabled }
    }
}