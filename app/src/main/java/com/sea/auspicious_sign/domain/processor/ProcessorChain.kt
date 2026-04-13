package com.sea.auspicious_sign.domain.processor

/**
 * 处理器链，将多个 DataProcessor 串联执行
 * 注意：链中处理器类型必须匹配（上一个的输出类型是下一个的输入类型）
 */
class ProcessorChain {
    private val processors = mutableListOf<DataProcessor<*, *>>()

    /**
     * 添加一个处理器到链尾
     * @param processor 处理器实例
     * @return 当前链实例，支持链式调用
     */
    fun add(processor: DataProcessor<*, *>): ProcessorChain {
        processors.add(processor)
        return this
    }

    /**
     * 执行整个处理器链
     * @param input 初始输入
     * @return 最终输出
     * @throws ClassCastException 如果链中处理器类型不匹配
     */
    suspend fun execute(input: Any): Any {
        var current: Any = input
        for (processor in processors) {
            @Suppress("UNCHECKED_CAST")
            current = (processor as DataProcessor<Any, Any>).process(current)
        }
        return current
    }

    /**
     * 清空所有处理器
     */
    fun clear() {
        processors.clear()
    }

    /**
     * 获取处理器数量
     */
    fun size(): Int = processors.size
}
