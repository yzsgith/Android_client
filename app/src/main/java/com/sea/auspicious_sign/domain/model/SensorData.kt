package com.sea.auspicious_sign.domain.model

/**
 * 心率原始数据
 */
data class RawHeartRate(
    val bpm: Int,
    val confidence: Float,
    val timestamp: Long
) : RawData

/**
 * 加速度计原始数据
 */
data class RawAccelerometer(
    val x: Float,
    val y: Float,
    val z: Float,
    val timestamp: Long
) : RawData

/**
 * 平滑后的心率数据（转化数据）
 */
data class SmoothedHeartRate(
    val bpm: Int,
    val isValid: Boolean,
    val timestamp: Long
) : TransformedData

/**
 * 心率告警数据（消费数据）
 */
data class HeartRateAlert(
    val level: AlertLevel,
    val message: String,
    val timestamp: Long
) : ConsumableData

enum class AlertLevel {
    NORMAL, WARNING, CRITICAL
}

/**
 * 步数统计（消费数据）
 */
data class StepCount(
    val steps: Int,
    val startTime: Long,
    val endTime: Long
) : ConsumableData
