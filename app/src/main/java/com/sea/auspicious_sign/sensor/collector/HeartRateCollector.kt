package com.sea.auspicious_sign.sensor.collector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sea.auspicious_sign.domain.model.RawHeartRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Random

class HeartRateCollector(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var heartRateSensor: Sensor? = null
    private val dataChannel = Channel<RawHeartRate>(Channel.UNLIMITED)
    val heartRateFlow = dataChannel.receiveAsFlow()
    private val random = Random()
    private var isSimulating = false
    private var simulationJob: kotlinx.coroutines.Job? = null

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                val bpm = event.values[0].toInt()
                val raw = RawHeartRate(bpm, 1.0f, System.currentTimeMillis())
                dataChannel.trySend(raw)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun start() {
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor != null) {
            sensorManager.registerListener(listener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            startSimulation()
        }
    }

    private fun startSimulation() {
        if (isSimulating) return
        isSimulating = true
        simulationJob = CoroutineScope(Dispatchers.IO).launch {
            while (isSimulating) {
                val bpm = 60 + random.nextInt(41)
                val raw = RawHeartRate(bpm, 0.9f, System.currentTimeMillis())
                dataChannel.send(raw)
                delay(1000)
            }
        }
    }

    fun stop() {
        isSimulating = false
        simulationJob?.cancel()
        sensorManager.unregisterListener(listener)
        dataChannel.close()
    }
}