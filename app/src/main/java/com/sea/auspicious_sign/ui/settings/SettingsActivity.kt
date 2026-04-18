// TODO: 作用 -- 提供设置界面，允许用户修改应用配置（API地址、上传间隔等）
package com.sea.auspicious_sign.ui.settings

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewmodel.compose.viewModel

import com.sea.auspicious_sign.ui.theme.Auspicious_signTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Auspicious_signTheme {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(LocalContext.current.applicationContext  as Application))
) {
    val apiUrl by viewModel.apiUrlFlow.collectAsState(initial = "")
    val uploadInterval by viewModel.uploadIntervalFlow.collectAsState(initial = 15)
    val enableHeartRate by viewModel.enableHeartRateFlow.collectAsState(initial = true)
    val enableAccel by viewModel.enableAccelFlow.collectAsState(initial = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("应用设置", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = apiUrl,
            onValueChange = { viewModel.updateApiBaseUrl(it) },
            label = { Text("服务器地址") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("上传间隔（分钟）：$uploadInterval", modifier = Modifier.weight(1f))
            Slider(
                value = uploadInterval.toFloat(),
                onValueChange = { viewModel.updateUploadInterval(it.toInt()) },
                valueRange = 5f..60f,
                steps = 10,
                modifier = Modifier.weight(2f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("启用心率传感器", modifier = Modifier.weight(1f))
            Switch(
                checked = enableHeartRate,
                onCheckedChange = { viewModel.updateEnableHeartRate(it) }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("启用加速度计", modifier = Modifier.weight(1f))
            Switch(
                checked = enableAccel,
                onCheckedChange = { viewModel.updateEnableAccel(it) }
            )
        }

        Button(
            onClick = { viewModel.resetToDefaults() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("恢复默认设置")
        }
    }
}
