package com.sea.auspicious_sign.domain.model

/**
 * 原始数据标记接口
 */
interface RawData

/**
 * 转化后的中间数据标记接口
 */
interface TransformedData

/**
 * 最终可使用的数据标记接口（如告警、统计结果）
 */
interface ConsumableData