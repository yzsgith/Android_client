package com.sea.auspicious_sign.domain

import com.sea.auspicious_sign.domain.model.RawHeartRate
import com.sea.auspicious_sign.domain.processor.impl.HeartRateSmoother
import com.sea.auspicious_sign.domain.processor.impl.HeartRateAnalyzer
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val raw = RawHeartRate(115, 0.95f, System.currentTimeMillis())
    val smoother = HeartRateSmoother()
    val analyzer = HeartRateAnalyzer()

    val smoothed = smoother.process(raw)
    val alert = analyzer.process(smoothed)

    println("平滑心率: ${smoothed.bpm}, 有效: ${smoothed.isValid}")
    println("告警: ${alert.level} - ${alert.message}")
}
