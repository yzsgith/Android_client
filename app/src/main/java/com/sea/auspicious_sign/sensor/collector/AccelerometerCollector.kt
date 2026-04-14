package com.sea.auspicious_sign.sensor.collector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sea.auspicious_sign.domain.model.RawAccelerometer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class AccelerometerCollector(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val dataChannel = Channel<RawAccelerometer>(Channel.UNLIMITED)
    val accelerometerFlow = dataChannel.receiveAsFlow()

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val raw = RawAccelerometer(x, y, z, System.currentTimeMillis())
                dataChannel.trySend(raw)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // 无需处理
        }
    }

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(listener)
        dataChannel.close()
    }
}
