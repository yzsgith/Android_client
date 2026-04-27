package com.sea.auspicious_sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sea.auspicious_sign.features.webview_interaction.core.WebViewActivity
import com.sea.auspicious_sign.sensor.SensorDataRepository
import com.sea.auspicious_sign.sensor.collector.AccelerometerCollector
import com.sea.auspicious_sign.sensor.collector.HeartRateCollector
import com.sea.auspicious_sign.sensor.upload.SensorUploadWorker
import com.sea.auspicious_sign.ui.settings.SettingsActivity
import com.sea.auspicious_sign.ui.theme.Auspicious_signTheme

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var heartRateCollector: HeartRateCollector
    private lateinit var accelerometerCollector: AccelerometerCollector
    private lateinit var sensorRepository: SensorDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorRepository = SensorDataRepository(this)
        heartRateCollector = HeartRateCollector(this)
        accelerometerCollector = AccelerometerCollector(this)

        // 启动采集器（阶段二测试）
        heartRateCollector.start()
        accelerometerCollector.start()

        // 收集数据并存入 Room
        lifecycleScope.launch {
            heartRateCollector.heartRateFlow.collect { raw ->
                sensorRepository.insertRawData(raw)
                Log.d("Sensor", "Heart rate saved: ${raw.bpm}")
            }
        }
        lifecycleScope.launch {
            accelerometerCollector.accelerometerFlow.collect { raw ->
                sensorRepository.insertRawData(raw)
                Log.d("Sensor", "Accelerometer saved: ${raw.x}, ${raw.y}, ${raw.z}")
            }
        }

        setContent {
            Auspicious_signTheme {
                MainScreen(onOpenWebView = {
                    startActivity(Intent(this, WebViewActivity::class.java))
                },
                    onTestUpload = {
                        val uploadRequest = OneTimeWorkRequestBuilder<SensorUploadWorker>()
                            .setConstraints(Constraints.NONE)
                            .build()
                        WorkManager.getInstance(this).enqueue(uploadRequest)
                        Log.d("MainActivity", "Test upload work enqueued")
                    }  ,
                    onOpenSettings = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        heartRateCollector.stop()
        accelerometerCollector.stop()
    }
}

@Composable
fun MainScreen(
    onOpenWebView: () -> Unit,
    onTestUpload: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = onOpenWebView) {
                Text("打开 WebView 测试页面")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onTestUpload) {
                Text("测试上传数据")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onOpenSettings) {
                Text("设置")
            }
        }
    }
}