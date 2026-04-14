package com.sea.auspicious_sign.domain.processor

/**
 * 单步数据转换处理器接口
 * @param IN 输入类型
 * @param OUT 输出类型
 */
interface DataProcessor<IN, OUT> {
    /**
     * 处理输入数据，返回转换后的结果
     * @param input 输入数据
     * @return 处理后的输出数据
     */
    suspend fun process(input: IN): OUT
}
