package com.sea.auspicious_sign.domain.model

/**
 * 原始心率数据
 * @param bpm 每分钟心跳数
 * @param confidence 置信度 (0.0 ~ 1.0)
 * @param timestamp 时间戳（毫秒）
 */
data class RawHeartRate(
    val bpm: Int,
    val confidence: Float,
    val timestamp: Long
) : RawData

/**
 * 原始加速度计数据
 * @param x X轴加速度 (m/s²)
 * @param y Y轴加速度
 * @param z Z轴加速度
 * @param timestamp 时间戳
 */
data class RawAccelerometer(
    val x: Float,
    val y: Float,
    val z: Float,
    val timestamp: Long
) : RawData

/**
 * 平滑后的心率数据（转化阶段）
 * @param bpm 平滑后的心率值
 * @param isValid 是否有效（基于置信度）
 * @param timestamp 时间戳
 */
data class SmoothedHeartRate(
    val bpm: Int,
    val isValid: Boolean,
    val timestamp: Long
) : TransformedData

/**
 * 心率告警数据（消费阶段）
 * @param level 告警等级
 * @param message 描述信息
 * @param timestamp 时间戳
 */
data class HeartRateAlert(
    val level: AlertLevel,
    val message: String,
    val timestamp: Long
) : ConsumableData

/**
 * 步数统计（消费阶段）
 * @param steps 步数
 * @param timestamp 统计时间
 */
data class StepCount(
    val steps: Int,
    val timestamp: Long
) : ConsumableData

enum class AlertLevel {
    NORMAL, WARNING, CRITICAL
}