package com.sea.auspicious_sign.domain.processor

import kotlinx.coroutines.flow.Flow

/**
 * 流式数据处理器接口，用于处理连续数据流（如传感器实时数据）
 * @param IN 输入流元素类型
 * @param OUT 输出流元素类型
 */
interface StreamingProcessor<IN, OUT> {
    /**
     * 处理输入流，返回转换后的流
     * @param input 输入数据流
     * @return 输出数据流
     */
    fun processStream(input: Flow<IN>): Flow<OUT>
}