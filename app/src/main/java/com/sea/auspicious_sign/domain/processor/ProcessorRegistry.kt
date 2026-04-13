package com.sea.auspicious_sign.domain.processor

import kotlin.reflect.KClass

/**
 * 处理器注册中心，管理不同原始数据类型对应的处理器链
 */
object ProcessorRegistry {
    private val registry = mutableMapOf<KClass<out Any>, ProcessorChain>()

    /**
     * 注册一个处理器链
     * @param dataType 原始数据的类型（通常是 RawData 的子类）
     * @param chain 对应的处理器链
     */
    fun register(dataType: KClass<out Any>, chain: ProcessorChain) {
        registry[dataType] = chain
    }

    /**
     * 获取指定数据类型对应的处理器链
     * @param dataType 数据类型
     * @return 处理器链，若未注册则返回 null
     */
    fun getChain(dataType: KClass<out Any>): ProcessorChain? {
        return registry[dataType]
    }

    /**
     * 移除指定数据类型的注册
     */
    fun unregister(dataType: KClass<out Any>) {
        registry.remove(dataType)
    }

    /**
     * 清空所有注册
     */
    fun clear() {
        registry.clear()
    }
}
