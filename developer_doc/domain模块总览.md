# domain 模块总览

> 本文档列出迁移至 `:domain` 纯 Kotlin 模块的所有核心文件。  
> `:domain` 模块不依赖 Android SDK，可在纯 JVM 环境中独立编译、测试和运行，不受 `:app` 模块中其他错误的影响。

## 迁移文件列表

| 作用 | 文件（点击链接跳转） |
|------|----------------------|
| 数据阶段标记接口（RawData, TransformedData, ConsumableData） | [domain/src/main/java/com/sea/auspicious_sign/domain/model/DataStages.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/model/DataStages.kt) |
| 传感器数据模型（RawHeartRate, SmoothedHeartRate, HeartRateAlert 等） | [domain/src/main/java/com/sea/auspicious_sign/domain/model/SensorData.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/model/SensorData.kt) |
| 单步数据处理器接口 DataProcessor<IN, OUT> | [domain/src/main/java/com/sea/auspicious_sign/domain/processor/DataProcessor.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/DataProcessor.kt) |
| 流式数据处理器接口 StreamingProcessor<IN, OUT> | [domain/src/main/java/com/sea/auspicious_sign/domain/processor/StreamingProcessor.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/StreamingProcessor.kt) |
| 处理器链（组合多个 DataProcessor） | [domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorChain.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorChain.kt) |
| 处理器注册中心 | [domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorRegistry.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorRegistry.kt) |
| 心率平滑处理器（示例实现） | [domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateSmoother.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateSmoother.kt) |
| 心率分析处理器（示例实现） | [domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateAnalyzer.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateAnalyzer.kt) |
| 处理策略接口及 StorageTarget 枚举 | [domain/src/main/java/com/sea/auspicious_sign/domain/strategy/ProcessingStrategy.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/strategy/ProcessingStrategy.kt) |
| 同步状态枚举（PENDING, SUCCESS, FAILED） | [domain/src/main/java/com/sea/auspicious_sign/sync/SyncStatus.kt](../domain/src/main/java/com/sea/auspicious_sign/sync/SyncStatus.kt) |
| 网络模型（UploadResponse, ProcessRequest, ResultResponse） | [domain/src/main/java/com/sea/auspicious_sign/data/remote/model/NetlifyModels.kt](../domain/src/main/java/com/sea/auspicious_sign/data/remote/model/NetlifyModels.kt) |
| 全局配置数据类（AppConfig） | [domain/src/main/java/com/sea/auspicious_sign/utils/AppConfig.kt](../domain/src/main/java/com/sea/auspicious_sign/utils/AppConfig.kt) |
| 纯逻辑测试运行入口（可选，用于独立验证） | [domain/src/main/java/com/sea/auspicious_sign/domain/TestRunner.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/TestRunner.kt) |

## 使用说明

- 以上所有文件均**不依赖 Android SDK**，可以在 `domain` 模块中直接编写单元测试或带有 `main` 函数的入口进行调试。
- 在 Android Studio 中，点击任意链接即可打开对应文件（如果文件不存在，请根据路径手动创建）。
- `:app` 模块已通过 `implementation(project(":domain"))` 依赖本模块，因此可以正常使用其中的类和接口。
- 后续新增任何不依赖 Android 的业务逻辑或数据模型，均应优先放在 `domain` 模块中。

## 相关文档

- [总览.md](./总览.md)
- [基础数据结构.md](./基础数据结构.md)
- [基础数据处理接口.md](./基础数据处理接口.md)
- [示例处理器实现.md](./示例处理器实现.md)

---

**生成时间**：2026-04-14  
**维护者**：项目团队