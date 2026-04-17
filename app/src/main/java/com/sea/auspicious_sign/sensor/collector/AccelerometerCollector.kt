package com.sea.auspicious_sign.sensor.collector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sea.auspicious_sign.domain.model.RawAccelerometer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Random

class AccelerometerCollector(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val dataChannel = Channel<RawAccelerometer>(Channel.UNLIMITED)
    val accelerometerFlow = dataChannel.receiveAsFlow()
    private val random = Random()
    private var isSimulating = false
    private var simulationJob: kotlinx.coroutines.Job? = null

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val raw = RawAccelerometer(event.values[0], event.values[1], event.values[2], System.currentTimeMillis())
                dataChannel.trySend(raw)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun start() {
        if (accelerometer != null) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            startSimulation()
        }
    }

    private fun startSimulation() {
        if (isSimulating) return
        isSimulating = true
        simulationJob = CoroutineScope(Dispatchers.IO).launch {
            while (isSimulating) {
                val x = random.nextFloat() * 4 - 2
                val y = random.nextFloat() * 4 - 2
                val z = random.nextFloat() * 4 - 2
                val raw = RawAccelerometer(x, y, z, System.currentTimeMillis())
                dataChannel.send(raw)
                delay(200)
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