package com.sea.auspicious_sign.domain.processor

import kotlin.reflect.KClass

object ProcessorRegistry {
    private val registry = mutableMapOf<KClass<out Any>, ProcessorChain>()

    fun register(dataType: KClass<out Any>, chain: ProcessorChain) {
        registry[dataType] = chain
    }

    fun getChain(dataType: KClass<out Any>): ProcessorChain? = registry[dataType]

    fun unregister(dataType: KClass<out Any>) {
        registry.remove(dataType)
    }

    fun clear() {
        registry.clear()
    }
}
