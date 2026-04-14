package com.sea.auspicious_sign.domain.processor

/**
 * 处理器链，将多个 DataProcessor 串联执行
 * 注意：链中处理器类型必须匹配（上一个的输出类型是下一个的输入类型）
 */
class ProcessorChain {
    private val processors = mutableListOf<DataProcessor<*, *>>()

    fun add(processor: DataProcessor<*, *>): ProcessorChain {
        processors.add(processor)
        return this
    }

    suspend fun execute(input: Any): Any {
        var current: Any = input
        for (processor in processors) {
            @Suppress("UNCHECKED_CAST")
            current = (processor as DataProcessor<Any, Any>).process(current)
        }
        return current
    }

    fun clear() {
        processors.clear()
    }

    fun size(): Int = processors.size
}
