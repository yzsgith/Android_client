package com.sea.auspicious_sign.sensor.collector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sea.auspicious_sign.domain.model.RawHeartRate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class HeartRateCollector(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var heartRateSensor: Sensor? = null
    private val dataChannel = Channel<RawHeartRate>(Channel.UNLIMITED)
    val heartRateFlow = dataChannel.receiveAsFlow()

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                val bpm = event.values[0].toInt()
                val confidence = 1.0f // 实际传感器不提供置信度，可估算
                val raw = RawHeartRate(bpm, confidence, System.currentTimeMillis())
                dataChannel.trySend(raw)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // TODO: 处理精度变化
        }
    }

    fun start() {
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor != null) {
            sensorManager.registerListener(listener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            // TODO: 模拟数据 fallback 或提示无传感器
            startSimulation()
        }
    }

    private fun startSimulation() {
        // TODO: 定期生成模拟心率数据
    }

    fun stop() {
        sensorManager.unregisterListener(listener)
        dataChannel.close()
    }
}
