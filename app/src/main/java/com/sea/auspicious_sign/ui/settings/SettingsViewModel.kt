// TODO: 作用 -- 设置界面的 ViewModel，负责读取和更新 DataStore 中的配置项
package com.sea.auspicious_sign.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sea.auspicious_sign.utils.AppPreferences
import com.sea.auspicious_sign.utils.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    // 公开 Flow 供 UI 观察（自动从 DataStore 读取）
    val apiUrlFlow = AppPreferences.getApiBaseUrlFlow(dataStore)
    val uploadIntervalFlow = AppPreferences.getUploadIntervalFlow(dataStore)
    val enableHeartRateFlow = AppPreferences.getEnableHeartRateFlow(dataStore)
    val enableAccelFlow = AppPreferences.getEnableAccelFlow(dataStore)

    // 本地状态（例如用于 Slider 的即时更新，可选）
    private val _uploadInterval = MutableStateFlow(AppPreferences.DEFAULT_UPLOAD_INTERVAL)
    val uploadInterval: StateFlow<Int> = _uploadInterval.asStateFlow()

    init {
        viewModelScope.launch {
            _uploadInterval.value = AppPreferences.getUploadInterval(dataStore)
        }
    }

    fun updateApiBaseUrl(newUrl: String) {
        viewModelScope.launch {
            AppPreferences.setApiBaseUrl(dataStore, newUrl)
        }
    }

    fun updateUploadInterval(interval: Int) {
        viewModelScope.launch {
            AppPreferences.setUploadInterval(dataStore, interval)
            _uploadInterval.value = interval
        }
    }

    fun updateEnableHeartRate(enabled: Boolean) {
        viewModelScope.launch {
            AppPreferences.setHeartRateEnabled(dataStore, enabled)
        }
    }

    fun updateEnableAccel(enabled: Boolean) {
        viewModelScope.launch {
            AppPreferences.setAccelEnabled(dataStore, enabled)
        }
    }

    fun resetToDefaults() {
        viewModelScope.launch {
            AppPreferences.setApiBaseUrl(dataStore, AppPreferences.DEFAULT_API_BASE_URL)
            AppPreferences.setUploadInterval(dataStore, AppPreferences.DEFAULT_UPLOAD_INTERVAL)
            AppPreferences.setHeartRateEnabled(dataStore, AppPreferences.DEFAULT_ENABLE_HEART_RATE)
            AppPreferences.setAccelEnabled(dataStore, AppPreferences.DEFAULT_ENABLE_ACCEL)
            _uploadInterval.value = AppPreferences.DEFAULT_UPLOAD_INTERVAL
        }
    }
}