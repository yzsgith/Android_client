# domain 模块基础数据处理接口

> 本文档列出 `:domain` 模块中所有纯 Kotlin/JVM 的数据处理接口（不依赖 Android SDK）。  
> 对应的文件链接指向 `domain` 模块源码路径。

## 一、处理器接口

| 接口/抽象类 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `DataProcessor<IN, OUT>` | 单步数据转换，输入类型 IN，输出类型 OUT | `suspend fun process(input: IN): OUT` | [domain/processor/DataProcessor.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/DataProcessor.kt) |
| `StreamingProcessor<IN, OUT>` | 处理流式数据（如传感器连续数据流） | `fun processStream(input: Flow<IN>): Flow<OUT>` | [domain/processor/StreamingProcessor.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/StreamingProcessor.kt) |
| `ProcessorChain` | 组合多个 `DataProcessor` 形成链式调用 | `fun add(processor: DataProcessor<*, *>)`，`suspend fun execute(input: Any): Any` | [domain/processor/ProcessorChain.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorChain.kt) |
| `ProcessorRegistry` | 注册和管理不同数据类型对应的处理器链 | `fun register(dataType: KClass<out RawData>, chain: ProcessorChain)`，`fun getChain(...): ProcessorChain?` | [domain/processor/ProcessorRegistry.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorRegistry.kt) |

## 二、策略接口

| 接口/抽象类 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `ProcessingStrategy` | 决定是否本地处理、存储目标等 | `fun shouldProcessLocally(data: RawData): Boolean`，`fun getStorageTarget(data: RawData): StorageTarget` | [domain/strategy/ProcessingStrategy.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/strategy/ProcessingStrategy.kt) |

## 三、示例处理器实现（参考）

| 类 | 作用 | 文件链接 |
| --- | --- | --- |
| `HeartRateSmoother` | 心率平滑处理器：原始心率 → 平滑心率 | [domain/processor/impl/HeartRateSmoother.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateSmoother.kt) |
| `HeartRateAnalyzer` | 心率分析处理器：平滑心率 → 告警等级 | [domain/processor/impl/HeartRateAnalyzer.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateAnalyzer.kt) |

---

**生成时间**：2026-04-21  
**维护者**：项目团队
