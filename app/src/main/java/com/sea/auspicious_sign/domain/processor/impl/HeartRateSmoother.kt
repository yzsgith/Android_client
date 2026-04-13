package com.sea.auspicious_sign.domain.processor.impl

import com.sea.auspicious_sign.domain.model.RawHeartRate
import com.sea.auspicious_sign.domain.model.SmoothedHeartRate
import com.sea.auspicious_sign.domain.processor.DataProcessor

/**
 * 心率平滑处理器：原始心率 -> 平滑心率
 * TODO: 实现滑动平均或中值滤波算法
 */
class HeartRateSmoother : DataProcessor<RawHeartRate, SmoothedHeartRate> {
    override suspend fun process(input: RawHeartRate): SmoothedHeartRate {
        // TODO: 实现平滑算法
        return SmoothedHeartRate(
            bpm = input.bpm,
            isValid = input.confidence > 0.7f,
            timestamp = input.timestamp
        )
    }
}