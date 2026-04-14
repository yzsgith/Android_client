package com.sea.auspicious_sign.domain.model

/**
 * 标记原始数据（来自传感器或文件）
 */
interface RawData

/**
 * 标记转化后的中间数据
 */
interface TransformedData

/**
 * 标记最终可使用的数据（如告警、统计结果）
 */
interface ConsumableData
