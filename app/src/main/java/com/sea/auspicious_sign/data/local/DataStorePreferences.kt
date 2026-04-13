package com.sea.auspicious_sign.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class DataStorePreferences(private val context: Context) {
    companion object {
        val AUTO_UPLOAD_KEY = booleanPreferencesKey("auto_upload")
    }

    val autoUploadFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[AUTO_UPLOAD_KEY] ?: true }

    suspend fun setAutoUpload(enabled: Boolean) {
        context.dataStore.edit { settings ->
            settings[AUTO_UPLOAD_KEY] = enabled
        }
    }
}