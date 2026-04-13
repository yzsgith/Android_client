package com.sea.auspicious_sign.domain.processor.impl

import com.sea.auspicious_sign.domain.model.HeartRateAlert
import com.sea.auspicious_sign.domain.model.SmoothedHeartRate
import com.sea.auspicious_sign.domain.processor.DataProcessor

enum class AlertLevel { NORMAL, WARNING, CRITICAL }

data class HeartRateAlert(
    val level: AlertLevel,
    val message: String,
    val timestamp: Long
)

/**
 * 心率分析处理器：平滑心率 -> 告警等级
 */
class HeartRateAnalyzer : DataProcessor<SmoothedHeartRate, HeartRateAlert> {
    override suspend fun process(input: SmoothedHeartRate): HeartRateAlert {
        return when {
            input.bpm > 120 -> HeartRateAlert(AlertLevel.WARNING, "心率过高", input.timestamp)
            input.bpm < 50 -> HeartRateAlert(AlertLevel.WARNING, "心率过低", input.timestamp)
            else -> HeartRateAlert(AlertLevel.NORMAL, "正常", input.timestamp)
        }
    }
}